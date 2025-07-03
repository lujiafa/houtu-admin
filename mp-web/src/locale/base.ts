import en from 'element-plus/es/locale/lang/en'
import zhCn from 'element-plus/es/locale/lang/zh-cn'

// 支持的国际化信息
export const supportedLocales = [
    {
        code:'en',
        label: 'English',
        i18n: 'en',
        elementPlusLocale: en,
    },
    {
        code:'zhCn',
        label: '中文',
        i18n: 'zhCn',
        elementPlusLocale: zhCn,
    }
];

// 默认本地化语言
export const defaultLocale = supportedLocales[0];
