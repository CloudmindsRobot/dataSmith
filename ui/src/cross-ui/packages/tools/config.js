// 配置信息类
export default {
  cookieExpires: 1, // cookie过期时间
  pageSize: 12, // 翻页数量
  pageSizeOptions: ['12', '24', '48', '96'], // 一页数量
  millseconds: 86400000, // 毫米换算单位（天）
  ERROR: 'serverError', // 统一的调用服务错误标识（本地化标签）
  baseUrl: process.env.VUE_APP_BASE_API,// styleguide:build,
  baseUrlPrefix: {
    datasmith: '/v1/',
  },
  timeout: 30000, // 服务接口调用超时毫秒,
  language: 'language', //language(语言)key
  theme: 'theme',  //theme(主题)key
  seting: { // cookie
    datasmith: 'datasmith_setting'
  },
  socketUrl: `wss://${window.location.host}/ws`, //'wss://cross-fit86.harix.iamidata.com/ws',
  socketHeader: {
    login: 'root',
    passcode: 'crss123456',
    host: 'vhost_frontend'
  },
  commonError: 'commonError', //统一的处理失败错误提示
  systemId: 1, //系统ID
  emitPrefix: 'datasmith',
  onceSize: 10000, // 一次请求数量
  user: 'user', // cookie的user(登录用户)key
  menu: { // 菜单
    openKeys: [], // 展开菜单的key
    selectedKeys: [], //选中菜单的key
    name: 'datasmith_menu',
    props: {  // 菜单源属性配置
      key: 'id',
      parent: 'parentId',
      icon: 'icon',
      route: 'component',
      children: 'children',
      master: 'master'
    }
  },
  languages: [{label: '中文(简体)', code: 'zh-CN'}],
  helpUrl: '', // 帮助文档路径
  remember:{
    datasmith: 'datasmith_remember'
  }, // cookie的记住密码key
  version: { // 版本号
    datasmith: '1.8.0.1'
  },
  systems: {
    datasmith: ['datasmith']
  }, // 系统编码
  emptyText: '-', // 空值显示字符
  home: '/#/master/home',
  invalid: '/dsms/#/401',
  page403: '/dsms/#/403',
  loginUrl: '/dsms/#/login',
  platform: 'datasmith', // 平台
  pageCode: 'pageCode',
  token: 'Basic Y3JzczpjcnNz', // 默认的服务调用Header token
  tokenKey: 'token',
  session: 'sessionId',
  requestFormData: false,
  systemLogoKey:'systemLogo',
  css: 'light' // 主题
}
