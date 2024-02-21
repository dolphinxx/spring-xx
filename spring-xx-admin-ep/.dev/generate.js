const { faker } = require("@faker-js/faker/locale/zh_CN");

const { readFileSync, existsSync, writeFileSync, readdirSync } = require("fs");
const { resolve, join } = require("path");

const dataCount = 155;

function getGenerator(type) {
  let generator = faker;
  for (const f of type.split(".")) {
    generator = generator[f];
  }
  return generator;
}

function resolveVariables(args, context, record) {
  if (typeof args === "string") {
    if (args.startsWith("$$")) {
      return context[args.substring(2)];
    }
    if (args.startsWith("$")) {
      return record[args.substring(1)];
    }
    return args;
  }
  if (args instanceof Array) {
    for (let i = 0; i < args.length; i++) {
      args[i] = resolveVariables(args[i], context, record);
    }
    return args;
  }
  if (
    typeof args === "object" &&
    args.constructor === Object &&
    Object.getPrototypeOf(args) === Object.prototype
  ) {
    for (let o in args) {
      if (args.hasOwnProperty(o)) {
        args[o] = resolveVariables(args[o], context, record);
      }
    }
    return args;
  }
  return args;
}

function toSqliteType(value) {
  if (value instanceof Date) {
    return value.getTime();
  }
  if (typeof value === "boolean") {
    return value ? 1 : 0;
  }
  return value;
}

function generateData(scheme, context) {
  const record = {};
  for (let o in scheme) {
    if ((o.startsWith("%") && o.endsWith("%")) || !scheme.hasOwnProperty(o)) {
      continue;
    }
    const fieldOptions = scheme[o];
    if (fieldOptions.type === "var") {
      record[o] = resolveVariables(fieldOptions.ref, context, record);
      continue;
    }
    if (fieldOptions.nullable && Math.random() > 0.8) {
      record[o] = null;
      continue;
    }
    const generator = getGenerator(fieldOptions.type);
    if (typeof generator !== "function") {
      throw new Error("generator is not a function");
    }
    let value;
    if (fieldOptions.arguments) {
      value = generator(
        resolveVariables(fieldOptions.arguments, context, record)
      );
    } else {
      value = generator();
    }
    record[o] = toSqliteType(value);
  }
  return record;
}

function generate(schemeFile, dataFile) {
  const scheme = JSON.parse(readFileSync(schemeFile, { encoding: "utf-8" }));
  const context = { now: new Date() };
  const data = [];
  for (let i = 0; i < dataCount; i++) {
    data.push(generateData(scheme, context));
  }
  writeFileSync(dataFile, JSON.stringify(data, null, 2), { encoding: "utf-8" });
}

(function () {
  const name = process.argv[2];
  if (name) {
    schemeFile = resolve(__dirname, `${name}.scheme.json`);
    if (!existsSync(schemeFile)) {
      console.error(`scheme file for [${name}] not exists`);
      return;
    }
    generate(schemeFile, resolve(__dirname, `${name}.data.json`));
    console.log("finished.");
    return;
  }
  const dataDir = join(__dirname, "data");
  for (const name of readdirSync(dataDir)) {
    if (!name.endsWith("scheme.json")) {
      continue;
    }
    console.log(`generating ${name}`);
    const schemeFile = join(dataDir, name);
    const dataFile = join(dataDir, name.replace(".scheme.json", ".data.json"));
    generate(schemeFile, dataFile);
  }
  console.log("finished.");
})();
