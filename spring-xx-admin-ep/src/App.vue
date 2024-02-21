<script setup lang="ts">
import { ref } from 'vue';
import zhCn from 'element-plus/dist/locale/zh-cn.mjs';
import { useAppStore } from "@/store/app";

const initialized = ref(false);
const loading = ElLoading.service({
  lock: true,
  text: '加载中',
  background: 'rgba(0, 0, 0, 0.7)',
});
const appStore = useAppStore();
appStore.loadSettings().then(() => {
  initialized.value = true;
  loading.close();
});

</script>

<template>
  <div class="app">
    <el-config-provider :locale="zhCn">
      <router-view v-if="initialized" />
    </el-config-provider>
  </div>
</template>
