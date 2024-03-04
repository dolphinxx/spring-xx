<template>
  <v-container>
    <template class="project-container">
      <v-card v-for="project in store.projects" :key="project.id" variant="outlined" class="project" :color="project.id === store.currentId ? 'primary' : ''">
        <v-card-title style="cursor: pointer;" @click="store.chooseProject(project.id)">{{ project.name }}</v-card-title>
        <v-card-subtitle>{{ project.path || "[not specified]" }}</v-card-subtitle>
        <v-card-actions>
          <v-btn @click="openForm(project)">Edit</v-btn>
          <v-btn @click="removeProject(project.id)" :disabled="project.id === store.currentId">Remove</v-btn>
        </v-card-actions>
      </v-card>
      <v-card variant="flat" color="primary" class="project project-add">
        <v-btn @click="openForm()" variant="flat" color="primary" style="width: 100%;height: 100%;">
          <v-icon icon="mdi-plus" :size="48"></v-icon>
        </v-btn>
      </v-card>
    </template>
  </v-container>
  <v-dialog v-model="showingForm" style="max-width: 40rem;">
    <v-card class="pa-6">
      <v-card-title>{{ formData.id ? "Edit" : "Add" }}</v-card-title>
      <v-form ref="formRef" validate-on="blur lazy" @submit.prevent>
        <v-text-field v-model="formData.name" label="Project Name" :rules="[rules.required]"></v-text-field>
        <v-text-field v-model="formData.path" label="Project Path" :rules="[rules.required, rules.dirExists]"></v-text-field>
      </v-form>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn variant="text" @click="cancelForm">Cancel</v-btn>
        <v-btn variant="text" @click="submitForm">Submit</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
<script lang="ts" setup>
import {useAppStore} from "@/store/app";
import {ref} from "vue";
import {dirExists, saveSettings} from "@/api/api";
import type {VForm} from "vuetify/components/VForm"
import {useDialogStore} from "@/store/dialog";

const rules = {
  required: value => Boolean(value) || 'Required.',
  dirExists: async value => (await dirExists(value)) || "Directory not exists.",
};

const store = useAppStore();
const dialogStore = useDialogStore();
const defaultProject = {
  id: "",
  name: "",
  path: "",
};
const formRef = ref<VForm>();
const showingForm = ref(false);
const formData = ref<Project>({...defaultProject});
const openForm = (data?: Project) => {
  formData.value = data ? {...data} : {...defaultProject};
  showingForm.value = true;
}
const cancelForm = () => {
  showingForm.value = false;
}
const submitForm = async () => {
  if (!(await formRef.value?.validate())) {
    return;
  }
  const data = formData.value;
  saveSettings(data).then(() => store.saveProject(data)).then(() => showingForm.value = false);
}
const removeProject = async (id) => {
  dialogStore.confirm({
    msg: "Remove project?",
    ok: ()=> store.removeProject(id),
  });
}
</script>
<style lang="scss" scoped>
.project-container {
  margin: -0.5rem;
  display: flex;
  flex-wrap: wrap;
}

.project {
  margin: 0.5rem;
}

.project-add {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 7.625rem;
}
</style>
