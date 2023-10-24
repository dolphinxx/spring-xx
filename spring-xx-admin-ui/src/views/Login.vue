<template>
  <v-app>
    <v-main class="login-main">
      <v-card
        class="mx-auto pa-12 pb-8"
        elevation="8"
        width="448"
        rounded="lg"
      >
        <v-form ref="formRef" validate-on="blur">
          <v-img
            class="mx-auto my-6"
            max-width="228"
            :src="loginLogo"
          ></v-img>
          <div class="text-subtitle-1 text-medium-emphasis">账号</div>
          <v-text-field
            v-model.trim="username"
            :rules="usernameRules"
            placeholder="请输入账号"
            prepend-inner-icon="$mdi-account-outline"
            variant="outlined"
            density="compact"
            required
          ></v-text-field>
          <div class="text-subtitle-1 text-medium-emphasis d-flex align-center justify-space-between">
            密码
            <a
              class="text-caption text-decoration-none text-blue"
              href="#"
              rel="noopener noreferrer"
              target="_blank"
              tabindex="-1"
            >
              忘记密码？</a>
          </div>
          <v-text-field
            :append-inner-icon="passwordVisible ? '$mdi-eye-off' : '$mdi-eye'"
            :type="passwordVisible ? 'text' : 'password'"
            placeholder="请输入密码"
            prepend-inner-icon="$mdi-lock-outline"
            variant="outlined"
            density="compact"
            @click:append-inner="passwordVisible = !passwordVisible"
            @keydown.enter="submitLogin"
            v-model.trim="password"
            :rules="passwordRules"
            required
          ></v-text-field>


          <v-checkbox
            v-model="rememberMe"
            label="记住登录状态"
            density="compact"
            :hide-details="true"
            required
          ></v-checkbox>

          <v-messages :active="true" color="error" :messages="errorMsg" style="padding-bottom: 6px;padding-inline-start: 16px;padding-inline-end: 16px;opacity: 1;"/>
          <v-btn
            :block="true"
            class="mb-8"
            color="blue"
            size="large"
            variant="tonal"
            @click="submitLogin"
          >
            登录
          </v-btn>
        </v-form>
      </v-card>
    </v-main>
  </v-app>
</template>
<script lang="ts" setup>
import {ref} from "vue";
import {login} from '@/api/common';
import {requiredRule, usernameRule, lengthBetweenRuleBuilder} from "@/utils/validation";
import loginLogo from "@/assets/login-logo.svg";
import {useRouter} from "vue-router";
import {useAppStore} from "@/store/app";

const router = useRouter();
const store = useAppStore();

const formRef = ref<VFormRef>();

const username = ref("");
const password = ref("");
const rememberMe = ref(false);
const passwordVisible = ref(false);
const errorMsg = ref("");

const usernameRules = [requiredRule, usernameRule, lengthBetweenRuleBuilder(4, 20)];
const passwordRules = [requiredRule, lengthBetweenRuleBuilder(6, 20)];

const submitLogin = async () => {
  if (!(await formRef.value!.validate()).valid) {
    return;
  }
  formRef.value!.resetValidation();
  errorMsg.value = "";
  login(username.value, password.value, rememberMe.value).then(r => {
    store.setPrincipal(r);
    router.replace("/");
  }).catch(e => {
    console.error(e);
    errorMsg.value = e.message || e;
  });
};
</script>
<style lang="scss" scoped>
.login-main {
  display: flex;
  align-items: center;
  background-color: rgb(var(--v-theme-secondary));
}
</style>
