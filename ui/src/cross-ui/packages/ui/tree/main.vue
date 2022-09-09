<template>
  <div class="cross-tree">
    <a-tree v-bind="$attrs"
            v-on="$listeners"
            :draggable="drag"
            :expandedKeys.sync="expanded"
            @drop="onDrop">
      <!-- @slot 自定义节点渲染 -->
      <slot name="title"
            slot="title"
            slot-scope="row">
        <a-row>
          <a-col v-bind="layout.left">
            <a-input v-if="row[keyField] === editingNode[keyField] && !row.loading"
                     style="width: 80%; min-width: 50%;"
                     :ref="row[keyField]"
                     size="small"
                     v-model.trim="editingNode[titleField]">
            </a-input>
            <div class="cross-tree__title" v-else>
              <!-- @slot 自定义图标 -->
              <slot name="icon"
                    :slot-scope="row"/>
              <span v-if="row[titleField].indexOf(searchValue) > -1">
                  {{ row[titleField].substr(0, row[titleField].indexOf(searchValue)) }}<span class="orange-bg">{{ searchValue }}</span>{{ row[titleField].substr(row[titleField].indexOf(searchValue) + searchValue.length) }}
                </span>
              <span v-else>
                  {{ row[titleField] }}
                </span>
            </div>
            <template v-if="row[keyField] === editingNode[keyField] && !row.loading">
              <a-icon class="iconfont"
                      type="check"
                      @click="handleConfirm"/>
              <a-icon class="iconfont"
                      type="close"
                      @click="handleCancel"/>
            </template>
          </a-col>
          <a-col v-bind="layout.right"
                 class="text-right">
            <template v-if="!row.loading && row[keyField] !== editingNode[keyField]">
              <!-- @slot 自定义操作键 -->
              <slot name="operation"
                    :slot-scope="row"></slot>
              <template v-if="row.editable === undefined ? editable : !!row.editable">
                <a-tooltip placement="top"
                           :title="$t('edit')">
                  <a-icon class="iconfont icon-bg"
                          type="edit"
                          @click="() => handleEdit(row)"/>
                </a-tooltip>
                <a-divider type="vertical"/>
              </template>
              <template v-if="row.removable === undefined ? removable : !!row.removable">
                <a-tooltip placement="top"
                           :title="$t('delete') ">
                  <a-icon class="iconfont icon-bg"
                          type="delete"
                          @click="() => handleDelete(row)"/>
                </a-tooltip>
                <a-divider type="vertical"/>
              </template>
              <template v-if="row.addable === undefined ? addable : !!row.addable">
                <a-dropdown placement="bottomCenter"
                            :trigger="['click']">
                  <a-tooltip :title="$t('add')"
                             placement="top">
                    <a-icon class="iconfont icon-bg"
                            type="plus-square"/>
                  </a-tooltip>
                  <a-menu slot="overlay"
                          @click="({ key }) => onMenuClick(key, row)">
                    <a-menu-item key="addSibling">
                      {{ $t('addSibling') }}
                    </a-menu-item>
                    <a-menu-item key="addChild">
                      {{ $t('addChild') }}
                    </a-menu-item>
                  </a-menu>
                </a-dropdown>
                <a-divider v-if="$attrs.draggable"
                           type="vertical"/>
              </template>
              <a-tooltip v-if="$attrs.draggable"
                         placement="topRight"
                         :title="$t('drag')">
                <a-icon class="iconfont icon-bg"
                        type="drag"
                        @mousedown="drag = true"
                        @mouseup="drag = false"/>
              </a-tooltip>
            </template>
          </a-col>
        </a-row>
      </slot>
    </a-tree>
    <a-empty v-if="!treeData[0][childrenField].length"
             :image="emptyImage"/>
  </div>
</template>

<i18n src="../../locale/share/common.json"></i18n>
<i18n>
{
  "zh-CN": {
    "add": "添加节点",
    "addSibling": "添加同级",
    "addChild": "添加下级"
  },
  "zh-HK": {
    "add": "添加節點",
    "addSibling": "添加同級",
    "addChild": "添加下級"
  },
  "en-US": {
    "add": "Add node",
    "addSibling": "Add sibling",
    "addChild": "Add child"
  },
  "ja-JP": {
    "add": "ノードを追加",
    "addSibling": "兄弟を追加する",
    "addChild": "部下を追加する"
  },
  "th-TH": {
    "add": "เพิ่มโหนด",
    "addSibling": "เพิ่มพี่น้อง",
    "addChild": "เพิ่มลูกน้อง"
  }
}
</i18n>

