type DataTableSort = { key: string; order: "asc" | "desc" };
type DataTableOptions = {
  page: number;
  itemsPerPage: number;
  sortBy: Ref<DataTableSort[]>;
  groupBy: Ref<string[]>;
  search: string | undefined;
};

type ColumnDefinition<T> = {
  key: string;
  title?: string;
  sortable?: boolean;
  align?: "left" | "right" | "center";
  width?: number|string;
  render?: (row: T, num: number) => VNode;
  formatter?: (value: any) => string;
};

type Sort = { property: string; order: "asc" | "desc" };

type DataFetcher<T> = (params: {
  sort: Sort[];
  search: Record<string, any>;
}) => Promise<Array<T>>;

type PageDataFetcher<T> = (params: {
  page: number;
  size: number;
  sort: Sort[];
  search: Record<string, any>;
}) => Promise<Page<T>>;

type RowAction<T> = {
  title?: string;
  icon: string;
  color?: string;
  action: (row: T, num: number) => Promise<void> | void;
};
