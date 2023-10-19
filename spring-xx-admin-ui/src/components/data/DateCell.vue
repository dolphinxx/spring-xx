<script lang="ts">
import {createTextVNode, inject} from "vue";
import {format} from "date-fns";
import {LocalizationKey} from "@/symbols";
const {locale} = inject(LocalizationKey);

const defaultFormatter = 'yyyy-MM-dd HH:mm:ss';

function convert(value:string|number|Date, formatter:string):string {
  const type = typeof value;
  if(type === 'number' || type === 'string') {
    value = new Date(value);
  }
  return format(value as Date, formatter, {locale});
}

export default {
  props: {
    value: {
      required: true,
      type: [String, Number, Object as Date],
    },
    formatter: {
      required: false,
      type: String,
      default: defaultFormatter,
    }
  },
  setup(props) {
    return () => createTextVNode(convert(props.value, props.formatter || defaultFormatter));
  }
}
</script>
