<script setup lang="ts" generic="T">
import {type FormInstance} from 'element-plus';
import {computed, onMounted, ref, toRaw, useSlots} from 'vue';
import CrudTable from "@/components/CrudTable.vue";
import {crudDefaults} from "@/defaults";
import {prepareFields} from "@/composables/crud";

type PropsType = {
  search?: FormField[];
  columns: CrudTableColumn[];
  defaultPage?: number;
  defaultSize?: number;
  rowKey?: string;
  disableIndexColumn?: boolean;
  disableSelectionColumn?: boolean;
  rowReadHandler?: (id: any) => any;
  rowCreateHandler?: (entity: any) => any;
  rowEditHandler?: (id: any) => any;
  rowUpdateHandler?: (entity: any) => any;
  rowDeleteHandler?: (id: any) => void;
  creatingProps?: (FormField | string)[];
  editingProps?: (FormField | string)[];
} & ({
  fetcher: CrudDataFetcher<T>;
  paginate?: false;
} | {
  fetcher: PaginateCrudDataFetcher<T>;
  paginate: true;
  page?: number;
  size?: number;
});

const props = withDefaults(defineProps<PropsType>(), {rowKey: crudDefaults.rowKey});
const slots = useSlots();

const slotColumns = computed(() => {
  const result: string[] = [];
  let name: string;
  for (const column of props.columns) {
    name = `header.${column.prop}`;
    if (slots[name]) {
      result.push(name);
    }
    name = `item.${column.prop}`;
    if (slots[name]) {
      result.push(name);
    }
  }
  if (slots['header.operations']) {
    result.push('header.operations')
  }
  if (slots['item.operations']) {
    result.push('item.operations')
  }
  return result;
});

const searchFormRef = ref<FormInstance>();
const crudTableRef = ref<InstanceType<typeof CrudTable>>();

const editing = ref(false);
const creating = ref(false);

const items = ref<T[]>();
const totalPages = ref(0);
const totalCount = ref(0);

const creatingFields = props.creatingProps ? prepareFields(props.creatingProps, props.columns) : null;
const creatingModel = ref({});

const searchParams = ref<Record<string, any>>((function () {
  const _: Record<string, any> = {};
  if (props.search) {
    for (let prop of props.search) {
      _[prop.prop!] = prop.modelValue;
    }
  }
  return _;
})());
const sortByParams = ref<SortBy>({name: '', order: null});
const paginateParams = ref<PaginateParams>({
  page: props.defaultPage ?? crudDefaults.page.page,
  size: props.defaultSize ?? crudDefaults.page.size,
});

const loadDataState = ref(false);

const loadData = async () => {
  try {
    loadDataState.value = true;
    const params: PaginateCrudDataFetcherParams = {
      ...toRaw(searchParams.value),
      sortBy: sortByParams.value.order === null ? undefined : toRaw(sortByParams.value),
    };
    if (props.paginate) {
      params.page = toRaw(paginateParams.value);
      const data = (await props.fetcher(params)) as Page<T>;
      items.value = data.items;
      totalPages.value = data.totalPages;
      totalCount.value = data.totalCount;
    } else {
      items.value = (await props.fetcher(params)) as T[];
    }
  } catch (err) {
    ElMessage({
      showClose: true,
      type: 'error',
      message: String(err),
    });
  } finally {
    loadDataState.value = false;
  }
}

const sortChangeHandler = ({prop, order}: {
  column: TableColumnCtx,
  prop: string,
  order: 'ascending' | 'descending' | null
}) => {
  sortByParams.value.name = prop;
  sortByParams.value.order = order === null ? null : (order === 'descending' ? 'desc' : 'asc');
  loadData();
};

const submitCrudForm = async () => {
  console.log(toRaw(searchParams));
  await loadData();
};
const resetCrudForm = () => {
  searchFormRef.value?.resetFields();
}

const deleteRows = () => {
  const selections = crudTableRef.value!.tableRef!.getSelectionRows();

  console.log(selections);
}

const createRow = () => {

}

const saveRow = () => {

}


onMounted(() => {
  loadData();
})

