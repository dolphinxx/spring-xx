import {postForm, request, uploadFile} from "@/api/request";

export const getSettings = async (): Promise<Settings> => {
  return request<Settings>("/sys/settings");
};

export const login = async (username: string, password: string, rememberMe: boolean): Promise<Principal> => {
  return request<Principal>("/login", {
    method: "POST",
    body: new URLSearchParams({
      username,
      password,
      'remember-me': rememberMe
    }).toString()
  });
}

export const logout = async (): Promise<void> => request<void>("/logout");

export const getPrincipal = async () => request<Principal>("/sys/principal");

export const getButtons = async () => request<Array<Btn>>("/sys/buttons");

export const reloadButtons = async () => request<void>("/sys/reload_buttons", {method: "POST"});

export const reloadAuthorities = async () => request<void>("/sys/reload_authorities", {method: "POST"});

export const uploadStorage = async (file: File, progressListener?: UploadProgressListener) => uploadFile<UploadedFile>("/storage/upload", file, progressListener);

export const removeStorage = async (key: string) => postForm<boolean>("/storage/remove", {key});

export const uploadHandler: FileUploadHandler = {
  upload(file: File, progressListener?: UploadProgressListener): Promise<UploadedFile> {
    return uploadStorage(file, progressListener);
  },
  remove(key: string): Promise<boolean> {
    return removeStorage(key);
  },
  buildUrl(key: string): string {
    return window.__APP__.storagePrefix + key;
  }
};
