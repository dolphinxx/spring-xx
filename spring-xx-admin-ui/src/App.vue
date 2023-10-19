<template>
  <v-app>
    <v-main>
      <router-view v-if="initialized"/>
      <div v-if="!initialized" style="position: fixed;left: 0;top: 0;right: 0;bottom: 0;display: flex;align-items: center;justify-content: center;">
        <v-progress-circular indeterminate/>
      </div>
    </v-main>
  </v-app>
  <Dialog></Dialog>
</template>

<script setup lang="ts">
import {ref} from "vue";
import {useAppStore} from "@/store/app";
import Dialog from "@/components/dialog.vue";

const initialized = ref(false);
useAppStore().loadSettings().then(() => initialized.value = true);


</script>
