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
        skipperdrukte: resolve(__dirname, "/drukte/skipper/index.html"),
        bossdrukte: resolve(__dirname, "/drukte/boss/index.html"),
        ais: resolve(__dirname, "/data/overtochten/index.html"),
      },
    },
    outDir: "../src/main/webapp/",
  },
});
