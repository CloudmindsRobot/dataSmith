import {Empty} from 'ant-design-vue'
import Config from '../tools/config'

export default {
    data() {
        return {
            emptyImage: Empty.PRESENTED_IMAGE_SIMPLE,
            emptyText: Config.emptyText
        }
    }
}