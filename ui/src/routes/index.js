const routes = [{
    path: '/',
    redirect: {name: 'login'}
}, {
    path: '/login',
    name: 'login',
    component: () => import('../views/Login.vue'),
    meta: {
        isLogin: false
    }
}, {
    path: '/master',
    name: 'master',
    component: () => import('../views/Master.vue'),
    meta: {
        isLogin: true
    },
    children: [{
        path: 'log',
        name: 'log',
        component: () => import('../views/Log.vue'),
        meta: {
            isLogin: true
        }
    }, {
        path: 'model',
        name: 'model',
        component: () => import('../views/Model.vue'),
        meta: {
            isLogin: true
        }
    }, {
        path: 'source',
        name: 'source',
        component: () => import('../views/Source.vue'),
        meta: {
            isLogin: true
        }
    }, {
        path: 'target',
        name: 'target',
        component: () => import('../views/Target.vue'),
        meta: {
            isLogin: true
        }
    }]
}]

export default routes
