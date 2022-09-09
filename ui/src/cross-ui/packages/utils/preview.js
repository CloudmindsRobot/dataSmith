import $ from 'jquery'
import '@fancyapps/fancybox'

export default {
    fancyBox (group = '') {
        $(`[data-fancybox${ group ? ('="'+ group + '"') : '' }]`).fancybox({
            buttons: ['zoom', 'slideShow', 'thumbs', 'close']
        })
    }
}