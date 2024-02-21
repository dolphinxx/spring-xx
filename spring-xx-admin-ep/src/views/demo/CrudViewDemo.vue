<script setup lang="ts">
import {ref, h} from "vue";
import {type TableColumnCtx} from 'element-plus';
import {format as formatDate} from 'date-fns';
import * as userApi from '@/api/sys/user';
import {createDateFormatter} from '@/utils/crud';
import CrudView from "@/components/CrudView.vue";
import LoadableButton from "@/components/LoadableButton.vue";
import {delay} from "@/utils/utils.ts";

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
    formProps: {
      type: 'select',
      options: [
        {
          label: '禁用',
          value: 0
        },
        {
          label: '启用',
          value: 1
        },
      ]
    }
  },
  {
    prop: 'createTime',
    label: '创建时间',
    formatter: (_row: any, _column: TableColumnCtx<any>, cellValue: any) => cellValue ? formatDate(cellValue, 'yyyy-MM-dd HH:mm:ss') : '',
    formProps: {
      type: 'date',
      attrs: {
        'value-format': 'YYYY-MM-DD',
      }
    }
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
    formProps: {
      type: "textarea",
    }
  }
];

const readingProps: string[] = [
  'id',
  'name',
  'username',
  'status',
  'createTime',
  'updateTime',
  'creatorId',
  'lastModifierId',
  'lastModifyTime',
  'remark',
];

const editingProps: (FormField | string)[] = [
  'name',
  'remark'
];
const creatingProps: (FormField | string)[] = ['name', 'username', 'remark'];

const searchOptions: (FormField | string)[] = [
  'name',
  'status',
  {
    label: '日期',
    prop: 'createTime',
  },
]

const crudRef = ref<InstanceType<typeof CrudView>>();

const fetcher: PaginateCrudDataFetcher<UserVO> = (params: PaginateCrudDataFetcherParams) => {
  return userApi.paginateUsers(params);
}

const readingRowFetcher = async (id: any) => {
  return userApi.readUser(id);
}

const rowCreateHandler = async (entity: UserForm) => {
  console.log('creating', entity);
  return userApi.createUser(entity);
}

const rowUpdateHandler = async (entity: UserForm) => {
  console.log('updating', entity);
  return userApi.updateUser(entity);
}

const rowDeleteHandler = async (id: any) => {
  console.log('deleting', id);
  return id instanceof Array ? userApi.deleteUsers(id) : userApi.deleteUser(id);
}

const greeting = (name: string) => {
  ElNotification({
    title: '问候',
    message: `你好，${name}！`,
  })
}

const resetPassword = async (id:string) => {
  console.log('重置密码', id);
  await delay(() => {}, 2000);
  ElNotification({
    type: 'success',
    title: '操作成功',
    message: '密码已重置成------',
  });
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
      <CrudView
          ref="crudRef"
          :columns="columns"
          :paginate="true"
          :search="searchOptions"
          :fetcher="fetcher"
          :row-creating-handler="rowCreateHandler"
          :reading-row-fetcher="readingRowFetcher"
          :row-updating-handler="rowUpdateHandler"
          :row-deleting-handler="rowDeleteHandler"
          :reading-props="readingProps"
          :creating-props="creatingProps"
          :editing-props="editingProps"
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
          <loadable-button :link="true" type="primary" :load="() => resetPassword(row.id)">重置密码</loadable-button>
          <el-button :link="true" type="primary" @click="greeting(row.name)">问候</el-button>
        </template>
      </CrudView>
    </el-card>
  </view-container>
</template>

<style lang="scss">
td.success-cell {
  color: var(--el-color-success);
}
</style>

