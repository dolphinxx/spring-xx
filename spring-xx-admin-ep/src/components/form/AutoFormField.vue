<script setup lang="ts">
import { createTextVNode, h, type VNode } from 'vue'
import ImageUploader from './ImageUploader.vue';
import AttachmentUploader from './AttachmentUploader.vue';

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
}

const props = defineProps<FormField>();
const emit = defineEmits(['update:modelValue']);

const renderVNode = (param: string | VNode | ((property: any) => VNode)) => {
  const type = typeof param;
  if (type === 'string') {
    return createTextVNode(param as string);
  }
  if (type === 'function') {
    return (param as Function)();
  }
  return param;
}
const inputTypes = ['input', 'text', 'textarea', 'password'];
const renderField = () => {
  const fieldProps: Record<string, any> = {
    ...props.attrs,
    name: props.prop,
    clearable: true,
    multiple: props.multiple,
    placeholder: props.placeholder,
    modelValue: props.modelValue,
    'onUpdate:modelValue': (v: any) => emit('update:modelValue', v),
  }
  if (props.type === undefined || inputTypes.includes(props.type)) {
    fieldProps.type = props.type === 'textarea' || props.type === 'password' ? props.type : 'text';
    return h(ElInput, fieldProps);
  }
  if (props.type === 'number') {
    return h(ElInputNumber, fieldProps);
  }
  if (props.type === 'select') {
    return h(ElSelect, fieldProps, () => props.options!.map((option, i) => h(ElOption, { key: i, label: option.label, value: option.value })));
  }
  if (props.type === 'radio') {
    return h(ElRadioGroup, fieldProps, () => props.options!.map((option, i) => h(ElRadio, { key: i, label: option.value }, () => option.label)));
  }
  if (props.type === 'checkbox') {
    if (!props.options) {
      fieldProps.label = props.modelValue;
      return h(ElCheckbox, fieldProps, () => props.label);
    }
    return h(ElCheckboxGroup, fieldProps, () => props.options!.map((option, i) => h(ElCheckbox, { key: i, label: option.value }, () => option.label)));
  }
  if (props.type === 'date' || props.type === 'datetime') {
    if (!fieldProps.type) {
      fieldProps.type = props.type;
    }
    return h(ElDatePicker, fieldProps);
  }
  if (props.type === 'time') {
    return h(ElTimePicker, fieldProps);
  }
  if (props.type === 'color') {
    return h(ElColorPicker, fieldProps);
  }
  if (props.type === 'rate') {
    return h(ElRate, fieldProps);
  }
  if (props.type === 'slider') {
    return h(ElSlider, fieldProps);
  }
  if (props.type === 'switch') {
    return h(ElSwitch, fieldProps);
  }
  if(props.type === 'image') {
    // fieldProps.action = props.action ?? appStorage.settings.uploadUrl;
    // fieldProps.class = 'image-uploader';
    // fieldProps['show-file-list'] = false;
    // fieldProps.onSuccess = (response:any) => {
    //   emit('update:modelValue', response.data.url);
    // }
    // return h(ElUpload, fieldProps, () => props.modelValue ? h('img', {src: props.modelValue, class: 'image-uploader-preview'}) : h(ElIcon, {class: 'image-uploader-icon'}, () => h(Plus)));
    return h(ImageUploader, fieldProps);
  }
  if(props.type === 'attachment') {
    return h(AttachmentUploader, fieldProps);
  }
}

const render = () => h(ElFormItem, { prop: props.prop }, {
  label: props.label && !(props.type === 'checkbox' && !props.options) ? () => renderVNode(props.label!) : undefined,
  default: renderField,
});
</script>

<template>
  <render />
</template>
