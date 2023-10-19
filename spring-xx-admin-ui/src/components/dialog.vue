<template>
  <v-dialog
    v-for="wrapper in store.dialogs"
    :close-on-back="false"
    :persistent="true"
    v-model="wrapper.showing"
    :key="wrapper.id"
    :class="wrapper.type + '-dialog'"
  >
    <v-card>
      <v-card-item v-if="wrapper.options?.msg">{{ wrapper.options?.msg }}</v-card-item>
      <component
        v-if="wrapper.options?.component"
        :is="wrapper.options?.component?.type"
        v-bind="{...wrapper.options?.component?.props}"
      ></component>
      <v-card-actions>
        <v-spacer/>
        <v-btn @click="store.close(wrapper.id, false)">Cancel</v-btn>
        <v-btn @click="store.close(wrapper.id, true)">Ok</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
<script lang="ts" setup>
import {useDialogStore} from "@/store/dialog";

const store = useDialogStore();
</script>
<style lang="scss">
.confirm-dialog, .alert-dialog {
  &> div.v-overlay__content {
    width: auto;
    min-width: 24rem;
  }
}
</style>
