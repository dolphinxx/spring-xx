import {snakeToPascalCase} from "@/utils/utils";

export function importDynamic(target: string) {
  const tokens = target.split('/');
  tokens.splice(0, 1);
  tokens[tokens.length - 1] = formatComponentName(tokens[tokens.length - 1]);
  if(tokens.length === 1) {
    // noinspection TypeScriptCheckImport
    return import(`@/views/${tokens[0]}.vue`);
  }
  if(tokens.length === 2) {
    // noinspection TypeScriptCheckImport
    return import(`@/views/${tokens[0]}/${tokens[1]}.vue`);
  }
  if(tokens.length === 3) {
    // noinspection TypeScriptCheckImport
    return import(`@/views/${tokens[0]}/${tokens[1]}/${tokens[2]}.vue`);
  }
  if(tokens.length === 4) {
    // noinspection TypeScriptCheckImport
    return import(`@/views/${tokens[0]}/${tokens[1]}/${tokens[2]}/${tokens[3]}.vue`);
  }
  if(tokens.length === 5) {
    // noinspection TypeScriptCheckImport
    return import(`@/views/${tokens[0]}/${tokens[1]}/${tokens[2]}/${tokens[3]}/${tokens[4]}.vue`);
  }
  throw new Error('too many paths');
}
function formatComponentName(name:string) {
  if(name === '') {
    return 'Index';
  }
  if(/^\d+$/.test(name)) {
    return 'Detail';
  }
  if(name.indexOf('?') !== -1) {
    name = name.substring(0, name.indexOf('?'));
  }
  return snakeToPascalCase(name);
}
