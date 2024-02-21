import { type Ref, computed, ref, watch } from "vue";
import { uploadHandler as defaultUploadHandler } from "@/api/common";
import { nextId } from "@/utils/utils";
import { useAppStore } from "@/store/app";

export type UploaderPropsType = (
  | {
      modelValue?: string;
      multiple?: false;
    }
  | {
      modelValue?: string[];
      multiple: true;
    }
) & {
  accept?: string;
  uploadHandler?: FileUploadHandler;
};

type UploadTask = {
  id: string;
  name: string;
  key?: string;
  url?: string;
  progress?: number;
  error?: string;
  removed?: boolean;
};

export function useUploader(
  props: UploaderPropsType,
  emitModelValue: (val: any) => void
) {
  const appStorage = useAppStore();
  const urlPrefix = appStorage.settings.uploaded.prefix;

  const uploadHandler = props.uploadHandler || defaultUploadHandler;

  const items: Ref<(UploadTask | string)[]> = ref(
    props.multiple
      ? props.modelValue
        ? [...props.modelValue]
        : []
      : props.modelValue
      ? [props.modelValue]
      : []
  );

  const displayItems = computed(() =>
    items.value.map((_) =>
      typeof _ === "string"
        ? {
            id: _,
            key: _,
            name: _,
            progress: 0,
            url: uploadHandler.buildUrl!(_, urlPrefix),
          }
        : _
    )
  );

  // when modelValue modified by others.
  watch(
    () => props.modelValue,
    () => {
      if (props.multiple) {
        if (props.modelValue && props.modelValue.length > 0) {
          const newItems: (UploadTask | string)[] = [...props.modelValue];
          for (let i = props.modelValue.length; i < items.value.length; i++) {
            if (typeof items.value[i] === "object") {
              newItems.push(items.value[i]);
            }
          }
          items.value = newItems;
        } else {
          items.value = items.value.filter((_) => typeof _ === "object");
        }
      } else {
        if (props.modelValue) {
          items.value[0] = props.modelValue;
        } else {
          items.value = [];
        }
      }
    }
  );
  // if (props.multiple) {
  //   items.value.push({
  //     id: '1000',
  //     name: '1000.png',
  //     progress: 0,
  //   });
  //   items.value.push({
  //     id: '1001',
  //     name: '1001.png',
  //     progress: 0,
  //     error: 'failed',
  //   });
  //   items.value.push({
  //     id: '1002',
  //     name: '1002.png',
  //     progress: 50,
  //   });
  //   items.value.push({
  //     id: '1003',
  //     name: '1704809557041.jpg',
  //     url: '/api/static/tmp/1704809557041.jpg',
  //   });
  // }

  const handleFiles = async (e: Event) => {
    const files = (e.target as HTMLInputElement).files!;
    if (!files || files.length === 0) {
      return;
    }
    if (props.multiple) {
      for (let i = 0; i < files.length; i++) {
        const file = files[i];
        items.value.push(<UploadTask>{
          id: nextId(),
          name: file.name,
          progress: 0,
        });
        const task = items.value[items.value.length - 1] as UploadTask;
        uploadHandler
          .upload(
            file,
            (loaded, total) => (task.progress = (loaded * 100) / total)
          )
          .then((r) => {
            const index = items.value.findIndex(
              (_) => typeof _ === "object" && !_.removed && _.id === task.id
            );
            if (index === -1) {
              return;
            }
            items.value[index] = r.key;
            flushValue();
          })
          .catch((e) => {
            task.error = String(e) || "failed";
          });
      }
    } else {
      const file = files![0];
      items.value[0] = <UploadTask>{
        id: nextId(),
        name: file.name,
        progress: 0,
      };
      const task = items.value[0] as UploadTask;
      uploadHandler
        .upload(file, (loaded, total) => {
          task.progress = (loaded * 100) / total;
        })
        .then((r) => {
          const index = items.value.findIndex(
            (_) => typeof _ === "object" && !_.removed && _.id === task.id
          );
          if (index === -1) {
            return;
          }
          items.value[0] = r.key;
          flushValue();
        })
        .catch((e) => {
          task.error = String(e) || "failed";
        });
    }
    (e.target as HTMLInputElement).value = "";
  };
  const flushValue = () => {
    const val = items.value.filter(
      (item) => typeof item === "string"
    ) as string[];
    emitModelValue(
      props.multiple ? val : val.length === 0 ? undefined : val[0]
    );
  };
  const removeItem = (item: UploadTask) => {
    const index = items.value.findIndex(
      (_) => item.id === (typeof _ === "string" ? _ : _.id)
    );
    if (index === -1) {
      return;
    }
    if (typeof items.value[index] === "string") {
      const key = items.value[index] as string;
      items.value[index] = {
        id: key,
        name: key,
        key,
        progress: 0,
        removed: true,
      };
    } else {
      (items.value[index] as UploadTask).removed = true;
    }
    // item removal animation
    setTimeout(() => {
      const index = items.value.findIndex(
        (_) => item.id === (typeof _ === "string" ? _ : _.id)
      );
      if (index === -1) {
        return;
      }
      const toRemove = items.value.splice(index, 1)[0];
      if (!appStorage.settings.uploaded.sendRemoval) {
        return;
      }
      const key = typeof toRemove === "string" ? toRemove : toRemove.key;
      if (!key) {
        return;
      }
      const remove = (props.uploadHandler || defaultUploadHandler).remove;
      if (remove) {
        remove(key);
        flushValue();
      }
    }, 200);
  };
  return {
    props,
    items: displayItems,
    handleFiles,
    removeItem,
    multiple: props.multiple,
  };
}
