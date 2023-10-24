<template>
  <vue-date-picker
    ref="dp"
    :model-value="value"
    @update:model-value="handleValue"
    :clearable="false"
    :locale="localization?.locale"
    :format-locale="localization?.formatLocale"
    :timezone="localization?.timezone"
    :enable-seconds="enableSeconds"
    :format="fmt"
    :range="range"
    :partial-range="false"
    :teleport="true"
    v-bind="dpProps"
  >
    <template v-slot:action-buttons>
      <v-btn density="compact" variant="flat" @click="() => dp.clearValue()">清空</v-btn>
      <v-btn density="compact" variant="flat" color="primary" @click="() => dp.selectDate()">选择</v-btn>
    </template>
    <template v-slot:dp-input>
      <v-text-field
        :model-value="display"
        @click:clear="() => dp.clearValue()"
        style="box-sizing: border-box"
        @keydown.prevent=""
        :clearable="true"
        v-bind="inputProps"
      >
        <template v-slot:prepend-inner>
          <v-icon icon="$mdi-calendar"/>
        </template>
      </v-text-field>
    </template>
  </vue-date-picker>
</template>
<script lang="ts" setup>
import {computed, inject, ref} from "vue";
import VueDatePicker from '@vuepic/vue-datepicker';
import type {ModelValue} from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css';

import {format, parse} from "date-fns";
import {LocalizationKey} from "@/symbols";

const localization = inject(LocalizationKey);

type PropsType = {
  modelValue?: string | number | Date | Array<string | number | Date>;
  dateType?: FieldDateType['subtype'];
  range?: boolean;
  multiple?: boolean;
  format?: string;
  delimiter?: string;
  dpProps?: Record<string, any>;
  inputProps?: Record<string, any>;
};
const props = defineProps<PropsType>();
const emit = defineEmits(['update:modelValue']);
const dp = ref();
const enableSeconds = computed(() => !props.dateType || props.dateType === 'datetime' || props.dateType === 'time');
const fmt = ref(props.format || 'yyyy-MM-dd HH:mm:ss');

type PickerTime = {
  hours: number;
  minutes: number;
  seconds: number;
}
type PickerMonth = {
  year: number;
  month: number;
}

const formatTime = (v: PickerTime) => v ? format(new Date(1970, 0, 1, v.hours, v.minutes, v.seconds), fmt.value, {locale: localization?.formatLocale}) : '';
const formatMonth = (v: PickerMonth) => v ? format(new Date(v.year, v.month, 1, 0, 0, 0), fmt.value, {locale: localization?.formatLocale}) : '';
const parseDateByFormat = (val: string | string[]): Date | Date[] => {
  const referenceDate = new Date();
  if (val instanceof Array) {
    return val.map(v => parse(v, fmt.value as string, referenceDate, {locale: localization?.formatLocale}));
  }
  return parse(val as string, fmt.value as string, referenceDate, {locale: localization?.formatLocale});
}
const parseTime = (val: string | string[]): PickerTime | PickerTime[] => {
  const dates = parseDateByFormat(val);
  if (val instanceof Array) {
    return (dates as Array<Date>).map(vv => {
      const v = vv || new Date();
      return {hours: v.getHours(), minutes: v.getMinutes(), seconds: v.getSeconds()};
    });
  }
  return {hours: (dates as Date).getHours(), minutes: (dates as Date).getMinutes(), seconds: (dates as Date).getSeconds()};
};

const parseMonth = (val: string | string[]): PickerMonth | PickerMonth[] => {
  const dates = parseDateByFormat(val);
  if (val instanceof Array) {
    return (dates as Array<Date>).map(v => ({year: v.getFullYear(), month: v.getMonth()}));
  }
  return {year: (dates as Date).getFullYear(), month: (dates as Date).getMonth()};
}

const value = computed<ModelValue>({
  get() {
    if (props.modelValue === undefined) {
      return props.range || props.multiple ? [] : null;
    }
    // 对于time，转换成{hours, minutes, seconds}格式
    if (props.dateType === 'time') {
      return parseTime(props.modelValue as string | string[]);
    }
    if (props.dateType === 'month') {
      return parseMonth(props.modelValue as string | string[]);
    }
    return props.modelValue as ModelValue;
  },
  set(value) {
    emit('update:modelValue', value)
  }
});
const handleValue = val => {
  if (props.dateType === 'time') {
    if (val instanceof Array) {
      value.value = val.map(formatTime);
    } else {
      value.value = formatTime(val) as any;
    }
  } else if (props.dateType === 'month') {
    if (val instanceof Array) {
      value.value = val.map(formatMonth);
    } else {
      value.value = formatMonth(val) as any;
    }
  } else {
    value.value = val;
  }
  return true;
}
const display = computed(() => {
  if (!props.modelValue) {
    return '';
  }
  const modelVal: string | Date | Number | Array<string | Date | Number> = props.modelValue!;
  if (modelVal instanceof Array) {
    return modelVal.map(v => v instanceof Date ? format(v, fmt.value, {locale: localization?.formatLocale}) : v).join(props.multiple ? ', ' : (props.delimiter || ' - '));
  } else {
    if (modelVal instanceof Date) {
      return format(modelVal, fmt.value, {locale: localization?.formatLocale});
    }
    return modelVal;
  }
});
</script>
