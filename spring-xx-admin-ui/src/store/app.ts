// Utilities
import {defineStore} from 'pinia'
import {getButtons, getSettings} from "@/api/common";
import type {Router} from "vue-router";
import {importDynamic} from "@/utils/internal";

export const useAppStore = defineStore('app', {
  state: () => ({
    principal: null,
    settings: {},
    menus: null,
  } as { principal: Principal | null; settings: Settings; menus: Array<Btn> | null }),
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
        if (import.meta.env.DEV) {
          menus.push({
            id: 10000,
            name: 'Demo',
            key: 'demo',
            type: 1,
            parentId: null,
            order: 10000,
            target: null,
            icon: 'mdi-apps',
            children: [
              {
                id: 10001,
                name: 'MenusDemo',
                key: 'menu-demo',
                type: 1,
                parentId: null,
                order: 1,
                icon: 'mdi-menu',
                target: '/demo/menu_demo',
              },
              {
                id: 10002,
                name: 'AutoFormDemo',
                key: 'auto-form-demo',
                type: 1,
                parentId: null,
                order: 2,
                icon: 'mdi-menu',
                target: '/demo/auto_form_demo',
              },
            ]
          })
        }

        let isCurrentRouteDynamic = false;
        const currentRoute = router.currentRoute.value.path;

        function addRoutes(list) {
          list.forEach(m => {
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
    setPrincipal(principal: Principal) {
      this.$state.principal = principal;
    }
  },
})
