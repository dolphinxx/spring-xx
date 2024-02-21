<script setup lang="ts">
import {h, ref, useSlots} from 'vue';
import type {ButtonProps} from "element-plus/es/components/button/src/button";
import {cloneExclusively} from "@/utils/object.ts";

type PropsType = ButtonProps & { message: string; load: () => Promise<any> };

const props = defineProps<PropsType>();
const slots = useSlots();
const loading = ref(false);
// unexpected double-click protection
const protecting = ref(false);
const confirming = ref(false);
const timeoutId = ref<number | undefined>();

const render = () => {
  const buttonProps = cloneExclusively({
    disabled: props.disabled || loading.value || protecting.value,
    onClick: () => {
      if (confirming.value) {
        if (timeoutId.value !== undefined) {
          clearTimeout(timeoutId.value);
        }
        confirming.value = false;
        loading.value = true;
        props.load().finally(() => loading.value = false);
        return;
      }
      confirming.value = true;
      protecting.value = true;
      setTimeout(() => {
        protecting.value = false;
        timeoutId.value = setTimeout(() => confirming.value = false, 2000);
      }, 500);
    },
  }, props, ['icon', 'message', 'load', 'disabled']);
  return h(ElTooltip, {
    content: props.message,
    placement: 'top',
    visible: confirming.value,
  }, () => h(ElButton, buttonProps, () => {
    const result: any = [];
    if (loading.value) {
      result.push(h(ElIcon, {class: 'is-loading'}, () => h(IconEpLoading)));
    } else if (confirming.value) {
      result.push(h(ElIcon, {color: '#F56C6C'}, () => h(IconEpCircleCheck)));
    } else {
      if (slots['icon']) {
        result.push(slots['icon']());
      } else if (props.icon) {
        result.push(h(ElIcon, {}, props.icon.render));
      }
    }
    result.push(slots['default']());
    return result;
  }));
}
</script>

<template>
  <render/>
</template>
