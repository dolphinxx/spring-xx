import NProgress from 'nprogress';
import { useAppStore } from "@/store/app";
import { type Router } from "vue-router";
import {saveLoginRedirectUrl} from "@/composables/auth";

const createGuard = (router: Router) => {
  const appStore = useAppStore();

  router.beforeResolve((to, _from, next) => {
    if (to.name) {
        NProgress.start();
    }
    next();
  })

  router.afterEach((to, from, failure) => {
    console.log("route", to, from, failure);
    NProgress.done();
    if (!failure) {
      if(to.fullPath === '/login' && from && from.fullPath !== '/login' && from.fullPath !== '/') {
        saveLoginRedirectUrl(from.fullPath);
      }
      appStore.addTab(to);
    }
  });
};

export default createGuard;
