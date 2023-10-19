<template>
  <v-text-field v-bind:value="display" v-bind="textFieldProps" @keydown.prevent="">
    <template v-slot:prepend-inner>
      <v-icon icon="mdi-calendar"/>
    </template>
    <v-dialog v-model="dialogState" location-strategy="static" activator="parent" width="auto">
      <vue-date-picker
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
        :multi-calendars="range ? {solo: true} : undefined"
        v-bind="datePickerProps"
        :inline="true"
      />
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
  textFieldProps: {
    required: false,
    type: Object,
  },
  datePickerProps: {
    required: false,
    type: Object,
  }
});

const enableSeconds = computed(() => !props.dateType || props.dateType === 'datetime' || props.dateType === 'time');

const dialogState = ref(false);
const fmt = ref(props.format || 'yyyy-MM-dd HH:mm:ss');

const formatTime = v => format(new Date(1970, 0, 1, v.hours, v.minutes, v.seconds), fmt.value, {locale: localization?.formatLocale});
const parseTime = (val: string | string[]) => {
  const referenceDate = new Date();
  if (val instanceof Array) {
    return val.map(v => {
      const time = parse(v, fmt.value, referenceDate, {locale: localization?.formatLocale});
      return {hours: time.getHours(), minutes: time.getMinutes(), seconds: time.getSeconds()};
    });
  }
  const time = parse(val as string, fmt.value, referenceDate, {locale: localization?.formatLocale});
  return {hours: time.getHours(), minutes: time.getMinutes(), seconds: time.getSeconds()};
};

const emit = defineEmits(['update:modelValue']);
const value = computed({
  get() {
    if (!props.modelValue) {
      return props.modelValue;
    }
    // 对于time，转换成{hours, minutes, seconds}格式
    if (props.dateType === 'time') {
      var r = parseTime(props.modelValue);
      console.log(r);
      return r;
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
  if (props.dateType === 'time') {
    if (val instanceof Array) {
      value.value = val.map(formatTime);
    } else {
      value.value = formatTime(val) as any;
    }
  } else {
    value.value = val;
  }
  dialogState.value = false;
  return true;
}
</script>
