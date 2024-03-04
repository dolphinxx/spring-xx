type R<T> = {
  data: T;
  status: number;
  msg: string;
}

type Project = {
  id: string;
  name: string;
  path: string;
};

type Settings = {
  projects: Array<Project>;
  currentId: string;
}

type RequestHeaders = {
  [key: string]: string;
};

type RequestOptions = ({
  method?: "GET";
  headers?: RequestHeaders;
} | {
  method: "POST";
  headers?: RequestHeaders & { "Content-Type"?: "application/json" | "application/x-www-form-urlencoded" | "text/plain"; };
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
