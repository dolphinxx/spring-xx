<template>
  <v-container>
    <h1>Demo - Menus</h1>
    <div style="width: 200px;">
      <Menu :items="data"/>
    </div>
  </v-container>
</template>
<script lang="ts" setup>
import Menu from '@/components/Menu.vue';
import {ref} from "vue";
const data = ref([
  {
    id: 100000,
    name: 'A',
    key: 'a',
    type: 1,
    parentId: null,
    order: 100000,
    target: null,
    icon: 'mdi-alert',
    children: [
      {
        id: 1000000,
        name: 'A-A',
        key: 'a-a',
        type: 1,
        parentId: null,
        order: 1000000,
        target: '/a/a',
        children: [
          {
            id: 10000000,
            name: 'A-A-A',
            key: 'a-a-a',
            type: 1,
            parentId: null,
            order: 10000000,
            target: '/a/a/a',
          }
        ]
      },
    ]
  },
  {
    id: 200000,
    name: 'B',
    key: 'b',
    type: 1,
    parentId: null,
    order: 200000,
    target: null,
    children: [
      {
        id: 2000000,
        name: 'B-B',
        key: 'b-b',
        type: 1,
        parentId: null,
        order: 2000000,
        target: '/b/b',
        children: [
          {
            id: 20000000,
            name: 'B-B-B',
            key: 'b-b-b',
            type: 1,
            parentId: null,
            order: 20000000,
            target: '/b/b/b',
          }
        ]
      },
      {
        id: 2000001,
        name: 'B-B2',
        key: 'b-b2',
        type: 1,
        parentId: null,
        order: 2000001,
        target: '/b/b2',
        children: [
          {
            id: 20000001,
            name: 'B-B2-B',
            key: 'b-b2-b',
            type: 1,
            parentId: null,
            order: 20000001,
            target: '/b/b2/b',
          }
        ]
      },
    ]
  },
  {
    id: 3000000,
    name: 'C',
    key: 'c',
    type: 1,
    parentId: null,
    order: 3000000,
    target: '/c',
  },
  {
    id: 1,
    name: '工作台',
    key: 'desktop',
    type: 1,
    parentId: null,
    order: 1,
    icon: 'mdi-monitor',
    children: [
      {
        id: 2,
        name: '通知公告',
        key: 'notice',
        type: 1,
        parentId: null,
        order: 1,
        icon: 'mdi-message',
        target: '/notice',
      },
      {
        id: 2,
        name: '通知公告',
        key: 'notice',
        type: 1,
        parentId: null,
        order: 1,
        target: null,
      }
    ],
  },
  {
    id: 2,
    name: '系统管理',
    key: 'system',
    type: 1,
    parentId: null,
    order: 1,
    icon: 'mdi-cog-outline',
    children: [
      {
        id: 2,
        name: '用户管理',
        key: 'user',
        type: 1,
        parentId: null,
        order: 1,
        target: '/sys/user',
        icon: 'mdi-account-outline',
      },
      {
        id: 3,
        name: '角色管理',
        key: 'role',
        type: 1,
        parentId: null,
        order: 1,
        target: '/sys/role',
        icon: 'mdi-account-multiple-outline',
      },
      {
        id: 3,
        name: '权限管理',
        key: 'perm',
        type: 1,
        parentId: null,
        order: 1,
        target: '/sys/perm',
        icon: 'mdi-shield-account-outline',
      },
      {
        id: 3,
        name: '按钮管理',
        key: 'btn',
        type: 1,
        parentId: null,
        order: 1,
        target: '/sys/btn',
        icon: 'mdi-button-cursor',
      },
    ],
  }
]);
</script>
