<template>
  <div>Standalone</div>
  <div>
    <slot-parent :data="data"/>
  </div>
</template>
<script lang="ts" setup>
import SlotParent from '@/components/SlotParent';
import {ref} from "vue";
const data = ref({class:'foo', title: 'Foo'});
</script>
