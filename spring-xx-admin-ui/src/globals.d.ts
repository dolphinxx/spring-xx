type Override<T1, T2> = Omit<T1, keyof T2> & T2;

type StringConverter = (v: any) => string;

type R<T> = {
  data: T;
  status: number;
  msg: string;
}

type Page<T> = {
  /**
   * the first page is 1.
   */
  page: number;
  size: number;
  totalCount: number;
  totalPages: number;
  items: T[];
}

type RequestHeaders = {
  [key: string]: string;
  "Content-Type"?: "application/json" | "application/x-www-form-urlencoded" | "text/plain";
};

type RequestOptions = ({
  method?: "GET" | "POST";
  headers?: RequestHeaders;
  query?: Record<string, any>;
  body?: BodyInit | null;
});

type DialogState = {
  confirms: DialogOptions[];
  alerts: DialogOptions[];
}

interface DialogOptions {
  msg?: string;
  component?: {
    type: any;
    props?: any;
  };
  ok?: () => void;
}

type DialogOptionsWrapper = {
  id: number;
  showing: boolean;
  type: "confirm" | "alert";
  options?: DialogOptions
};

type Settings = {
  name: string;
}

type Principal = {
  id: number;
  name: string;
}

type Btn = {
  id: number;
  name: string;
  key: string;
  type: 1 | 2;
  target: string | null;
  perm?: string | null;
  parentId?: number | null;
  icon?: string | null;
  order: number;
  children?: Array<Btn> | null;
  active?: boolean;
}

declare global {
  interface Window {
    __APP__: {
      storagePrefix: string;
    };
  }
}
declare var hasValue: (v: any) => boolean;
