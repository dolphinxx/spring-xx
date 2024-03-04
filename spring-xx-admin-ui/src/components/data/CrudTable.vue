<script setup lang="ts" generic="T extends Record<string, any>">
import { h, ref, defineExpose, computed, onMounted, watch } from "vue";
import type { Ref, VNode } from "vue";
import { VBtn, VDefaultsProvider, VToolbar, VTooltip } from "vuetify/components";
import { defaultPageSize } from "@/utils/constants";
import DataFormField from "@/components/data/DataFormField.vue";
import { unref } from "vue";
import { toRaw } from "vue";
import { VDataTable } from "vuetify/lib/labs/components.mjs";
import CrudTableCell from "@/components/data/CrudTableCell";

const dataFormDefaults = {
  VTextField: {
    density: "compact",
    variant: "outlined",
    color: "primary",
  },
  VSelect: {
    density: "compact",
    variant: "outlined",
    color: "primary",
  }
};

const toolbarDefaults = {
  VBtn: {
    height: 32,
    variant: 'outlined',
    color: 'grey'
  }
};

type PropsType<T> = {
  modelValue?: any[];
  hover?: boolean;
  stripe?: boolean;
  border?: 'none' | 'row' | 'grid';
  columns: Array<ColumnDefinition<T>>;
  disableDetail?: boolean;
  disableCreate?: boolean;
  disableEdit?: boolean;
  disableDelete?: boolean;
  disableDeleteMultiple?: boolean;
  disableSelectColumn?: boolean;
  disableActionsColumn?: boolean;
  actions?: RowAction<T>[];
  itemValue?: string;
  returnObject?: boolean;
} & ({
  paginate?: false,
  fetcher: DataFetcher<T>;
} | {
  paginate: true,
  pageSize?: number,
  pageSizes?: number[],
  fetcher: PageDataFetcher<T>;
});

const tableRef = ref<VDataTable>();
const props = withDefaults(defineProps<PropsType<T>>(), { stripe: undefined, hover: true, itemValue: 'id', pageSizes: () => [5, 10, 15, 20, 25, 50], modelValue: () => [] });
const emit = defineEmits<{ (event: 'update:modelValue', data: any[]): void }>();

const selectedAll = computed({
  get() {
    return items.value && items.value.length > 0 && selections.value.length === items.value.length;
  },
  set(val: boolean) {
    if (val) {
      selections.value = items.value.map(_ => _[props.itemValue]);
    } else {
      if (selections.value.length > 0) {
        selections.value.splice(0, selections.value.length);
      }
    }
  }
});
const selections = ref<any[]>([]);
watch(selections, () => {
  emit('update:modelValue', props.returnObject ? selections.value.map(s => items.value.find(e => e[props.itemValue] === s)) : toRaw(selections.value));
}, { deep: true });
function collectSelections() {
  const result: any[] = [];
  if (props.returnObject) {
    for (let i = 0, len = selections.value.length; i < len; i++) {
      if (selections.value[i]) {
        result.push(items.value[i]);
      }
    }
  } else {
    for (let i = 0, len = selections.value.length; i < len; i++) {
      if (selections.value[i]) {
        result.push(items.value[i][props.itemValue]);
      }
    }
  }
  return result;
}

const columnDefinitionMap: Record<string, ColumnDefinition<T>> = {};
props.columns.forEach(c => columnDefinitionMap[c.key] = c);
const loading = ref(true);
const loadData = async () => {
  loading.value = true;
  // clear selection
  selectedAll.value = false;
  if (selections.value.length > 0) {
    selections.value.splice(0, selections.value.length);
  }

  try {
    if (props.paginate) {
      const r = await (props.fetcher as PageDataFetcher<T>)({ page: dataOptions.value.page, size: dataOptions.value.size, sort: toRaw(dataOptions.value.sortBy), search: toRaw(searchParams.value) });
      items.value = r.items;
      dataOptions.value.page = r.page;
      dataOptions.value.size = r.size;
      totalCount.value = r.totalCount;
      totalPages.value = r.totalPages;
    } else {
      const r = await (props.fetcher as DataFetcher<T>)({ sort: toRaw(dataOptions.value.sortBy), search: toRaw(searchParams.value) });
      items.value = r;
    }

  } finally {
    loading.value = false;
  }
};

const tableClass = computed(() => {
  const _: string[] = [];
  console.log({ ...props });
  if (props.stripe || (props.stripe === undefined && props.border !== 'grid')) {
    _.push('stripe');
  }
  if (props.hover) {
    _.push('crud-table-hover');
  }
  _.push(`border-${props.border}`);
  return _;
});

