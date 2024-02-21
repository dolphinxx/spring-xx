<script setup lang="ts">
import { computed, ref } from "vue";
import { useUploader, type UploaderPropsType } from '@/composables/uploader';
const props = defineProps<UploaderPropsType>();
// switch to defineModel?
const emit = defineEmits(["update:modelValue"]);
const fileInputRef = ref<HTMLInputElement>();
const emitModelValue = (val: any) => emit("update:modelValue", val);
const { items, handleFiles, removeItem, multiple } = useUploader(props, emitModelValue);
// type PropsType = ({
//   modelValue?: string;
//   multiple?: false;
// } | {
//   modelValue?: string[];
//   multiple: true;
// }) & {
//   uploadHandler?: FileUploadHandler;
// }
// type UploadTask = {
//   id: string;
//   name: string;
//   key?: string;
//   url?: string;
//   progress?: number;
//   error?: string;
//   removed?: boolean;
// }

// const appStorage = useAppStore();

// const props = defineProps<PropsType>();
// const uploadHandler = (props.uploadHandler || defaultUploadHandler);
// const fileInputRef = ref<HTMLInputElement>();
// const items = ref<(UploadTask)[]>(props.multiple ? (props.modelValue ? props.modelValue.map(_ => ({ id: nextId(), name: _, key: _, url: uploadHandler.buildUrl!(_, appStorage.settings.uploadedPrefix) })) : []) : (props.modelValue ? [{ id: nextId(), name: props.modelValue, key: props.modelValue, url: uploadHandler.buildUrl!(props.modelValue, appStorage.settings.uploadedPrefix) }] : []));
// // if (props.multiple) {
// //   items.value.push({
// //     id: '1000',
// //     name: '1000.png',
// //     progress: 0,

// //   });
// //   items.value.push({
// //     id: '1001',
// //     name: '1001.png',
// //     progress: 0,
// //     error: 'failed',
// //   });
// //   items.value.push({
// //     id: '1002',
// //     name: '1002.png',
// //     progress: 50,
// //   });
// //   items.value.push({
// //     id: '1003',
// //     name: '1704809557041.jpg',
// //     url: '/api/static/tmp/1704809557041.jpg',
// //   });
// // }

const successUrls = computed(() => items.value.filter(item => !item.removed && item.url).map(item => item.url as string));
// const emit = defineEmits(["update:modelValue"]);
// const handleFiles = async (e: Event) => {
//   const files = (e.target as HTMLInputElement).files!;
//   if (!files || files.length === 0) {
//     return;
//   }
//   if (props.multiple) {
//     for (let i = 0; i < files.length; i++) {
//       const file = files[i];
//       const task: UploadTask = {
//         id: nextId(),
//         name: file.name,
//         progress: 0,
//       };
//       items.value.push(task);
//       uploadHandler.upload(file, (loaded, total) => loaded * 100 / total).then(r => {
//         const item = items.value[items.value.findIndex(_ => !_.removed && typeof _ === 'object' && _.id === task.id)];
//         item.key = r.key;
//         item.url = uploadHandler.buildUrl!(r.key, appStorage.settings.uploadedPrefix);
//         flushValue();
//       }).catch(e => {
//         task.error = String(e) || 'failed';
//       });
//     }
//   } else {
//     const file = files![0];
//     const task: UploadTask = {
//       id: nextId(),
//       name: file.name,
//       progress: 0,
//     };
//     console.log(task);
//     items.value[0] = task;
//     uploadHandler.upload(file, (loaded, total) => loaded / total).then(r => {
//       const item = items.value[items.value.findIndex(_ => !_.removed && typeof _ === 'object' && _.id === task.id)];
//       item.key = r.key;
//       item.url = uploadHandler.buildUrl!(r.key, appStorage.settings.uploadedPrefix);
//       flushValue();
//     }).catch(e => {
//       task.error = String(e) || 'failed';
//     });
//   }
//   (e.target as HTMLInputElement).value = '';
// }
// const flushValue = () => {
//   const val = items.value.map(item => item.key);
//   emit("update:modelValue", props.multiple ? val : (val.length === 0 ? undefined : val[0]));
// }
// const removeItem = (item: UploadTask) => {
//   item.removed = true;
//   setTimeout(() => {
//     const index = items.value.findIndex(_ => _.id === item.id);
//     if (index !== -1) {
//       items.value.splice(index, 1);
//       if (appStorage.settings.logicDeleteUploaded && item.key) {
//         const remove = (props.uploadHandler || defaultUploadHandler).remove;
//         if (remove) {
//           remove(item.key);
//           flushValue();
//         }
//       }
//     }
//   }, 200);
// }
</script>

