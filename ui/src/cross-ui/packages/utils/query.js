import {isObject, isString, isArray} from './types'

export const getQueryObj = (value) => {
    const obj = {}

    function setParams(val) {
        const params = val.split('=')
        if (params.length > 1) {
            obj[params[0]] = params[1]
        }
    }

    if (isString(value)) {
        if (value.indexOf('&') !== -1) {
            value.split('&').forEach(item => {
                setParams(item)
            })
        } else {
            setParams(value)
        }
    }
    return obj
}

export const getValueInArray = (list, key, source, children, result) => {
    if (!isArray(list)) {
        return
    }
    for (let i = 0; i < list.length; i++) {
        if (list[i][source] === key) {
            Object.assign(result, list[i])
            break
        }
        if (list[i][children]) {
            getValueInArray(list[i][children], key, source, children, result)
        }
    }
}

export const setObjQuery = (obj) => {
    let url = ''
    isObject(obj) && Object.keys(obj).forEach(k => {
        url += `${k}=${obj[k]}`
    })
    return url
}
