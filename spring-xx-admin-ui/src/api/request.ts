let loginHandler:() => void;
// TODO
export const applyLoginHandler = (handler:() => void) => loginHandler = handler;
export const request = async <T>(url: string, options?: RequestOptions): Promise<T> => {
  const requestInit = {
    method: options?.method || 'GET',
    headers: Object.assign({}, options?.headers)!,
    body: options?.body,
  } as RequestInit & {
    headers: Record<string, string>;
  };
  if (requestInit.method === 'POST' && requestInit.body && !requestInit.headers["Content-Type"]) {
    requestInit.headers["Content-Type"] = "application/x-www-form-urlencoded";
  }
  url = `/api${url}`;
  if (requestInit.method === 'GET' && options?.query) {
    const queryString = new URLSearchParams(options.query).toString();
    if (url.indexOf('?') === -1) {
      url = url + '?' + queryString;
    } else {
      url = (url.endsWith('?') ? '' : '?') + queryString;
    }
  }
  console.log(url, requestInit);
  return fetch(url, requestInit).then(response => {
    if (response.status === 200) {
      return (response.json() as Promise<R<string>>).then(res => {
        if (res.status === 200) {
          return res.data as T;
        }
        if(res.status === 401 && loginHandler) {
          loginHandler();
        }
        throw new Error(res.msg || 'request failed with status ' + res.status);
      });
    }
    throw new Error(`failed to perform request ${url}, status=${response.status}`);
  });
}

export const postForm = async <T>(url: string, body?: Record<string, any> | null, headers?: RequestHeaders): Promise<T> => {
  return request<T>(url, {
    method: "POST",
    headers: {
      ...headers,
      "Content-Type": "application/x-www-form-urlencoded",
    },
    body: body ? new URLSearchParams(body).toString() : null,
  });
}

export const postJson = async <T>(url: string, body: any, headers?: RequestHeaders): Promise<T> => {
  return request<T>(url, {
    method: "POST",
    headers: {
      ...headers,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(body),
  })
}

export const uploadFile = async <T>(url: string, file: File, progressListener?: UploadProgressListener): Promise<T> => {
  url = `/api${url}`;
  const xhr = new XMLHttpRequest();
  return await new Promise((resolve, reject) => {
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
          console.log(xhr.response);
          const res = JSON.parse(xhr.response);
          if (res.status === 200) {
            resolve(res.data as T);
            return;
          }
          if(res.status === 401 && loginHandler) {
            loginHandler();
          }
          reject(new Error(res.msg || 'request failed with status ' + res.status));
          return;
        }
        reject(new Error(`failed to perform request ${url}, status=${xhr.status}`));
      }
    });
    const fileData = new FormData();
    fileData.append("file", file);
    xhr.open("POST", url, true);
    xhr.send(fileData);
  });
}
