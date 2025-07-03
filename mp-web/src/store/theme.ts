import {defineStore} from 'pinia'

// 主题 状态管理
export const useThemeStore = defineStore('theme', {
    state: () => {
        return {
            loginThemeStyle:{},
            indexAsideLogoThemeStyle:{},
            indexHeaderThemeStyle:{},
        }
    },
    actions: {
        initTheme() {
            let val = localStorage.getItem('my-theme');
            let idx = 0;
            if (/^\d+$/.test(val)) {
                let tmpIndex = parseInt(val);
                if (tmpIndex < themes.length) {
                    idx = tmpIndex;
                }
            }
            this.applyTheme(idx);
        },
        applyTheme(idx: number) {
            if (idx >= themes.length) return;
            let theme = themes[idx];
            this.themeName = theme.themeName;
            // 登录页主题样式
            this.loginThemeStyle["--login-container-background-color"] = theme.loginPageBackgroundColor;
            // 功能主页-侧边菜单部分主题样式
            document.documentElement.style.setProperty('--el-menu-bg-color', theme.asideMenuBackgroundColor);
            document.documentElement.style.setProperty('--el-menu-text-color', theme.asideMenuColor);
            document.documentElement.style.setProperty('--el-menu-active-color', theme.asideMenuActiveColor);
            document.documentElement.style.setProperty('--el-menu-hover-bg-color', theme.asideMenuActiveBackgroundColor);
            // 功能主页-侧边Logo部分主题样式
            this.indexAsideLogoThemeStyle["--aside-logo-container-background-color"] = theme.asideLogoBackgroundColor;
            this.indexAsideLogoThemeStyle["--aside-logo-container-color"] = theme.asideLogoColor;
            // 功能主页-头部主题样式
            this.indexHeaderThemeStyle["--header-container-background-color"] = theme.headerBackgroundColor;
            this.indexHeaderThemeStyle["--header-container-color"] = theme.headerFontColor;
            this.indexHeaderThemeStyle["--header-nav-item-hover-background-color"] = theme.headerNavItemHoverBackground;
            localStorage.setItem('my-theme', idx.toString())
        }
    }
})

export const themes = [
    {
        themeName: '默认',
        loginPageBackgroundColor: '#ffffff',
        asideLogoBackgroundColor: '#001529',
        asideLogoColor: '#ffffff',
        asideMenuBackgroundColor: '#001529',
        asideMenuColor: '#ffffff',
        asideMenuActiveColor: '#409EFF',
        asideMenuActiveBackgroundColor: '#223F7B',
        headerBackgroundColor: '#ffffff',
        headerFontColor: '#000000',
        headerNavItemHoverBackground: '#e9e9eb',
    }, {
        themeName: '黑与绿',
        loginPageBackgroundColor: '#00a65a',
        asideLogoBackgroundColor: '#001529',
        asideLogoColor: '#ffffff',
        asideMenuBackgroundColor: '#001529',
        asideMenuColor: '#ffffff',
        asideMenuActiveColor: '#409EFF',
        asideMenuActiveBackgroundColor: '#223F7B',
        headerBackgroundColor: '#00a65a',
        headerFontColor: '#ffffff',
        headerNavItemHoverBackground: '#b3e19d',
    }, {
        themeName: '黑与红',
        loginPageBackgroundColor: '#d73925',
        asideLogoBackgroundColor: '#001529',
        asideLogoColor: '#ffffff',
        asideMenuBackgroundColor: '#001529',
        asideMenuColor: '#ffffff',
        asideMenuActiveColor: '#409EFF',
        asideMenuActiveBackgroundColor: '#223F7B',
        headerBackgroundColor: '#d73925',
        headerFontColor: '#ffffff',
        headerNavItemHoverBackground: '#fcd3d3',
    }, {
        themeName: '绿色',
        loginPageBackgroundColor: '#009688',
        asideLogoBackgroundColor: '#009688',
        asideLogoColor: '#ffffff',
        asideMenuBackgroundColor: '#009688',
        asideMenuColor: '#ffffff',
        asideMenuActiveColor: '#000000',
        asideMenuActiveBackgroundColor: '#00786D',
        headerBackgroundColor: '#80cbc4',
        headerFontColor: '#ffffff',
        headerNavItemHoverBackground: '#00786D',
    }, {
        themeName: '深绿',
        loginPageBackgroundColor: '#009688',
        asideLogoBackgroundColor: '#009688',
        asideLogoColor: '#ffffff',
        asideMenuBackgroundColor: '#009688',
        asideMenuColor: '#ffffff',
        asideMenuActiveColor: '#000000',
        asideMenuActiveBackgroundColor: '#00786D',
        headerBackgroundColor: '#ffffff',
        headerFontColor: '#000000',
        headerNavItemHoverBackground: '#e9e9eb',
    }, {
        themeName: '翠绿',
        loginPageBackgroundColor: '#00a65a',
        asideLogoBackgroundColor: '#00a65a',
        asideLogoColor: '#ffffff',
        asideMenuBackgroundColor: '#00a65a',
        asideMenuColor: '#ffffff',
        asideMenuActiveColor: '#000000',
        asideMenuActiveBackgroundColor: '#10B66A',
        headerBackgroundColor: '#ffffff',
        headerFontColor: '#000000',
        headerNavItemHoverBackground: '#e9e9eb',
    }, {
        themeName: '黄色',
        loginPageBackgroundColor: '#E74C3C',
        asideLogoBackgroundColor: '#E74C3C',
        asideLogoColor: '#ffffff',
        asideMenuBackgroundColor: '#E74C3C',
        asideMenuColor: '#ffffff',
        asideMenuActiveColor: '#000000',
        asideMenuActiveBackgroundColor: '#F75C4C',
        headerBackgroundColor: '#f3a69e',
        headerFontColor: '#ffffff',
        headerNavItemHoverBackground: '#f56c6c',
    }, {
        themeName: '黄与白',
        loginPageBackgroundColor: '#E74C3C',
        asideLogoBackgroundColor: '#E74C3C',
        asideLogoColor: '#ffffff',
        asideMenuBackgroundColor: '#E74C3C',
        asideMenuColor: '#ffffff',
        asideMenuActiveColor: '#000000',
        asideMenuActiveBackgroundColor: '#F75C4C',
        headerBackgroundColor: '#ffffff',
        headerFontColor: '#000000',
        headerNavItemHoverBackground: '#e9e9eb',
    }, {
        themeName: '黑与翠绿',
        loginPageBackgroundColor: '#00a65a',
        asideLogoBackgroundColor: '#33a65a',
        asideLogoColor: '#ffffff',
        asideMenuBackgroundColor: '#001529',
        asideMenuColor: '#ffffff',
        asideMenuActiveColor: '#409EFF',
        asideMenuActiveBackgroundColor: '#223F7B',
        headerBackgroundColor: '#00a65a',
        headerFontColor: '#ffffff',
        headerNavItemHoverBackground: '#b3e19d',
    }
]