defineExpose({
  loadData,
  submitCrudForm,
  resetCrudForm,
  searchForm: searchFormRef,
  searchParams,
  sortByParams,
  paginateParams,
  items,
  totalPages,
  totalCount,
})
</script>

<template>
  <div>
    <el-form ref="searchFormRef" v-if="search && search.length > 0" :inline="true" :model="searchParams"
             class="crud-form">
      <auto-form :fields="search" :inline="true" :model-value="searchParams"/>
      <el-button type="primary" @click="submitCrudForm()" :loading="loadDataState">
        <el-icon v-if="!loadDataState">
          <icon-ep-search/>
        </el-icon>
        <span>搜索</span>
      </el-button>
      <el-button @click="resetCrudForm()" :plain="true">
        <el-icon>
          <icon-ep-refresh-left/>
        </el-icon>
        <span>重置</span>
      </el-button>
      <slot name="searchFormButtons"/>
    </el-form>
  </div>
  <div class="crud-toolbar">
    <div>
      <el-button type="primary" @click="createRow()">
        <el-icon>
          <icon-ep-plus/>
        </el-icon>
        <span>新增</span>
      </el-button>
      <el-button type="danger" :plain="true" @click="deleteRows()">
        <el-icon>
          <icon-ep-delete/>
        </el-icon>
        <span>删除</span>
      </el-button>
      <slot name="toolbarButtons"/>
    </div>
    <div>
      <slot name="toolbarRightButtons"/>
      <el-tooltip content="刷新">
        <el-button :circle="true" @click="loadData()" :disabled="loadDataState">
          <el-icon :class="loadDataState ? 'is-loading' : ''">
            <icon-ep-loading v-if="loadDataState"/>
            <icon-ep-refresh v-else/>
          </el-icon>
        </el-button>
      </el-tooltip>
    </div>
  </div>
  <div class="crud-table">
    <crud-table ref="crudTableRef" :data="items" :columns="columns" :loading="loadDataState" :row-key="rowKey" :border="true"
                :disable-index-column="disableIndexColumn" :disable-selection-column="disableSelectionColumn"
                :row-create-handler="rowCreateHandler" :row-read-handler="rowReadHandler"
                :row-edit-handler="rowEditHandler" :row-update-handler="rowUpdateHandler"
                :row-delete-handler="rowDeleteHandler" :sort-change-handler="sortChangeHandler">
      <template v-for="(name, i) in slotColumns" :key="i" v-slot:[name]="slotData">
        <slot :name="name" v-bind="slotData"/>
      </template>
    </crud-table>
  </div>
  <div class="crud-footer">
    <div></div>
    <el-pagination v-if="paginate" v-model:current-page="paginateParams.page" v-model:page-size="paginateParams.size"
                   :background="true" :page-count="totalPages" :total="totalCount" @current-change="loadData()"
                   @size-change="loadData()" layout="total,sizes,prev,pager,next,jumper">
    </el-pagination>
  </div>
  <el-dialog v-if="rowCreateHandler" v-model="creating" title="新增">
    <el-form>
      <auto-form :fields="creatingFields!" :model-value="creatingModel"/>
    </el-form>
    <template #footer>
      <el-button @click="creating = false">取消</el-button>
      <el-button @click="saveRow()">提交</el-button>
    </template>
  </el-dialog>
  <el-dialog v-model="editing" title="修改">

  </el-dialog>
</template>

<style lang="scss">
.crud-form,
.crud-toolbar > * {
  margin: -6px;

  .el-form-item {
    margin-right: 0;
  }

  & > * {
    margin: 6px !important;
  }
}
</style>

<style scoped lang="scss">
.crud-toolbar,
.crud-footer {
  display: flex;
  justify-content: space-between;
  margin-top: 12px;
}

.crud-table {
  margin-top: 12px;
}

.crud-form {
  display: flex;
  flex-wrap: wrap;
  flex-direction: row;


  label {
    white-space: nowrap;
    flex-grow: 0;
    flex-shrink: 0;
    align-items: center;
    display: inline-flex;

    .control-label {
      margin-right: 0.5rem;
    }
  }
}
</style>
