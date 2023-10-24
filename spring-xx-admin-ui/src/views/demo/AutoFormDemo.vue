<template>
  <v-container>
    <auto-form :meta="meta" :data="data"/>
  </v-container>
</template>
<script lang="ts" setup>
import AutoForm from "@/components/AutoForm.vue";
import {ref} from "vue";
const meta: FormFieldGroup[] = [
  {
    title: 'Input',
    fields: [
      {
        key: 'input',
        title: 'Input',
      },
      {
        key: 'placeholder',
        title: 'Placeholder',
        placeholder: 'input placeholder',
      },
      {
        key: 'hint',
        title: 'Hint',
        hint: 'input hint',
      },
      {
        key: 'textarea',
        title: 'Textarea',
        type: 'textarea',
        hint: 'input hint',
        placeholder: 'Placeholder',
        cols: 2,
      },
      {
        key: 'file',
        title: 'File',
        type: 'file'
      },
      {
        key: 'fileMultiple',
        title: 'File Multiple',
        type: 'file',
        multiple: true,
      },
      {
        key: 'attachment',
        title: 'Attachment',
        type: 'attachment'
      },
      {
        key: 'attachmentMultiple',
        title: 'Attachment Multiple',
        type: 'attachment',
        multiple: true,
      },
      {
        key: 'image',
        title: 'Image',
        type: 'image'
      },
      {
        key: 'imageMultiple',
        title: 'Image Multiple',
        type: 'image',
        multiple: true,
      },
    ],
  },
  {
    title: 'Checkbox',
    fields: [
      {
        key: 'checkbox',
        title: 'Checkbox',
        type: 'checkbox',
        options: [
          {
            label: 'Label1',
            value: 'value1',
          },
          {
            label: 'Label2',
            value: 'value2',
          },
          {
            label: 'Label3',
            value: 'value3',
          },
        ]
      },
      {
        key: 'checkbox2',
        title: 'Checkbox',
        type: 'checkbox',
        options: [
          {
            label: 'Label1',
            value: 'value1',
          },
          {
            label: 'Label2',
            value: 'value2',
          },
          {
            label: 'Label3',
            value: 'value3',
          },
        ]
      },
      {
        key: 'checkboxMultiple',
        title: 'Multiple',
        type: 'checkbox',
        multiple: true,
        options: [
          {
            label: 'Label1',
            value: 'value1',
          },
          {
            label: 'Label2',
            value: 'value2',
          },
          {
            label: 'Label3',
            value: 'value3',
          },
        ]
      },
      {
        key: 'checkboxMultiple2',
        title: 'Multiple',
        type: 'checkbox',
        multiple: true,
        options: [
          {
            label: 'Label1',
            value: 'value1',
          },
          {
            label: 'Label2',
            value: 'value2',
          },
          {
            label: 'Label3',
            value: 'value3',
          },
        ]
      },
    ],
  },
  {
    title: 'Radio',
    fields: [
      {
        key: 'radio',
        title: 'Radio',
        type: 'radio',
        options: [
          {
            label: 'Label1',
            value: 'value1',
          },
          {
            label: 'Label2',
            value: 'value2',
          },
          {
            label: 'Label3',
            value: 'value3',
          },
        ]
      },
    ],
  },
  {
    title: 'Switch',
    fields: [
      {
        key: 'switch',
        title: 'Switch',
        type: 'switch',
      },
      {
        key: 'switch2',
        title: 'Switch',
        type: 'switch',
      },
    ],
  },
  {
    title: 'Combobox',
    fields: [
      {
        key: 'combobox',
        title: 'Combobox',
        type: 'combobox',
        options: [
          {
            label: 'Label1',
            value: 'value1',
          },
          {
            label: 'Label2',
            value: 'value2',
          },
          {
            label: 'Label3',
            value: 'value3',
          },
        ]
      },
      {
        key: 'comboboxMultiple',
        title: 'Multiple',
        type: 'combobox',
        multiple: true,
        options: [
          {
            label: 'Label1',
            value: 'value1',
          },
          {
            label: 'Label2',
            value: 'value2',
          },
          {
            label: 'Label3',
            value: 'value3',
          },
        ]
      },
    ],
  },
  {
    title: 'Select',
    fields: [
      {
        key: 'select',
        title: 'Select',
        type: 'select',
        options: [
          {
            label: 'Label1',
            value: 'value1',
          },
          {
            label: 'Label2',
            value: 'value2',
          },
          {
            label: 'Label3',
            value: 'value3',
          },
        ]
      },
      {
        key: 'selectMultiple',
        title: 'Multiple',
        type: 'select',
        multiple: true,
        options: [
          {
            label: 'Label1',
            value: 'value1',
          },
          {
            label: 'Label2',
            value: 'value2',
          },
          {
            label: 'Label3',
            value: 'value3',
          },
        ]
      },
    ],
  },
  {
    title: '时间选取',
    fields: [
      {
        key: 'date',
        title: 'Date',
        type: 'date',
      },
      {
        key: 'date1',
        title: 'Date',
        type: 'date',
      },
      {
        key: 'time',
        title: 'Time',
        type: {type: 'date', subtype: 'time'},
      },
      {
        key: 'time1',
        title: 'Time',
        type: {type: 'date', subtype: 'time'},
      },
      {
        key: 'dateTime',
        title: 'DateTime',
        type: 'datetime',
      },
      {
        key: 'dateTime1',
        title: 'DateTime',
        type: 'datetime',
      },
      {
        key: 'year',
        title: 'Year',
        type: {type: 'date', subtype: 'year'},
      },
      {
        key: 'year1',
        title: 'Year',
        type: {type: 'date', subtype: 'year'},
      },
      {
        key: 'month',
        title: 'Month',
        type: {type: 'date', subtype: 'month'},
      },
      {
        key: 'month1',
        title: 'Month',
        type: {type: 'date', subtype: 'month'},
      },
      {
        key: 'startDate',
        title: 'Date区间',
        type: 'date',
        range: ['endDate']
      },
      {
        key: 'startTime',
        title: 'Time区间',
        type: {type: 'date', subtype: 'time'},
        range: ['endTime']
      },
      {
        key: 'startDateTime',
        title: 'DateTime区间',
        type: 'datetime',
        range: ['endDateTime']
      },
      {
        key: 'startYear',
        title: 'Year区间',
        type: {type: 'date', subtype: 'year'},
        range: ['endYear']
      },
      {
        key: 'startMonth',
        title: 'Month区间',
        type: {type: 'date', subtype: 'month'},
        range: ['endMonth']
      },
    ],
  }
];
const data = ref({
  input: 1,
  textarea: '',
  placeholder: '管理员',
  hint: 'admin',
  checkbox: 'value2',
  switch: true,
  attachment: undefined,
  attachmentMultiple: [],
  checkboxMultiple: ['value1', 'value3'],
  date: new Date('2000-01-01T01:01:01'),
  dateTime: new Date('2000-01-01T01:01:01'),
  month: '2000-01',
  startDate: '2000-01-01',
  endDate: '2000-01-02',
  startDateTime: new Date('2000-01-01T01:01:01'),
  endDateTime: new Date('2000-02-01T01:01:01'),
  createDate: new Date('2000-02-01T01:01:01'),
  year: new Date('2000-02-01T01:01:01'),
  time: '01:01:01',
  startTime: '01:01:01',
  endTime: '01:01:02',
});
</script>
