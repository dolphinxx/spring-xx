<script lang="ts">
import {h, ref} from "vue";
import type {VNode} from "vue";
import ExpansionCard from "@/components/ExpansionCard.vue";
import DatePicker from "@/components/DatePicker.vue";
import {VCardText, VCol, VRow, VTextField} from "vuetify/components";

const defaultDateFormats = {
  year: 'yyyy',
  month: 'MM',
  date: 'yyyy-MM-dd',
  time: 'HH:mm:ss',
  datetime: 'yyyy-MM-dd HH:mm:ss',
}

const renderField = (field:FormField, data:any, editMode:boolean|undefined):VNode => {
  if(editMode) {

  }
  if(field.type === 'date' || field.type === 'datetime' || (typeof field.type === 'object' && field.type.type === 'date')) {
    let type = field.type;
    if(typeof type !== 'object') {
      type = {type: 'date', subtype: type};
    }
    const datePickerProps:Record<string, any> = {};
    const textFieldProps:Record<string, any> = {placeholder: field.placeholder, density: 'compact', variant: 'outlined', hint: field.hint};
    const isRange = field.range && field.range.length === 1;
    const fmt = field.format || defaultDateFormats[type.subtype];
    if(type.subtype === 'date') {
      datePickerProps['enable-time-picker'] = false;
    } else if(type.subtype === 'time') {
      datePickerProps['time-picker'] = true;
    } else if(type.subtype === 'year') {
      datePickerProps['year-picker'] = true;
    }
    const props = {
      dateType: type.subtype,
      format: fmt,
      datePickerProps,
      textFieldProps,
    };
    if(isRange) {
      props['range'] = true;
      props['delimiter'] = ' -- ';
      props['modelValue'] = [data[field.key], data[field.range![0]]];
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
  return h(VTextField, {placeholder: field.placeholder, density: 'compact', variant: 'outlined', hint: field.hint});
};

const renderLabel = (label:string):VNode => h('div', {class: ['text-subtitle-1', 'text-medium-emphasis']}, label);

const renderFields = (fields: FormField[], data:any, editMode:boolean|undefined):VNode[] => {
  const result:VNode[] = [];
  for(let i = 0,len=fields.length;i < len;i++) {
    const field = fields[i];
    if(!(field.cols === 2 || field.range) && i < len - 1 && fields[i + 1].cols !== 2) {
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
      type: Array as Array<FormFieldGroup|FormField>,
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
    let buildChildren:() => VNode|VNode[];
    if(isGrouped) {
      buildChildren = () => {
        return props.meta.map(group => h(ExpansionCard, {title: group.title, style: {marginBottom: '2rem'}}, () => h(VCardText, {}, () => renderFields(group.fields, props.data, props.editMode))));
      }
    } else {
      buildChildren = () => renderFields(props.meta, props.data, props.editMode);
    }
    return () => h('div', {}, buildChildren());
  }
};
</script>
<style>
.dp__action_button {
  white-space: nowrap;
}
</style>
