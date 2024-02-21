<script setup lang="ts" generic="T">
import {computed, h, onMounted, ref, Slot, toRaw, unref, useSlots, VNode, withDirectives} from 'vue';
import {crudDefaults} from "@/defaults.ts";
import {ElMessage, type FormInstance, type TableInstance, vLoading} from 'element-plus';
import {builtInTableCellFormatters} from "@/utils/crud.ts";
import AutoForm from "@/components/form/AutoForm.vue";
import {prepareFields} from "@/composables/crud.ts";
import {cloneExplicitly} from "@/utils/object.ts";
import DoubleCheckButton from "@/components/DoubleCheckButton.vue";

type PropsType = {
  search?: (FormField | string)[];
  columns: CrudTableColumn[];
  defaultPage?: number;
  defaultSize?: number;
  rowKey?: string;
  disableIndexColumn?: boolean;
  disableSelectionColumn?: boolean;
  disableCreatingRow?: boolean;
  disableReadingRow?: boolean;
  disableEditingRow?: boolean;
  disableDeletingRow?: boolean;
  readingRowFetcher?: (id: any) => any;
  editingRowFetcher?: (id: any) => any;
  rowCreatingHandler?: (entity: any) => any;
  rowUpdatingHandler?: (entity: any) => any;
  rowDeletingHandler?: (id: any|any[]) => void;
  reloadAfterCreation?: boolean;
  reloadAfterUpdate?: boolean;
  reloadAfterDeletion?: boolean;
  readingProps?: string[];
  creatingProps?: (FormField | string)[];
  editingProps?: (FormField | string)[];
  tableAttrs?: Record<string, any>;
} & ({
  fetcher: CrudDataFetcher<T>;
  paginate?: false;
} | {
  fetcher: PaginateCrudDataFetcher<T>;
  paginate: true;
  page?: number;
  size?: number;
});

const props = withDefaults(defineProps<PropsType>(), {
  rowKey: crudDefaults.rowKey,
  reloadAfterCreation: crudDefaults.reloadAfterCreation,
  reloadAfterUpdate: crudDefaults.reloadAfterUpdate,
  reloadAfterDeletion: crudDefaults.reloadAfterDeletion,
});
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
const tableRef = ref<TableInstance>();

const items = ref<T[]>();
const totalPages = ref(0);
const totalCount = ref(0);

const searchFields: FormField[] | undefined = props.search ? prepareFields(props.search, props.columns) : undefined;

const reading = ref(false);
const readingModel = ref({});

const creating = ref(false);
const creatingFields: FormField[] | undefined = props.creatingProps ? prepareFields(props.creatingProps, props.columns) : undefined;
const creatingModel = ref({});
const creatingFormRef = ref<FormInstance>();

const editing = ref(false);
const editingFields: FormField[] | undefined = props.editingProps ? prepareFields(props.editingProps, props.columns) : undefined;
const editingModel = ref({});
const editingFormRef = ref<FormInstance>();

