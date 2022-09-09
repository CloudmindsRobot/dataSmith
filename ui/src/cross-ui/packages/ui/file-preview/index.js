import FilePreview from './main'

FilePreview.install = function (Vue) {
    Vue.component(FilePreview.name, FilePreview)
    return Vue
}

export default FilePreview