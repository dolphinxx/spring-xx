const { faker } = require("@faker-js/faker/locale/zh_CN");

const { readFileSync, existsSync, writeFileSync } = require("fs");
const { resolve, dirname } = require("path");

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
    if (fieldOptions.arguments) {
      record[o] = generator(
        resolveVariables(fieldOptions.arguments, context, record)
      );
    } else {
      record[o] = generator();
    }
  }
  return record;
}

(function () {
  let schemeFile = process.argv[2];
  if (!schemeFile) {
    console.error("Usage: yarn generate <schemeFile>");
    return;
  }
  schemeFile = resolve(__dirname, schemeFile);
  if (!existsSync(schemeFile)) {
    console.error(`scheme file[${schemeFile}] not exists`);
    return;
  }
  const destDir = dirname(schemeFile);
  const scheme = JSON.parse(readFileSync(schemeFile, { encoding: "utf-8" }));
  const context = { now: new Date() };
  const dest = scheme["%dest%"];
  if (scheme["%paginate%"]) {
    const totalCount = scheme["%paginate%"].totalCount;
    const size = scheme["%paginate%"].size;
    const totalPages = Math.ceil(totalCount / size);
    context.totalPages = totalPages;
    context.size = size;
    context.totalCount = totalCount;
    const itemsField = scheme["%paginate%"].items || "items";
    const contentScheme = scheme["%paginate%"].scheme;
    for (let i = 1; i <= totalPages; i++) {
      context.page = i;
      const data = generateData(scheme, context, { excludes: ["itemsField"] });
      const items = (data[itemsField] = []);
      let pageSize =
        i === totalPages ? Math.min(size, totalCount % size || size) : size;
      for (let j = 0; j < pageSize; j++) {
        const record = generateData(contentScheme, context);
        items.push(record);
      }
      writeFileSync(
        resolve(
          destDir,
          dest.replace(/\{(\w+)}/g, (_, m) => context[m])
        ),
        JSON.stringify(data, null, 2),
        {
          encoding: "utf-8",
        }
      );
    }
  } else {
    writeFileSync(
      resolve(
        destDir,
        dest.replace(/\{(\w+)}/g, (_, m) => context[m])
      ),
      JSON.stringify(generateData(scheme, context), null, 2),
      { encoding: "utf-8" }
    );
  }
})();
