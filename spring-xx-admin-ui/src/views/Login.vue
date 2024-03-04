<template>
  <v-app>
    <v-main class="login-main">
      <login-form :onSuccess="handleLoginSuccess"/>
    </v-main>
  </v-app>
</template>
<script lang="ts" setup>
import {useRouter} from "vue-router";
import LoginForm from "@/components/LoginForm.vue";

const router = useRouter();
const handleLoginSuccess = () => router.replace("/");
</script>
<style lang="scss" scoped>
.login-main {
  display: flex;
  align-items: center;
  background-color: rgb(var(--v-theme-secondary));
}
</style>
