<template>
  <v-app>
    <v-layout>
      <v-app-bar color="surface-variant">
        <v-app-bar-nav-icon variant="text" :icon="showingDrawer ? 'mdi-close' : 'mdi-menu'" @click.stop="showingDrawer = !showingDrawer"></v-app-bar-nav-icon>
        <v-toolbar-title>Application bar</v-toolbar-title>
        <v-spacer></v-spacer>
      </v-app-bar>
      <v-navigation-drawer v-model="showingDrawer" :permanent="true">
        <v-list>
          <v-list-item>
            <template v-slot:prepend>
              <v-list-item-media :start="true">
                <v-tooltip text="Current Project">
                  <template v-slot:activator="{ props }">
                    <v-icon v-bind="props" icon="mdi-dns"></v-icon>
                  </template>
                </v-tooltip>
              </v-list-item-media>
            </template>
            <v-list-item-title>{{ store.currentProject.name }}</v-list-item-title>
            <template v-slot:append>
              <v-btn icon="mdi-chevron-right" variant="plain" to="/projects"/>
            </template>
            <!--            <template v-slot:append>-->
            <!--              <v-menu v-model="showingProjects" :close-on-content-click="false">-->
            <!--                <template v-slot:activator="{ props }">-->
            <!--                  <v-btn icon="mdi-dots-vertical" v-bind="props"></v-btn>-->
            <!--                </template>-->
            <!--                <v-card min-width="300">-->
            <!--                  <v-card-title>Projects</v-card-title>-->
            <!--                  <v-list>-->
            <!--                    <v-list-item-->
            <!--                      v-for="(project) in projects"-->
            <!--                      :key="project.id"-->
            <!--                      color="primary"-->
            <!--                      :active="currentProject.id === project.id"-->
            <!--                      variant="text"-->
            <!--                      @click="currentProject = project"-->
            <!--                    >-->
            <!--                      <v-list-item-title>{{ project.name }}</v-list-item-title>-->
            <!--                      <v-list-item-subtitle>{{ project.path }}</v-list-item-subtitle>-->
            <!--                      <v-list-item-action>-->
            <!--                        <v-btn @click.prevent="" density="compact" variant="text" style="margin-right: 0.5rem;">Edit</v-btn>-->
            <!--                        <v-btn density="compact" variant="text">Delete</v-btn>-->
            <!--                      </v-list-item-action>-->
            <!--                    </v-list-item>-->
            <!--                  </v-list>-->

            <!--                  <v-card-actions>-->
            <!--                    <v-spacer></v-spacer>-->

            <!--                    <v-btn-->
            <!--                      variant="text"-->
            <!--                      @click="showingProjects = false"-->
            <!--                    >-->
            <!--                      Cancel-->
            <!--                    </v-btn>-->
            <!--                    <v-btn-->
            <!--                      color="primary"-->
            <!--                      variant="text"-->
            <!--                      @click="showingProjects = false"-->
            <!--                    >-->
            <!--                      Save-->
            <!--                    </v-btn>-->
            <!--                  </v-card-actions>-->
            <!--                </v-card>-->
            <!--              </v-menu>-->

            <!--            </template>-->
          </v-list-item>
        </v-list>
        <v-divider></v-divider>
        <v-list>
          <v-list-item>
            123
          </v-list-item>
        </v-list>
      </v-navigation-drawer>
      <v-main>
        <router-view/>
      </v-main>
    </v-layout>
  </v-app>
</template>

<script lang="ts" setup>
import {ref} from "vue";
import {useAppStore} from "@/store/app";

const store = useAppStore();
const showingDrawer = ref<boolean>(false);

</script>
<style lang="scss">
.project-list {
  .project-list-item {
    display: flex;
    flex-direction: row;
    padding: 0.5rem;

    &:hover {
      background-color: rgb(var(--v-theme-primary));
    }

    .project-list-content {
      flex-grow: 1;
      flex-shrink: 1;
    }

    .project-list-action {
      flex-grow: 0;
      flex-shrink: 0;
      display: flex;
      flex-direction: column;
    }
  }
}
</style>
