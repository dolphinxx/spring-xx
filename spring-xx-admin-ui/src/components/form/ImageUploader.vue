<template>
  <div>
    <div class="upload-container">
      <div class="v-btn upload-item upload-placeholder" @click="fileInput?.click()">
        <v-icon icon="$mdi-plus"/>
        <input ref="fileInput" type="file" :multiple="!!multiple" style="display:none;" @change="handleFiles">
      </div>
      <template v-for="(val, i) in items" :key="i">
        <v-hover v-slot="{ isHovering, props }">
          <div class="upload-item" v-bind="props">
            <div v-if="typeof val === 'string'" class="upload-item-preview" :style="{'background-image': renderImage(val)}"></div>
            <div v-if="typeof val === 'object'" class="upload-item-preview upload-item-loading">
              <v-progress-circular v-if="!val.error" :size="36" :width="18" :model-value="val.progress"></v-progress-circular>
              <v-tooltip v-if="val.error" location="top" :text="val.error">
                <template v-slot:activator="{props}">
                  <v-icon v-bind="props" color="error" :size="32" icon="$mdi-alert-circle"/>
                </template>
              </v-tooltip>
            </div>
            <v-btn v-if="isHovering" class="upload-item-close" variant="flat" color="error" icon="$mdi-close" :size="32" @click="removeItem(val)"/>
          </div>
        </v-hover>
      </template>
    </div>
    <v-input v-model="value" v-bind="$attrs"/>
  </div>
</template>
<script lang="ts" setup>
import {computed, ref} from "vue";
import {uploadHandler as defaultUploadHandler} from "@/api/common";
import {nextId} from "@/utils";

type PropsType = ({
  modelValue?: string;
  multiple?: false;
} | {
  modelValue?: string[];
  multiple: true;
}) & {
  uploadHandler?: FileUploadHandler;
}
type UploadTask = {
  id: string;
  name: string;
  progress: number;
  error?: string;
}
const props = defineProps<PropsType>();
const renderImage = (key:string) => `url(${(props.uploadHandler || defaultUploadHandler).buildUrl!(key)})`;
const fileInput = ref<HTMLInputElement>();
const items = ref<(string | UploadTask)[]>(props.multiple ? (props.modelValue ? [...props.modelValue] : []) : (props.modelValue ? [props.modelValue] : []));
const emit = defineEmits(["update:modelValue"]);
const value = computed<string | string[] | undefined>({
  get() {
    return props.multiple && props.modelValue === undefined ? [] : props.modelValue;
  },
  set(val) {
    emit("update:modelValue", val);
  }
});
const handleFiles = async (e:Event) => {
  const files = (e.target as HTMLInputElement).files!;
  if (!files || files.length === 0) {
    return;
  }
  if (props.multiple) {
    for (let i = 0;i < files.length;i++) {
      const file = files[i];
      const task: UploadTask = {
        id: nextId(),
        name: file.name,
        progress: 0,
      };
      items.value.push(task);
      (props.uploadHandler || defaultUploadHandler).upload(file, (loaded, total) => loaded * 100 / total).then(r => {
        items.value[items.value.findIndex(_ => typeof _ === 'object' && _.id === task.id)] = r.key;
        flushTasks();
      }).catch(e => {
        task.error = String(e) || 'failed';
      });
    }
  } else {
    const file = files![0];
    const task: UploadTask = {
      id: nextId(),
      name: file.name,
      progress: 0,
    };
    console.log(task);
    items.value[0] = task;
    (props.uploadHandler || defaultUploadHandler).upload(file, (loaded, total) => loaded / total).then(r => {
      items.value[items.value.findIndex(_ => typeof _ === 'object' && _.id === task.id)] = r.key;
      flushTasks();
    }).catch(e => {
      task.error = String(e) || 'failed';
    });
  }
  (e.target as HTMLInputElement).value = '';
}
const flushTasks = () => {
  if (items.value.every(_ => typeof _ === 'string')) {
    if (props.multiple) {
      value.value = [...(items.value as string[])];
    } else {
      value.value = items.value.length === 0 ? undefined : items.value[0] as string;
    }
  }
}
const removeItem = (key: string | UploadTask) => {
  items.value.splice(items.value.indexOf(key), 1);
  if (typeof key === 'string') {
    const remove = (props.uploadHandler || defaultUploadHandler).remove;
    if (remove) {
      remove(key);
    }
    if (props.multiple) {
      (value.value as string[]).splice((value.value as string[]).indexOf(key), 1);
    } else {
      value.value = undefined;
    }
  }
  flushTasks();
}
</script>
<style lang="scss" scoped>
.upload-container {
  display: flex;
  flex-wrap: wrap;
  flex-direction: row;
  justify-content: start;

  .upload-item {
    width: 128px;
    height: 128px;
    cursor: pointer;
    margin: 6px;
    position: relative;
    border: 1px dotted rgba(var(--v-border-color), var(--v-border-opacity));

    &:hover {
      border: 1px dotted rgba(var(--v-theme-primary), var(--v-medium-emphasis-opacity));
    }

    .upload-item-preview {
      width: 100%;
      height: 100%;
      background-position: center;
      background-size: contain;
    }

    .upload-item-loading {
      display: flex;
      justify-content: center;
      align-items: center;
      font-size: 14px;
      color: rgba(var(--v-theme-primary), var(--v-medium-emphasis-opacity));
    }

    .upload-item-close {
      position: absolute;
      top: -8px;
      right: -8px;
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
