<template>
  <v-col :cols="12" :sm="4" :md="3" :lg="2">
    <v-label :text="label" style="flex-grow: 0;flex-shrink: 0;"/>
    <div style="flex-grow: 1;flex-shrink: 1;">
      <slot name="default"></slot>
    </div>
  </v-col>
</template>
<script lang="ts" setup>
const props = defineProps<{label:string}>();
</script>
