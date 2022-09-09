 // Promise.all 的问题是一旦有请求失败就会抛出错误，然而有时候希望某个请求失败后，其他请求的结果能够正常返回，针对这种情况引入Promise.allSettled，成功的promise将返回一个包含status和value的对象，失败的promise将返回一个包含status和reason的对象
 export const PromiseAllSettled =  function(promises){
    return new Promise(function(resolve,reject){
        if(!Array.isArray(promises)){
            return reject(new TypeError('参数不是数组'))
        }
        let resovedCounter = 0
        let res = new Array(promises.length)
        promises.forEach(function(p,index){
            p.then(function(data){
                resovedCounter++
                res[index] = {status:true,value:data}
                if(resovedCounter === promises.length){
                    return resolve(res)
                }
            },function(reason){
                resovedCounter++
                res[index] = {status:false,value:reason}
                if(resovedCounter === promises.length){
                    return resolve(res)
                }
            })
        })
    })
}
 // 路过路径字符串获取深层对象属性值
 export const getValueByPath=(obj,path,seprate='.') => {
    path = Array.isArray(path)?path:path.split(seprate);
    return path.reduce((pre,cur)=>pre&&pre[cur],obj)
}

export const debounce = {
    inserted: function (el, binding) {
      let timer
      el.addEventListener('click', (...args) => {
            if (timer) {
                clearTimeout(timer)
            }
            timer = setTimeout(() => {
                binding.value(...args)
            }, 1000)
      })
    }
}