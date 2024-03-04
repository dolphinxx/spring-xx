// Utilities
import {defineStore} from 'pinia'
import {getButtons, getSettings} from "@/api/common";
import type {Router} from "vue-router";
import {importDynamic} from "@/utils/internal";
import demoMenus from "@/demo";

export const useAppStore = defineStore('app', {
  state: () => ({
    principal: null,
    settings: {},
    menus: null,
    showingLogin: false,
  } as { principal: Principal | null; settings: Settings; menus: Array<Btn> | null; showingLogin: boolean; }),
  actions: {
    async loadSettings() {
      return getSettings().then(r => {
        this.$state.settings = r;
      });
    },
    async loadButtons(router: Router) {
      return getButtons().then(r => {
        const menus: Array<Btn> = [];
        const buttons: Array<Btn> = [];
        const map = {};
        r.filter(b => {
          if (b.type === 2) {
            buttons.push(b);
            return false;
          }
          map[b.id] = b;
          return true;
        }).forEach(b => {
          if (b.parentId) {
            const p = map[b.parentId];
            if (p.children == null) {
              p.children = [];
            }
            p.children.push(b);
          } else {
            menus.push(b);
          }
        });

        function sort(list: Array<Btn>) {
          list.sort((a, b) => a.order - b.order);
          for (const m of list) {
            if (m.children) {
              sort(m.children);
            }
          }
        }

        sort(menus);
        menus.push(...demoMenus);

        let isCurrentRouteDynamic = false;
        const currentRoute = router.currentRoute.value.path;

        function addRoutes(list) {
          list.forEach(m => {
            if (m.icon) {
              m.icon = '$' + m.icon;
            }
            if (m.target) {
              // create route from the menu target
              router.addRoute('Authenticated', {path: m.target, name: m.name, component: () => importDynamic(m.target)})
              if (m.target === currentRoute) {
                isCurrentRouteDynamic = true;
              }
            }
            if (m.children) {
              addRoutes(m.children);
            }
          })
        }

        // add all the menus containing a target to the route
        addRoutes(menus);
        this.$state.menus = menus;
        if (isCurrentRouteDynamic) {
          // reload current route
          router.replace(router.currentRoute.value.fullPath);
        }
      });
    },
    setPrincipal(principal: Principal | null) {
      this.$state.principal = principal;
    },
    toggleLoginDialog(showing: boolean) {
      this.$state.showingLogin = showing;
    }
  },
})
