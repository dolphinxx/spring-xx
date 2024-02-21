<script setup lang="ts">
import LoginForm from '@/components/LoginForm.vue';
import { clearLoginRedirectUrl, getLoginRedirectUrl } from '@/composables/auth';
import { useRouter } from 'vue-router';
const router = useRouter();
const handleLoginSuccess = () => {
  const redirectUrl = getLoginRedirectUrl();
  clearLoginRedirectUrl();
  router.replace(redirectUrl || "/");
}
</script>
<template>
  <el-container class="app-container">
    <div class="login-main">
      <login-form :on-success="handleLoginSuccess" />
    </div>
  </el-container>
</template>

<style lang="scss" scoped>
.login-main {
  width: 100%;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: var(--el-color-primary-light-3);
}
</style>
