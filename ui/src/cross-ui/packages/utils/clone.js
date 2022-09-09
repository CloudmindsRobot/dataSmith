import cloneDeep from 'lodash/cloneDeep'

export default {
    copy(obj) {
        return JSON.parse(JSON.stringify(obj))
    },
    merge(target) {
        for (let i = 1, j = arguments.length; i < j; i++) {
            const source = arguments[i] || {};
            for (const prop in source) {
                if (source.hasOwnProperty(prop)) {
                    const value = source[prop];
                    if (value !== undefined) {
                        target[prop] = value;
                    }
                }
            }
        }
        return target
    },
    deepCopy(obj) {
        return cloneDeep(obj)
    }
}