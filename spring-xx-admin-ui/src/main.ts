/**
 * main.ts
 *
 * Bootstraps Vuetify and other plugins then mounts the App`
 */

// Components
import App from './App.vue'

// Composables
import {createApp} from 'vue'

// Plugins
import {registerPlugins} from '@/plugins'
import '@/styles/main.scss';
import zhCN from "date-fns/locale/zh-CN";
import {LocalizationKey} from "@/symbols";

window.__APP__ = {
  storagePrefix: import.meta.env.VITE_STORAGE_PREFIX,
};
globalThis.hasValue = (v:any) => v !== undefined && v !== null;

const app = createApp(App)

app.provide<Localization>(LocalizationKey, {timezone: 'Asia/Shanghai', locale: 'zh-CN', formatLocale: zhCN});

registerPlugins(app)

app.mount('#app')
