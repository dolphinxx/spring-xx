<template>
  <v-container>
    <crud-table :columns="columns" :paginate="true" :fetcher="fetchData">
      <template v-slot:item.createTime="{item}">
      <date-cell :value="item.createTime"/>
    </template>
    </crud-table>
  </v-container>
</template>
<script lang="ts" setup>
import {h, ref} from "vue";
import type {Ref, VNode} from "vue";
import {VBtn} from "vuetify/components";
import {paginateUsers} from "@/api/sys/user";
import {useRouter} from "vue-router";
import {defaultPageSize} from "@/utils/constants";
import CrudTable from "@/components/data/CrudTable.vue";


const router = useRouter();

const fetchData:PageDataFetcher<UserVO> = ({page, size, sort, search}):Promise<Page<UserVO>> => {
  return paginateUsers({page: page, size: size, sort: sort});
};

const columns = ref([
  {
    title: '编号',
    key: 'id',
  },
  {
    title: '名称',
    key: 'name',
  },
  {
    title: '账号',
    key: 'username',
  },
  {
    title: '创建时间',
    key: 'createTime',
  },
]);

</script>
<style lang="scss">

</style>
