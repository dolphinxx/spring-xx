<script lang="ts">
import {h, ref} from "vue";
import type {VNode, Component} from "vue";
import ExpansionCard from "@/components/ExpansionCard.vue";
import DatePicker from "@/components/DatePicker.vue";
import AttachmentUploader from "@/components/form/AttachmentUploader.vue";
import ImageUploader from "@/components/form/ImageUploader.vue";
import {
  VCardText,
  VCheckbox,
  VCol,
  VCombobox,
  VDefaultsProvider,
  VFileInput,
  VRadio,
  VRadioGroup, VRangeSlider,
  VRow,
  VSelect, VSlider,
  VSwitch,
  VTextarea,
  VTextField
} from "vuetify/components";

import {useDisplay} from "vuetify";
import type {DisplayInstance} from "vuetify";


const formDefaults = {
  VTextField: {
    density: 'compact',
    variant: 'outlined',
  },
  VTextarea: {
    density: 'compact',
    variant: 'outlined',
    counter: true,
    autoGrow: true,
  },
  VFileInput: {
    density: 'compact',
    variant: 'outlined',
  },
  VCheckbox: {
    density: 'compact',
    color: 'primary',
  },
  VRadioGroup: {
    density: 'compact',
    color: 'primary',
  },
  VCombobox: {
    density: 'compact',
    variant: 'outlined',
  },
  VSelect: {
    density: 'compact',
    variant: 'outlined',
  },
  VSwitch: {
    color: 'primary',
  },
  VSlider: {
    color: 'primary',
  },
  VRangeSlider: {
    color: 'primary',
  },
};

const defaultDateFormats = {
  year: 'yyyy',
  month: 'yyyy-MM',
  date: 'yyyy-MM-dd',
  time: 'HH:mm:ss',
  datetime: 'yyyy-MM-dd HH:mm:ss',
}

const simpleFieldComponents: Record<string, Component> = {
  textarea: VTextarea,
  switch: VSwitch,
  file: VFileInput,
  attachment: AttachmentUploader,
  image: ImageUploader,
};

function isDoubleCol(field: FormField): boolean {
  if (field.cols === 2) {
    return true;
  }
  if (field.type === undefined) {
    return false;
  }
  if (field.range) {
    if (typeof field.type === 'string') {
      return field.type === 'date' || field.type === 'datetime';
    }
    return field.type.type === 'date' && (field.type.subtype === 'date' || field.type.subtype === 'datetime');
  }
  return false;
}

function renderDateField(field: FormField, data: any, editMode: boolean | undefined) {
  const type: FieldDateType = typeof field.type === 'object' ? field.type : {type: 'date', subtype: field.type || 'datetime'} as FieldDateType;
  const dpProps: Record<string, any> = {};
  const inputProps: Record<string, any> = {placeholder: field.placeholder, name: field.key, hint: field.hint, rules: field.rules};
  const isRange = field.range;
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
    props['modelValue'] = [data[field.key], data[field.range]].filter(v => v !== undefined);
    props['onUpdate:modelValue'] = v => {
      if (v) {
        data[field.key] = v[0];
        data[field.range] = v[1];
      } else {
        data[field.key] = undefined;
        data[field.range] = undefined;
      }
    };
  } else {
    props['modelValue'] = data[field.key];
    props['onUpdate:modelValue'] = v => data[field.key] = v;
  }
  return h(DatePicker, props);
}

const renderField = (field: FormField, data: any, editMode: boolean | undefined): VNode => {
  if (field.render) {
    return field.render(field, data, editMode);
  }
  if (editMode) {

  }
  if (field.type === 'date' || field.type === 'datetime' || (typeof field.type === 'object' && field.type.type === 'date')) {
    return renderDateField(field, data, editMode);
  }
  const commonProps: Record<string, any> = {placeholder: field.placeholder, name: field.key, hint: field.hint, clearable: true, rules: field.rules};
  if (field.range) {
    commonProps['modelValue'] = [data[field.key], data[field.range]].filter(v => hasValue(v));
    commonProps['onUpdate:modelValue'] = v => {
      data[field.key] = v[0];
      data[field.range] = v[1];
    };
  } else {
    commonProps['modelValue'] = field.multiple && !hasValue(data[field.key]) ? [] : data[field.key];
    commonProps['onUpdate:modelValue'] = v => data[field.key] = v;
  }
  if (!!field.multiple) {
    commonProps['multiple'] = true;
  }
  if (field.type === 'checkbox') {
    delete commonProps.name;
    return h('div', {}, field.options!.map((option, i, array) => h(VCheckbox, {
      ...commonProps,
      label: option.label,
      class: i === array.length - 1 ? '' : 'hide-details',
      value: option.value,
    })));
  }
  if (field.type === 'radio') {
    delete commonProps.name;
    return h(VRadioGroup, commonProps, () => field.options!.map((option) => h(VRadio, {label: option.label, value: option.value})));
  }
  if (field.type === 'combobox' || field.type === 'select') {
    commonProps.items = field.options;
    commonProps.itemTitle = 'label';
    commonProps.itemValue = 'value';
    commonProps.chips = field.multiple === true;
    return h(field.type === 'combobox' ? VCombobox : VSelect, commonProps);
  }
  if (field.type === 'slider') {
    commonProps['thumb-label'] = true;
    commonProps.strict = true;
    commonProps.min = field.min === undefined ? 0 : field.min;
    commonProps.max = field.max === undefined ? 100 : field.max;
    if (field.step) {
      commonProps.step = field.step;
    }
    if (field.range) {
      commonProps['modelValue'] = [data[field.key] || 0, data[field.range] || 0];
      return h(VRangeSlider, commonProps);
    }
    return h(VSlider, commonProps);
  }
  const simpleComponent = simpleFieldComponents[field.type];
  if (simpleComponent) {
    return h(simpleComponent, commonProps);
  }
  return h(VTextField, commonProps);
};

const renderLabel = (label: string): VNode => h('div', {class: ['text-subtitle-1', 'text-medium-emphasis']}, label);

const renderFields = (fields: FormField[], data: any, display: DisplayInstance, editMode: boolean | undefined): VNode[] => {
  const result: VNode[] = [];
  for (let i = 0, len = fields.length; i < len; i++) {
    const field = fields[i];
    if (!isDoubleCol(field) && i < len - 1 && !isDoubleCol(fields[i + 1])) {
      const nextField = fields[i + 1];
      result.push(h(VRow, {}, () => [
        h(VCol, {cols: display.smAndUp.value ? 6 : 12}, () => [renderLabel(field.title), renderField(field, data, editMode)]),
        h(VCol, {cols: display.smAndUp.value ? 6 : 12}, () => [renderLabel(nextField.title), renderField(nextField, data, editMode)])
      ]));
      i++;
      continue;
    }
    result.push(h(VRow, {}, () => h(VCol, {cols: !display.smAndUp.value || isDoubleCol(field) ? 12 : 6}, () => [renderLabel(field.title), renderField(field, data, editMode)])));
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
    const display = useDisplay();
    const isGrouped = ref(props.meta.length > 0 && props.meta[0].hasOwnProperty('fields'));
    let buildChildren: () => VNode | VNode[];
    if (isGrouped) {
      buildChildren = () => {
        return props.meta.map(group => h(ExpansionCard, {
          title: group.title,
          style: {marginBottom: '2rem'}
        }, () => h(VCardText, {}, () => renderFields(group.fields, props.data, display, props.editMode))));
      }
    } else {
      buildChildren = () => renderFields(props.meta, props.data, display, props.editMode);
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
