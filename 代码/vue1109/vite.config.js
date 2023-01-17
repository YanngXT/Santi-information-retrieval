import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    proxy: {// 跨域代理
      '/apis': {
        // target: 'http://' + env.VUE_APP_BASE_API,
        target: 'http://localhost:8090/', // 
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/apis/, '')
      },
      // 代理 WebSocket 或 socket
      // '/socket.io': {
      //   target: 'ws://localhost:3000',
      //   ws: true
      //  }
    },
  },
})
