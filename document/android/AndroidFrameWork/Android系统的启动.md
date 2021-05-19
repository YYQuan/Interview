# Android 系统的启动

 android系统的启动 就分为如下 部分

- init 启动
- zygote 启动
- systemServer启动
- launcher启动



上电之后，bootloader被加载， 然后BootLoader会启动linux系统。
linux系统就会拉起 init 进程。
init进程就是系统当中 第一个被启动的进程。

init进程 进行了一些初始化之后， 就会拉起zygote进程。
zygote进程再拉起system_server进程。
system_server中就会把系统服务启动起来，然后system_server中再拉起AMS。
system_server在把必要服务都启动起来之后， system_server再调用ams的接口来启动 launcher。

这样android系统就启动起来了。




启动流程如下：

![image-20210513214802987](https://i.loli.net/2021/05/13/18TeRwxJrDVXmqt.png)

## Init进程启动



init 进程中做了很多事情。
我们主要关注其启动zygote进程即可。

**init进程通过查找 init.rc脚本 根据脚本配置去启动zygote进程**。

### init.rc

![image-20210518213148343](https://i.loli.net/2021/05/18/p67rSZ9wMtja2ze.png)





### tips: 僵尸进程

```
init  进程中  linux中fork出的子进程停止时，父进程是感知不到的。
但是系统进程表中仍然保留着子进程的信息 
这种在系统进程表中存在，但是实际已经被停止的进程就叫僵尸进程。
由于系统进程表是有限的，如果表中有很多僵尸进程的话，那么就有可能导致系统无法在创建新进程。

```



## Zygote启动流程

前面已经讲到init进程 通过init.rc文件来启动zygote进程。
zygote启动之后 做甚呢？

通过app_main.cpp 调用到 ZygoteInit.java.
在ZygoteInit.java 是第一个java层的文件。
也就是说zygote进程开创了java框架层

接下来看看 ZygoteInit.java的代码。

### ZygoteInit.java

-  注册socket

![image-20210518214940375](https://i.loli.net/2021/05/18/o6qFLAcKSPCe72J.png)

- fork systemServer
- 进入等待 ams 创建新进程的msg的循环中

![image-20210518221225996](https://i.loli.net/2021/05/18/nUEFVYs93xWz4lQ.png)





所以zygoteInit.java 中最主要做的事情

1. fork了systemserver
2. 创建了socket服务端。
   然后监听socket服务端中的来自ams的创建新进程的请求。 



### 小结



- 启动java 虚拟机, 调用了第一个被调用的java类， zygoteInit.java
- 创建了 socket服务端， 监听创建新进程的请求
- fork了systemServer

## SystemServer启动流程





systemServer 进程主要用于创建系统服务，包括 ams ，pms,wms等都是通过systemServer来启动的。



看看 systemServer的入口。



![image-20210518224044338](https://i.loli.net/2021/05/18/fZ3dhjpCVwo4Prs.png)



接着systemServer 进程会执行到 ZygoteInit.handleSystemServerProcess()

handleSystemServerProcess() 中会启动binder.

![image-20210518224421065](https://i.loli.net/2021/05/18/VXCa2nMfyk7FKq5.png)

systemServer进程启动binder之后  就可以进行ipc通讯了。

前面分析看出来了。
zygote进程通过  socket来进行  跨进程通讯。
而fork出来的systemServer进程，是把socket关闭了。
然后创建了binder线程池去进行跨进程通讯的。

创建完了binder之后 就会进入到SystemServer.java的main函数中。


![image-20210518225140495](https://i.loli.net/2021/05/18/brNfRGH1Fcme2Oi.png)

arg中有 systemServer的路径信息。

![image-20210518225318231](https://i.loli.net/2021/05/18/VtNBkiue7PrI3hT.png)





所以就到了SystemServer.java的main函数中了。

### SystemServer.java

- 创建系统服务管理器
- 创建各种系统服务

![image-20210518225856362](https://i.loli.net/2021/05/18/St2eCgrayH68DI3.png)

eg .
创建 ams，并且绑定系统管理器。

![image-20210518225946649](https://i.loli.net/2021/05/18/eQXC4JbE2TWlxL1.png)



### 小结

SystemServer进程主要做了下面几件事：

- 启动binder线程池  用于ipc通讯
- 创建系统服务管理器
- 启动各种系统服务

## Launcher启动流程

Launcher的启动是在SystemServer.startOtherServer中 调用ams函数来启动的。


![image-20210519072902887](https://i.loli.net/2021/05/19/T1MZNVUFQbasdA8.png)





![image-20210519074323081](https://i.loli.net/2021/05/19/YeVtslqNhHgpJwz.png)

![image-20210519074952014](https://i.loli.net/2021/05/19/PUoA7fmcIhRjyGk.png)



![image-20210519075118448](https://i.loli.net/2021/05/19/EVk34G6OtDjpyg8.png)

![image-20210519075313857](https://i.loli.net/2021/05/19/8BqK9MfCoVNETdt.png)

得到intent之后 就startHomeActivity

![image-20210519080203080](https://i.loli.net/2021/05/19/UxzAyjSNwegvHdq.png)



所以也就是说 launcher的启动就是
ams ，activityStackSuperVisor  两者相互作用的结果