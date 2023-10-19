<template>
  <v-container>
    <auto-form :meta="meta" :data="data"/>
    <vue-date-picker v-model="data.createTime" format="yyyy-MM-dd HH:mm:ss" locale="zh-CN" :format-locale="zhCN"></vue-date-picker>
    <date-picker v-model="data.createTime"></date-picker>
  </v-container>
</template>
<script lang="ts" setup>
import AutoForm from "@/components/AutoForm.vue";
import VueDatePicker from "@vuepic/vue-datepicker";
import {zhCN} from "date-fns/locale";
import {ref} from "vue";
import DatePicker from "@/components/DatePicker.vue";
const meta:FormFieldGroup[] = [
  {
    title: '基本信息',
    fields: [
      {
        key: 'id',
        title: 'ID',
      },
      {
        key: 'name',
        title: '名称',
        placeholder: '请输入昵称',
      },
      {
        key: 'username',
        title: '账号',
        hint: '用于登录的账号',
      },
    ],
  },
  {
    title: '审核信息',
    fields: [
      {
        key: 'creatorId',
        title: '创建者',
      },
      {
        key: 'lastModifierId',
        title: '修改者',
      },
      {
        key: 'lastModifyTime',
        title: '修改时间',
        type: 'datetime',
      },
    ],
  },
  {
    title: '其它信息',
    fields: [
      {
        key: 'startTime',
        title: '修改时间',
        type: 'datetime',
        range: ['endTime']
      },
      {
        key: 'createDate',
        title: '创建日期',
        type: 'date',
      },
      {
        key: 'createYear',
        title: '创建年份',
        type: {type: 'date', subtype: 'year'},
      },
      {
        key: 'time',
        title: '时间',
        type: {type: 'date', subtype: 'time'},
      },
      {
        key: 'time1',
        title: '时间',
        type: {type: 'date', subtype: 'time'},
        range: ['time2']
      },
    ],
  }
];
const data = ref({
  id: 1,
  name: '管理员',
  username: 'admin',
  createTime: '2000-01-01 01:01:01',
  startTime: new Date('2000-01-01T01:01:01'),
  endTime: new Date('2000-02-01T01:01:01'),
  createDate: new Date('2000-02-01T01:01:01'),
  createYear: new Date('2000-02-01T01:01:01'),
  time: '01:01:01',
  time1: '01:01:01',
  time2: '01:01:01',
});
</script>
