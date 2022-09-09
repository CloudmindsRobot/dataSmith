import Vue from 'vue'
import Router from 'vue-router'
import Config from './config'
import Seting from './seting'
import Routes from '../routes'

const originalPush = Router.prototype.push
Router.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}

Vue.use(Router)

const activeMenu = (menus, name) => {
    for (let i = 0; i < menus.length; i++) {
        if (name === menus[i][Config.menu.props.route]) {
            Config.menu.openKeys.push(menus[i][Config.menu.props.parent])
            Config.menu.selectedKeys.push(menus[i][Config.menu.props.key])
            break
        }
        if (menus[i][Config.menu.props.children]) {
            activeMenu(menus[i][Config.menu.props.children], name)
        }
    }
}

class router extends Router {
    constructor(routes) {
        super({
            base: process.env.BASE_URL,
            routes: Routes.concat(routes)
        })
        super.beforeEach((to, from, next) => {
            document.title = to.meta.subTitle || 'DataSmith'
            if (!to.matched.length) {
                next({path: '/404'})
                return
            }
            if (!Seting.get(Config.tokenKey) && to.meta.isLogin) {
                next({name: 'login'})
                return
            }
            const user = Seting.get(Config.user)
            if (!user && to.meta.isLogin) {
                next({name: '403'})
                return
            }
            next()
        })
        super.afterEach(to => {
            Config.menu.openKeys.length = Config.menu.selectedKeys.length = 0
            activeMenu(Seting.get(Config.menu.name) || [], to.name)
            window.scrollTo(0, 0)
        })
    }
}

export default router
