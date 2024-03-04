<script lang="ts">
import {createTextVNode, inject} from "vue";
import type { PropType } from "vue";
import {format} from "date-fns";
import {LocalizationKey} from "@/symbols";

const defaultFormatter = 'yyyy-MM-dd HH:mm:ss';

type PropsType = {
  value: string|number|Date;
  formatter?: string;
}

export default {
  props: {
    value: {
      required: true,
      type: [String, Number, Object ] as PropType<string|number|Date>,
    },
    formatter: {
      required: false,
      type: String,
      default: defaultFormatter,
    }
  },
  setup(props: PropsType) {
    const {formatLocale} = inject(LocalizationKey)!;
    function convert(value:string|number|Date, formatter:string):string {
      const type = typeof value;
      if(type === 'number' || type === 'string') {
        value = new Date(value);
      }
      return format(value as Date, formatter, {locale: formatLocale});
    }
    return () => createTextVNode(convert(props.value, props.formatter || defaultFormatter));
  }
}
</script>
