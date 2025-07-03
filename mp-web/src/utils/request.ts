import axios from 'axios'
import {ElMessage, ElMessageBox} from 'element-plus'
import i18n from "@/locale/i18n"
import router from '@/router/index'

const baseConfig = {
    baseURL: import.meta.env.VUE_APP_BASE_API,
    timeout: 15000,
}
axios.defaults.baseURL = baseConfig.baseURL;
axios.defaults.timeout = baseConfig.timeout;

export const defaultAxios = axios;
export const session = axios.create(baseConfig)
session.interceptors.request.use(config => {
    let locale = localStorage.getItem('locale');
    if (locale) {
        config.headers.set('Accept-Language', locale)
    }
    return config;
}, error => {
    ElMessage.error(i18n.global.t('error.networkMessage', {refMessage: error.message}));
});
session.interceptors.response.use(response => {
    let data = response.data;
    // 登录会话失效处理
    if (data.code == 15 || data.code == 16) {
        if (session["state"] === undefined || session["state"]) {
            session["state"] = false;
            ElMessageBox.alert(data.msg || i18n.global.t("error.sessionExpired"), i18n.global.t("common.prompt"), {
                confirmButtonText: i18n.global.t("common.confirm"),
                type: 'warning',
                center: true,
                showClose: false,
                callback: () => {
                    router.push("/login");
                }
            }).then(r => {});
        }
        return Promise.reject(data);
    } else if (data.code !== 0) {
        ElMessage({
            message: data.message,
            type: 'warning',
        })
        return Promise.reject(data);
    } else {
        return data;
    }
}, error => {
    if (error.response) {
        if (error.response.status >= 400 || error.response.status < 200) {
            ElMessage.error(i18n.global.t('error.networkMessage', {refMessage: error.response.status}));
            return Promise.reject(error);
        }
        // 其他暂不做处理
    } else {
        ElMessage.error(i18n.global.t('error.networkMessage', {refMessage: error.message}));
        return Promise.reject(error);
    }
});



