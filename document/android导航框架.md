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





NavHostFragment 是全部fragment的宿主。
所有的跳转都是通过NavHostFragment来处理的。

主要需要分析的流程
NavHostFragment的解析是在哪里进行的？
NavHostFragment路由间的跳转是怎么完成的？


先来分析 NavHostFragment的解析是哪开始的？

### NavHostFragment的解析

onFlate函数是NavHostFragment的路由xml的入口
**PS**：对于可以在xml文件中声明的类， 在创建完毕之后 都会回调到其onFlate函数。

![image-20201017045528448](https://i.loli.net/2020/10/17/lOqLBtU4jEucNMH.png)

![image-20201017045559841](https://i.loli.net/2020/10/17/5nHf6Je71iUwZmj.png)



onflate中拿到了自定义的属性  graphid 以及  defaultNavHost


然后 接着看NavHostFragment onCreate

![image-20201019071517126](https://i.loli.net/2020/10/19/vVDmURz4bWThOIS.png)



在onCreate 中初始化了 mNavController

![image-20201019071855067](https://i.loli.net/2020/10/19/ZxiYUDp8F9tKAzj.png)



为啥 fragment的navigation没有和activity类型的navigation 放在一起注册呢？
因为 activity是应用不可或缺的， 而fragment并不是不可或缺的



**NavDestination** ：xml中的路由节点

### NavHostFragment的路由跳转



### NavHostFragment的缺点



1. 全部的路由节点都需要在资源文件当中配置
2. Fragment的路由跳转是使用replace方法， 会导致生命周期的重新调度
3. 没有办法对路由跳转进行拦截

### NavHostFragment的优化

优化点 

-  不在需要在xml里对节点进行配置，用编码时加注解来代替
- 新增fragment navigator , 代替原来fragmentNavgator的由于replace导致的生命周期的调用
- 支持动态配置fragment页， 方便服务器下发配置，实现千人千面





#### 注解处理器 Proceesor

![image-20201023070739497](https://i.loli.net/2020/10/23/pNWFe1o3dmXBLAn.png)



这个注解器应该起到的作用是 把代码中的Destination节点信息都收集起来   然后输出成一个配置文件。 来代替navigation.xml的作用

主题逻辑很直接 ， 直接看源码即可。

生成配置文件

![image-20201023072941107](https://i.loli.net/2020/10/23/oqS1pfvEzBlUPtF.png)

接着就是解析配置文件

![image-20201023073619098](https://i.loli.net/2020/10/23/GyaOU3PnM7Cs62Z.png)

通过输出的文件 ，创建一个NavGraph 来模拟 navigation.xml被加载的过程



![image-20201023074220987](https://i.loli.net/2020/10/23/EHpUYwDAePSiaT3.png)



怎么代替默认的fragmentNavigation的处理呢？

![image-20201023074717964](https://i.loli.net/2020/10/23/ujM2AJotaKIniQF.png)



把自定义的navigator 添加到NavgateProvider里面，用自定义的navigator来创建destination即可。



自定义的FragmentNavigator怎么弄呢？

由于原本的FragmentNavitor fragment的相关属性是私有的，不能通过继承得到。 因此我们可以把全部源码拷贝出来，然后针对 navigate() 中的replace来进行修改。

![image-20201023075109451](https://i.loli.net/2020/10/23/SJ3vbPZVwIWzg25.png)



## ARouters原理分析

- ARouter配置与基本用法
- ARouter编译时原理
- ARouter运行时原理

![img](https://img.mukewang.com/wiki/5f29930209e8f4e815940972.jpg)



Arouter的功能分为了三个阶段：

1. 开发时   用注解来标识
2. 编译时 把开发时标志下来的注解给收集起来 并且记录到文件当中
3. 运行时 Arouter负责界面的跳转



因此 Arouter最重要的两步就是 

1. 编译时原理：是怎么收集注解信息的  
2. 运行时原理：是怎么完成界面跳转的





### 基本用法

![image-20201021072946494](https://i.loli.net/2020/10/21/Pz21oSgaQrByskY.png)





说明一下  IProvider  

IProvider 可以支持跨模块的服务调用， 这样可以避免无关模块之间的强耦合。





### 编译时原理

**三个注解处理器**

![image-20201021073407007](https://i.loli.net/2020/10/21/7InogbDNmKlPCu3.png)



#### BaseProcessor



这三个注解器都是继承至 BaseProcessor


![image-20201027072230865](https://i.loli.net/2020/10/27/MI1jNT5JGipoHkE.png)



#### RouterProcessor

![image-20201027073150130](https://i.loli.net/2020/10/27/ZAquVzay6Rkxiw7.png)



然后逐个解析Route注解

![image-20201027073340826](https://i.loli.net/2020/10/27/gGUvN8KwzJft7T1.png)



这个是啥判断方法呀 。以前没见过

创建.java文件

![image-20201027074213180](https://i.loli.net/2020/10/27/9AFRvrIgGt7EBST.png)

这里简单说明一个javapoet
javapoet的简单生成java文件的方法

![image-20201027074418211](https://i.loli.net/2020/10/27/f7H8AS1pEis3YCv.png)



判断当前被route注解的类的类型

![image-20201027075218740](https://i.loli.net/2020/10/27/kWeMUj5S14YAN7r.png)

对比下比较low的方法

![image-20201027075552067](https://i.loli.net/2020/10/27/ZFm2c3hS7faz1tT.png)