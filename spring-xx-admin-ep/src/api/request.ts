import { isPlainObject } from "@/utils/object";

let _loginHandler: () => void;
export const applyRequestHandlers = ({
  loginHandler,
}: {
  loginHandler: () => void;
}) => {
  _loginHandler = loginHandler;
};

const apiPrefix = "/api";

const serializeParamPair = (
  key: string,
  value: any,
  result: URLSearchParams
) => {
  if (isPlainObject(value)) {
    for (const o in value) {
      if (value.hasOwnProperty(o)) {
        serializeParamPair(`${key}.${o}`, value[o], result);
      }
    }
    return;
  }
  if (value instanceof Array) {
    for (let i = 0; i < value.length; i++) {
      serializeParamPair(`${key}[${i}]`, value[i], result);
    }
    return;
  }
  if(value !== undefined && value !== null && value !== '') {
    result.append(key, String(value));
  }
};

export const serializeParams = (params: Record<string, any>): string => {
  const result = new URLSearchParams();
  for (const o in params) {
    if (params.hasOwnProperty(o)) {
      serializeParamPair(o, params[o], result);
    }
  }
  return result.toString();
};

export const request = async <T>(
  url: string,
  options?: RequestOptions
): Promise<T> => {
  const requestInit = {
    method: options?.method || "GET",
    headers: Object.assign({}, options?.headers)!,
    body: options?.body,
  } as RequestInit & {
    headers: Record<string, string>;
  };
  if (
    requestInit.method === "POST" &&
    requestInit.body &&
    !requestInit.headers["Content-Type"]
  ) {
    requestInit.headers["Content-Type"] = "application/x-www-form-urlencoded";
  }
  url = `${apiPrefix}${url}`;
  if (requestInit.method === "GET" && options?.query) {
    const queryString = serializeParams(options.query);
    if (url.indexOf("?") === -1) {
      url = url + "?" + queryString;
    } else {
      url = (url.endsWith("?") ? "" : "?") + queryString;
    }
  }
  console.log(url, requestInit);
  return fetch(url, requestInit).then((response) => {
    if (response.status === 200) {
      return (response.json() as Promise<R<string>>).then((res) => {
        if (res.status === 200) {
          return res.data as T;
        }
        if (res.status === 401 && _loginHandler) {
          _loginHandler();
        }
        throw new Error(res.msg || "request failed with status " + res.status);
      });
    }
    throw new Error(
      `failed to perform request ${url}, status=${response.status}`
    );
  });
};

export const postForm = async <T>(
  url: string,
  body?: Record<string, any> | null,
  headers?: RequestHeaders
): Promise<T> => {
  console.log(body, serializeParams(body!));
  return request<T>(url, {
    method: "POST",
    headers: {
      ...headers,
      "Content-Type": "application/x-www-form-urlencoded",
    },
    body: body ? serializeParams(body) : null,
  });
};

export const postJson = async <T>(
  url: string,
  body: any,
  headers?: RequestHeaders
): Promise<T> => {
  return request<T>(url, {
    method: "POST",
    headers: {
      ...headers,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(body),
  });
};

export const uploadFile = async <T>(
  url: string,
  file: File,
  progressListener?: UploadProgressListener
): Promise<T> => {
  url = `${apiPrefix}${url}`;
  const xhr = new XMLHttpRequest();
  return await new Promise((resolve, reject) => {
    xhr.open("POST", url, true);
    if (progressListener) {
      xhr.upload.addEventListener("progress", (event) => {
        if (event.lengthComputable) {
          progressListener!(event.loaded, event.total);
        }
      });
    }
    xhr.addEventListener("error", (e) => {
      reject(new Error(e ? String(e) : "failed to perform xhr"));
    });
    xhr.addEventListener("loadend", () => {
      if (xhr.readyState === 4) {
        if (xhr.status === 200) {
          const res = JSON.parse(xhr.response);
          if (res.status === 200) {
            resolve(res.data as T);
            return;
          }
          console.log('should popup login 1', _loginHandler != null);
          if (res.status === 401 && _loginHandler) {
            console.log('should popup login 2');
            _loginHandler();
          }
          reject(
            new Error(res.msg || "request failed with status " + res.status)
          );
          return;
        }
        reject(
          new Error(`failed to perform request ${url}, status=${xhr.status}`)
        );
      }
    });
    const fileData = new FormData();
    fileData.append("file", file);
    xhr.send(fileData);
  });
};
