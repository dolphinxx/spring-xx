<template>
  <v-container>
    <div class="data-table-header"></div>
    <div class="data-table-content">
<!--      <v-table>-->
<!--        <thead>-->
<!--        <tr>-->
<!--          <th v-for="(column) in columnDefinitions" :key="column.name">{{ column.title ?? column.name }}</th>-->
<!--        </tr>-->
<!--        </thead>-->
<!--        <tbody>-->
<!--        <tr v-for="(row, i) in data" :key="i">-->

<!--          <td v-for="column in columnDefinitions">-->
<!--            <component :is="renderColumnData(row, column, i)"/>-->
<!--          </td>-->
<!--        </tr>-->
<!--        </tbody>-->
<!--      </v-table>-->
    </div>
    <hr/>
    <v-data-table-server
      :headers="headers"
      :items="data.items"
      :items-per-page="data.size"
      :items-length="data.totalCount"
      item-value="id"
      :page="data.page"
      :loading="loadDataState"
      :show-current-page="true"
      :sticky="true"
    >
      <template v-slot:item.#number="{index}">{{index + 1}}</template>
      <template v-slot:item.createTime="{item}">
        <date-cell :value="item.createTime"/>
      </template>
      <template v-slot:item.#actions="{ item }">
        <v-tooltip text="Detail" location="top">
          <template v-slot:activator="{props}">
            <v-btn
              v-bind="props"
              icon="mdi-eye"
              size="small"
              variant="text"
              @click="readItem(item)"
            />
          </template>
        </v-tooltip>
        <v-tooltip text="Edit" location="top">
          <template v-slot:activator="{props}">
            <v-btn
              v-bind="props"
              icon="mdi-pencil"
              size="small"
              variant="text"
              @click="editItem(item)"
            />
          </template>
        </v-tooltip>
        <v-tooltip text="Delete" location="top">
          <template v-slot:activator="{props}">
            <v-btn
              v-bind="props"
              icon="mdi-delete"
              size="small"
              variant="text"
              @click="deleteItem(item)"
            />
          </template>
        </v-tooltip>
      </template>
    </v-data-table-server>
  </v-container>
</template>
<script lang="ts" setup>
import {h, ref} from "vue";
import type {VNode} from "vue";
import {VBtn} from "vuetify/components";
import {copyObject} from "@/utils/objects";
import DateCell from "@/components/data/DateCell.vue";
import {paginateUsers} from "@/api/sys/user";
import {useRouter} from "vue-router";

const router = useRouter();

type UserFO = Override<UserVO, {
  createTime: string;
}>

type ColumnDefinition<T> = {
  name: string;
  title?: string;
  render?: (row: T, num: number) => VNode;
  formatter?: (value: any) => string;
}

const defaultDateFormatter = (v: Date) => `${v.getFullYear()}-${String(v.getMonth()).padStart(2, '0')}-${String(v.getDate()).padStart(2, '0')} ${String(v.getHours()).padStart(2, '0')}:${String(v.getMinutes()).padStart(2, '0')}:${String(v.getSeconds()).padStart(2, '0')}`;

const defaultFormatter = v => String(v);

const renderColumnData = (row: UserVO, definition: ColumnDefinition<UserVO>, rowNum: number) => {
  const v = row[definition.name];
  if (definition.render) {
    return () => definition.render(row, rowNum);
  }
  if (definition.formatter) {
    return () => h('span', definition.formatter!(v));
  }
  if (definition.name === '#number') {
    return () => h('span', String(rowNum + 1));
  }
  if (definition.name === '#actions') {
    return () => h('div', {}, [h(VBtn, {text: '详情', prependIcon: 'mdi-eye', variant: 'plain'}), h(VBtn, {text: '编辑', prependIcon: 'mdi-pen', variant: 'plain'}), h(VBtn, {text: '删除', prependIcon: 'mdi-delete', variant: 'plain'})]);
  }
  if (v instanceof Date) {
    return () => h('span', `${v.getFullYear()}-${String(v.getMonth()).padStart(2, '0')}-${String(v.getDate()).padStart(2, '0')} ${String(v.getHours()).padStart(2, '0')}:${String(v.getMinutes()).padStart(2, '0')}:${String(v.getSeconds()).padStart(2, '0')}`);
  }

  return () => h('span', String(v));
}

const columnDefinitions = ref<ColumnDefinition<UserVO>[]>([
  {
    title: '#',
    name: '#number',
  },
  {
    title: '编号',
    name: 'id',
  },
  {
    title: '名称',
    name: 'name',
  },
  {
    title: '账号',
    name: 'username',
  },
  {
    title: '创建时间',
    name: 'createTime',
  },
  {
    title: '操作',
    name: '#actions',
  },
]);

const columnDefinitionMap = {};
columnDefinitions.value.forEach(c => columnDefinitionMap[c.name] = c);

const data = ref<Page<UserVO>>({
  page: 1,
  size: 20,
  totalCount: 0,
  totalPages: 0,
  items: [],
});


const headers = ref([
  {
    title: '#',
    key: '#number',
  },
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
  {
    title: '操作',
    key: '#actions',
  },
]);

const defaultItem:UserFO = {
  id: 0,
  name: '',
  username: '',
  createTime: '',
}

const propertyConverters = {
  createTime: (v:Date) => `${v.getFullYear()}-${String(v.getMonth()).padStart(2, '0')}-${String(v.getDate()).padStart(2, '0')} ${String(v.getHours()).padStart(2, '0')}:${String(v.getMinutes()).padStart(2, '0')}:${String(v.getSeconds()).padStart(2, '0')}`,
};

const loadDataState = ref(true);

const loadData = async () => {
  loadDataState.value = true;
  return paginateUsers().then(r => {
    data.value = r;
  }).finally(() => {
    loadDataState.value = false;
  });
}

loadData();

const formDialog = ref(false);
const editedIndex = ref(-1);
const editedItem = ref<UserFO>({
  ...defaultItem,
});

const editItem = (item:UserVO) => {
  editedIndex.value = data.value.items.indexOf(item);
  editedItem.value = copyObject({}, item, propertyConverters);
  formDialog.value = true;
}

const deleteItem = (item:UserVO) => {
  editedIndex.value = data.value.items.indexOf(item);
}

const readItem = (item:UserVO) => {
  router.push({path: `/sys/user/${item.id}`})
}
</script>
