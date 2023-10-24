<template>
  <v-text-field v-bind:value="display" v-bind="inputProps" @keydown.prevent="">
    <template v-slot:prepend-inner>
      <v-icon icon="$mdi-calendar"/>
    </template>
    <v-dialog v-model="dialogState" location-strategy="static" activator="parent" width="auto">
      <vue-date-picker
        ref="dp"
        :locale="localization?.locale"
        :format-locale="localization?.formatLocale"
        :timezone="localization?.timezone"
        :model-value="value"
        @update:model-value="handleDate"
        :enable-seconds="enableSeconds"
        :format="fmt"
        select-text="确定"
        :range="range"
        :partial-range="false"
        :multi-dates="multiple"
        :multi-calendars="range && dateType !== 'year' ? {solo: true} : undefined"
        v-bind="dpProps"
        :inline="true"
      >
        <template v-slot:action-buttons>
          <v-btn density="compact" variant="flat" @click="() => dp.clearValue()">清空</v-btn>
          <v-btn density="compact" variant="flat" color="primary" @click="() => dp.selectDate()">选择</v-btn>
        </template>
      </vue-date-picker>
    </v-dialog>
  </v-text-field>
</template>
<script lang="ts" setup>
import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css';
import {computed, inject, ref} from "vue";
import {format, parse} from "date-fns";
import {LocalizationKey} from "@/symbols";

const localization = inject(LocalizationKey);

const props = defineProps({
  modelValue: {
    type: [String, Date, Array, Number],
    required: false,
  },
  dateType: {
    required: false,
    type: String,
    default: 'datetime',
  },
  range: {// select a range of dates
    required: false,
    type: Boolean,
    default: false,
  },
  multiple: {// TODO: select multiple dates
    required: false,
    type: Boolean,
    default: false,
  },
  format: {
    type: [String, Function],
    required: false,
  },
  delimiter: {
    type: String,
    required: false,
  },
  inputProps: {
    required: false,
    type: Object,
  },
  dpProps: {
    required: false,
    type: Object,
  }
});

const dp = ref();

const enableSeconds = computed(() => !props.dateType || props.dateType === 'datetime' || props.dateType === 'time');

const dialogState = ref(false);
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

const formatTime = (v: PickerTime) => format(new Date(1970, 0, 1, v.hours, v.minutes, v.seconds), fmt.value, {locale: localization?.formatLocale});
const formatMonth = (v: PickerMonth) => format(new Date(v.year, v.month, 1, 0, 0, 0), fmt.value, {locale: localization?.formatLocale});
const parseDateByFormat = (val: string | string[]): Date | Date[] => {
  const referenceDate = new Date();
  if (val instanceof Array) {
    return val.map(v => parse(v, fmt.value, referenceDate, {locale: localization?.formatLocale}));
  }
  return parse(val as string, fmt.value, referenceDate, {locale: localization?.formatLocale});
}
const parseTime = (val: string | string[]): PickerTime | PickerTime[] => {
  const dates = parseDateByFormat(val);
  if (val instanceof Array) {
    return (dates as Array<Date>).map(v => ({hours: v.getHours(), minutes: v.getMinutes(), seconds: v.getSeconds()}));
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

const emit = defineEmits(['update:modelValue']);
const value = computed({
  get() {
    if (!props.modelValue) {
      return props.modelValue;
    }
    // 对于time，转换成{hours, minutes, seconds}格式
    if (props.dateType === 'time') {
      return parseTime(props.modelValue);
    }
    if (props.dateType === 'month') {
      return parseMonth(props.modelValue);
    }
    return props.modelValue
  },
  set(value) {
    emit('update:modelValue', value)
  }
});
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

const handleDate = val => {
  console.log(val)
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
  dialogState.value = false;
  return true;
}
</script>
