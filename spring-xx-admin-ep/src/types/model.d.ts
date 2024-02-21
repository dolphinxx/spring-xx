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

type UserVO = {
  id: number;
  name: string;
  username: string;
  createTime: Date;
}

type UserForm = {
  id?: number;
  name: string;
  username: string;
}
