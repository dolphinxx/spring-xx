<template>
  <v-container>
    <div style="margin: -8px">
      <v-btn style="margin: 8px;" @click="reloadButtons" :loading="reloadButtonsState">Reload Buttons</v-btn>
      <v-btn style="margin: 8px;" @click="reloadAuthorities" :loading="reloadAuthoritiesState">Reload Authorities</v-btn>
    </div>
  </v-container>
</template>
<script lang="ts" setup>
import * as api from '@/api/common';
import {ref} from "vue";

const reloadButtonsState = ref(false);
const reloadButtons = async () => {
  reloadButtonsState.value = true;
  api.reloadButtons().finally(() => reloadButtonsState.value = false);
}

const reloadAuthoritiesState = ref(false);
const reloadAuthorities = async () => {
  reloadAuthoritiesState.value = true;
  api.reloadAuthorities().finally(() => reloadAuthoritiesState.value = false);
}
</script>
