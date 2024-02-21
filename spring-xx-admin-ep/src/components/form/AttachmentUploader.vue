<script lang="ts" setup>
import { ref } from "vue";
import { useUploader, type UploaderPropsType } from '@/composables/uploader';

const props = defineProps<UploaderPropsType>();
// switch to defineModel?
const emit = defineEmits(["update:modelValue"]);
const fileInputRef = ref<HTMLInputElement>();
const emitModelValue = (val: any) => emit("update:modelValue", val);
const { items, handleFiles, removeItem, multiple } = useUploader(props, emitModelValue);
</script>

<template>
  <div style="width: 100%;">
    <div>
      <el-button @click="fileInputRef?.click()">
        <el-icon>
          <icon-ep-upload />
        </el-icon>
        <span>点击上传</span>
      </el-button>
      <input ref="fileInputRef" type="file" :multiple="!!multiple" :accept="$props.accept" style="display:none;" @change="handleFiles">
    </div>
    <div class="attachment-items">
      <template v-for="val in items" :key="val.id">
        <div :class="`attachment-item ${val.removed ? 'attachment-item-removed' : ''}`" v-bind="props">
          <div class="attachment-icon">
            <template v-if="!!val.error">
              <el-tooltip :content="val.error" placement="right">
                <el-icon class="error">
                  <icon-ep-warning />
                </el-icon>
              </el-tooltip>
            </template>
            <template v-else>
              <el-icon>
                <icon-ep-paperclip />
              </el-icon>
            </template>
          </div>
          <div class="attachment-content">
            <div v-if="!val.key && !val.error && val.progress !== undefined" class="attachment-progress">
              <div :style="{ width: `${val.progress || 0}%` }"></div>
            </div>
            <span>{{ val.key || val.name }}</span>
          </div>
          <div class="attachment-close-icon">
            <el-icon @click="removeItem(val)">
              <icon-ep-close />
            </el-icon>
          </div>
        </div>
      </template>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.attachment-items {
  padding-top: 0.5rem;
}

.attachment-item {
  cursor: pointer;
  border-radius: 4px;
  display: flex;
  flex-wrap: nowrap;
  transition: all .5s cubic-bezier(.55, 0, .1, 1);
  margin-bottom: 5px;
  padding: 0 3px;
  height: 24px;

  &:hover {
    background-color: var(--el-fill-color-light);

    .attachment-close-icon {
      visibility: visible;
    }
  }

  &.upload-item-removed {
    height: 0;
    opacity: 0;
  }

  .attachment-icon {
    flex: 0;
    margin-right: 0.5rem;
    display: flex;
    align-items: center;

    .error {
      color: var(--el-color-danger);
    }
  }

  .attachment-close-icon {
    flex: 0;
    margin-left: 0.5rem;
    display: flex;
    align-items: center;
    visibility: hidden;

    &:hover {
      color: var(--el-color-danger);
    }
  }

  .attachment-content {
    font-size: 14px;
    padding: 0 2px;
    flex-grow: 1;
    flex-shrink: 1;
    position: relative;
    display: flex;
    align-items: center;

    span {
      line-height: 24px;
      position: relative;
      z-index: 2;
      overflow: hidden;
      white-space: nowrap;
      word-break: keep-all;
      text-overflow: ellipsis;
    }

    .attachment-progress {
      position: absolute;
      z-index: 1;
      left: 0;
      bottom: 0;
      width: 100%;
      height: 4px;
      opacity: 0.25;
      background-color: var(--el-border-color-lighter);

      div {
        height: 100%;
        background-color: var(--el-color-primary-light-5);
      }
    }
  }
}
</style>
