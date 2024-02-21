<template>
  <view-container v-loading="!store.menus">
    <el-empty v-if="store.menus" description="页面不存在" />
  </view-container>
</template>
<script lang="ts" setup>
import { useAppStore } from "@/store/app";
// import { useRoute, useRouter } from "vue-router";
// import { importDynamic } from "@/utils/internal";

const store = useAppStore();
// const router = useRouter();
// const route = useRoute();
// importDynamic(route.fullPath).then(c => {
//   // the trailing number in the path is replaced with `:id`
//   const path = route.fullPath.replace(/\/\d+$/, '/:id');
//   router.addRoute('Authenticated', { path, name: Symbol(route.fullPath), component: async () => c });
//   // reload current route
//   router.replace({ path: route.fullPath });
// }).catch(() => notFound.value = true).finally(() => {
//   if(loading) {
//     loading.close();
//   }
// });
</script>
