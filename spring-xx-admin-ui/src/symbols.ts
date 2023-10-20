import type {InjectionKey} from "vue";

// if reactive is required, change to InjectionKey<Ref<Localization>>
export const LocalizationKey = Symbol('Localization') as InjectionKey<Localization>;
