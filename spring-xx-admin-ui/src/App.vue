<template>
  <v-app>
    <router-view v-if="initialized"/>
    <v-main v-if="!initialized">
      <div style="position: fixed;left: 0;top: 0;right: 0;bottom: 0;display: flex;align-items: center;justify-content: center;">
        <v-progress-circular indeterminate/>
      </div>
    </v-main>
    <Dialog></Dialog>
    <v-dialog v-model="appStore.showingLogin" :persistent="true">
      <login-form :onSuccess="handleLoginSuccess"/>
    </v-dialog>
  </v-app>
</template>

<script setup lang="ts">
import {ref} from "vue";
import {useAppStore} from "@/store/app";
import Dialog from "@/components/dialog.vue";
import LoginForm from "@/components/LoginForm.vue";

const initialized = ref(false);
const appStore = useAppStore();
appStore.loadSettings().then(() => initialized.value = true);
const handleLoginSuccess = () => appStore.toggleLoginDialog(false);

</script>
