import Antd from 'ant-design-vue'
import formCreate from '@form-create/ant-design-vue'
import FcEditor from '@form-create/component-wangeditor'
import elColorPicker from 'el-color-picker'
import VueLocalStorage from 'vue-localstorage'
import VueI18n from 'vue-i18n'
import Search from './ui/search/index.js'
import PageTable from './ui/page-table/index.js'
import Layout from './ui/layout/index.js'
import Header from './ui/header/index.js'
import Menu from './ui/menu/index.js'
import Toolbar from './ui/toolbar/index.js'
import Avatar from './ui/avatar/index.js'
import ChangePassword from './ui/change-password/index.js'
import Main from './ui/main/index.js'
import Login from './ui/login/index.js'
import Card from './ui/common-card/index.js'
import Table from './ui/common-table/index.js'
import Modal from './ui/modal/index.js'
import StepsModal from './ui/steps-modal/index.js'
import Popover from './ui/popover/index.js'
import Transfer from './ui/transfer/index.js'
import OperateDialog from './ui/operate-dialog/index.js'
import List from './ui/common-list/index.js'
import Tabs from './ui/tabs/index.js'
import Form from './ui/form/index.js'
import Tree from './ui/tree/index.js'
import E401 from './ui/401/index.js'
import E403 from './ui/403/index.js'
import E404 from './ui/404/index.js'
import Statistics from './ui/statistics/index.js'
import Charts from './ui/charts/index.js'
import Container from './ui/container/index.js'
import Timeline from './ui/timeline/index.js'
import ContainerDetails from './ui/container-details/index.js'
import UserCenter from './ui/user-center/index.js'
import Panel from './ui/panel/index.js'
import Descriptions from './ui/descriptions/index.js'
import FilePreview from './ui/file-preview/index.js'
import LayoutCols from './ui/layout-cols/index.js'
import ExportBtn from './ui/export-btn/index'
import ImportBtn from './ui/import-btn/index'
import EditableCell from './ui/editable-cell/index'
import CommonIcon from './ui/common-icon/index'
import AdPreview from './ui/ad-preview/index'
import ResultPanel from './ui/result-panel/index'
import CopyCell from './ui/copy-cell/index.js'
import FormLabel from './ui/form-label/index.js'
import Codemirror from './ui/codemirror/index.js'
import Drawer from './ui/drawer/index'

// import locale from './locale/index.js'
import tools from './tools/index.js'
import { httpCatch, confirm } from './tools/httpCatch'
import './css/fonts/iconfont.js'
import '../packages/css/index.css'

const components = [
  Search,
  PageTable,
  Layout,
  Header,
  Menu,
  Toolbar,
  Avatar,
  ChangePassword,
  Main,
  Login,
  Card,
  Table,
  Modal,
  StepsModal,
  Popover,
  Transfer,
  OperateDialog,
  List,
  Tabs,
  Form,
  Tree,
  E401,
  E403,
  E404,
  Statistics,
  Charts,
  Container,
  Timeline,
  ContainerDetails,
  UserCenter,
  Panel,
  Descriptions,
  FilePreview,
  LayoutCols,
  ExportBtn,
  ImportBtn,
  EditableCell,
  CommonIcon,
  AdPreview,
  ResultPanel,
  CopyCell,
  Drawer,
  FormLabel,
  Codemirror
];

const install = function(Vue, opts = {}) {
  // locale.use(opts.locale)
  // locale.i18n(opts.i18n)

  components.forEach(component => {
    Vue.component(component.name, component)
  });
  Vue.component('editor', FcEditor)
  Vue.component('ColorPicker', elColorPicker)

  Vue.use(Antd)
  Vue.use(formCreate)
  Vue.use(VueI18n)
  Vue.use(VueLocalStorage)

  Vue.prototype._http = httpCatch
  Vue.prototype._confirm = confirm
};

/* istanbul ignore if */
if (typeof window !== 'undefined' && window.Vue) {
  install(window.Vue)
}

// export const CrossLocale = {
//   locale: locale.use,
//   i18n: locale.i18n
// }

export const CrossUI = {
  install,
  Search,
  PageTable,
  Layout,
  Header,
  Menu,
  Toolbar,
  Avatar,
  ChangePassword,
  Main,
  Login,
  Card,
  Table,
  Modal,
  StepsModal,
  Popover,
  Transfer,
  OperateDialog,
  List,
  Tabs,
  Form,
  Tree,
  E401,
  E403,
  E404,
  Statistics,
  Charts,
  Container,
  Timeline,
  ContainerDetails,
  UserCenter,
  Panel,
  Descriptions,
  FilePreview,
  LayoutCols,
  FcEditor,
  ExportBtn,
  ImportBtn,
  EditableCell,
  CommonIcon,
  AdPreview,
  ResultPanel,
  CopyCell,
  elColorPicker,
  Drawer,
  FormLabel,
  Codemirror
}

export const CrossTool = {
  ...tools
}

