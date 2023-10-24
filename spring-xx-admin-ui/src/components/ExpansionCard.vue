<template>
  <v-card variant="flat">
    <v-card-title style="font-size:1rem;line-height: 1;display: flex;align-items: center;">{{ title }}<v-spacer/><v-btn density="compact" variant="flat" :icon="show ? '$mdi-chevron-up' : '$mdi-chevron-down'" @click="show = !show"/></v-card-title>
    <v-expand-transition>
      <div v-show="show">
        <v-divider/>
        <slot name="default"></slot>
      </div>
    </v-expand-transition>
  </v-card>
</template>
<script lang="ts" setup>
import {ref} from "vue";

const props = defineProps({
  title: String,
  defaultExpand: {
    type: Boolean,
    required: false,
    default: true,
  }
});
const show = ref(props.defaultExpand);
</script>
