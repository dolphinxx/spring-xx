import {format} from "date-fns";

export function formatProperty(value:any, formatter:string):string {
  if(value instanceof Date) {
    return format(value, formatter);
  }
  throw new Error(`unknown format: type=${typeof value}, formatter=${formatter}`);
}

export function copyObject<S, T>(target:any, source:S, converters:Record<string, string|StringConverter>):T {
  if(!converters) {
    return Object.assign(target, source) as T;
  }
  for(let o in source) {
    if(source.hasOwnProperty(o)) {
      const sourceValue = source[o];
      if(converters.hasOwnProperty(o)) {
        const converter = converters[o];
        if(typeof converter === 'string') {
          target[o] = formatProperty(sourceValue, converter) as any;
          continue;
        }
        target[o] = converter(sourceValue) as any;
        continue;
      }
      target[o] = sourceValue;
    }
  }
  return target as T;
}
