export default {
    init(list, labelS, labelT, valueS, valueT, children) {
        return list.map(d => {
            const _data = {}
            _data[labelT] = d[labelS]
            _data[valueT] = d[valueS]
            _data[children] = d[children] ? this.init(d[children], labelS, labelT, valueS, valueT, children) : null
            return _data
        })
    },
    parse(list, labelS, labelT, children, prefix) {
        list.forEach(d => {
            d[labelT] = `${prefix}.${d[labelS]}`
            d[children] && this.parse(d[children], labelS, labelT, children, prefix)
        })
    },
    setSlot(list, prop, slot) {
        list.forEach(node => {
            node[prop] = slot
            node.children && this.setSlot(node.children, prop, slot)
        })
    },
    filter(tree, func) {
        return tree.map(node => ({...node})).filter(node => {
            node.children = node.children && this.filter(node.children, func)
            return func(node) || (node.children && node.children.length)
        })
    },
    filter2(tree, func) {
        return tree.map(node => ({...node})).filter(node => {
            node.children = node.children && this.filter2(node.children, func)
            return func(node)
        })
    },
    traverse(tree, id, path) {
        for (let i = 0; i < tree.length; i++) {
            const item = tree[i]
            path.push(item)
            if (item.id === id) {
                return path
            } else if (item.children && item.children.length) {
                const length = path.length
                this.traverse(item.children, id, path)
                if (length < path.length) {
                    return path
                } else {
                    path.pop()
                }
            } else {
                path.pop()
            }
        }
        return path
    },
    transfer(list, id, pid) {
        const _init = (node, list, id, pid) => {
            const children = list.filter(l => l[pid] === node[id])
            const orders = list.filter(l => l[pid] !== node[id])
            children.forEach(n => _init(n, orders, id, pid))
            node.children = children
        }
        const parents = list.filter(l => !l[pid])
        const children = list.filter(l => !!l[pid])
        parents.forEach(p => _init(p, children, id, pid))
        return parents
    },
    getNode(tree, id, value) {
        let node
        const _filter = (list, id, value) => {
            for (let i = 0; i < list.length; i++) {
                if (list[i][id] === value) {
                    node = list[i]
                    break
                }
                list[i].children && _filter(list[i].children, id, value)
            }
        }
        _filter(tree, id, value)
        return node
    }
}