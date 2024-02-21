export {};

declare global {
  // copy from ElTableColumn.formatter in element-plus
  type TableColumnCtx =
    import("element-plus/es/components/table/src/table-column/defaults").TableColumnCtx<any>;
  type TableCellScope = {
    row: any;
    column: TableColumnCtx;
    $index: number;
    cellIndex: number;
    expanded: boolean;
  };
  type CrudTableCellFormatter = (
    row: any,
    column: TableColumnCtx,
    cellValue: any,
    index: number
  ) =>
    | string
    | import("vue").VNode<
        import("vue").RendererNode,
        import("vue").RendererElement,
        {
          [key: string]: any;
        }
      >;
}
