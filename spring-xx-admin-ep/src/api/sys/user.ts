import {postForm, request} from "@/api/request";

const prefix = "/system/user";

export const paginateUsers = (search?: Record<string, any>) => request<Page<UserVO>>(`${prefix}/paginate`, {query: search});

export const readUser = (id:number) => request<UserVO>(`${prefix}/read?id=${id}`);

export const createUser = (entity: UserForm) => postForm<UserVO>(`${prefix}/create`, entity);

export const updateUser = (entity: UserForm) => postForm<UserVO>(`${prefix}/update`, entity);

export const deleteUser = (id:number) => postForm<void>(`${prefix}/delete`, {id});

export const deleteUsers = (ids: number[]) => postForm<void>(`${prefix}/batchDelete`, {ids: ids.join(',')});
