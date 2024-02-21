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

type UploadProgressListener = (loaded:number, total:number) => void;
