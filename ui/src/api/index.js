/**
 * 设置api地址前缀
 *
 * @param urls api地址
 * @param prefix api前缀
 * @returns {{}}
 */
const contact = (urls, prefix) => {
    const api = {}
    urls.forEach((url, index) => {
        Object.keys(url).forEach(k => url[k] = `${prefix[index]}${url[k]}`)
        Object.assign(api, url)
    })
    return api
}

/**
 *  api接口
 */
export default contact([{
    dataSourceList: 'dataSource/pageList',
    dataSourceSave: 'dataSource/save',
    dataSourceDelete: 'dataSource/deleteById',
    dataModelList: 'dataModel/pageList',
    dataModelSave: 'dataModel/save',
    dataModelDelete: 'dataModel/deleteById',
    dataModelValidate: 'dataModel/validateConfig',
    dataTableList: 'dataTable/pageList',
    dataTableSave: 'dataTable/save',
    dataTableDelete: 'dataTable/deleteById',
    dataTableExecuteJob: 'dataTable/executeJob',
    dataTableDetails: 'dataTable/getDetailById',
    logList: 'dataTable/pageJobLogList',
    dataTableDeleteFields: 'dataTable/deleteFields',
    dataModelDetail: 'dataModel/getDetailById',
    validateCron: 'dataTable/validateCron',
    getAllTables: 'dataSource/getAllTables',
    dataSourceValidate: 'dataSource/validate',
    dataSourceGetSchemas: 'dataSource/getSchemas',
    dataSourceGetTables: 'dataSource/getTables',
    dataSourceGetTableColumns: 'dataSource/getTableColumns'
}], ['/v1/'])
