<script setup lang="ts">
import {delay} from "@/utils/utils.ts";
import IconEpDelete from '~icons/ep/delete';

const load = async () => delay(() => {}, 2000);
</script>

<template>
  <double-check-button message="确定要删除吗？" :load="load" :icon="IconEpDelete">
    <span>删除</span>
  </double-check-button>
  <double-check-button message="确定要删除吗？" :load="load">
    <template #icon>
      <el-icon color="green">
        <icon-ep-delete/>
      </el-icon>
    </template>
    <span>删除</span>
  </double-check-button>
</template>
