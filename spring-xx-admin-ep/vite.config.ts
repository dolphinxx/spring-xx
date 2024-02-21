/// <reference types="vitest" />

// Configure Vitest (https://vitest.dev/config/)
import path from "path";
import {defineConfig} from "vite";
import vue from "@vitejs/plugin-vue";

import {fileURLToPath, URL} from "node:url";

import AutoImport from "unplugin-auto-import/vite";
import Components from "unplugin-vue-components/vite";
import {ElementPlusResolver} from "unplugin-vue-components/resolvers";
import Icons from "unplugin-icons/vite";
import IconsResolver from 'unplugin-icons/resolver'

const srcPath = path.resolve(__dirname, 'src')

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    AutoImport({
      resolvers: [
        ElementPlusResolver(),
        IconsResolver({
          prefix: 'Icon',
        }),
      ],
      dts: path.join(srcPath, "types/auto-imports.d.ts"),
    }),
    Components({
      resolvers: [
        // Auto register icon components
        IconsResolver({
          prefix: 'Icon',
          enabledCollections: ['ep'],
        }),
        ElementPlusResolver({
          importStyle: "sass",
        }),
      ],
      dts: path.join(srcPath, "types/components.d.ts"),
    }),
    Icons({
      compiler: "vue3",
    }),
  ],
  define: {"process.env": {}},
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", import.meta.url)),
    },
    extensions: [".js", ".json", ".jsx", ".mjs", ".ts", ".tsx", ".vue"],
  },
  server: {
    port: 3001,
    proxy: {
      "/api": {
        target: "http://localhost:8081",
        changeOrigin: true,
        secure: false,
        ws: true,
        rewrite: (path) => path.replace(/^\/api/, ""),
      },
    },
  },
  test: {
    globals: true,
    environment: "jsdom",
  },
});