<template>
  <div>
    <div class="upload-container">
      <div v-if="props.multiple || items.length < 1" class="v-btn upload-item upload-placeholder" @click="fileInputRef?.click()">
        <el-icon class="upload-icon"><icon-ep-plus /></el-icon>
        <input ref="fileInputRef" type="file" :multiple="!!multiple" :accept="props.accept || 'image/*'" style="display:none;" @change="handleFiles">
      </div>
      <template v-for="val in items" :key="val.id">
        <div :class="`upload-item ${val.removed ? 'upload-item-removed' : ''}`" v-bind="props">
          <el-image v-if="val.url" class="upload-item-preview" :src="val.url" fix="contain" :preview-src-list="successUrls" :initial-index="successUrls.indexOf(val.url)" />
          <div v-else class="upload-item-loading">
            <el-progress v-if="!val.error" :width="64" type="circle" :indeterminate="val.progress === 0" :percentage="val.progress" />
            <el-tooltip v-if="val.error" location="top" :content="val.error">
              <el-icon class="upload-error-icon">
                <icon-ep-warning />
              </el-icon>
            </el-tooltip>
          </div>
          <el-icon class="upload-close-icon" @click="removeItem(val)">
            <icon-ep-close />
          </el-icon>
        </div>
      </template>
    </div>
  </div>
</template>

<style scoped lang="scss">
.upload-container {
  display: flex;
  flex-wrap: wrap;
  flex-direction: row;
  justify-content: start;

  .upload-item {
    user-select: none;
    width: 128px;
    height: 128px;
    overflow: hidden;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    margin: 6px;
    position: relative;
    border: 1px dashed var(--el-border-color);
    border-radius: 4px;
    transition: var(--el-transition-duration-fast);

    &:hover {
      border-color: var(--el-color-primary);
    }

    &.upload-item-removed {
      width: 0;
      opacity: 0;
    }

    .upload-icon {
      font-size: 2rem;
      color: var(--el-text-color-placeholder);
    }

    .upload-error-icon {
      font-size: 2rem;
      color: var(--el-color-danger);
    }

    .upload-item-loading {
      display: flex;
      justify-content: center;
      align-items: center;
      font-size: 14px;
      color: rgba(var(--v-theme-primary), var(--v-medium-emphasis-opacity));
    }

    .upload-close-icon {
      font-size: 1.5rem;
      position: absolute;
      top: 0px;
      right: 0px;
      display: none;

      &:hover {
        color: white;
        background-color: var(--el-color-danger);
      }
    }

    &:hover {
      .upload-close-icon {
        display: block;
      }
    }
  }

  .upload-placeholder {
    font-size: 32px;
    color: rgba(var(--v-border-color), var(--v-border-opacity));

    &:hover {
      color: rgba(var(--v-theme-primary), var(--v-medium-emphasis-opacity));
    }
  }
}


.attachment-item {
  cursor: pointer;
  border-radius: 4px;

  .v-list-item__prepend {
    margin-right: 0.5em;
  }

  .v-list-item__content {
    overflow: hidden;
    white-space: nowrap;
    word-break: keep-all;
    text-overflow: ellipsis;
  }
}
</style>
