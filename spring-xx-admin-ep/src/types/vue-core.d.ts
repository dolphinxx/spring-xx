import type { VNode } from "vue";

export {};

// private
type Children = string | number | boolean | VNode | null | Children[];

type Slot = () => Children;

type Slots = { [name: string]: Slot };

declare global {
  type CustomRender = VNode | Slot | Slots;
}
