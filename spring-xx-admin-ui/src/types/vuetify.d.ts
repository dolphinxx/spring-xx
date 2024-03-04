import type {Ref, ComputedRef, ShallowRef} from "vue";

declare global {
  // vuetify VForm
  type VFormRef = {
    errors: Ref<{
      id: string | number;
      errorMessages: string[];
    }[]>;
    isDisabled: ComputedRef<boolean>;
    isReadonly: ComputedRef<boolean>;
    isValidating: ShallowRef<boolean>;
    isValid: Ref<boolean | null> & {
      readonly externalValue: boolean | null;
    };
    items: Ref<{
      id: string | number;
      validate: () => Promise<string[]>;
      reset: () => void;
      resetValidation: () => void;
      isValid: boolean | null;
      errorMessages: string[];
    }[]>;
    validate: () => Promise<{
      valid: boolean;
      errors: {
        id: string | number;
        errorMessages: string[];
      }[];
    }>;
    reset: () => void;
    resetValidation: () => void;
  } & HTMLFormElement;

  type SelectItemKey = boolean | null | undefined | string | (string | number)[] | ((item: Record<string, any>, fallback?: any) => any);
  type DataTableCompareFunction<T = any> = (a: T, b: T) => number;
  type DataTableHeader = {
    key?: 'data-table-group' | 'data-table-select' | 'data-table-expand' | (string & {});
    value?: SelectItemKey;
    title: string;
    colspan?: number;
    rowspan?: number;
    fixed?: boolean;
    align?: 'start' | 'end' | 'center';
    width?: number | string;
    minWidth?: string;
    maxWidth?: string;
    sortable?: boolean;
    sort?: DataTableCompareFunction;
};
}
