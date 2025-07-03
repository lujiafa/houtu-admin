import { defineStore } from 'pinia'


// 基础公共信息 状态管理
export const useBaseStore = defineStore('base', {
    state: () => {
        return {
            systemName: '',
            // 是否为移动端视图
            mobileView: false,
            // 全局组件大小
            globalComponentSize: 'default',
            // 是否已登录 true-已登录 false-未登录
            loggedIn: false,
        }
    },
    actions: {
    }
})
