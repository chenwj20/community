# community
FreeMi社区
## 在线体验
[https://freemi.cn/](https://freemi.cn/ "https://freemi.cn/")

## 技术栈
1. SpringBoot框架。
2. Thymeleaf模板引擎。
3. 数据访问层：Mybatis，通用mapper。
4. 数据库：MySql。
5. 服务器：内置Tomcat。
6. 前端相关:Jquery,Layui,Bootstrap，Ajax等。
7. 文件上传：阿里云OOS对象存储。
8. 富文本编辑器：Editormd。
9. OAuth2授权登入（QQ、Github）

## 主要功能
1. 登入，注册(可用qq，gthub) 
2. 发帖 
3. 编辑帖子
4. 删除帖子
5. 收藏帖子
6. 选择分类
7. 选择标签
8. 排序
9. 评论
10. 点赞（帖子或评论）
11. 通知
12. 回收站
13. 用户资料编辑
14. 签到
# 目录结构
   ```
       ├─cn.cwj.community      应用目录
       │  ├─controller         控制器目录
       │  ├─model              映射数据库实体类
       │  ├─dto                数据传输层
       │  ├─intercepter        拦截器
       │  ├─enums              枚举类
       │  ├─provider           提供类
       │  ├─service            业务逻辑层
       │  ├─advice             异常处理
       │  ├─exception          自定义异常
       │  ├─mapper             数据访问层
       │  ├─util               工具类
       │__├─schedule           定时任务
  ```
## 快速运行
1. 安装必备工具  
JDK1.8，Maven
2. 克隆代码到本地 
3. 将本项目sql导入新创建的数据库。
4. 根据提示，编辑application.yml文件。并将各种id改成自己的

5. 运行打包命令
   ```sh 
   mvn clean package
   ```
 
6. 运行项目  
   ```sh
    java -jar target/community-0.0.1-SNAPSHOT.jar
    ```
8. 访问项目
   ```
   https://localhost
   ```
### 资料
[Spring 文档](https://spring.io/guides)    
[Spring Web](https://spring.io/guides/gs/serving-web-content/)   
[es](https://elasticsearch.cn/explore)    
[Github deploy key](https://developer.github.com/v3/guides/managing-deploy-keys/#deploy-keys)    
[Bootstrap](https://v3.bootcss.com/getting-started/)    
[Github OAuth](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)    
[Spring](https://docs.spring.io/spring-boot/docs/2.0.0.RC1/reference/htmlsingle/#boot-features-embedded-database-support)    
[菜鸟教程](https://www.runoob.com/mysql/mysql-insert-query.html)    
[Thymeleaf](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#setting-attribute-values)    
[Spring Dev Tool](https://docs.spring.io/spring-boot/docs/2.0.0.RC1/reference/htmlsingle/#using-boot-devtools)  
[Spring MVC](https://docs.spring.io/spring/docs/5.0.3.RELEASE/spring-framework-reference/web.html#mvc-handlermapping-interceptor)  
[Markdown 插件](http://editor.md.ipandao.com/)   
[UFfile SDK](https://github.com/ucloud/ufile-sdk-java)  
[Count(*) VS Count(1)](https://mp.weixin.qq.com/s/Rwpke4BHu7Fz7KOpE2d3Lw)  

### 工具
[Git](https://git-scm.com/download)   
[Visual Paradigm](https://www.visual-paradigm.com)    
[Flyway](https://flywaydb.org/getstarted/firststeps/maven)  
[Lombok](https://www.projectlombok.org)    
[ctotree](https://www.octotree.io/)   
[Table of content sidebar](https://chrome.google.com/webstore/detail/table-of-contents-sidebar/ohohkfheangmbedkgechjkmbepeikkej)    
[One Tab](https://chrome.google.com/webstore/detail/chphlpgkkbolifaimnlloiipkdnihall)    
[Live Reload](https://chrome.google.com/webstore/detail/livereload/jnihajbhpnppcggbcgedagnkighmdlei/related)  
[Postman](https://chrome.google.com/webstore/detail/coohjcphdfgbiolnekdpbcijmhambjff)

### 特别感谢
[码问](http://www.mawen.co/) 
[LayUI](https://fly.layui.com/) 
