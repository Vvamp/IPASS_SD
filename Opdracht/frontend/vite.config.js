import { resolve } from "path";
import { defineConfig } from "vite";
export default defineConfig({
  build: {
    sourcemap: true,
    rollupOptions: {
      input: {
        main: resolve(__dirname, "index.html"),
        ais: resolve(__dirname, "/data/overtochten/index.html"),
      },
    },
    outDir: "../src/main/webapp/",
  },
});
