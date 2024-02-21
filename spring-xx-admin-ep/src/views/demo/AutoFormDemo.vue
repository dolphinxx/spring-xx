<script setup lang="ts">
import { type FormInstance } from 'element-plus';
import { ref, toRaw } from 'vue';
import ImageUploader from '@/components/form/ImageUploader.vue';
import AttachmentUploader from '@/components/form/AttachmentUploader.vue';

const searchOptions: FormField[] = [
  {
    prop: 'field11',
  },
  {
    label: '字段1',
    prop: 'field1',
  },
  {
    label: '数字',
    prop: 'number',
    type: 'number',
    modelValue: 3,
    attrs: {
      min: 2,
      max: 5,
    },
  },
  {
    label: '选择',
    prop: 'select',
    type: 'select',
    options: [
      {
        label: '选项1',
        value: 1
      },
      {
        label: '选项2',
        value: 2
      },
      {
        label: '选项3',
        value: 3
      },
    ]
  },
  {
    label: '单选',
    prop: 'radio',
    type: 'radio',
    modelValue: 2,
    options: [
      {
        label: '选项1',
        value: 1
      },
      {
        label: '选项2',
        value: 2
      },
      {
        label: '选项3',
        value: 3
      },
    ]
  },
  {
    label: '多选',
    prop: 'checkbox',
    type: 'checkbox',
    options: [
      {
        label: '选项1',
        value: 1
      },
      {
        label: '选项2',
        value: 2
      },
      {
        label: '选项3',
        value: 3
      },
    ]
  },
  {
    label: '单个多选',
    prop: 'checkbox2',
    type: 'checkbox',
    modelValue: true,
  },
  {
    label: '日期',
    prop: 'date',
    type: 'date',
  },
  {
    label: '日期+时间',
    prop: 'datetime',
    type: 'datetime',
  },
  {
    label: '时间',
    prop: 'time',
    type: 'time',
  },
  {
    label: '颜色',
    prop: 'color',
    type: 'color',
  },
  {
    label: '评分',
    prop: 'rate',
    type: 'rate',
  },
  {
    label: '滑块',
    prop: 'slider',
    type: 'slider',
    attrs: {
      min: 5,
      max: 120,
    }
  },
  {
    label: '开关',
    prop: 'switch',
    type: 'switch',
  },
  {
    label: '单图片',
    prop: 'image',
    type: 'image',
  },
  {
    label: '多图片',
    prop: 'images',
    type: 'image',
    multiple: true,
  },
  {
    label: '单附件',
    prop: 'attachment',
    type: 'attachment',
    attrs: {
      accept: 'video/*'
    }
  },
  {
    label: '多附件',
    prop: 'attachments',
    type: 'attachment',
    multiple: true,
  },
]
const searchFormRef = ref<FormInstance>();
const searchParams = ref((function () {
  const _: Record<string, any> = {};
  for (const field of searchOptions) {
    _[field.prop!] = field.modelValue;
  }
  return _;
}()));
const submit = () => {
  console.log(toRaw(searchParams.value));
}
const reset = () => {
  searchFormRef.value?.resetFields();
}

const uploadedImages = ref([

])
const uploadedImage = ref();
const attachment = ref();
const attachments = ref([]);
</script>

<template>
  <view-container>
    <el-card>
      <el-form ref="searchFormRef" :inline="true" :model="searchParams" class="crud-form">
        <auto-form :inline="true" :fields="searchOptions" v-model="searchParams"></auto-form>
        <el-button type="primary" @click="submit()">搜索</el-button>
        <el-button @click="reset()">重置</el-button>
      </el-form>
    </el-card>
    <el-card>
      <el-form ref="searchFormRef" :model="searchParams" label-position="top" class="crud-form">
        <auto-form :fields="searchOptions" v-model="searchParams"></auto-form>
        <el-button type="primary" @click="submit()">搜索</el-button>
        <el-button @click="reset()">重置</el-button>
      </el-form>
    </el-card>
    <el-card>
      <image-uploader :multiple="true" :items="uploadedImages"></image-uploader>
    </el-card>
    <el-card>
      <image-uploader :items="uploadedImage"></image-uploader>
    </el-card>
    <el-card>
      <attachment-uploader :items="attachment"></attachment-uploader>
    </el-card>
    <el-card>
      <attachment-uploader :multiple="true" :items="attachments"></attachment-uploader>
    </el-card>
  </view-container>
</template>
