import { resolve } from "path";
import { defineConfig } from "vite";
export default defineConfig({
  build: {
    sourcemap: true,
    rollupOptions: {
      input: {
        main: resolve(__dirname, "index.html"),
        bossschedule: resolve(__dirname, "rooster/boss/index.html"),
        skipperschedule: resolve(__dirname, "/rooster/skipper/index.html"),
      },
    },
    outDir: "../src/main/webapp/",
  },
});
