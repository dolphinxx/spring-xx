// Utilities
import {defineStore} from 'pinia'
import {getSettings, saveSettings} from "@/api/api"
import {nextId} from "@/utils";

export const useAppStore = defineStore('app', {
  state: () => ({
    projects: [],
    currentId: "",
  } as Settings),
  getters: {
    currentProject: (state: Settings) => {
      if (!state.currentId) {
        return state.projects[0];
      }
      return state.projects.find(e => e.id === state.currentId) || state.projects[0];
    },
  },
  actions: {
    async loadSettings() {
      getSettings().then(r => {
        if (!r.currentId) {
          r.currentId = r.projects[0].id;
        }
        this.$state = r;
      });
    },
    async save() {
      return saveSettings(this.$state);
    },
    async saveProject(data) {
      if (!data.id) {
        data.id = nextId();
        this.$state.projects.push(data);
      } else {
        Object.assign(this.$state.projects.find(e => e.id === data.id), data);
      }
      return this.save();
    },
    async chooseProject(id) {
      this.$state.currentId = id;
      return this.save();
    },
    async removeProject(id) {
      const index = this.$state.projects.findIndex(e => e.id === id);
      if (index !== -1) {
        this.$state.projects.splice(index, 1);
        return this.save();
      }
    }
  }
})
