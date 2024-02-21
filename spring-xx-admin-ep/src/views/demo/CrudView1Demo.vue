<script setup lang="ts">
import {ref, h} from "vue";
import {type TableColumnCtx} from 'element-plus';
import {format as formatDate} from 'date-fns';
import * as userApi from '@/api/sys/user';
import {createDateFormatter} from '@/utils/crud';
import CrudView from "@/components/CrudView1.vue";

const columns: CrudTableColumn[] = [
  {
    prop: 'id',
    label: '编号',
  },
  {
    prop: 'name',
    label: '名称',
    sortable: true,
    attrs: {
      'class-name': 'success-cell'
    }
  },
  {
    prop: 'username',
    label: '用户名'
  },
  {
    prop: 'status',
    label: '状态',
    sortable: true,
  },
  {
    prop: 'createTime',
    label: '创建时间',
    formatter: (_row: any, _column: TableColumnCtx<any>, cellValue: any) => cellValue ? formatDate(cellValue, 'yyyy-MM-dd HH:mm:ss') : '',
  },
  {
    prop: 'updateTime',
    label: '更新时间',
    formatter: createDateFormatter('yyyy-MM-dd HH:mm:ss'),
  },
  {
    prop: 'creatorId',
    label: '创建者',
  },
  {
    prop: 'lastModifierId',
    label: '修改者'
  },
  {
    prop: 'lastModifyTime',
    label: '修改时间',
    formatter: 'date'
  },
  {
    prop: 'remark',
    label: '备注',
  }
];

const editingProps: (FormField | string)[] = [
  'name',
  {
    prop: 'remark',
    type: 'textarea'
  }
];
const creatingProps: (FormField | string)[] = ['name', 'username', {prop: 'remark', type: 'textarea'}];

const searchOptions: FormField[] = [
  {
    label: '名称',
    prop: 'name',
  },
  {
    label: '状态',
    prop: 'status',
    type: 'select',
    options: [
      {
        label: '0',
        value: 0
      },
      {
        label: '1',
        value: 1
      },
    ]
  },
  {
    label: '日期',
    prop: 'createTime',
    type: 'date',
    attrs: {
      'value-format': 'YYYY-MM-DD',
    }
  },
]

const crudRef = ref<InstanceType<typeof CrudView>>();

const fetcher: PaginateCrudDataFetcher<UserVO> = (params: PaginateCrudDataFetcherParams) => {
  return userApi.paginateUsers(params);
}

const rowReadHandler = (id: any) => {
}

const rowCreateHandler = (entity: UserForm) => {
}

const rowUpdateHandler = (entity: UserForm) => {
}

const rowDeleteHandler = (id: any) => {
}

const greeting = (name: string) => {
  ElNotification({
    title: '问候',
    message: `你好，${name}！`,
  })
}
const showCrudInfo = () => {
  ElMessageBox({
    title: 'Crud信息',
    message: h('div', null, [
      h(ElDescriptions, {
        title: '表单',
        border: true,
        column: 2
      }, () => Object.entries(crudRef.value.searchParams).map(entry => h(ElDescriptionsItem, {
        label: entry[0],
      }, () => String(entry[1] ?? '')))),
      h(ElDivider),
      h(ElDescriptions, {
        title: '表格',
        border: true,
        column: 2
      }, () => [
        ['page', crudRef.value.paginateParams.page],
        ['size', crudRef.value.paginateParams.size],
        ['totalCount', crudRef.value.totalCount],
        ['totalPages', crudRef.value.totalPages],
      ].map(entry => h(ElDescriptionsItem, {label: entry[0]}, () => String(entry[1] ?? ''))))
    ]),
    callback: () => {
    },
  });
}
</script>

<template>
  <view-container>
    <el-card>
      <crud-view ref="crudRef" :columns="columns" :paginate="true" :search="searchOptions" :fetcher="fetcher"
                 :row-create-handler="rowCreateHandler" :row-read-handler="rowReadHandler"
                 :row-update-handler="rowUpdateHandler" :row-delete-handler="rowDeleteHandler"
                 :creating-props="creatingProps"
      >
        <template #searchFormButtons>
          <el-button @click="showCrudInfo()">自定义按钮</el-button>
        </template>
        <template #toolbarButtons>
          <el-button @click="showCrudInfo()">自定义按钮</el-button>
        </template>
        <template #toolbarRightButtons>
          <el-tooltip content="自定义按钮">
            <el-button :circle="true" @click="showCrudInfo()">
              <el-icon>
                <icon-ep-plus/>
              </el-icon>
            </el-button>
          </el-tooltip>
        </template>
        <template #[`item.remark`]="{row, column}">
          <el-popover trigger="hover" :content="row[column.property]">
            <template #reference>
              <span class="ellipsis-2">{{ row[column.property] }}</span>
            </template>
          </el-popover>
        </template>
        <template #[`item.operations`]="{row}">
          <el-button :link="true" type="primary" @click="greeting(row.name)">问候</el-button>
        </template>
      </crud-view>
    </el-card>
  </view-container>
</template>

<style lang="scss">
td.success-cell {
  color: var(--el-color-success);
}
</style>

