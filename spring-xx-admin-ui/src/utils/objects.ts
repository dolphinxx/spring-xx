import {format} from "date-fns";

export function formatProperty(value:any, formatter:string):string {
  if(value instanceof Date) {
    return format(value, formatter);
  }
  throw new Error(`unknown format: type=${typeof value}, formatter=${formatter}`);
}
