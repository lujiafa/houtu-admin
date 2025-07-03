import './assets/css/index.scss';

import router from '@/router/index';
import {createPinia} from 'pinia';
import {createApp} from 'vue';
import Cookies from 'js-cookie';
import {Icon} from '@iconify/vue';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import * as ElementPlusIconsVue from '@element-plus/icons-vue';
import i18n from "@/locale/i18n.ts";
import {defaultAxios, session} from '@/utils/request.ts';
import App from './App.vue'

const pinia = createPinia()
const app = createApp(App)
    .use(ElementPlus)
    .use(pinia)
    .use(i18n)
    .use(router);
app.config.globalProperties.$cookies = Cookies;
app.config.globalProperties.$axios = defaultAxios;
app.config.globalProperties.$session = session;
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
app.component('Icon', Icon);
app.mount('#app');
