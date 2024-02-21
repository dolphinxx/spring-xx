<script setup lang="ts">
import {ref, h, useAttrs, useSlots, withDirectives, type Slot, type VNode} from 'vue';
import {type TableInstance, vLoading} from 'element-plus';
import {builtInTableCellFormatters} from '@/utils/crud';
import IconEpDelete from '~icons/ep/delete';
import IconEpEdit from '~icons/ep/edit';
import IconEpView from '~icons/ep/view';

type PropType = {
  data?: Array<any>;
  columns: CrudTableColumn[];
  disableIndexColumn?: boolean;
  disableSelectionColumn?: boolean;
  rowKey: string;
  rowReadHandler?: (id: any) => any;
  rowCreateHandler?: (entity: any) => any;
  rowEditHandler?: (id: any) => any;
  rowUpdateHandler?: (entity: any) => any;
  rowDeleteHandler?: (id: any) => void;
  sortChangeHandler?: (params: {
    column: TableColumnCtx,
    prop: string,
    order: 'ascending' | 'descending' | null
  }) => void;
  loading?: boolean;
  // 透传属性
  attrs?: Record<string, any>;
}


const props = defineProps<PropType>();
const $attrs = useAttrs();
const slots = useSlots();
const tableRef = ref<TableInstance>()

const handleRowRead = (scope: TableCellScope) => {
  // TODO: 查看行详情
  console.log('handleRowRead', scope);
}

const handleRowEdit = (scope: TableCellScope) => {
  // TODO: 编辑行
  console.log('handleRowEdit', scope);
}

const handleRowDelete = (scope: TableCellScope) => {
  // TODO: 删除行
  console.log('handleRowDelete', scope);
  props.rowDeleteHandler!(scope.row[props.rowKey]);
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

const render = () => {
  const vnode = h(ElTable, {
    // no-reactive
    ...$attrs,
    // reactive
    ...props.attrs,
    rowKey: props.rowKey,
    ref: tableRef,
    onSortChange: props.sortChangeHandler,
    data: props.data,
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
    if (slots['item.operations'] || props.rowReadHandler || props.rowUpdateHandler || props.rowDeleteHandler) {
      content.push(h(ElTableColumn, {label: '操作', className: 'operations'}, {
        header: slots['header.operation'],
        default: (scope: TableCellScope) => {
          const defaultSlot: (VNode | Slot)[] = [];
          // 操作列
          if (props.rowReadHandler) {
            // TODO: 按钮loading时隐藏图标
            defaultSlot.push(h(ElButton, {
              link: true,
              type: 'primary',
              onClick: () => handleRowRead(scope)
            }, () => [h(ElIcon, {}, () => h(IconEpView)), h('span', {innerText: '查看'})]));
          }
          if (props.rowUpdateHandler) {
            defaultSlot.push(h(ElButton, {
              link: true,
              type: 'primary',
              onClick: () => handleRowEdit(scope)
            }, () => [h(ElIcon, {}, () => h(IconEpEdit)), h('span', {innerText: '修改'})]));
          }
          if (props.rowDeleteHandler) {
            defaultSlot.push(h(ElButton, {
              link: true,
              type: 'danger',
              onClick: () => handleRowDelete(scope)
            }, () => [h(ElIcon, {}, () => h(IconEpDelete)), h('span', {innerText: '删除'})]));
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
  if (props.loading !== undefined) {
    return withDirectives(vnode, [[vLoading, props.loading]]);
  }
  return vnode;
};
defineExpose({
  tableRef,
})
</script>

<template>
  <render/>
</template>

<style lang="scss">
.crud-table td.operations .cell {
  display: flex;
  flex-wrap: wrap;

  & > * {
    margin: 0.25rem;
  }
}
</style>
