import Seting from '../tools/seting'
import Config from '../tools/config'
import empty from '../mixin/empty'
import {httpCatch} from '../tools/httpCatch'

export default {
    mixins: [empty],
    data() {
        return {
            showBtn: true, //Seting.getSession() === Seting.get(Config.session)
            platform: Config.platform
        }
    },
    methods: {
        /**
         * 清理账户Cookie及其他信息(菜单项)
         *
         * @param {-}
         */
        logout() {
            Seting.get(Config.tokenKey) && httpCatch({
                method: 'POST',
                url: `${Config.baseUrlPrefix[Config.platform]}auth/logout`
            }, true, false)
            Seting.remove()
            window.localStorage.removeItem(Config.menu.name)
            window.location.replace(Config.loginUrl)
        },
        /**
         * 路由跳转
         *
         * @param {string} name 路由名称
         */
        path(name) {
            this.$router && this.$router.push({name: name})
        }
    }
}
