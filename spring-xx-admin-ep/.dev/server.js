const express = require("express");
const fileUpload = require("express-fileupload");
const bodyParser = require("body-parser");
const fs = require("fs");
const path = require("path");
const useRoutes = require("./routes");
const {restSuccess, restServerError, delay, extFromMimeType, offsetDay} = require("./utils");
const {db, prepareData} = require("./db");
const Cache  = require('file-system-cache').default;

const cache = Cache({
  basePath: path.join(__dirname, '.cache'),
})

process.on('unhandledRejection', error => {
  console.error('unhandledRejection', error);
});

prepareData();

const app = express();
const port = 8081;

const publicApis = ["/", "/login", "/sys/settings"];
const crudEntries = [
  {
    table: "user", prefix: "/system", columns: {
      'id': null,
      name: {condition: 'contains'},
      username: null,
      password: null,
      status: null,
      remark: null,
      createTime: {fn: 'DATE'},
      updateTime: null,
    },
  },
];

let increment = 0;

function loadData(file) {
  return JSON.parse(
    fs.readFileSync(path.resolve(__dirname, file), {encoding: "utf-8"})
  );
}

app.use("/static/tmp", express.static(path.join(__dirname, "tmp")));
app.use(
  fileUpload({
    limits: {fileSize: 50 * 1024 * 1024},
  })
);
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

app.use((req, res, next) => {
  if (
    !publicApis.includes(req.path) &&
    !cache.getSync('authenticates.admin')
  ) {
    res.json({status: 401, msg: "未登录", data: null});
    return;
  }
  delay(500).then(() => next());
});

app.get("/", (req, res) => res.json(restSuccess({message: "Hello World!"})));

app.post("/login", (req, res) => {
  if (req.body.username === "admin" && req.body.password === "123456") {
    const authenticated = {};
    res.cookie("authenticated", "1", {path: "/", expires: offsetDay(new Date(), 1)});
    if (req.body["remember-me"] === "true") {
      authenticated.rememberMe = new Date().getTime() + "";
      res.cookie("remember-me", authenticated.rememberMe, {path: "/", expires: offsetDay(new Date(), 7)});
    } else {
      // remove remember-me cookie
      res.cookie("remember-me", authenticated.rememberMe, {path: "/", expires: offsetDay(new Date(), -1)});
    }
    cache.setSync('authenticates.admin', authenticated, 7 * 24 * 3600);
    res.json(restSuccess(loadData(path.join(__dirname, "login.json"))));
    return;
  }
  res.json({status: 401, msg: "账号或密码错误", data: null});
});

app.get("/logout", (req, res) => {
  cache.removeSync('authenticated.admin');
  const yesterday = offsetDay(new Date(), -1);
  res.cookie("authenticated", "0", {path: "/", expires: yesterday});
  // remove remember-me cookie
  res.cookie("remember-me", "", {path: "/", expires: yesterday});
  res.json(restSuccess(null));
});

app.post("/storage/upload", async (req, res) => {
  if (!req.files) {
    res.json(restServerError("没有文件"));
    return;
  }
  const file = req.files[Object.keys(req.files)[0]];
  const filename =
    new Date().getTime() +
    String(increment++).padStart(3, "0") +
    (path.extname(file.name) || extFromMimeType(file.mimetype) || "");
  const filepath = path.join(__dirname, "tmp", filename);
  await file.mv(filepath);
  res.json(restSuccess({url: "/api/static/tmp/" + filename, key: filename}));
});

app.post("/storage/remove", (req, res) => {
  const key = req.body.key;
  if (key) {
    const filepath = path.join(__dirname, "tmp", key);
    if (fs.existsSync(filepath) && fs.statSync(filepath).isFile()) {
      fs.rmSync(filepath);
      res.json({status: 200, msg: null, data: null});
      return;
    }
  }
  res.json(restServerError("资源不存在"));
});

if (crudEntries.length > 0) {
  useRoutes(crudEntries, app);
}

app.all("*", async (req, res) => {
  const dataFile = path.join(__dirname, req.path + ".json");
  if (fs.existsSync(dataFile)) {
    res.json(restSuccess(loadData(dataFile)));
    return;
  }
  res.json({status: 404, msg: "资源不存在", data: null});
});

app.use(function (err, req, res, next) {
  res.json({status: 500, msg: String(err)});
});

app.listen(port, () => {
  console.log(`Mock server listening on port ${port}`);
});

process.on("SIGTERM", () => {
  console.info("SIGTERM signal received.");
  db.close(() => {
    process.exit(0);
  });
});
