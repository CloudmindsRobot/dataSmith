import EditableCell from './main'

EditableCell.install = function (Vue) {
    Vue.component(EditableCell.name, EditableCell)
    return Vue
}

export default EditableCell