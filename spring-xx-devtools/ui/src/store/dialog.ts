import {defineStore} from "pinia";


export const useDialogStore = defineStore('dialog', {
  state: () => ({
    confirms: [],
    alerts: [],
    dialogs: ((() => {
      const r: DialogOptionsWrapper[] = [];
      for (let i = 0; i < 5; i++) {
        r.push({
          id: i + 1,
          type: "confirm",
          showing: false,
          options: undefined,
        })
      }
      return r;
    })()),
  }),
  actions: {
    close(id: number, result: boolean) {
      const dialog = this.$state.dialogs[id - 1];
      dialog.showing = false;
      if (result && dialog.options!.ok) {
        dialog.options!.ok!();
      }
      dialog.options = undefined;
    },
    confirm(dialog: DialogOptions): number {
      for (let wrapper of this.$state.dialogs) {
        if (wrapper.options === undefined) {
          wrapper.options = dialog;
          wrapper.type = "confirm";
          wrapper.showing = true;
          return wrapper.id;
        }
      }
      throw new Error("global dialog exceeded.");
    },
    alert(dialog: DialogOptions): number {
      for (let wrapper of this.$state.dialogs) {
        if (wrapper.options === undefined) {
          wrapper.options = dialog;
          wrapper.type = "alert";
          wrapper.showing = true;
          return wrapper.id;
        }
      }
      throw new Error("global dialog exceeded.");
    }
  }
})
