<template>
  <div>
    <v-input v-model="value" v-bind="$props">
      <div>
        <v-btn :prepend-icon="multiple ? '$mdi-upload-multiple' : '$mdi-upload'" text="点击上传" variant="flat" color="primary" @click="fileInput.click()"/>
        <input ref="fileInput" type="file" :multiple="!!multiple" style="display:none;" @change="handleFiles">
      </div>
    </v-input>
    <div>
      <template v-for="(val, i) in items" :key="i">
        <v-hover v-slot="{ isHovering, props }">
          <div class="v-list-item attachment-item" v-bind="props">
            <span class="v-list-item__overlay"></span>
            <div class="v-list-item__prepend">
              <v-icon icon="$mdi-paperclip" :size="16"/>
            </div>
            <div v-if="typeof val === 'string'" class="v-list-item__content">{{ val }}</div>
            <div v-if="typeof val === 'object'" class="v-list-item__content" style="position: relative;">
              <v-tooltip v-if="val.error" location="top" :text="val.error">
                <template v-slot:activator="{props}">
                  <v-icon v-bind="props" color="error" :size="24" icon="$mdi-alert-circle"/>
                </template>
              </v-tooltip>
              <v-progress-linear v-if="!val.error" :model-value="val.progress" color="primary" :absolute="true" location="bottom" class="attachment-item-progress"/>
              {{val.name}}
            </div>
            <div class="v-list-item__append">
              <v-btn v-if="isHovering" variant="plain" color="primary" icon="$mdi-close" :size="20" @click="removeItem(val)"/>
            </div>
          </div>
        </v-hover>
      </template>
    </div>
  </div>
</template>
<script lang="ts" setup>
import {computed, ref} from "vue";
import {uploadHandler} from "@/api/common";
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
const handleFiles = async (e) => {
  const files = e.target.files;
  if (!files || files.length === 0) {
    return;
  }
  if (props.multiple) {
    for (const file of files) {
      const task: UploadTask = {
        id: nextId(),
        name: file.name,
        progress: 0,
      };
      items.value.push(task);
      (props.uploadHandler || uploadHandler).upload(file, (loaded, total) => loaded / total).then(r => {
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
    (props.uploadHandler || uploadHandler).upload(file, (loaded, total) => loaded / total).then(r => {
      items.value[items.value.findIndex(_ => typeof _ === 'object' && _.id === task.id)] = r.key;
      flushTasks();
    }).catch(e => {
      task.error = String(e) || 'failed';
    });
  }
  e.target.value = null;
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
    const remove = (props.uploadHandler || uploadHandler).remove;
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
