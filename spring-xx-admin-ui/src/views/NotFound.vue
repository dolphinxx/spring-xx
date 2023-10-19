<template>
  <v-container>
    <div v-if="store.menus && notFound">
      <h1>404</h1>
    </div>
    <div v-if="!store.menus">
      <v-progress-circular :indeterminate="true"/>
    </div>
  </v-container>
</template>
<script lang="ts" setup>
import {useAppStore} from "@/store/app";
import {useRoute, useRouter} from "vue-router";
import {importDynamic} from "@/utils/internal";
import {ref} from "vue";

const store = useAppStore();
const router = useRouter();
const route = useRoute();
const notFound = ref(false);
importDynamic(route.fullPath).then(c => {
  // the trailing number in the path is replaced with `:id`
  const path = route.fullPath.replace(/\/\d+$/, '/:id');
  router.addRoute('Authenticated', {path, name: Symbol(route.fullPath), component: async () => c});
  // reload current route
  router.replace({path: route.fullPath});
}).catch(e => notFound.value = true);
</script>
