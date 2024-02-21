<script setup lang="ts">
import { useAppStore } from "@/store/app";
import SideMenu from '@/components/SideMenu.vue';

const store = useAppStore();


</script>

<template>
  <el-aside>
    <el-scrollbar height="100%" v-loading="!store.menus">
      <!-- <el-menu>
        <el-sub-menu index="2">
          <template #title>
            <el-icon>
              <icon-setting />
            </el-icon>
            <span>内容管理</span>
          </template>
          <el-menu-item index="2-1">
            <el-icon>
              <icon-user />
            </el-icon>
            <span>商品管理</span>
          </el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="1">
          <template #title>
            <el-icon>
              <icon-setting />
            </el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="1-1">
            <el-icon>
              <icon-user />
            </el-icon>
            <span>用户管理</span>
          </el-menu-item>
        </el-sub-menu>
      </el-menu> -->
      <side-menu v-if="store.menus" :items="store.menus"/>
    </el-scrollbar>
  </el-aside>
</template>

<style scoped lang="scss">
.el-aside {
  height: 100%;
  overflow: hidden;
}
.el-menu {
}
</style>
