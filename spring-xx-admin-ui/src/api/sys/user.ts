import {postForm, request} from "@/api/request";

const prefix = "/system/user";

export const paginateUsers = (search?: Record<string, any>) => request<Page<UserVO>>(`${prefix}/paginate`, {query: search});

export const readUser = (id) => request<UserVO>(`${prefix}/read?id=${id}`);

export const createUser = (entity: UserVO) => postForm<UserVO>(`${prefix}/create`, entity);

export const updateUser = (entity: UserVO) => postForm<UserVO>(`${prefix}/update`, entity);

export const deleteUser = (id) => postForm<void>(`${prefix}/delete`, {id});
