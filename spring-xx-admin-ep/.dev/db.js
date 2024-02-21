const sqlite3 = require("sqlite3").verbose();
const db = new sqlite3.Database(":memory:");
const fs = require("fs");
const path = require("path");

function listDataFiles() {
  return fs
    .readdirSync(path.join(__dirname, "data"))
    .filter((_) => _.endsWith(".data.json"));
}

function generateInsertSql(table, props) {
  const columns = [];
  const placeholders = [];
  for (const prop of props) {
    columns.push(`\`${prop}\``);
    placeholders.push("?");
  }
  return `INSERT INTO \`${table}\`(${columns.join(
    ","
  )})VALUES(${placeholders.join(",")})`;
}

function prepareData() {
  db.serialize(() => {
    console.log("preparing db...");
    const scheme = fs.readFileSync(path.join(__dirname, "data/scheme.sql"), {
      encoding: "utf-8",
    });
    db.run(scheme);
    for (const dataFile of listDataFiles()) {
      const data = JSON.parse(
        fs.readFileSync(path.join(path.join(__dirname, "data", dataFile)), {
          encoding: "utf-8",
        })
      );
      if (data.length === 0) {
        continue;
      }
      const props = Object.keys(data[0]);
      const table = dataFile.substring(0, dataFile.indexOf("."));
      const stmt = db.prepare(generateInsertSql(table, props));
      for (const record of data) {
        stmt.run(props.map((prop) => record[prop]));
      }
      stmt.finalize();
    }
    console.log("db prepared.");
  });
}

module.exports = {
  db,
  prepareData,
};