const searchParams = ref<Record<string, any>>((function () {
  const _: Record<string, any> = {};
  if (props.search) {
    for (let prop of props.search) {
      if (typeof prop === 'string') {
        _[prop] = undefined;
      } else {
        _[prop.prop!] = prop.modelValue;
      }
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

function getPropLabel(propName: string, columns: CrudTableColumn[]) {
  return columns.find(column => column.prop === propName)?.label;
}

const submitCrudForm = async () => {
  await loadData();
};
const resetCrudForm = () => {
  searchFormRef.value?.resetFields();
}

const deleteRows = async () => {
  const selections = tableRef.value?.getSelectionRows();
  if (selections.length === 0) {
    ElMessage({
      type: 'warning',
      message: '请选择至少一条记录',
    });
    return;
  }
  console.log('handleRowsDelete', selections);
  try {
    const ids = selections.map(row => row[props.rowKey]);
    await props.rowDeletingHandler!(ids);
    if (props.reloadAfterDeletion) {
      await loadData();
    } else {
      items.value = items.value!.filter(item => !ids.includes((item as any)[props.rowKey]));
    }
  } catch (err) {
    ElNotification({
      type: 'error',
      title: '操作失败',
      message: String(err),
      duration: 0,
    });
  }
  console.log(selections);
}

const readRow = async (scope: TableCellScope) => {
  // TODO: 查看行详情
  if (props.readingRowFetcher) {
    try {
      readingModel.value = await props.readingRowFetcher!(scope.row[props.rowKey]);
    } catch (e) {
      ElMessage({
        type: 'error',
        showClose: true,
        message: String(e),
      });
      return;
    }
  } else {
    readingModel.value = toRaw(scope.row);
  }
  reading.value = true;
}

const createRow = () => {
  creating.value = true;
}

const saveRow = async () => {
  try {
    const result = await props.rowCreatingHandler!(toRaw(creatingModel.value));
    creating.value = false;
    if (props.reloadAfterCreation) {
      await loadData();
      return;
    }
    if (result) {
      items.value!.push(result);
    }
  } catch (err) {
    ElNotification({
      type: 'error',
      title: '操作失败',
      message: String(err),
      duration: 0,
    });
  }
}

const editRow = async (scope: TableCellScope) => {
  let toEdit;
  if (props.editingRowFetcher) {
    toEdit = await props.editingRowFetcher(scope.row[props.rowKey]);
  } else {
    toEdit = toRaw(scope.row);
  }
  const editingProps = editingFields!.map(_ => _.prop!);
  editingProps.push('id');
  editingModel.value = cloneExplicitly({}, toEdit, editingProps);
  editing.value = true;
}

const updateRow = async () => {
  try {
    const model = toRaw(editingModel.value);
    const id = (model as any)[props.rowKey];
    const result = await props.rowUpdatingHandler!(model);
    editing.value = false;
    if (props.reloadAfterUpdate) {
      await loadData();
      return;
    }
    if (result) {
      items.value!.splice(items.value!.findIndex(item => (item as any)[props.rowKey] === id), 1, result);
    }
  } catch (err) {
    ElNotification({
      type: 'error',
      title: '操作失败',
      message: String(err),
      duration: 0,
    });
  }
}

const deleteRow = async (scope: TableCellScope) => {
  console.log('handleRowDelete', scope);
  try {
    const id = scope.row[props.rowKey];
    await props.rowDeletingHandler!(id);
    if (props.reloadAfterDeletion) {
      await loadData();
    } else {
      items.value!.splice(items.value!.findIndex(item => (item as any)[props.rowKey] === id), 1);
    }
  } catch (err) {
    ElNotification({
      type: 'error',
      title: '操作失败',
      message: String(err),
      duration: 0,
    });
  }

}

const renderCell = ({row, column}: { row: any; column: any; $index: number }) => {
  return h('span', row[column.property]);
}
const renderColumn = (column: CrudTableColumn, headerSlot: Slot | undefined, itemSlot: Slot | undefined) => {
  const _props: Record<string, any> = {
    // reactive
    ...column.attrs,
    prop: column.prop,
    label: column.label,
  };
  if (column.expand) {
    _props.type = 'expand';
  }
  if (column.sortable) {
    _props.sortable = 'custom';
  }
  if (column.formatter) {
    if (typeof column.formatter === 'string') {
      _props.formatter = builtInTableCellFormatters[column.formatter];
    } else {
      _props.formatter = column.formatter;
    }
  }
  const children: Record<string, any> = {}
  if (headerSlot) {
    children.header = headerSlot;
  }
  if (itemSlot) {
    children.default = itemSlot;
  }
  if (_props.type === undefined && _props.formatter === undefined && !children.default) {
    children.default = renderCell
  }
  return h(ElTableColumn, _props, children);
}

const renderTable = () => {
  const vnode = h(ElTable, {
    border: true,
    ...props.tableAttrs,
    rowKey: props.rowKey,
    ref: tableRef,
    onSortChange: sortChangeHandler,
    data: items.value,
  }, () => {
    const content = [];
    if (!props.disableSelectionColumn) {
      content.push(h(ElTableColumn, {type: 'selection'}));
    }
    if (!props.disableIndexColumn) {
      content.push(h(ElTableColumn, {type: 'index', label: '#'}));
    }
    for (const column of props.columns) {
      const headerSlot = slots[`header.${column.prop}`];
      const itemSlot = slots[`item.${column.prop}`];
      content.push(renderColumn(column, headerSlot, itemSlot));
    }
    // include operations column
    if (slots['item.operations'] || !props.disableReadingRow || !props.disableEditingRow || !props.disableDeletingRow) {
      content.push(h(ElTableColumn, {label: '操作', className: 'operations'}, {
        header: slots['header.operation'],
        default: (scope: TableCellScope) => {
          const defaultSlot: (VNode | Slot)[] = [];
          // 操作列
          if (!props.disableReadingRow) {
            // TODO: 按钮loading时隐藏图标
            defaultSlot.push(h(ElButton, {
              link: true,
              type: 'primary',
              onClick: () => readRow(scope)
            }, () => [h(ElIcon, {}, () => h(IconEpView)), h('span', {innerText: '查看'})]));
          }
          if (!props.disableEditingRow) {
            defaultSlot.push(h(ElButton, {
              link: true,
              type: 'primary',
              onClick: () => editRow(scope),
            }, () => [h(ElIcon, {}, () => h(IconEpEdit)), h('span', {innerText: '修改'})]));
          }
          // if (!props.disableDeletingRow) {
          //   defaultSlot.push(h(ElButton, {
          //     link: true,
          //     type: 'danger',
          //     onClick: () => deleteRow(scope)
          //   }, () => [h(ElIcon, {}, () => h(IconEpDelete)), h('span', {innerText: '删除'})]));
          // }
          if (!props.disableDeletingRow) {
            defaultSlot.push(h(DoubleCheckButton, {
              link: true,
              type: 'danger',
              message: '确定要删除吗？',
              load: async () => deleteRow(scope)
            }, {
              icon: () => h(ElIcon, {}, () => h(IconEpDelete)),
              default: () => h('span', {innerText: '删除'}),
            }));
          }
          if (slots['item.operations']) {
            defaultSlot.push(...slots['item.operations'](scope));
          }
          return defaultSlot;
        },
      }));
    }
    return content;
  });
  return h('div', {class: 'crud-table',}, withDirectives(vnode, [[vLoading, loadDataState.value]]));
};

const renderSearchForm = () => {
  return h('div', null, h(ElForm, {
    ref: searchFormRef,
    inline: true,
    class: 'crud-form',
    model: searchParams,
  }, () => {
    const children: any[] = [
      h(AutoForm, {fields: searchFields!, inline: true, modelValue: unref(searchParams), 'onUpdate:modelValue': (v: any) => searchParams.value = v}),
      h(ElButton, {type: 'primary', disabled: loadDataState.value, onClick: submitCrudForm}, () => [
        h(ElIcon, {class: loadDataState.value ? 'is-loading' : ''}, () => loadDataState.value ? h(IconEpLoading) : h(IconEpSearch)),
        h('span', null, '搜索'),
      ]),
      h(ElButton, {plain: true, onClick: resetCrudForm}, () => [
        h(ElIcon, () => h(IconEpRefreshLeft)),
        h('span', null, '重置'),
      ]),
    ];
    if (slots['searchFormButtons']) {
      children.push(slots['searchFormButtons']());
    }
    return children;
  }));
}

const renderToolbar = () => {
  const leftChildren: any[] = [];
  if (!props.disableCreatingRow) {
    leftChildren.push(h(ElButton, {type: 'primary', onClick: createRow}, () => [
      h(ElIcon, () => h(IconEpPlus)),
      h('span', null, '新增'),
    ]));
  }
  if (!props.disableDeletingRow) {
    leftChildren.push(h(ElButton, {type: 'danger', plain: true, onClick: deleteRows}, () => [
      h(ElIcon, () => h(IconEpDelete)),
      h('span', null, '删除'),
    ]));
  }
  if (slots['toolbarButtons']) {
    leftChildren.push(slots['toolbarButtons']());
  }
  const rightChildren: any[] = [];
  if (slots['toolbarRightButtons']) {
    rightChildren.push(slots['toolbarRightButtons']());
  }
  rightChildren.push(h(ElTooltip, {content: '刷新'}, () => [
    h(ElButton, {circle: true, onClick: loadData, disabled: loadDataState.value}, () => [
      loadDataState.value ? h(ElIcon, {class: 'is-loading'}, () => h(IconEpLoading)) : h(ElIcon, () => h(IconEpRefresh)),
    ]),
  ]));
  return h('div', {class: 'crud-toolbar'}, [
    h('div', null, leftChildren),
    h('div', null, rightChildren),
  ]);
};

const renderFooter = () => {
  const children: any[] = [
    h('div'),
  ];
  if (props.paginate) {
    children.push(h(ElPagination, {
      currentPage: paginateParams.value.page,
      'onUpdate:currentPage': (v: any) => paginateParams.value.page = v,
      pageSize: paginateParams.value.size,
      'onUpdate:pageSize': (v: any) => paginateParams.value.size = v,
      background: true,
      pageCount: totalPages.value,
      total: totalCount.value,
      onCurrentChange: loadData,
      onSizeChange: loadData,
      layout: 'total,sizes,prev,pager,next,jumper',
    }));
  }
  return h('div', {class: 'crud-footer'}, children);
}

const renderRowReadingDialog = () => h(ElDialog, {
  modelValue: reading.value,
  'onUpdate:modelValue': (v: any) => reading.value = v,
  title: '详情',
  closeOnClickModal: true,
}, () => h(ElDescriptions, {border: true, column: 2}, () => Object.entries(readingModel.value).map(entry => h(ElDescriptionsItem, {label: getPropLabel(entry[0], props.columns)}, () => String(entry[1] ?? '')))));

const renderRowCreateDialog = () => h(ElDialog, {
  modelValue: creating.value,
  'onUpdate:modelValue': (v: any) => creating.value = v,
  onClose: () => creatingFormRef.value?.resetFields(),
  title: '新增',
  closeOnClickModal: false,
}, {
  default: () => h(ElForm, {ref: creatingFormRef, model: creatingModel}, () => h(AutoForm, {fields: creatingFields!, modelValue: unref(creatingModel), 'onUpdate:modelValue': (v: any) => creatingModel.value = v})),
  footer: () => [
    h(ElButton, {onClick: () => creating.value = false}, () => '取消'),
    h(ElButton, {onClick: saveRow}, () => '提交'),
  ],
});

const renderRowEditDialog = () => h(ElDialog, {
  modelValue: editing.value,
  'onUpdate:modelValue': (v: any) => editing.value = v,
  onClose: () => editingFormRef.value?.resetFields(),
  title: '修改',
  closeOnClickModal: false,
}, {
  default: () => h(ElForm, {ref: editingFormRef, model: editingModel}, () => h(AutoForm, {fields: editingFields!, modelValue: unref(editingModel), 'onUpdate:modelValue': (v: any) => editingModel.value = v})),
  footer: () => [
    h(ElButton, {onClick: () => editing.value = false}, () => '取消'),
    h(ElButton, {onClick: updateRow}, () => '提交'),
  ],
});

const render = () => {
  const children: any[] = [];
  if (props.search && props.search.length > 0) {
    children.push(renderSearchForm());
  }
  children.push(renderToolbar());
  children.push(renderTable());
  children.push(renderFooter());
  if (!props.disableReadingRow) {
    children.push(renderRowReadingDialog());
  }
  if (!props.disableCreatingRow) {
    children.push(renderRowCreateDialog());
  }
  if (!props.disableEditingRow) {
    children.push(renderRowEditDialog());
  }
  return h('div', {}, children);
}

onMounted(() => {
  loadData();
});

defineExpose({
  loadData,
  submitCrudForm,
  resetCrudForm,
  table: tableRef,
  searchForm: searchFormRef,
  searchParams,
  sortByParams,
  paginateParams,
  items,
  totalPages,
  totalCount,
});
</script>

<template>
  <render/>
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
