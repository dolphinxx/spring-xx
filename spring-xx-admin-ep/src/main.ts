import {createApp} from "vue";
import "nprogress/nprogress.css";
import "@/styles/main.scss";
import App from "./App.vue";

// Plugins
import {pinia, router, components, icons} from "@/plugins";

import {useAppStore} from "@/store/app";

import {LocalizationKey} from "@/symbols";

import {applyRequestHandlers} from "@/api/request";
import createGuard from "./router/guards";
import {redirectToLogin} from "@/composables/auth.ts";

window.__APP__ = {};

const app = createApp(App);

app.use(pinia);
app.use(router);
app.use(components);
app.use(icons);

app.provide<Localization>(LocalizationKey, {
  timezone: "Asia/Shanghai",
  locale: "zh-CN",
});

app.mount("#app");

const appStore = useAppStore();

createGuard(router);

applyRequestHandlers({loginHandler: () => redirectToLogin(router)});
