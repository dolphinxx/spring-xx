<script setup lang="ts">

import {cloneExclusively} from "@/utils/object.ts";
import {h, ref, useSlots} from "vue";
import type {ButtonProps} from "element-plus/es/components/button/src/button";

const props = defineProps<ButtonProps & {load: () => Promise<any>}>();
const slots = useSlots();
const loading = ref(false);
const render = () => {
  const buttonProps = cloneExclusively({
    disabled: props.disabled || loading.value,
    onClick: () => {
      loading.value = true;
      props.load().finally(() => loading.value = false);
    }
  }, props, ['icon', 'load', 'disabled']);
  return h(ElButton, buttonProps, () => {
    const result: any = [];
    if (loading.value) {
      result.push(h(ElIcon, {class: 'is-loading'}, () => h(IconEpLoading)));
    } else {
      if (slots['icon']) {
        result.push(slots['icon']());
      } else if (props.icon) {
        result.push(h(ElIcon, {}, props.icon.render));
      }
    }
    result.push(slots['default']());
    return result;
  });
}
</script>

<template>
  <render/>
</template>
