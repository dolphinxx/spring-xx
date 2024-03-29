// copied from vuetify
type ValidationResult = string | boolean;
type ValidationRule = ValidationResult | PromiseLike<ValidationResult> | ((value: any) => ValidationResult) | ((value: any) => PromiseLike<ValidationResult>);


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
  | "slider"
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
  // date, slider
  range?: string;
  // date, slider
  min?: any;
  max?: any;
  // slider
  step?: number;
  cols?: 1 | 2;
  render?: (field: FormField, data: any, editMode?: boolean) => any;
  uploadHandler?:FileUploadHandler;
  rules?: ValidationRule[];
}
type FormFieldGroup = {
  title: string;
  fields: FormField[];
}
