type FieldOption = {
  label: string;
  value: string;
}
type FieldSimpleType = "input" | "textarea" | "select" | "combobox" | "checkbox" | "radio" | "switch" | "date" | "datetime" | "number" | "file";
type FieldDateType = {type:"date", subtype: "date"|"time"|"datetime"|"year"|"month"};
type FormField = {
  key: string;
  title: string;
  placeholder?: string;
  hint?: string;
  type?: FieldSimpleType | FieldDateType;
  multiple?: boolean;
  options?: FieldOption[];
  format?: string;
  range?: string[];
  cols?: 1 | 2;
}
type FormFieldGroup = {
  title: string;
  fields: FormField[];
}
