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
        throw new Error(res.msg);
      });
    }
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
