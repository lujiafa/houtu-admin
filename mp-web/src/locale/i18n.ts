import {createI18n} from 'vue-i18n';
import {defaultLocale} from "./base";
import {en} from "./lang/en";
import {zhCn} from "./lang/zhCn";


const messages = {
    en,
    zhCn,
};

const i18n = createI18n({
    // 默认语言，根据浏览器自动设定
    locale: defaultLocale.i18n,
    // 如果当前选择的语言没有对应的翻译，则回退到该语言
    fallbackLocale: defaultLocale.i18n,
    messages,
});

export default i18n;
