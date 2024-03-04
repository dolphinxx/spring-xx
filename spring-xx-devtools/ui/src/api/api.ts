import {nextId} from "@/utils";

export const request = async <T>(url: string, options?: RequestOptions): Promise<T> => {
  const requestInit = {
    method: options?.method || 'GET',
    headers: Object.assign({}, options?.headers)!
  } as RequestInit & {
    headers: Record<string, string>;
  };
  if (requestInit.method === 'POST' && !requestInit.headers["Content-Type"]) {
    requestInit.headers["Content-Type"] = "application/x-www-form-urlencoded";
  }
  return fetch(url, requestInit).then(response => {
    if (response.status === 200) {
      return (response.json() as Promise<R<string>>).then(res => {
        if (res.status === 200) {
          return JSON.parse(res.data) as T;
        }
        throw new Error(res.msg);
      });
    }
  });
}

export const getSettings = async (): Promise<Settings> => {
  // return request<Settings>("/settings");
  const data:Settings = JSON.parse(window.localStorage.getItem("settings") || "{\"projects\":[]}");
  if(!data.projects || data.projects.length === 0) {
    data.projects = [{
      id: nextId(),
      name: "Default",
      path: "",
    }];
  }
  return data;
};

export const saveSettings = async (data): Promise<void> => {
  // return request<void>("/settings", {
  //   method: "POST",
  //   body: JSON.stringify(data),
  //   headers: {
  //     "Content-Type": "text/plain"
  //   }
  // })
  window.localStorage.setItem("settings", JSON.stringify(data));
};

export const dirExists = async (path): Promise<boolean> => {
  // return request<>("/common/exists?type=dir");
  return new Promise<boolean>((resolve, reject) => {
    if(!path.endsWith("/")) {
      return resolve(false);
    }
    resolve(true);
  });
}
