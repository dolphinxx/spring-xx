import {App} from "vue";

import IconEpCircleCheck from '~icons/ep/circle-check';
import IconEpMenu from '~icons/ep/menu';
import IconEpUser from '~icons/ep/user';
import IconEpSetting from '~icons/ep/setting';
import IconEpOperation from '~icons/ep/operation';
import IconEpLock from '~icons/ep/lock';
import IconEpUnlock from '~icons/ep/unlock';
import IconEpPointer from '~icons/ep/pointer';
import IconEpGrid from '~icons/ep/grid';
import IconEpScaleToOriginal from '~icons/ep/scale-to-original';

// register icons for dynamic menus
const icons = {
  IconEpCircleCheck,
  IconEpMenu,
  IconEpUser,
  IconEpSetting,
  IconEpOperation,
  IconEpLock,
  IconEpUnlock,
  IconEpPointer,
  IconEpScaleToOriginal,
  IconEpGrid,
}

export default {
  install(app: App) {
    for (const [key, component] of Object.entries(icons)) {
      app.component(key, component);
    }
  },
};
