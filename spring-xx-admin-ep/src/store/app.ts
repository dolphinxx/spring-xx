// Utilities
import { defineStore } from "pinia";
import { getButtons, getSettings } from "@/api/common";
import type { RouteLocationNormalized, Router } from "vue-router";
import { importDynamic } from "@/utils/internal";
import demoMenus from "@/demo";

type ViewTab = { path: string; name: string };

type AppState = {
  principal: Principal | null;
  settings: Settings;
  menus: Array<Btn> | null;
  tabs: ViewTab[];
};

export const useAppStore = defineStore("app", {
  state: () =>
    <AppState>{
      principal: null,
      settings: {},
      menus: null,
      tabs: [] as ViewTab[],
    },
  actions: {
    addTab(tab: RouteLocationNormalized) {
      if (tab.name === "NotFound" || tab.fullPath === '/login') {
        return;
      }
      const existsIndex = this.tabs.findIndex((el) => el.path === tab.fullPath);
      const viewTab = { path: tab.fullPath, name: tab.name as string };
      if (existsIndex === -1) {
        if(tab.fullPath === '/') {// home page is always in the first position
          this.tabs.unshift(viewTab);
        } else {
          this.tabs.push(viewTab);
        }
      } else {
        this.tabs.splice(existsIndex, 1, viewTab);
      }
    },
    removeTab(path: string) {
      const index = this.tabs.findIndex((el) => (el.path === path));
      if (index !== -1) {
        this.tabs.splice(index, 1);
      }
    },
    async loadSettings() {
      return getSettings().then((r) => {
        this.$state.settings = r;
      });
    },
    async loadButtons(router: Router) {
      return getButtons().then((r) => {
        const menus: Array<Btn> = [];
        const buttons: Array<Btn> = [];
        const map: { [key: number]: Btn } = {};
        r.filter((b) => {
          if (b.type === 2) {
            buttons.push(b);
            return false;
          }
          map[b.id] = b;
          return true;
        }).forEach((b) => {
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

        function addRoutes(list: Btn[]) {
          list.forEach((m) => {
            if (m.icon) {
              m.icon = "icon-" + m.icon;
            }
            if (m.target) {
              // create route from the menu target
              router.addRoute("Authenticated", {
                path: m.target,
                name: m.name,
                component: () => importDynamic(m.target!),
              });
              if (m.target === currentRoute) {
                isCurrentRouteDynamic = true;
              }
            }
            if (m.children) {
              addRoutes(m.children);
            }
          });
        }

        // add all the menus containing a target to the route
        addRoutes(menus);
        this.$state.menus = menus;
        if (isCurrentRouteDynamic) {
          // reload current route
          console.log("replace route");
          router.replace(router.currentRoute.value.fullPath);
        }
      });
    },
    setPrincipal(principal: Principal | null) {
      this.$state.principal = principal;
    },
  },
});
