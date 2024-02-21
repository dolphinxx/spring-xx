<script setup lang="ts">
import { h, resolveComponent } from 'vue';
import { useRoute } from 'vue-router';

const props = defineProps<{ items: Btn[] }>();
const route = useRoute();

function renderMenu(list: Btn[], parentValue?: string) {
  if (!list || list.length === 0) {
    return [];
  }
  return list.filter(item => item.target || (item.children != null && item.children.length > 0)).map((item, i) => {
    const props: { key: any; index: string; onClick?: () => void } = { key: i, index: '' };
    if (item.target) {
      props.index = item.target;
      // props.onClick = () => router.push(item.target!);
    }
    if (item.children == null) {
      return h(ElMenuItem, props, () => [h(ElIcon, item.icon ? () => h(resolveComponent(item.icon!)) : undefined), h('span', item.name)]);
    }
    const index = parentValue ? `${parentValue}-${i};` : `${i};`;
    props.index = index;
    return h(ElSubMenu, { ...props }, {
      title: () => [h(ElIcon, item.icon ? () => h(resolveComponent(item.icon!)) : undefined), h('span', item.name)],
      default: () => renderMenu(item.children!, index),
    })
  });
}

const render = () => h(ElMenu, {
  router: true,
  defaultActive: route.path,
}, () => renderMenu(props.items));
</script>

<template>
  <render />
</template>
