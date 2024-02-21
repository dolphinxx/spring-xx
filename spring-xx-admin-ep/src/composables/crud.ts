import {cloneDefaults} from "@/utils/object.ts";

export const prepareFields = (fields: (FormField | string)[], columns: CrudTableColumn[]) => fields.map(f => {
    const isString = typeof f === 'string';
    const propName = isString ? f : f.prop;
    const column = columns.find(c => c.prop === propName);
    const field: FormField = isString ? {prop: propName, label: column?.label} : {...f};
    if (column?.formProps) {
        cloneDefaults(field, column.formProps);
    }
    return field;
})

export const useCrud = () => {

}
