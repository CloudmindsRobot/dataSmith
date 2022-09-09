import Config from '../tools/config'
import Seting from '../tools/seting'

export default{
  props: {
  /**
   * 用户API
   *
   * {load:加载租户信息,update:更新租户信息,remove:移除商标,changePassword:修改密码}
   */
  api: {
      type: Object,
      default: () => {
        return {
          load: `${Config.baseUrlPrefix[Config.platform]}/upms/v1/tenant/list`,
          update: `${Config.baseUrlPrefix[Config.platform]}/upms/v1/tenant/update`,
          remove: `${Config.baseUrlPrefix[Config.platform]}/ceph/v1/delete`,
          changePassword: `${Config.baseUrlPrefix[Config.platform]}/upms/v1/staff/update_my_pwd`
        }
      }
    },
    /**
     * 用户
     *
     */
    user: {
      type: Object,
      default: () => {
        return Seting.get(Config.user) || {}
      }
    }
  },
  computed: {
    version () {
      return Config.version[Config.platform]
    }
  },
  data () {
    return {}
  }
}
