type AutoFormFieldType =
  | "input"
  | "textarea"
  | "password"
  | "number"
  | "select"
  | "checkbox"
  | "radio"
  | "date"
  | "datetime"
  | "time"
  | "file"
  | "image"
  | "attachment"
  | "color"
  | "rate"
  | "slider"
  | "switch";

type FormFieldOption = {
  label: string | number;
  value: any;
};

type FormField = {
  type?: AutoFormFieldType;
  label?: string | VNode | ((property: any) => VNode);
  prop?: string;
  hint?: string;
  placeholder?: string;
  clearable?: boolean;
  modelValue?: any;
  /**
   * 透传属性
   */
  attrs?: Record<string, any>;
  options?: FormFieldOption[];
  multiple?: boolean;
};