<script>
import empty from '../../mixin/empty'

/**
 * 树状组件
 * 除了组件定制内容外，其他配置项跟ant-design的<a-tree>殊无二致
 *
 * @displayName Tree
 * @version 1.0
 */
export default {
  name: 'cross-tree',
  mixins: [empty],
  props: {
    /**
     * 展开的树节点keys合集
     */
    expandedKeys: {
      type: Array,
      default: () => []
    },
    /**
     * 树节点配置项
     * 全部节点是否可编辑，在每个节点中也可以配置该字段来对每个节点进行控制
     *
     * @values true,false
     */
    editable: {
      type: Boolean,
      default: false
    },
    /**
     * 树节点配置项
     * 全部节点是否可删除，在每个节点中也可以配置该字段来对每个节点进行控制
     *
     * @values true,false
     */
    removable: {
      type: Boolean,
      default: false
    },
    /**
     * 树节点配置项
     * 全部节点是否可添加子节点，在每个节点中也可以配置该字段来对每个节点进行控制
     *
     * @values true,false
     */
    addable: {
      type: Boolean,
      default: false
    },
    /**
     * 树节点渲染参数
     *
     * {left:表示左边内容占据大小,right:表示操作按钮}
     */
    layout: {
      type: Object,
      default: () => {
        return {
          left: {
            xxl: 17,
            xl: 16,
            gl: 15,
            md: 14,
            sm: 13,
            xs: 12
          },
          right: {
            xxl: 7,
            xl: 8,
            gl: 9,
            md: 10,
            sm: 11,
            xs: 12
          }
        }
      }
    },
    /**
     * 过滤字符串
     */
    searchValue: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      editingNode: {},
      item: null,
      editingParentNode: null,
      expanded: [],
      currentTitle: '',
      drag: false
    }
  },
  computed: {
    /**
     * 树形数据
     *
     * @returns {[{root: boolean}]}
     */
    treeData() {
      const data = this.$attrs.treeData || this.$attrs['tree-data']
      return [
        {
          root: true,
          [this.titleField]: 'root',
          [this.keyField]: Symbol(),
          [this.childrenField]: data
        }
      ]
    },
    /**
     * 替换字段
     *
     * @returns {string}
     */
    replaceFields() {
      return this.$attrs.replaceFields || this.$attrs['replace-fields']
    },
    /**
     * 标题字段
     *
     * @returns {string|*}
     */
    titleField() {
      return this.replaceFields && this.replaceFields.title ? this.replaceFields.title : 'title'
    },
    /**
     * 键字段
     *
     * @returns {string|*}
     */
    keyField() {
      return this.replaceFields && this.replaceFields.key ? this.replaceFields.key : 'key'
    },
    /**
     * 子级字段
     *
     * @returns {string|*}
     */
    childrenField() {
      return this.replaceFields && this.replaceFields.children ? this.replaceFields.children : 'children'
    }
  },
  watch: {
    expanded: {
      deep: true,
      immediate: true,
      handler(nv) {
        /**
         * 展开节点触发
         *
         * @property {object} nv 节点
         */
        this.$emit('update:expandedKeys', nv.slice())
      }
    },
    expandedKeys: {
      deep: true,
      immediate: true,
      handler(nv) {
        this.expanded = nv.slice()
      }
    }
  },
  methods: {
    /**
     * 编辑节点
     *
     * @param {object} node 节点对象
     */
    handleEdit(node) {
      // 正在操作其他行时自动取消
      if (this.editingNode && this.editingNode[this.keyField]) {
        this.handleCancel()
      }
      this.editingNode = node.dataRef
      this.currentTitle = node[this.titleField]
      this.$nextTick(() => {
        this.$refs[node[this.keyField]].focus()
      })
    },
    /**
     * 确认事件
     *
     * @param {-}
     */
    handleConfirm() {
      if (this.editingParentNode) {
        /**
         * 在确认节点新增时触发
         *
         * @property {Object} node 节点数据
         * @property {Object} parentNode 父节点数据
         */
        this.$emit('confirm-add', this.editingNode, this.editingParentNode)
      } else {
        /**
         * 在确认节点编辑时触发
         *
         * @property {Object} node 节点数据
         */
        this.$emit('confirm-edit', this.editingNode)
      }
      this.editingNode = {}
      this.editingParentNode = null
    },
    /**
     * 取消事件
     *
     * @param {-}
     */
    handleCancel() {
      if (this.editingParentNode) {
        /**
         * 在取消新增节点时触发
         *
         * @property {Object} node 节点数据
         */
        this.$emit('cancel-add', this.editingNode)
        const index = this.editingParentNode[this.childrenField].indexOf(this.editingNode)
        if (index !== -1) {
          this.editingParentNode[this.childrenField].splice(index, 1)
        }
        this.editingParentNode = null
      } else {
        this.$set(this.editingNode, this.titleField, this.currentTitle)
        /**
         * 在取消编辑节点时触发
         *
         * @property {Object} node 节点数据
         */
        this.$emit('cancel-edit', this.editingNode)
      }
      this.editingNode = {}
    },
    /**
     * 删除节点
     *
     * @param {object} row 当前节点
     */
    handleDelete(row) {
      /**
       * 删除节点触发
       *
       * @property {object} row 节点
       */
      this.$emit('delete', row)
    },
    /**
     * 添加子节点
     *
     * @param {object} row 当前节点
     */
    handleAddChild(row) {
      // 正在操作其他行时自动取消
      if (this.editingNode && Object.keys(this.editingNode).length > 0) {
        this.handleCancel()
      }
      this.editingNode = {
        [this.titleField]: '',
        [this.keyField]: Date.now(),
      }
      if (row.dataRef[this.childrenField]) {
        row.dataRef[this.childrenField].push(this.editingNode)
      } else {
        this.$set(row.dataRef, 'children', [this.editingNode])
      }
      this.editingParentNode = row.dataRef
      this.$nextTick(() => {
        // 展开节点
        if (this.expanded.indexOf(row[this.keyField]) === -1) {
          this.expanded.push(row[this.keyField])
        }
        // 聚焦
        this.$nextTick(() => {
          this.$refs[this.editingNode[this.keyField]] && this.$refs[this.editingNode[this.keyField]].focus()
        })
      })
    },
    /**
     * 获取父节点
     *
     * @param {array} tree 树形数据集
     * @param {string} key 键
     * @returns {*}
     */
    getParentNode(tree, key) {
      for (let i = 0; i < tree.length; i++) {
        const node = tree[i]
        if (node[this.childrenField] && node[this.childrenField].length) {
          if (node[this.childrenField].find(e => e[this.keyField] === key)) return node
          const res = this.getParentNode(node[this.childrenField], key)
          if (res) return res
        }
      }
    },
    /**
     * 点击菜单
     *
     * @param {string} key 键
     * @param {object} row 当前节点
     */
    onMenuClick(key, row) {
      this[key](row)
    },
    /**
     * 添加同级节点
     *
     * @param {object} row 当前节点
     */
    addSibling(row) {
      const parent = this.getParentNode(this.treeData, row[this.keyField])
      if (parent) {
        this.handleAddChild({
          ...parent,
          dataRef: parent
        })
      }
    },
    /**
     * 添加子节点
     *
     * @param {object} row 当前节点
     */
    addChild(row) {
      this.handleAddChild(row)
    },
    /**
     * 拖拽释放
     *
     * @param {object} info 节点数据
     */
    onDrop(info) {
      this.drag = false
      /**
       *  拖拽结束触发
       *
       *  @property {Object} info 节点数据
       */
      this.$emit('drop', info)
    }
  }
}
</script>

<style lang="scss">
.cross-tree {
  .ant-tree {
    li {
      .ant-tree-node-content-wrapper {
        width: 100%;
      }
    }
  }

  .cross-tree__title {
    overflow: hidden;
    text-overflow: ellipsis;
  }
}

.cross-tree {
  .ant-tree-node-selected {
    border-right: 2px solid #1890ff;
  }
}
</style>
