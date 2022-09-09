export default {
  data () {
    return {
    }
  },
  methods: {
    /**
     * 调用接口获取文档流
     *
     * @param {string} api 接口地址
     * @param {object} params 参数
     * @param {string} title 标题
     */
    exportData (api, params, title) {
      if (!api) {
        return
      }
      this.confirmLoading = true
      this._http({
        url: api,
        method: this.apiMethod,
        params: this.apiMethod === 'POST' ? null : params,
        data: this.apiMethod === 'POST' ? params : null,
        responseType: 'blob'
      }).then(response => {
        this.confirmLoading = false
        // Http.options.responseType = 'json'
        if (response.code !== 0){
          return
        }
        this.downloadFile(response, title)
        this.visible = false
      }).catch(error => {
        // Http.options.responseType = 'json'
        this.confirmLoading = false
        this.visible = false
      })
    },
    /**
     * 把文档流转成xls文件
     *
     * @param {object} data 文件数据
     * @param {string} title 标题
     */
    downloadFile(data, title) {
      // console.log('title', title)
      const urlTarget = window.URL || window.webkitURL // 后者是webkit内核  一般给手机端的
      const url = urlTarget.createObjectURL(new Blob([data]))
      const link = document.createElement('a')
      link.style.display = 'none'
      link.href = url
      link.setAttribute(
        'download',
        `${this.$t(title)}.xls`
      )
      document.body.appendChild(link)
      link.click()
      // 移除下载链接
      document.body.removeChild(link)
      this.clearValidateStates()
    }
  }
}