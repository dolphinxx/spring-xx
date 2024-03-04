/**
 * plugins/vuetify.ts
 *
 * Framework documentation: https://vuetifyjs.com`
 */

// Styles
// import '@mdi/font/css/materialdesignicons.css'
import 'vuetify/styles'
import {aliases, mdi} from 'vuetify/iconsets/mdi-svg'
import {
  mdiAccountGroup,
  mdiAccountMultipleOutline,
  mdiAccountOutline, mdiAlertCircle, mdiAlertCircleOutline,
  mdiApps, mdiCalendar, mdiChevronDown,
  mdiChevronUp,
  mdiClose, mdiCodeJson,
  mdiCogOutline,
  mdiConsole, mdiDelete, mdiDeleteOutline, mdiEye, mdiEyeOff, mdiGestureTapButton,
  mdiHome, mdiLockOutline,
  mdiMenu, mdiMessageQuestion, mdiPaperclip, mdiPen, mdiPencil,
  mdiPlus,
  mdiPower, mdiReload, mdiShieldLockOutline, mdiSpeedometer, mdiUpload, mdiUploadMultiple, mdiViewDashboard
} from '@mdi/js';

// Composables
import {createVuetify} from 'vuetify'
import type {IconSet} from 'vuetify'
import { pl, zhHans } from 'vuetify/locale'

import {
  VDataTable,
  VDataTableServer,
  VDataTableVirtual,
} from "vuetify/labs/VDataTable";

const extraIconAlias: Record<string, string> = {
  ['mdi-power']: mdiPower,
  ['mdi-plus']: mdiPlus,
  ['mdi-apps']: mdiApps,
  ['mdi-menu']: mdiMenu,
  ['mdi-eye']: mdiEye,
  ['mdi-eye-off']: mdiEyeOff,
  ['mdi-calendar']: mdiCalendar,
  ['mdi-alert-circle']: mdiAlertCircle,
  ['mdi-alert-circle-outline']: mdiAlertCircleOutline,
  ['mdi-error']: mdiMessageQuestion,
  ['mdi-chevron-up']: mdiChevronUp,
  ['mdi-chevron-down']: mdiChevronDown,
  ['mdi-upload']: mdiUpload,
  ['mdi-upload-multiple']: mdiUploadMultiple,
  ['mdi-code-json']: mdiCodeJson,
  ['mdi-shield-lock-outline']: mdiShieldLockOutline,
  ['mdi-gesture-tap-button']: mdiGestureTapButton,
  ['mdi-home']: mdiHome,
  ['mdi-paperclip']: mdiPaperclip,
  ['mdi-lock-outline']: mdiLockOutline,
  ['mdi-cog-outline']: mdiCogOutline,
  ['mdi-console']: mdiConsole,
  ['mdi-close']: mdiClose,
  ['mdi-account-outline']: mdiAccountOutline,
  ['mdi-account-multiple-outline']: mdiAccountMultipleOutline,
  ['mdi-pen']: mdiPen,
  ['mdi-pencil']: mdiPencil,
  ['mdi-delete']: mdiDelete,
  ['mdi-delete-outline']: mdiDeleteOutline,
  ['mdi-reload']: mdiReload,

  ['mdi-account-group']: mdiAccountGroup,
  ['mdi-view-dashboard']: mdiViewDashboard,
  ['mdi-speedometer']: mdiSpeedometer,
}

// https://vuetifyjs.com/en/introduction/why-vuetify/#feature-guides
export default createVuetify({
  theme: {
    themes: {
      light: {
        colors: {
          primary: '#1867C0',
          secondary: '#5CBBF6',
        },
      },
    },
  },
  components: {
    VDataTable,
    VDataTableServer,
    VDataTableVirtual,
  },
  icons: {
    defaultSet: 'mdi',
    aliases: {
      ...aliases,
      ...extraIconAlias,
    },
    sets: {
      mdi,
    } as Record<string, IconSet>,
  },
  locale: {
    locale: 'zhHans',
    messages: { zhHans, pl },
  },
})
