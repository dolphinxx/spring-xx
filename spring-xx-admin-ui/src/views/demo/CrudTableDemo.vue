<template>
  <v-container>
    <crud-table ref="crudTable" border="grid" :columns="columns" :actions="actions" :paginate="true" :fetcher="fetchData">
      <template v-slot:toolbar-actions>
        <v-btn text="测试" class="ml-3" @click="handleClickFoo"/>
      </template>
      <template v-slot:item.score="{item}"><v-chip color="primary">{{ item.score }}</v-chip></template>
    </crud-table>
  </v-container>
</template>
<script lang="ts" setup>
import {h, ref} from "vue";
import type { ComponentExposed } from 'vue-component-type-helpers';
import CrudTable from "@/components/data/CrudTable.vue";
import { delay } from "@/utils";

type DemoVO = {
  id: number;
  name: string;
  score: number;
  createTime: Date;
}

const crudTable = ref<ComponentExposed<typeof CrudTable<DemoVO>> | null>(null);

const handleClickFoo = () => {
  const selected = crudTable.value?.getSelection();
  if(selected) {
    console.log(selected.map(_ => _.id).join(', '));
  }
}

const actions:RowAction<DemoVO>[] = [
  {
    title: 'extra action',
    icon: '$mdi-home',
    color: '#fedfac',
    action: async (item, rowNum) => console.log('extra action', rowNum, item),
  }
]

const data = (() => {
  const result:DemoVO[] = [];
  for(let i = 0;i < 121;i++) {
    result.push({
      id: i + 1,
      name: `demo${i + 1}`,
      score: Math.trunc(Math.random() * 1000),
      createTime: new Date(2000, 0, i + 1),
    });
  }
  return result;
})();

const fetchData: PageDataFetcher<DemoVO> = async ({ page, size, sort, search }): Promise<Page<DemoVO>> => {
  console.log('fetchData', page, size, sort, search);
  return delay(() => {
    const d = [...data];
    if (sort.length > 0) {
      d.sort((a, b) => {
        for (const s of sort) {
          const property = s.property as keyof DemoVO;
          const isDesc = s.order === 'desc' ? -1 : 1;
          if (a[property] < b[property]) {
            return -1 * isDesc;
          }
          if (b[property] < a[property]) {
            return 1 * isDesc;
          }
        }
        return 0;
      });
    }
    const offset = (page - 1) * size;
    return {
      page: page,
      size: size,
      totalCount: d.length,
      totalPages: Math.ceil(d.length / size),
      items: d.slice(offset, offset + size),
    };
  }, 1000);
};

const columns = ref([
  {
    title: 'ID',
    key: 'id',
    sortable: true,
  },
  {
    title: '名称',
    key: 'name',
    sortable: true,
  },
  {
    title: '分数',
    key: 'score',
  },
  {
    title: '创建时间',
    key: 'createTime',
  },
]);
</script>
