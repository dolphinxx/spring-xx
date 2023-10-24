export const requiredRule = (v: string | null | undefined) => (v !== undefined && v !== null && v !== '') || '该字段必须';
export const nonEmptyRule = (v: any) => (v instanceof Array && v.length > 0) || '该字段不能为空';

export const usernameRule = (v: string) => /^[a-zA-Z0-9_]+$/.test(v) || '只允许字母、数字和下划线';

export const lengthBetweenRuleBuilder = (min: number, max: number) => (v: string) => (v.length >= min && v.length <= max) || `长度只能在${min}和${max}之间`;
