<script setup lang="ts">
import { useAppStore } from '@/store/app';
import { isDark, toggleDark } from '@/composables/dark';
import { logout } from '@/api/common';
import { useRouter } from 'vue-router';

const store = useAppStore();
const router = useRouter();
const _logout = () => {
  logout();
  router.replace('/login');
}
</script>

<template>
  <el-header class="is-flex">
    <el-menu class="header" mode="horizontal" :ellipsis="false" :router="true" :default-active="$route.path">
      <div class="app-brand">XX管理系统</div>
      <el-menu-item index="/">首页</el-menu-item>
      <el-menu-item index="/demo/menus">菜单示例</el-menu-item>
      <div class="spacer"></div>
      <div class="header-icon">
        <el-icon @click="toggleDark()">
          <icon-ep-sunny v-if="!isDark" />
          <icon-ep-moon v-if="isDark" />
        </el-icon>
      </div>
      <el-dropdown class="header-user" v-if="store.principal">
        <span class="header-user-title">
          <el-avatar :size="32"> {{ store.principal!.name[0].toUpperCase() }} </el-avatar>
          {{ store.principal!.name }}
          <el-icon class="el-icon--right">
            <icon-ep-arrow-down />
          </el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item>个人信息</el-dropdown-item>
            <el-dropdown-item @click="_logout()">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </el-menu>
  </el-header>
</template>

<style scoped lang="scss">
.el-header {
  --el-header-height: 50px;
  --el-menu-base-level-padding: 16px;
}

.header-icon {
  display: inline-flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  margin: 0;
  border-bottom: 2px solid transparent;
  color: var(--el-menu-text-color);
  cursor: pointer;
  padding: 0 var(--el-menu-base-level-padding);
  transition: border-color var(--el-transition-duration), background-color var(--el-transition-duration), color var(--el-transition-duration);

  &:hover {
    outline: 0;
    color: var(--el-menu-hover-text-color);
    background-color: var(--el-menu-hover-bg-color);
  }
}

.header-user {
  margin: 0 16px;

  .header-user-title {
    cursor: pointer;
    display: flex;
    align-items: center;
    outline: none;

    .el-avatar {
      margin-right: 0.5rem;
    }
  }
}

.header {
  --el-menu-horizontal-height: 50px;
  width: 100%;

  .el-menu-item {
    border-bottom: none !important;
  }
}

.app-brand {
  width: var(--el-aside-width);
  display: flex;
  justify-content: center;
  align-items: center;
}

.spacer {
  flex: 1;
}
</style>
