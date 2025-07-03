import {defineStore} from 'pinia'
import i18n from "@/locale/i18n"
import MenuItem from '@/components/interface/MenuItem'
import ModuleItem from "@/components/interface/ModuleItem";

// 主页 状态管理
export const useStore = defineStore('index', {
    state: () => {
        // 初始化首页
        const homeMenuItem : MenuItem = {
            menuId: "home",
            menuName: i18n.global.t('homeMenuName'),
            menuType: 1,
            iconType: 1,
            icon: "HomeFilled",
            pathType: 1,
            path: "/index/home",
            perms: [],
            ancestors: [],
            closable: false
        };
        return {
            homeMenu: homeMenuItem,
            // 是否启用首页
            enableHome: import.meta.env.VITE_ENABLE_HOME === "true" || import.meta.env.VITE_ENABLE_HOME == undefined,
            // 用户信息
            userInfo: {
                admin: false,
                perms: []
            },
            // 侧边栏是否展开
            asideExpand: true,
            // 主页tabs激活所有模块列表
            activatedModules: [],
            // 主页当前激活使用模块
            currentActiveModule: null,
        }
    },
    actions: {
        tryApplyModule(router: any, moduleId: string) {
            let tmpActiveModule = this.activatedModules.find(m => moduleId === m.moduleId);
            if (tmpActiveModule) {
                this.currentActiveModule = tmpActiveModule;
                router.push(tmpActiveModule.path);
                return true;
            }
            return false;
        },
        applyModule(router: any, module: ModuleItem, force = false) {
            if (!force && this.tryApplyModule(router, module.moduleId)) {
                return
            }
            this.activatedModules.push(module);
            this.currentActiveModule = module;
            router.push(module.path);
        },
        removeModule(router, moduleId: string) {
            let activatedModules = this.activatedModules;
            for (let i = 0; i < activatedModules.length; i++) {
                let module = activatedModules[i];
                if (module.moduleId !== moduleId) continue;
                this.activatedModules.splice(i, 1);
                if (module.moduleId === this.currentActiveModule.moduleId) {
                    let lastModule = activatedModules.length > 0 ? activatedModules[(activatedModules.length - 1)] : null;
                    this.currentActiveModule = lastModule;
                    if (lastModule && lastModule.moduleId) {
                        this.tryApplyModule(router, lastModule.moduleId);
                    }
                }
                break;
            }
        },
        clearApplyModule() {
            this.activatedModules = [];
            this.currentActiveModule = null;
        },
        /**
         * 当前活跃模块中是否拥有X权限
         * @param permsCode
         */
        verifyActiveModulePerms(permsCode: string) {
            let currentActiveModule = this.currentActiveModule;
            if (!currentActiveModule || !currentActiveModule.perms) return false;
            return this.currentActiveModule.perms.includes(permsCode);
        }
    }
})
