<script setup lang="ts">
import BaseHeader from '@/layouts/authenticated/BaseHeader.vue'
import BaseSide from '@/layouts/authenticated/BaseSide.vue'
import ViewTabs from '@/components/ViewTabs.vue';
import { useRouter } from 'vue-router'
import { useAppStore } from "@/store/app";
import { getPrincipal } from '@/api/common';
import { hasAuthCookie } from '@/composables/auth';
import {redirectToLogin} from "@/composables/auth";

const store = useAppStore();
const router = useRouter();



(async function () {
  if (store.principal === null) {
    if (hasAuthCookie()) {
      // trigger remember me login
      try {
        const principal = await getPrincipal();
        store.setPrincipal(principal);
        // remember-me authentication success.
      } catch (ignore) {
        redirectToLogin(router);
        return;
      }
    } else {
      // redirect to login page
      redirectToLogin(router);
      return;
    }
  }
  store.loadButtons(router);
}())
</script>
<template>
  <el-container class="app-container" v-loading="!store.principal">
    <template v-if="store.principal">
      <base-header />
      <el-container style="overflow: hidden;">
        <base-side />
        <el-main>
          <view-tabs />
          <div class="route-container">
            <router-view />
          </div>
        </el-main>
      </el-container>
    </template>
  </el-container>
</template>

<style lang="scss">
.el-header {
  --el-header-padding: 0;
  background-color: var(--el-bg-color);
  user-select: none;
}

.el-aside,
.app-brand {
  --el-aside-width: 200px;
  background-color: var(--el-bg-color);
  user-select: none;
}

.el-main {
  --el-main-padding: 0;
}

.route-container {
  padding: 8px 6px;
}
</style>
