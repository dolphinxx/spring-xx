<script setup lang="ts">
import { h, useSlots } from 'vue';

const props = withDefaults(defineProps<{ scrollable?: boolean }>(), { scrollable: true });
const slots = useSlots();
const render = () => {
  const content = () => h('div', { class: 'view-container' }, slots.default ? slots.default!() : '');
  if (props.scrollable) {
    return h(ElScrollbar, {style: {height: '100%'}}, content);
  }
  return content();
};
</script>

<template>
  <render>

  </render>
</template>
