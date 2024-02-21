import { type App } from "vue";

import ViewContainer from "@/components/ViewContainer.vue";
import CrudView from "@/components/CrudView1.vue";
import CrudTable from "@/components/CrudTable.vue";
import AutoForm from "@/components/form/AutoForm.vue";
import AutoFormField from "@/components/form/AutoFormField.vue";
import DoubleCheckButton from "@/components/DoubleCheckButton.vue";
import LoadableButton from "@/components/LoadableButton.vue";
// import * as icons from "./icons";

const components = {
  ViewContainer,
  CrudView,
  CrudTable,
  AutoFormField,
  AutoForm,
  DoubleCheckButton,
  LoadableButton,
};

export default {
  install(app: App) {
    // for (const [key, component] of Object.entries(icons)) {
    //   app.component("Icon" + key, component);
    // }
    for (const [key, component] of Object.entries(components)) {
      app.component(key, component);
    }
  },
};
