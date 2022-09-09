'use strict'
const webpack = require('webpack')
const path = require('path')

function resolve(dir) {
    return path.join(__dirname, dir)
}

// 移除把scss当做入口而产生的js文件
class RemoveThemeJsPlugin {
    apply(compiler) {
        compiler.hooks.emit.tap('RemoveThemeJsPlugin', compilation => {
            Object.keys(compilation.assets)
                .filter(file => /(dark|light).*\.js$/.test(file))
                .forEach(file => {
                    delete compilation.assets[file]
                })
        })
    }
}

const HtmlWebpackSkipAssetsPlugin = require('html-webpack-skip-assets-plugin').HtmlWebpackSkipAssetsPlugin;

module.exports = {
    publicPath: '/dsms/',
    outputDir: 'dist/dsms',
    assetsDir: 'static',
    lintOnSave: process.env.NODE_ENV === 'development', // 关闭eslint检查
    productionSourceMap: false,
    css: {
        // 引入MiniCssExtractPlugin
        // !!!会重复注入css，用HtmlWebpackSkipAssetsPlugin插件剔除
        extract: {
            filename: "static/css/[name].css",
            chunkFilename: "static/css/[name].chunk.css"
        },
        // loader传递选项
        loaderOptions: {
            less: {
                lessOptions: {
                    javascriptEnabled: true // 使用~ant-design-vue/dist/antd.less 必须设置
                }
            }
        }
    },
    chainWebpack: config => {
        config.name('DataSmith')  // page title
            .devtool('source-map')
        // 开发服务配置
        config.devServer
            .port(process.env.port || process.env.npm_config_port || 9527) // 服务端口
            .open(true) // 自动打开浏览器
            .https(true) // https
            .hot(true)
            .overlay({
                warnings: false,
                errors: true //出现编译器错误或警告时，在浏览器中显示全屏覆盖
            })
        // 修改 entry 配置,增加theme样式文件打包入口
        config
            .entry('light')
            .add(resolve('./src/assets/css/light.less'))
            .end()
            .entry('dark')
            .add(resolve('./src/assets/css/dark.less'))
            .end()
            .output
            .libraryExport('default');

        config.module
            .rule('i18n')
            .resourceQuery(/blockType=i18n/)
            .type('javascript/auto')
            .use('i18n')
            .loader('@kazupon/vue-i18n-loader')
            .end()

        config.plugin('RemoveThemeJsPlugin')
            .use(RemoveThemeJsPlugin)// 删除掉webpack打包后的theme对应的jsonp/js模块
            .before('html')
            .end()
            .plugin('HtmlWebpackSkipAssetsPlugin')
            .use(HtmlWebpackSkipAssetsPlugin, [{excludeAssets: [/((dark.*\.(js|css))|(light.*\.js))$/i]}])

        // 配置模块定义 definitions
        config.plugin('record').use(webpack.ProvidePlugin, [{
            $: 'jquery',
            jQuery: 'jquery',
            'windows.jQuery': 'jquery'
        }])
        config.optimization.splitChunks({}) // 默认的splitChunks会将node_modules下的所有内容合并打包，因此这里去掉splitChunks的默认优化
        config.plugins.delete('preload')
        config.plugins.delete('prefetch')
    }
}
