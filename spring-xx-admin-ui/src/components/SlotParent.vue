<template>
  <div>Slot Parent</div>
  <render/>
  <hr/>
  <slot-demo>
    <template #default="slotProps">
      <div :class="slotProps.class">In Default Slot, {{slotProps.title}}</div>
    </template>
  </slot-demo>
  <hr/>
  <v-list>
    <v-list-group>
      <template #activator="{props}">
        <v-list-item
          v-bind="props"
          prepend-icon="mdi-account-circle"
          title="Users"
        ></v-list-item>
      </template>
      <v-list-item>Inner Item</v-list-item>
    </v-list-group>
  </v-list>
  <hr/>
  <v-list>
    <renderList/>
  </v-list>
  <hr/>
</template>
<script lang="ts" setup>
import {h} from 'vue';
import SlotDemo from '@/components/SlotDemo';
import {VListGroup, VListItem} from "vuetify/components";
const props = defineProps<{class?:string;title?:string}>();
const render = () => {
  console.log(arguments);
  return h(SlotDemo, props, {
    default: (slotProps) => {
      console.log('slotProps', slotProps);
      return h('div', {class: slotProps.class}, `In Default Slot, ${slotProps.title}`);
    },
    header: () => h('div', 'In Header Slot'),
    footer: () => h('div', 'In Footer Slot'),
  });
}
const renderList = () => {
  return h(VListGroup, {}, {
    activator: (slotProps) => {
      console.log('slotProps', slotProps);
      return h(VListItem, {title: 'Users', 'prepend-icon': 'mdi-account-circle', ...slotProps.props});
    },
    default: () => h(VListItem, {title: 'Inner Item'})
  })
}
</script>
<style scoped>
.foo {
  color: red;
}
</style>
