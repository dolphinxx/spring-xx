const http = require("http");
const fs = require("fs");
const { extname, join } = require("path");

const host = "localhost";
const port = 8081;

const serveUrl = `http://${host}:${port}`;

let increment = 0;

function success(data) {
  return typeof data === "string"
    ? `{
    "status": 200,
    "msg": null,
    "data": ${data}
  }`
    : JSON.stringify({
        data,
        status: 200,
        msg: null,
      });
}

function loadData(file) {
  return success(fs.readFileSync(file, { encoding: "utf-8" }));
}

async function delay(ms) {
  return new Promise((resolve, reject) => {
    setTimeout(() => resolve(), ms);
  });
}

const publicApis = ["/sys/settings"];

const authenticates = {};

const BOUNDARY = /^multipart\/.+?(?:; boundary=(?:(?:"(.+)")|(?:([^\s]+))))$/i;

const getMatching = (str, regex) => {
  const m = regex.exec(str);
  if (m) {
    return m[1];
  }
  return null;
};

const mimeMap = {
  ".ico": "image/x-icon",
  ".html": "text/html",
  ".js": "text/javascript",
  ".json": "application/json",
  ".css": "text/css",
  ".png": "image/png",
  ".jpg": "image/jpeg",
  ".wav": "audio/wav",
  ".mp3": "audio/mpeg",
  ".svg": "image/svg+xml",
  ".pdf": "application/pdf",
  ".doc": "application/msword",
};

const requestListener = async function (req, res) {
  const uri = new URL((req.headers.host ? `http://${req.headers.host}` : serveUrl) + req.url);
  const method = req.method.toUpperCase();
  console.log(req.method, uri, req.headers);
  let body;
  let parts;
  if (method === "POST") {
    body = await new Promise((resolve, reject) => {
      const chunks = [];
      req.on("data", (chunk) => chunks.push(chunk));
      req.on("end", () => {
        resolve(Buffer.concat(chunks));
      });
      req.on("error", (err) => {
        reject(err);
      });
    });
    if (req.headers["content-type"] === "application/x-www-form-urlencoded") {
      body = Object.fromEntries(new URLSearchParams(body.toString("utf-8")));
      console.log("body", body);
    } else if (BOUNDARY.test(req.headers["content-type"])) {
      const m = BOUNDARY.exec(req.headers["content-type"]);
      // https://www.w3.org/Protocols/rfc1341/7_2_Multipart.html
      // https://www.w3.org/TR/html401/interact/forms.html#h-17.13.4.2
      const boundary = "--" + (m[1] || m[2]);
      let rawData = body.toString("latin1");
      // console.log("|" + rawData + "|");
      rawData = rawData.substring(rawData.indexOf(boundary) + boundary.length, rawData.indexOf(boundary + "--"));
      const rawParts = rawData.split("\r\n" + boundary);
      parts = [];
      for (let i = 0; i < rawParts.length; i++) {
        const item = rawParts[i];
        const headersEnd = item.indexOf("\r\n\r\n");
        const partHeaders = item.substring(0, headersEnd);
        const name = getMatching(partHeaders, /name="([^"]+)"/);
        const filename = getMatching(partHeaders, /filename="([^"]*)"/);
        const contentType = getMatching(partHeaders, /Content-Type:\s*([^\r\n]+)/)?.trim();
        const content = item.substring(headersEnd + 4);
        parts.push({
          name,
          filename,
          contentType,
          content,
        });
      }
    }
  }
  await delay(500);
  res.setHeader("Content-Type", "application/json");
  res.statusCode = 200;
  const cookies = [];
  let response;
  const dataFile = uri.pathname.endsWith("/paginate")
    ? join(__dirname, uri.pathname + "." + (uri.searchParams.get("page.page") || "1") + ".json")
    : join(__dirname, uri.pathname + ".json");
  if (uri.pathname.startsWith("/static/tmp/")) {
    const filename = uri.pathname.substring(12);
    const ext = extname(filename);
    const contentType = mimeMap[ext] || "text/plain";

    res.setHeader("Content-Type", contentType);
    res.end(fs.readFileSync(join(__dirname, "tmp", filename)));
    return;
  }
  switch (uri.pathname) {
    case "/": {
      response = JSON.stringify({ message: "Hello World!" });
      break;
    }
    case "/login": {
      if (body.username === "admin" && body.password === "123456") {
        const authenticated = {};
        authenticates.admin = authenticated;
        cookies.push("authenticated=1; path=/");
        if (body["remember-me"] === "true") {
          authenticated.rememberMe = new Date().getTime() + "";
          cookies.push("remember-me=" + authenticated.rememberMe + "; path=/");
        }
        response = loadData(dataFile);
      } else {
        response = JSON.stringify({ status: "401", msg: "Bad credentials", data: null });
      }
      break;
    }
    case "/logout": {
      delete authenticates.admin;
      cookies.push("authenticated=0; path=/");
      cookies.push("remember-me=false; path=/");
      response = JSON.stringify({ status: 200, msg: null, data: null });
      break;
    }
    case "/storage/upload": {
      const file = parts.find((p) => p.filename);
      const filename = new Date().getTime() + String(increment++).padStart(3, "0") + (extname(file.filename) || "");
      const filepath = join(__dirname, "tmp", filename);
      fs.writeFileSync(filepath, file.content, { encoding: "binary" });
      response = JSON.stringify({ status: 200, msg: null, data: { url: "/api/static/tmp/" + filename, key: filename } });
      break;
    }
    case "/storage/remove": {
      const key = body.key;
      if (key) {
        const filepath = join(__dirname, "tmp", key);
        if (fs.existsSync(filepath) && fs.statSync(filepath).isFile()) {
          fs.rmSync(filepath);
          response = JSON.stringify({ status: 200, msg: null, data: null });
          break;
        }
      }
      response = JSON.stringify({ status: 500, msg: "资源不存在", data: null });
      break;
    }
    default: {
      if (!publicApis.includes(uri.pathname) && !authenticates.hasOwnProperty("admin")) {
        response = JSON.stringify({ status: "401", msg: "未登录", data: null });
        break;
      }
      if (fs.existsSync(dataFile)) {
        response = loadData(dataFile);
        break;
      }
      if (uri.pathname.endsWith("/paginate")) {
        const firstPageDataFile = join(__dirname, uri.pathname + ".1.json");
        if (fs.existsSync(firstPageDataFile)) {
          // fallback to first page if data exists.
          response = loadData(firstPageDataFile);
          break;
        }
      }
      res.end(JSON.stringify({ message: "Not found" }));
      return;
    }
  }
  if (cookies.length > 0) {
    res.setHeader("Set-Cookie", cookies);
  }
  if (response) {
    res.end(response);
  }
};

const server = http.createServer(requestListener);
server.listen(port, host, () => {
  console.log(`Mock server is running on http://${host}:${port}`);
});