onMounted(() => {
  loadData();
});

const totalPages = ref(0);
const totalCount = ref(0);

const dataOptions = ref<{ page: number; size: number; sortBy: Sort[] }>({
  page: 1,
  size: (props as any).pageSize || defaultPageSize,
  sortBy: [],
});
watch(dataOptions, () => loadData(), {deep: true});
const rowActions = ref((() => {
  const result: RowAction<T>[] = [];
  if (!props.disableDetail) {
    result.push({ title: '详情', icon: '$mdi-eye', action: readItem, color: 'primary' });
  }
  if (!props.disableEdit) {
    result.push({ title: '编辑', icon: '$mdi-pen', action: editItem, color: 'primary' });
  }
  if (!props.disableDelete) {
    result.push({ title: '删除', icon: '$mdi-delete-outline', action: deleteItem, color: 'error' });
  }
  if (props.actions) {
    result.push(...props.actions);
  }
  return result;
})());
const headers = computed(() => {
  const result: ColumnDefinition<T>[] = [];
  // if (!props.disableSelectColumn) {
  //   result.push({
  //     title: '#',
  //     key: '#index',
  //     sortable: false,
  //     align: "center",
  //   });
  // }
  result.push(...props.columns);
  if (!props.disableActionsColumn) {
    result.push({
      title: '操作',
      key: '#actions',
      sortable: false,
      align: 'center',
      width: rowActions.value.length * 30 - 6 + 32,
    });
  }
  return result;
});
const items = ref<Array<T>>([]) as Ref<Array<T>>;
const searchParams = ref<Record<string, any>>({});

const formDialog = ref(false);
const editedIndex = ref(-1);
const editedItem = ref<T>();

function createItem() {

}

function editItem(item: T) {
  editedIndex.value = items.value.indexOf(item);
  editedItem.value = Object.assign({}, item);
  formDialog.value = true;
}

function deleteItem(item: T) {
  editedIndex.value = items.value.indexOf(item);
}

function deleteMultipleItems() {

}

function readItem(item: T) {
}

function resetSearch() {
  searchParams.value = {};
}
defineExpose({
  getTable() {
    return tableRef;
  },
  getSelection() {
    return tableRef.value?.modelValue as T[] | undefined;
  },
});
</script>
<template>
  <div class="crud">
    <v-sheet class="crud-search compact-form hide-details">
      <v-defaults-provider :defaults="dataFormDefaults">
        <slot name="top">
          <v-row :no-gutters="true">
            <data-form-field label="名称">
              <v-text-field />
            </data-form-field>
            <data-form-field label="选择">
              <v-select :items="[{ title: 'a', value: 1 }, { title: 'aaaaaaaaaaaaaaaaaaaaaaaaa', value: 1111111111111111111 }]"></v-select>
            </data-form-field>
            <v-col :cols="12" :sm="4" :md="3" :lg="2">
              <v-label text="名称" />
              <v-text-field />
            </v-col>
            <v-col :cols="12" :sm="4" :md="3" :lg="2">
              <v-label text="名称" />
              <v-text-field />
            </v-col>
            <data-form-field label="标签">
              <v-text-field />
            </data-form-field>
            <v-col>
              <v-btn text="搜索" variant="flat" :height="32" color="primary" @click="loadData"/>
              <v-btn text="重置" variant="outlined" :height="32" color="grey" class="ml-3" @click="resetSearch"/>
            </v-col>
          </v-row>
        </slot>
      </v-defaults-provider>
    </v-sheet>
    <slot name="toolbar">
      <v-defaults-provider :defaults="toolbarDefaults">
        <div class="crud-toolbar">
          <v-btn v-if="!disableCreate" text="新增" variant="flat" color="primary" @click="createItem" />
          <v-btn v-if="!disableDeleteMultiple" text="删除" color="red" class="ml-3" @click="deleteMultipleItems" />
          <slot name="toolbar-actions"></slot>
          <v-spacer />
          <v-btn color="primary" :width="32" icon="$mdi-reload" />
        </div>
      </v-defaults-provider>
    </slot>
    <table class="crud-table" :class="tableClass">
      <slot name="thead" :headers="headers">
        <thead>
          <tr>
            <th v-if="!disableSelectColumn" style="width: 56px;">
              <div class="crud-table-cell cell-select">
                <v-checkbox-btn :indeterminate="!selectedAll && selections.length > 0" v-model="selectedAll" />
              </div>
            </th>
            <th v-for="(column, colNum) in headers" :key="colNum" :style="{ width: column.width ? typeof column.width === 'number' ? column.width + 'px' : column.width : undefined }">
              <div class="crud-table-cell" :class="[`text-${column.align ?? 'left'}`]">{{ column.title === undefined ? column.key : column.title }}</div>
            </th>
          </tr>
        </thead>
      </slot>
      <tbody>
        <template v-if="!loading && (items && items.length > 0)">
          <tr v-for="(row, rowNum) in items" :key="rowNum">
            <td v-if="!disableSelectColumn">
              <div class="crud-table-cell cell-select">
                <v-checkbox-btn :value="row[itemValue]" v-model="selections" :multiple="true" />
              </div>
            </td>
            <td v-for="(column, colNum) in headers" :key="colNum">
              <div class="crud-table-cell" :class="[`text-${column.align ?? 'left'}`]">
                <slot :name="`item.${column.key}`" :item="row" :column="column" :row-num="rowNum" :col-num="colNum">
                  <crud-table-cell :item="row" :column="column" :row-num="rowNum" :col-num="colNum" :row-actions="rowActions" />
                </slot>
              </div>

            </td>
          </tr>
        </template>
        <template v-if="!loading && (!items || items.length === 0)">
          <tr>
            <td :colspan="headers.length + (disableSelectColumn ? 0 : 1)">
              <slot name="empty">Empty</slot>
            </td>
          </tr>
        </template>
        <template v-if="loading">
          <tr>
            <td :colspan="headers.length + (disableSelectColumn ? 0 : 1)">
              <slot name="loading">Loading</slot>
            </td>
          </tr>
        </template>
      </tbody>
    </table>
    <div v-if="paginate" class="crud-page hide-details compact-form">
      <slot name="pagination">
        共{{ totalCount }}条&nbsp;&nbsp;每页&nbsp;<v-select style="flex-grow: 0;flex-shrink: 0;" variant="outlined" density="compact" v-model="dataOptions.size" :items="pageSizes" />&nbsp;条&nbsp;&nbsp;<v-pagination v-model="dataOptions.page" :size="32" :length="totalPages" />
      </slot>
    </div>
  </div>
