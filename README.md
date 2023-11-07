# Berry-Video 树莓拍

> author：方宇杰(后端)  李建君(前端)

2023年 七牛云 1024 创作节校园编程马拉松参赛作品

## 提交材料位置

后端源码：`仓库根目录`

demo演示视频：

- B站链接：
- 百度云(备用):
  - 链接: https://pan.baidu.com/s/1k2JU6SUWUFbhIWjyDVZrvA?pwd=e1eu
  - 提取码: e1eu

设计文档：`仓库根目录/docs/树莓派设计文档.pdf`

## 如何运行项目

### 1. 准备环境：

```
MySQL 8.0
RabbitMQ 3.8.3
Nacos 2.x
ElasticSearch 7.17.0
ElasticSearch Ik-anaylzer
```

### 2. 数据库配置

1. 启动MySQL
2. 导入 `仓库根目录/docs/init.sql` SQL脚本，初始化表结构。
3. 去 `仓库根目录/berry-services/**/application-dev.yml` 修改数据库配置（都打上了`TODO`，可以在idea里查找）

### 3. 微信公众平台配置

1. 去 https://mp.weixin.qq.com/ 注册账号
1. 无须正式号，在后台的 "设置与开发" -> "开发者工具" => "公众平台测试测试账号"，即可领取对应的密钥

![image-20231107224548478](./README.assets/image-20231107224548478.png)

3. 获取密钥，将其配置在`berry-misc-service` 下的`application-dev.yaml` 配置下

![image-20231107224807031](./README.assets/image-20231107224807031.png)