<script lang="ts">
import {h, ref} from "vue";
import type {VNode} from "vue";
import ExpansionCard from "@/components/ExpansionCard.vue";
import DatePicker from "@/components/DatePicker.vue";
import {
  VCardText,
  VCheckbox,
  VCol,
  VCombobox,
  VDefaultsProvider,
  VFileInput,
  VRadio,
  VRadioGroup,
  VRow,
  VSelect,
  VSwitch,
  VTextarea,
  VTextField
} from "vuetify/components";

const formDefaults = {
  VTextField: {
    density: 'compact',
    variant: 'outlined',
  },
  VTextarea: {
    density: 'compact',
    variant: 'outlined',
  },
  VFileInput: {
    density: 'compact',
    variant: 'outlined',
  },
  VCheckbox: {
    density: 'compact',
  },
  VRadioGroup: {
    density: 'compact',
  },
  VCombobox: {
    density: 'compact',
    variant: 'outlined',
  },
  VSelect: {
    density: 'compact',
    variant: 'outlined',
  },
};

const defaultDateFormats = {
  year: 'yyyy',
  month: 'yyyy-MM',
  date: 'yyyy-MM-dd',
  time: 'HH:mm:ss',
  datetime: 'yyyy-MM-dd HH:mm:ss',
}

function renderDateField(field: FormField, data: any, editMode: boolean | undefined) {
  const type: FieldDateType = typeof field.type === 'object' ? field.type : {type: 'date', subtype: field.type || 'datetime'} as FieldDateType;
  const dpProps: Record<string, any> = {};
  const inputProps: Record<string, any> = {placeholder: field.placeholder, name: field.key, hint: field.hint};
  const isRange = field.range && field.range.length === 1;
  const fmt = field.format || defaultDateFormats[type.subtype];
  if (type.subtype === 'date') {
    dpProps['enable-time-picker'] = false;
  } else if (type.subtype === 'time') {
    dpProps['time-picker'] = true;
  } else if (type.subtype === 'year') {
    dpProps['year-picker'] = true;
  } else if (type.subtype === 'month') {
    dpProps['month-picker'] = true;
  }
  const props = {
    dateType: type.subtype,
    format: fmt,
    dpProps,
    inputProps,
  };
  if (isRange) {
    props['range'] = true;
    props['modelValue'] = [data[field.key], data[field.range![0]]].filter(v => v !== undefined);
    props['onUpdate:modelValue'] = v => {
      data[field.key] = v[0];
      data[field.range![0]] = v[1];
    };
  } else {
    props['modelValue'] = data[field.key];
    props['onUpdate:modelValue'] = v => data[field.key] = v;
  }
  return h(DatePicker, props);
}

const renderField = (field: FormField, data: any, editMode: boolean | undefined): VNode => {
  if (editMode) {

  }
  const commonProps: Record<string, any> = {placeholder: field.placeholder, name: field.key, hint: field.hint, clearable: true};
  if (field.type === 'checkbox') {
    return h('div', {}, field.options!.map((option, i, array) => h(VCheckbox, {
      ...commonProps,
      label: option.label,
      class: i === array.length - 1 ? '' : 'hide-details',
      multiple: !!field.multiple,
      value: option.value,
      modelValue: field.multiple && data[field.key] === undefined ? [] : data[field.key],
      'onUpdate:modelValue': v => data[field.key] = v
    })));
  }
  if (field.type === 'switch') {
    return h(VSwitch, {
      ...commonProps,
      color: 'primary',
      modelValue: data[field.key],
      'onUpdate:modelValue': v => data[field.key] = v,
    } as Record<string, any>);
  }
  if (field.type === 'radio') {
    return h(VRadioGroup, {
      ...commonProps,
      modelValue: data[field.key],
      'onUpdate:modelValue': v => data[field.key] = v
    }, () => field.options!.map((option) => h(VRadio, {label: option.label, value: option.value})));
  }
  if(field.type === 'combobox') {
    return h(VCombobox, {
      ...commonProps,
      multiple: !!field.multiple,
      modelValue: data[field.key],
      'onUpdate:modelValue': v => data[field.key] = v,
      items: field.options,
      itemTitle: 'label',
      itemValue: 'value',
      chips: field.multiple === true
    } as Record<string, any>);
  }
  if(field.type === 'select') {
    return h(VSelect, {
      ...commonProps,
      multiple: !!field.multiple,
      modelValue: data[field.key],
      'onUpdate:modelValue': v => data[field.key] = v,
      items: field.options,
      itemTitle: 'label',
      itemValue: 'value',
      chips: field.multiple === true
    } as Record<string, any>);
  }
  if (field.type === 'date' || field.type === 'datetime' || (typeof field.type === 'object' && field.type.type === 'date')) {
    return renderDateField(field, data, editMode);
  }
  if(field.type === 'textarea') {
    return h(VTextarea, {...commonProps, counter: true, autoGrow: true});
  }
  if(field.type === 'file') {
    return h(VFileInput, {...commonProps, multiple: !!field.multiple});
  }
  return h(VTextField, {...commonProps});
};

const renderLabel = (label: string): VNode => h('div', {class: ['text-subtitle-1', 'text-medium-emphasis']}, label);

const renderFields = (fields: FormField[], data: any, editMode: boolean | undefined): VNode[] => {
  const result: VNode[] = [];
  for (let i = 0, len = fields.length; i < len; i++) {
    const field = fields[i];
    if (!(field.cols === 2 || field.range) && i < len - 1 && fields[i + 1].cols !== 2) {
      const nextField = fields[i + 1];
      result.push(h(VRow, {}, () => [h(VCol, {cols: 6}, () => [renderLabel(field.title), renderField(field, data, editMode)]), h(VCol, {cols: 6}, () => [renderLabel(nextField.title), renderField(nextField, data, editMode)])]))
      i++;
      continue;
    }
    result.push(h(VRow, {}, () => h(VCol, {cols: (field.cols === 2 || field.range) ? 12 : 6}, () => [renderLabel(field.title), renderField(field, data, editMode)])));
  }
  return result;
}

export default {
  props: {
    meta: {
      required: true,
      type: Array as Array<FormFieldGroup | FormField>,
    },
    data: {
      required: false,
      type: Object,
    },
    editMode: {
      type: Boolean,
      default: true,
    }
  },
  setup(props) {
    const isGrouped = ref(props.meta.length > 0 && props.meta[0].hasOwnProperty('fields'));
    let buildChildren: () => VNode | VNode[];
    if (isGrouped) {
      buildChildren = () => {
        return props.meta.map(group => h(ExpansionCard, {
          title: group.title,
          style: {marginBottom: '2rem'}
        }, () => h(VCardText, {}, () => renderFields(group.fields, props.data, props.editMode))));
      }
    } else {
      buildChildren = () => renderFields(props.meta, props.data, props.editMode);
    }
    return () => h(VDefaultsProvider, {defaults: formDefaults} as Record<string, any>, () => h('div', {class: ['auto-form']}, buildChildren()));
  }
};
</script>
<style lang="scss">
//.dp__action_button {
//  white-space: nowrap;
//}
.auto-form {
  .hide-details > .v-input__details {
    display: none;
  }

  .v-selection-control--density-compact {
    min-height: var(--v-selection-control-size);
  }
}

</style>
