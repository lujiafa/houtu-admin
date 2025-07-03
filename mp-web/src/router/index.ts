import {createRouter, createWebHashHistory, RouteRecordRaw} from 'vue-router';
import customRoutes from './CustomRoutes'

const routes : Readonly<RouteRecordRaw[]> = [
    {
        path: '/login',
        component: () => import('@/views/Login.vue')
    },
    {
        path: '/mfa',
        component: () => import('@/views/LoginMFA.vue')
    },
    {
        path: '/index',
        component: () => import('@/views/Index.vue'),
        children: [
            {
                path: 'home',
                component: () => import('@/views/Home/index.vue')
            }, {
                path: 'user',
                component: () => import('@/views/UserManage/index.vue')
            }, {
                path: 'role',
                component: () => import('@/views/RoleManage/index.vue')
            }, {
                path: 'menu',
                component: () => import('@/views/MenuManage/index.vue')
            }, {
                path: 'org',
                component: () => import('@/views/OrgManage/index.vue')
            }, {
                path: 'post',
                component: () => import('@/views/PostManage/index.vue')
            }, {
                path: 'dict',
                component: () => import('@/views/DictManage/index.vue')
            }, {
                path: 'params',
                component: () => import('@/views/ParamsManage/index.vue')
            }, {
                path: 'announcement',
                component: () => import('@/views/Announcement/index.vue')
            }, {
                path: 'loginLog',
                component: () => import('@/views/LoginLog/index.vue')
            }, {
                path: 'optLog',
                component: () => import('@/views/OptLog/index.vue')
            }, {
                path: 'updatePassword',
                component: () => import('@/views/UserCenter/UpdateMySelfPassword.vue')
            }, {
                path: 'myOTP',
                component: () => import('@/views/UserCenter/MyOTP.vue')
            }, {
                path: 'ref/:url',
                component: () => import('@/views/IFrame/index.vue')
            },
            ...customRoutes
        ]
    },
];

const router = createRouter({
    // 使用hash模式，也可以选择createWebHistory()使用HTML5 History API
    history: createWebHashHistory(),
    routes,
});
/*
router.beforeEach((to, from, next) => {
});
*/

export default router;
