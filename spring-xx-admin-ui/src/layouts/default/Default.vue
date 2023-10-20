<template>
  <v-app>
    <v-layout v-if="store.principal" style="position: relative;">
      <v-navigation-drawer :model-value="true" :permanent="true" :rail="!showingDrawer" style="position: fixed;">
        <template v-slot:prepend>
          <div class="menu-header">
            <v-avatar class="menu-header-avatar" color="info">
              {{ store.principal.name[0].toUpperCase() }}
            </v-avatar>
            <span class="menu-header-name">{{ store.principal.name }}</span>
          </div>
          <v-divider></v-divider>
        </template>

        <template v-if="store.menus">
          <Menu :items="store.menus"></Menu>
        </template>
        <template v-slot:append>
          <v-divider></v-divider>
          <div class="logout">
            <v-list-item @click="handleLogout" title="退出登录" prepend-icon="mdi-power"/>
          </div>
        </template>
      </v-navigation-drawer>
      <v-app-bar density="compact" style="position: fixed;">
        <v-app-bar-nav-icon variant="text" :icon="showingDrawer ? 'mdi-close' : 'mdi-menu'" @click.stop="showingDrawer = !showingDrawer"></v-app-bar-nav-icon>
        <div id="top-navs">
          <v-btn prepend-icon="mdi-home" variant="plain" text="Home" to="/" :exact="true"/>
          <v-btn prepend-icon="mdi-home" variant="plain" text="Menus" to="/demo/menus" :exact="true"/>
        </div>
        <v-spacer></v-spacer>
      </v-app-bar>
      <v-main>
        <router-view/>
      </v-main>
    </v-layout>
  </v-app>
</template>

<script lang="ts" setup>
import {ref} from "vue";
import {useRouter} from 'vue-router'
import {useAppStore} from "@/store/app";
import Cookie from "js-cookie";
import {getPrincipal, logout} from "@/api/common";
import Menu from "@/components/Menu.vue";

const store = useAppStore();
const showingDrawer = ref<boolean>(false);
const router = useRouter();

function handleLogout() {
  logout().finally(() => {
    store.setPrincipal(null);
    redirectToLogin();
  })
}

function redirectToLogin() {
  router.replace({name: 'Login'});
}

(async function () {
  if (store.principal === null) {
    if (Cookie.get('authenticated') === '1') {
      // trigger remember me login
      try {
        const principal = await getPrincipal();
        store.setPrincipal(principal);
        // remember-me authentication success.
      } catch (ignore) {
        redirectToLogin();
        return;
      }
    } else {
      // redirect to login page
      redirectToLogin();
      return;
    }
  }
  store.loadButtons(router);
}())
</script>
<style lang="scss">
.menu-header {
  height: 48px;
  padding: 4px 16px;
  padding-inline-start: 16px;
  padding-inline-end: 16px;
  display: flex;
  flex-direction: row;
  flex-wrap: nowrap;
  align-items: center;
  .menu-header-name {
    margin-left: 0.5em;
  }
}
.v-navigation-drawer--rail {
  .menu-header {
    padding-left: 8px;
    padding-right: 8px;
    .menu-header-name {
      display: none;
    }
  }
  .v-list-group__items {
    .v-list-item {
      padding-inline-start: 8px !important;
    }
  }
}
#top-navs {
  .v-btn--active {
    color: rgb(var(--v-theme-primary));
  }
}
</style>
