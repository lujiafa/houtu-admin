import {fileURLToPath, URL} from 'node:url'

import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import Icons from "unplugin-icons/vite";

// docs: https://vitejs.dev/config/
export default defineConfig({
    plugins: [
        vue(),
        Icons({}),
    ],
    resolve: {
        alias: {
            '@': fileURLToPath(new URL('./src', import.meta.url))
        }
    },
    server: {
        // 启动服务器后是否自动打开浏览器
        open: false,
        proxy: {
            '/api': {
                target: 'http://localhost:9090',
                changeOrigin: true,
            }
        }
    }
})
