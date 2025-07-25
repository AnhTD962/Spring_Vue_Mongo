// vite.config.js
import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server: {
    host: '0.0.0.0',       // Allows access from outside the container (important for Docker)
    port: 8081,            // Set frontend dev server to run on port 8081
    proxy: {
      // Proxy requests starting with /api to the backend
      '/api': {
        target: 'http://backend:8080', // Your backend URL (assuming it runs on host port 8080)
        changeOrigin: true, // Necessary for virtual hosted sites
        rewrite: (path) => path.replace(/^\/api/, '/api'), // Keep the /api prefix
        secure: false, // Set to true if your backend uses HTTPS
      },
      // Proxy requests starting with /uploads to the backend
      '/uploads': {
        target: 'http://backend:8080', // Your backend URL for uploads
        changeOrigin: true,
        secure: false,
      }
    }
  }
})