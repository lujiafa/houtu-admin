import {defineStore} from 'pinia'
import {defaultLocale, supportedLocales} from '../locale/base';
// import ru from 'element-plus/es/locale/lang/ru'
// import ja from 'element-plus/es/locale/lang/ja'

// 语言国际化 状态管理
export const useLocaleStore = defineStore('locale', {
    state: () => {
        return {
            supportedLocales,
            elementPlusLocale: defaultLocale.elementPlusLocale,
            locale: defaultLocale.i18n
        }
    },
    actions: {
        initLocale(localeCode: string, i18n: any) {
            if (!localeCode) {
                localeCode = localStorage.getItem("locale");
                if (!localeCode) {
                    localeCode = defaultLocale.code;
                }
            }
            this.applyLocale(localeCode, i18n);
        },

        /**
         * 设置本地语言
         * @param localeCode 语言类型字符串
         * @param i18n i18n国际化对象
         */
        applyLocale(localeCode = defaultLocale.code, i18n: any) {
            let locale = this.supportedLocales.find(item => item.code === localeCode);
            if (!locale) {
                console.debug("locale not found: " + localeCode)
                return;
            }
            if (i18n) {
                i18n.locale = locale.i18n;
            }
            this.elementPlusLocale = locale.elementPlusLocale;
            localStorage.setItem("locale", locale.code);
        },
    }
})