</template>
<style lang="scss">
@use 'vuetify/lib/components/VTable/variables';

.crud-toolbar,
.crud-search {
  .v-btn--variant-outlined {
    --v-hover-opacity: 0.25;
  }
}

.crud-toolbar {
  display: flex;
  padding: 12px 0;

  .v-btn--icon {
    --v-btn-height: 16px;
  }

  .v-icon {
    --v-icon-size-multiplier: 0.75;
  }
}

.crud-search {
  .v-row.v-row--no-gutters>div {
    display: flex;
    padding: 6px;
  }

  .v-label {
    padding-right: 12px;
  }
}

.crud-table {
  width: 100%;
  color: variables.$table-color;
  background-color: variables.$table-background;
  border-spacing: 0;

  th,
  td {}

  th {
    height: variables.$table-header-height;
    background-color: rgba(var(--v-border-color), 0.02);
  }

  td {
    height: variables.$table-row-height;
  }

  .crud-table-cell {
    padding: 0 1rem;

    &.cell-select {
      padding: 0 0.5rem;
    }

    .crud-table-actions {
      margin: 0 auto;
      display: flex;

      &>* {
        margin-left: 6px;

        &:first-child {
          margin-left: 0;
        }
      }

      .v-btn--icon .v-icon {
        --v-icon-size-multiplier: 0.75;
      }
    }
  }

  &.crud-table-hover {
    tbody tr:hover>td {
      background-color: variables.$table-hover-color !important;
    }
  }


  &.stripe {
    tbody tr:nth-of-type(2n)>td {
      background-color: rgba(var(--v-border-color), 0.02);
    }
  }

  &.border-row {}

  &.border-grid {

    th,
    td {
      border-right: variables.$table-border;
    }

    th,
    td {
      &:first-of-type {
        border-left: variables.$table-border;
      }
    }

    th,
    td {
      border-top: variables.$table-border;
    }

    tr:last-of-type td {
      border-bottom: variables.$table-border;
    }
  }
}

.crud-page {
  display: flex;
  justify-content: end;
  align-items: center;
  padding: 1rem 0;

  .v-pagination__list {
    &>li {
      margin-top: 0;
      margin-bottom: 0;
    }
  }
}
</style>
