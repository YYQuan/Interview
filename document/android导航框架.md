# Android导航框架

![img](https://img.mukewang.com/wiki/5f2991f809ea460716341524.jpg)



ps：
Q：为啥要有路由？
A：

三个原因

1. 不利于解耦
   传统的路由：
   显示intent： 存在直接的类的依赖，
   隐式intent：各个组件需要在相互调用的时候 主要想mainfest里面去注册才行。也就是需要统一的管理，不利于多人协作
   也是组件化的基础
2. 原生的路由方式,无法插手启动的任何环节。在startActivity之后无法拦截，不能捕获，降级，只能交给系统，这是如果跳转失败的时候 就有可能发生异常；
3. 原生和H5之间的跳转







常规有两个方案：
Navigation  ：jetbrains的方案
ARouter		： ali的方案

![image-20201016100312669](https://i.loli.net/2020/10/16/ZLN7AvHYknw5chy.png)


从表格来看，
navigation  不支持模块间通讯 、不支持拦截器、不支持降级操作 是最大的缺点。
但是navigation是支持在android生成导航视图。
而且支持回退

## Navigation架构原理

![img](https://img.mukewang.com/wiki/5ef5fd8709f1047a40342042.jpg)




以Fragment为例

NavHostFragment 是全部fragment的宿主。



