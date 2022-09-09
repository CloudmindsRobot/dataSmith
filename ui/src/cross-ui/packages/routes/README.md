### routes

> 默认路由，主要提供401，403，404等错误码的路由

```text
───index.js             * 路由配置
```

### index.js

> 默认路由

*代码*

```javascript
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
```

#### 扩展阅读

[Vue Router](https://router.vuejs.org/zh/ "Vue Router")
