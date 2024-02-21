<script setup lang="ts">
import { useAppStore } from '@/store/app';
import { useRoute, useRouter } from 'vue-router';
const router = useRouter();
const route = useRoute();
const appStore = useAppStore();
function closeViewTab(targetName: string | number | undefined) {
  appStore.removeTab(targetName as string);
  if (targetName === route.fullPath) {
    if (appStore.tabs.length > 0) {
      router.replace(appStore.tabs[appStore.tabs.length - 1].path);
    } else {
      router.push('/');
    }
  }
}
function switchViewTab(targetName: string | number | undefined) {
  console.log('tab change')
  router.push(targetName as string);
}
</script>

<template>
  <div class="view-tabs">
    <el-tabs :model-value="$route.fullPath" @edit="closeViewTab" @tab-change="switchViewTab">
      <el-tab-pane v-for="tab in appStore.tabs" :key="tab.path" :label="(tab.name as string)" :name="tab.path" :closable="tab.path !== '/'"></el-tab-pane>
    </el-tabs>
  </div>
</template>

<style lang="scss">
.view-tabs {
  padding: 0 6px;
  background-color: var(--el-bg-color);

  .el-menu-item {}

  .el-tabs__header {
    margin-bottom: 0;

    .el-tabs__item.is-top {
      padding-left: 10px;
      padding-right: 10px;
      border-bottom: 2px solid transparent;

      &.is-active {
        border-bottom-color: var(--el-color-primary);
      }

      &:not(.is-active):not(:hover) {
        .is-icon-close {
          width: 0;
        }
      }
    }

    .el-tabs__nav-wrap::after {
      display: none;
    }

    .el-tabs__active-bar {
      display: none;
    }
  }
}
</style>
