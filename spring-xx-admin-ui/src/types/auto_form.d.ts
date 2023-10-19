type SelectOption = {
  label: string;
  value: string;
}
type FormField = {
  key: string;
  title: string;
  placeholder?: string;
  hint?: string;
  type?: "input" | "textarea" | "select" | "date" | "datetime" | {type:"date", subtype: "date"|"time"|"datetime"|"year"|"month"} | "number";
  multiple?: boolean;
  options?: SelectOption[];
  format?: string;
  range?: string[];
  cols?: 1 | 2;
}
type FormFieldGroup = {
  title: string;
  fields: FormField[];
}
