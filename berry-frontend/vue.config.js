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
        // target: 'http://berry-api.akfang.cn/',
        target: "http://localhost:5000/",
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    },
    client: {
      overlay: false,
    }
  },

})
