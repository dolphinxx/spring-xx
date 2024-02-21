<script setup lang="ts">
import { h } from 'vue';
import AutoFormField from '@/components/form/AutoFormField.vue';

const props = defineProps<{ fields: FormField[]; modelValue: any; inline?: boolean }>();
const emit = defineEmits(['update:modelValue'])

const renderFields = () => {
  return props.inline ? props.fields.map(field => h(AutoFormField, {
    ...field,
    modelValue: props.modelValue[field.prop!],
    'onUpdate:modelValue': (v: any) => {
      const newValue = {...props.modelValue};
      newValue[field.prop!] = v;
      emit('update:modelValue', newValue);
    },
  })) : h(ElRow, {gutter: 20}, () => props.fields.map(field => h(ElCol, { span: 12 }, () => h(AutoFormField, {
    ...field,
    modelValue: props.modelValue[field.prop!],
    'onUpdate:modelValue': (v: any) => {
      const newValue = {...props.modelValue};
      newValue[field.prop!] = v;
      emit('update:modelValue', newValue);
    },
  }))));
};

const render = () => renderFields();

</script>
<template>
  <render/>
</template>
