<!--<template>-->
<!--  <render/>-->
<!--&lt;!&ndash;  <v-list :opened="opened" @update:opened="cascade">&ndash;&gt;-->
<!--&lt;!&ndash;    <template v-for="(item, i) in items">&ndash;&gt;-->
<!--&lt;!&ndash;      <v-list-item v-if="item.target && !item.children" :to="item.target" :prepend-icon="item.icon" :title="item.name" :key="i"></v-list-item>&ndash;&gt;-->
<!--&lt;!&ndash;      <v-list-group v-if="item.children && item.children.length > 0" :key="i" :value="`root-${i};`">&ndash;&gt;-->
<!--&lt;!&ndash;        <template #activator="{props}">&ndash;&gt;-->
<!--&lt;!&ndash;          <v-list-item v-bind="props" :title="item.name"></v-list-item>&ndash;&gt;-->
<!--&lt;!&ndash;        </template>&ndash;&gt;-->
<!--&lt;!&ndash;        <template v-for="(item2, i2) in item.children">&ndash;&gt;-->
<!--&lt;!&ndash;          <v-list-item v-if="item2.target && !item2.children" :to="item2.target" :prepend-icon="item2.icon" :title="item2.name" :key="i2"></v-list-item>&ndash;&gt;-->
<!--&lt;!&ndash;          <v-list-group v-if="item2.children && item2.children.length > 0" :key="i2" :value="`root-${i};-${i2};`">&ndash;&gt;-->
<!--&lt;!&ndash;            <template #activator="{props}">&ndash;&gt;-->
<!--&lt;!&ndash;              <v-list-item v-bind="props" :title="item2.name"></v-list-item>&ndash;&gt;-->
<!--&lt;!&ndash;            </template>&ndash;&gt;-->
<!--&lt;!&ndash;            <v-list-item v-for="(item3, i3) in item2.children" :title="item3.name" :key="i3" :to="item3.target"></v-list-item>&ndash;&gt;-->
<!--&lt;!&ndash;          </v-list-group>&ndash;&gt;-->
<!--&lt;!&ndash;        </template>&ndash;&gt;-->
<!--&lt;!&ndash;      </v-list-group>&ndash;&gt;-->
<!--&lt;!&ndash;    </template>&ndash;&gt;-->
<!--&lt;!&ndash;  </v-list>&ndash;&gt;-->
<!--</template>-->
<script lang="ts">
import {h, ref} from 'vue';
import {VList, VListGroup, VListItem} from "vuetify/components";
import * as vue_router from "vue-router";

export default {
  props: {
    items: {
      type: Array as Btn[],
      required: true
    },
  },
  setup(props) {
    const opened = ref([]);

    function renderMenu(list: Btn[], parentValue?: string) {
      if (!list) {
        return [];
      }
      return list.filter(item => item.target || (item.children != null && item.children.length > 0)).map((item, i) => {
        const props: { title: string; key: any; value?: any; to?: vue_router.RouteLocationRaw | undefined; } = {title: item.name, key: i};
        if (item.icon) {
          props['prepend-icon'] = item.icon;
        }
        if (item.target) {
          props.value = item.key;
          props.to = item.target!;
        }
        if (item.children == null) {
          return h(VListItem, props);
        }
        const val = parentValue ? `${parentValue}-${i};` : `${i};`;
        props.value = val;
        return h(VListGroup, {...props}, {
          activator: slotProps => {
            return h(VListItem, {...slotProps.props});
          },
          default: () => renderMenu(item.children!, val)
        });
      });
    }

    const rootToLeaf = (leaf): string[] => {
      const nodes = leaf.split('-');
      const result = [];
      for (let i = 0; i < nodes.length; i++) {
        if (i === 0) {
          result.push(nodes[i]);
          continue;
        }
        result.push(`${nodes[i - 1]}-${nodes[i]}`);
      }
      return result;
    }

    const accordionify = (newVal) => {
      if (opened.value.length > newVal.length) {
        // close
        opened.value = newVal;
      } else {
        // open
        const openedLeaf = newVal[newVal.length - 1];
        opened.value = rootToLeaf(openedLeaf);
      }
      return true;
    }

    return () => h(VList, {
      density: 'compact',
      'opened': opened.value,
      'onUpdate:opened': accordionify,
      nav: true,
      exact: true,
      class: 'side-menu'
    }, () => renderMenu(props.items));
  }
}
</script>
