import { type TableColumnCtx } from "element-plus";
import { format as formatDate } from "date-fns";
import { h } from "vue";

export const createDateFormatter =
  (format: string) =>
  (row: any, column: TableColumnCtx<any>, cellValue: any, index: number) =>
    cellValue ? formatDate(cellValue, format) : "";

export const builtInTableCellFormatters: { [key: string]: CrudTableCellFormatter } = {
  date: createDateFormatter("yyyy-MM-dd"),
  datetime: createDateFormatter("yyyy-MM-dd HH:mm:ss"),
};
