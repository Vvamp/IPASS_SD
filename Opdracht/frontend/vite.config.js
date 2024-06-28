import { resolve } from "path";
import { defineConfig } from "vite";
export default defineConfig({
  build: {
    sourcemap: true,
    rollupOptions: {
      input: {
        main: resolve(__dirname, "index.html"),
        boss: resolve(__dirname, "boss/index.html"),
        bossschedule: resolve(__dirname, "boss/rooster/index.html"),
      },
    },
    outDir: "../src/main/webapp/",
  },
});
