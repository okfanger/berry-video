const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false,
  devServer: {
    open: false, // 构建完成自动打开浏览器
    host: '0.0.0.0',
    port: 8080,
    https: false, // 开启https代理
    proxy: {
      '/api': {
        // target: 'http://berry-api.akfang.cn/', // 透传环境
        target: "http://192.168.43.3:10010", // 局域网开发环境
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    },
  },

})
