type CrudSearchProperty = {
  label: string | VNode | ((property: any) => VNode);
  name: string;
  value?: any;
  type?: AutoFormFieldType;
  attrs?: Record<string, any>;
};

type CrudProperty = {
  label: string;
  name: string;
  value?: any;
  type?: AutoFormFieldType;
};

type PaginateParams = {
  page?: number;
  size?: number;
};

type SortBy = {
  name: string;
  order: "asc" | "desc" | null;
};

type CrudDataFetcherParams = Record<string, any> & {
  sortBy?: SortBy;
};

type CrudDataFetcher<T> = (params: CrudDataFetcherParams) => Promise<T[]>;

type PaginateCrudDataFetcherParams = CrudDataFetcherParams & {
  page?: PaginateParams;
};

type PaginateCrudDataFetcher<T> = (
  params: PaginateCrudDataFetcherParams
) => Promise<Page<T>>;

type CrudTableCellRender = () => VNodeChild;

type CrudTableColumn = {
  prop?: string;
  label?: string;
  expand?: boolean;
  formatter?: CrudTableCellFormatter | 'date' | 'datetime';
  sortable?: boolean;
  // 透传属性
  attrs?: Record<string, any>;
  formProps?: FormField;
};
