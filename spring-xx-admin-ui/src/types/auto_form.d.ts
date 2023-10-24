type UploadedFile = {
  key:string;
  url?:string;
}
type UploadProgressListener = (loaded:number, total:number) => void;
interface FileUploadHandler {
  upload(file:File, progressListener?: UploadProgressListener):Promise<UploadedFile>;
  remove?(key:string):Promise<boolean>;
  buildUrl?(key:string):string;
}
type FieldOption = {
  label: string;
  value: string;
}
type FieldSimpleType =
  "input"
  | "textarea"
  | "select"
  | "combobox"
  | "checkbox"
  | "radio"
  | "switch"
  | "date"
  | "datetime"
  | "number"
  | "file"
  | "attachment"
  | "image";
type FieldDateType = { type: "date", subtype: "date" | "time" | "datetime" | "year" | "month" };
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
  render?: (field: FormField, data: any, editMode?: boolean) => any;
  uploadHandler?:FileUploadHandler;
}
type FormFieldGroup = {
  title: string;
  fields: FormField[];
}
