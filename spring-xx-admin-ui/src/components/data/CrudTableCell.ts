import { PropType, h, defineComponent } from "vue";
import {
  VBtn,
  VDefaultsProvider,
  VToolbar,
  VTooltip,
} from "vuetify/components";
const actionsDefaults = {
  VBtn: {
    variant: "text",
    size: 24,
  },
};
export default defineComponent({
  props: {
    item: Object,
    column: Object as PropType<ColumnDefinition<any>>,
    rowNum: Number,
    colNum: Number,
    rowActions: Array as PropType<RowAction<any>[]>,
  },
  setup({ item, column, rowNum, rowActions }) {
    return () => {
      if (column!.key === "#index") {
        return h("span", String(rowNum! + 1));
      }
      if (column!.key === "#actions") {
        return h(
          "div",
          { class: "crud-table-actions" },
          h(
            VDefaultsProvider,
            {
              defaults: actionsDefaults,
            },
            () => {
              return rowActions!.map((action) => {
                if (action.title) {
                  return h(
                    VTooltip,
                    { text: action.title, location: "top" },
                    {
                      activator: (slotProps: any) =>
                        h(VBtn, {
                          icon: action.icon,
                          color: action.color,
                          onClick: () => action.action(item, rowNum!),
                          ...slotProps.props,
                        }),
                    }
                  );
                }
                return h(VBtn, {
                  icon: action.icon,
                  color: action.color,
                  onClick: () => action.action(item, rowNum!),
                });
              });
            }
          )
        );
      }
      const v = item![column!.key];
      if (column!.render) {
        return column!.render!(item, rowNum!);
      }
      if (column!.formatter) {
        return h("span", column!.formatter!(v));
      }
      if (v instanceof Date) {
        return h(
          "span",
          `${v.getFullYear()}-${String(v.getMonth()).padStart(2, "0")}-${String(
            v.getDate()
          ).padStart(2, "0")} ${String(v.getHours()).padStart(2, "0")}:${String(
            v.getMinutes()
          ).padStart(2, "0")}:${String(v.getSeconds()).padStart(2, "0")}`
        );
      }

      return h("span", String(v));
    };
  },
});
