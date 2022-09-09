const routes = [
    {
        path: '/401',
        name: '401',
        component: () => import('../ui/401/main.vue'),
        meta: {
            isLogin: false
        }
    },
    {
        path: '/403',
        name: '403',
        component: () => import('../ui/403/main.vue'),
        meta: {
            isLogin: false
        }
    },
    {
        path: '*',
        component: () => import('../ui/404/main.vue'),
        meta: {
            isLogin: false
        }
    }
]

export default routes