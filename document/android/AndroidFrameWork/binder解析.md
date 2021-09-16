# Bindler解析



参考[文章](https://blog.csdn.net/carson_ho/article/details/73560642)


首先binder 是一种android独有的一种IPC 通讯方式。

在分析binder之前， 先看看 常规的linux的IPC 通讯方式。



## 知识储备

### Linux的进程空间

 一个进程空间分为了  用户空间  和 内核空间。

- 进程间， 用户空间的数据不可共享

- 进程间， 内核空间的数据可以共享

- 进程中，内核空间和用户空间的数据需要通过操作系统的调用来进行交互

  ```java
  copy_from_user:  将用户空间的数据拷贝到内核空间
  copy_to_user:    将内核空间的数据拷贝到用户空间
  
  ```





![ç¤ºæå¾](https://imgconvert.csdnimg.cn/aHR0cHM6Ly91cGxvYWQtaW1hZ2VzLmppYW5zaHUuaW8vdXBsb2FkX2ltYWdlcy85NDQzNjUtMTNkNTkwNThkNGUwY2JhMS5wbmc_aW1hZ2VNb2dyMi9hdXRvLW9yaWVudC9zdHJpcCU3Q2ltYWdlVmlldzIvMi93LzEyNDA)



### 进程隔离& 跨进程通讯

- 进程隔离
  android为了安全性考虑， 一个进程是不允许直接访问另一个进程的。
  也就是说andorid 进程是相互独立的。
- 跨进程通讯
  由于进程隔离了， 但进程间的通讯是必不可少的。
  比如应用进程必须要和systemServer进程通讯 ，才能调起四大组件

### 跨进程通讯的基本原理

![ç¤ºæå¾](https://imgconvert.csdnimg.cn/aHR0cDovL3VwbG9hZC1pbWFnZXMuamlhbnNodS5pby91cGxvYWRfaW1hZ2VzLzk0NDM2NS0xMjkzNTY4NGU4ZWMxMDdjLnBuZz9pbWFnZU1vZ3IyL2F1dG8tb3JpZW50L3N0cmlwJTdDaW1hZ2VWaWV3Mi8yL3cvMTI0MA)

tip:

```
binder 机制  ： 对比起基本的跨进程通讯的优势在于 一次通讯是需要用户空间和内核空间交互一次即可。
是通过linux的 内存映射来实现的。
```



### 内存映射

具体请看文章：[操作系统：图文详解 内存映射](https://www.jianshu.com/p/719fc4758813)



## Binder机制模型

### 模型原理

**Binder**跨进程通讯机制  基于CS  模式。

![ç¤ºæå¾](https://imgconvert.csdnimg.cn/aHR0cDovL3VwbG9hZC1pbWFnZXMuamlhbnNodS5pby91cGxvYWRfaW1hZ2VzLzk0NDM2NS1jMTBkNjAzMmY5MWExMDNmLnBuZz9pbWFnZU1vZ3IyL2F1dG8tb3JpZW50L3N0cmlwJTdDaW1hZ2VWaWV3Mi8yL3cvMTI0MA)

### 模型中角色说明

![ç¤ºæå¾](https://imgconvert.csdnimg.cn/aHR0cDovL3VwbG9hZC1pbWFnZXMuamlhbnNodS5pby91cGxvYWRfaW1hZ2VzLzk0NDM2NS0xMzU1NjBjODdjOTgzZTQzLnBuZz9pbWFnZU1vZ3IyL2F1dG8tb3JpZW50L3N0cmlwJTdDaW1hZ2VWaWV3Mi8yL3cvMTI0MA)













## Binder驱动的作用与原理



### 简介

![ç¤ºæå¾](https://imgconvert.csdnimg.cn/aHR0cDovL3VwbG9hZC1pbWFnZXMuamlhbnNodS5pby91cGxvYWRfaW1hZ2VzLzk0NDM2NS04MmQ2YTA2NThlNTVlOWQ3LnBuZz9pbWFnZU1vZ3IyL2F1dG8tb3JpZW50L3N0cmlwJTdDaW1hZ2VWaWV3Mi8yL3cvMTI0MA)



### 跨进程通讯原理

![ç¤ºæå¾](https://imgconvert.csdnimg.cn/aHR0cDovL3VwbG9hZC1pbWFnZXMuamlhbnNodS5pby91cGxvYWRfaW1hZ2VzLzk0NDM2NS02NWE1YjE3NDI2YWVkNDI0LnBuZz9pbWFnZU1vZ3IyL2F1dG8tb3JpZW50L3N0cmlwJTdDaW1hZ2VWaWV3Mi8yL3cvMTI0MA)



#### 步骤说明

![ç¤ºæå¾](https://imgconvert.csdnimg.cn/aHR0cDovL3VwbG9hZC1pbWFnZXMuamlhbnNodS5pby91cGxvYWRfaW1hZ2VzLzk0NDM2NS1kM2M3OGIxOTNjM2U4YTM4LnBuZz9pbWFnZU1vZ3IyL2F1dG8tb3JpZW50L3N0cmlwJTdDaW1hZ2VWaWV3Mi8yL3cvMTI0MA)

#### 流程分析理解

1. 服务端向ServiceManager 注册binder， binder的信息由ServiceManager维护

2. 客户端通过名称去 请求 binder对象 ，

   - binder驱动 向 ServiceManager  获取
   - binder驱动返回给 客户端

3. 客户端调用binder接口

   - binder驱动 在内核空间新开辟接收缓存

   - binder驱动 把接收缓存 映射到  服务端的内核空间当中
     服务端的内核空间是映射到服务端的用户空间的 
     接收缓存 - 服务端内核空间 - 服务端的用户空间

     这个映射关系是在 进程启动的时候 就执行了的。
     属于是启动binder的一部分

   - binder驱动把客户端的数据 用客户端的用户空间拷贝到 新开辟的接收缓存当中

   - 通过内存映射 ，服务端在自己的用户空间接收到了数据

4. 服务端 返回数据

   - 服务端把自己的返回写入自己的用户空间当中， 由于内存映射 就等于是直接写入了 binder新开辟的接收缓存里
   - binder驱动把接收缓存里的返回值  拷贝到客户端的用户空间中 完成一次完整的binder通讯

### QA

Q:为啥还要有客户端开辟内核空间，映射到服务端的内核空间呢？
都在内核空间， 直接把服务端的内核空间客户端用不就得了？

A：其实ipc中的 共享内存使用的就是这种方式。这种方式最大的问题就是客户端和服务端可能同时操作这一块内存， 这样在多线程场景下，就需要做额外的同步操作。
所以给客户端的独立的内存空间缓存。



Q：为啥binder 只做了一次 数据拷贝？
首先这个一次 指的不是一次通讯 ，而不是 CS 一来一回 两个通讯。

进程在启动的时候 就启动binder的时候，就已经建立好了  本进程和内核之间的内存映射了。
包括 serviceManager也是这样的。
启动binder就是 其实就是

- 打开binder驱动
- 映射内存，设置缓存区
- 注册binder线程
- 进入binder线程的 loop 等待 驱动的消息



所以说 进程启动了binder之后， 就已经有了进程的用户空间和内核空间的内存映射了。

等到Client端 发送binder消息来的时候 binder驱动 
binder驱动重新再内存空间开辟了一块接收缓存。 
把新开辟的内存映射到 服务端内核空间；

对于这次通讯 就有了如下的内存映射关系：
binder驱动新开辟的接收缓存 -》 服务端的内核空间  -》 服务端的用户空间

这个时候Client端 把数据发送到 binder新开辟的接收缓存中 ，这个是一次 用户空间到内核空间的数据拷贝。
由于内存映射， 服务端的用户空间就已经直接感知到了。
就没有服务端的 内核空间到 用户空间的这次拷贝。

服务端回数据的流程类似， 少的是 服务端 用户空间到内核空间这次数据拷贝。

所以说 binder能够少一次 数据拷贝。
少的这个数据拷贝是 服务端的用户空间和内核空间的交互。



### 额外说明

##### 1. Client 进程和 ServerManger ,以及 服务进程 都需要通过binder驱动（使用 open 和 ioctl文件操作函数） 而非直接交互。

ServerManager 虽然是在 SystemServer 进程下， 但是也是要通过binder去访问 内核空间的。是不能直接的访问内核空间的。
只有binder驱动管理的才是 内核空间。

![ç¤ºæå¾](https://imgconvert.csdnimg.cn/aHR0cDovL3VwbG9hZC1pbWFnZXMuamlhbnNodS5pby91cGxvYWRfaW1hZ2VzLzk0NDM2NS1iNDcwMDhhMDkyNjViOWM2LnBuZz9pbWFnZU1vZ3IyL2F1dG8tb3JpZW50L3N0cmlwJTdDaW1hZ2VWaWV3Mi8yL3cvMTI0MA)



##### 2.Binder驱动和ServerManager 是framework 已经实现好的。但是应用进程之间 要binder通讯的话，需要开发者自己实现。

![ç¤ºæå¾](https://imgconvert.csdnimg.cn/aHR0cDovL3VwbG9hZC1pbWFnZXMuamlhbnNodS5pby91cGxvYWRfaW1hZ2VzLzk0NDM2NS0xNjMwYzY5ZTQ4Y2IxZGViLnBuZz9pbWFnZU1vZ3IyL2F1dG8tb3JpZW50L3N0cmlwJTdDaW1hZ2VWaWV3Mi8yL3cvMTI0MA)

##### 3.Binder请求的线程管理

- Server进程会创建很多线程来处理Binder请求。
  如 SystemServer进程 一开始就创建了binder线程池。
- Binder机制使用的线程是由binder驱动来进行管理，也由binder 驱动

所以说aidl接口 不知道会被运行在什么线程。但是肯定不是应用进程的线程。


一个进程的Binder线程的默认最大数量是16。超过的请求会被阻塞等待空闲的binder线程。

```
所以 Content provide中 同时有大量的CRUD操作是有可能有binder线程的约束，而长时间阻塞的。
```





## Binder机制在Android中的具体实现原理



### 注册服务



- 行为描述
  Service进程通过binder驱动 向 ServerManager注册
- 代码实现
  创建一个binder对象
  放在android工程中时，行为就是创建一个aidl接口 ，然后as自动生成的
  Stub类。

![image-20210521173609389](https://i.loli.net/2021/05/21/E13ySKrbFpRBWXn.png)

从一个最基本的AIDL 接口开始分析。


目的：看出 Binder是怎么注册到ServiceManager里面去的

下面是一个基础的AIDL。

![image-20210521180900931](https://i.loli.net/2021/05/21/EMfnZPmLuHUrx2O.png)

andoird中 一般 是service是binder的服务端。
在onBind里面返回了IBinder实例。

![image-20210521181312254](https://i.loli.net/2021/05/21/KDgWYhcUE53kGRy.png)



所以可以推测 serverManager通过固定的IInterface的asBinder来获取binder实例。
从而去完成 binder驱动上的维护操作
通过asInterface来拿到业务上的proxy 从而实现业务接口的操作。


从下面的代码看 的确是通过asInterface去拿到aidl业务接口的代理。

![image-20210521181957951](https://i.loli.net/2021/05/21/xDarhBVP2OzdJHQ.png)

虽然没看到 binder具体是怎么在serverManager里注册的。
但是应用进程的操作已经完成。

接着跟一下frameWork的代码

系统的binder会注册到ServiceManager中,普通的binder不会。

我们从ServiceManager来开始分析， 

![image-20210811184238209](https://i.loli.net/2021/08/11/GPadJyB4Nj3RshW.png)

serviceManager的启动的核心就以上三步：

- 开辟内存空间 建立内存映射
- 绑定binder驱动和ServiceManager
- 启动binder驱动的loop循环

ServiceManager的启动就完成了。

接着往下看，
ServiceManager启动完成之后，系统服务怎么注册到serviceManager中呢？

以显示系统的SurfaceFlinger服务为例子
SurfaceFlinger可以参考 [文章](https://www.cnblogs.com/blogs-of-lxl/p/11272756.html)

/[frameworks](http://androidxref.com/9.0.0_r3/xref/frameworks/)/[native](http://androidxref.com/9.0.0_r3/xref/frameworks/native/)/[services](http://androidxref.com/9.0.0_r3/xref/frameworks/native/services/)/[surfaceflinger](http://androidxref.com/9.0.0_r3/xref/frameworks/native/services/surfaceflinger/)/main_surfaceflinger.cpp

![image-20210812102403685](https://i.loli.net/2021/08/12/CnHbpJVODtu9XxY.png)

surfaceFlinger 注册到ServiceManager中的关键，就在于怎么获取这个serviceManager。
跟一下 defaultServiceManager()

![image-20210812105450981](https://i.loli.net/2021/08/12/R4osFExQUD15NdY.png)

上面可以看出， serviceManager 对应的binder是 保存在进程状态当中。

拿到serviceManager对应的binder之后 就可以进行service注册。
服务端在注册binder的时候 应该就建立好了 服务端的用户空间和内核空间的映射了。

客户端来的时候  只映射 客户端内存空间和  服务端 映射的那块内核空间即可。





### 获取服务

客户端在使用服务端binder之前怎么获取服务的？
也就是下图这个获取服务端binder做了些什么。

![image-20210521181957951](https://i.loli.net/2021/05/21/xDarhBVP2OzdJHQ.png)

实际上 和我们业务上调用的流程是一样的。
binder都是和四大组件的服务绑定在一块的。
你要先获得服务，才能通过bindService去获取binder.

![ç¤ºæå¾](https://imgconvert.csdnimg.cn/aHR0cDovL3VwbG9hZC1pbWFnZXMuamlhbnNodS5pby91cGxvYWRfaW1hZ2VzLzk0NDM2NS05YTJjN2I5ZTU5NDMzMmFlLnBuZz9pbWFnZU1vZ3IyL2F1dG8tb3JpZW50L3N0cmlwJTdDaW1hZ2VWaWV3Mi8yL3cvMTI0MA)

### 使用服务

实际上 通过 bindService拿到的binder是binder的代理， 只能通过其调用业务层的接口，binder的能力是已经被剥离了。

从aidl给业务生成的接口看看。

![image-20210521184002797](https://i.loli.net/2021/05/21/mJV9XLaY3CcHvrg.png)

序列化 之后 把参数通过 transact 发出。

![image-20210521184420817](https://i.loli.net/2021/05/21/Mb4uHfADRTZEtB9.png)

tansact 内部调用的就是onTransact



对于transact函数



![image-20210812121602215](https://i.loli.net/2021/08/12/PvTYS3eEcmB1LCb.png)

![image-20210521185701046](https://i.loli.net/2021/05/21/lSIVvoWz4cGKJCR.png)





这些bindler接口都是通过binder线程池来完成的。

先按下图理解。
客户端的binder接口 通过客户端的binder线程池去发送数据，
然后调用到 binder的代理后， 通过binder驱动 去到服务端的binder线程池中执行函数。
然后返回给 binder驱动。 驱动在返回客户端。



![img](https://imgconvert.csdnimg.cn/aHR0cDovL3VwbG9hZC1pbWFnZXMuamlhbnNodS5pby91cGxvYWRfaW1hZ2VzLzk0NDM2NS02MmZkYWI5MDVlN2QyNzA2LnBuZz9pbWFnZU1vZ3IyL2F1dG8tb3JpZW50L3N0cmlwJTdDaW1hZ2VWaWV3Mi8yL3cvMTI0MA)



客户端发送请求时，的确是在非调用线程

![image-20210521190530761](https://i.loli.net/2021/05/21/M8aGlIfPtwbcWKe.png)





![img](https://imgconvert.csdnimg.cn/aHR0cDovL3VwbG9hZC1pbWFnZXMuamlhbnNodS5pby91cGxvYWRfaW1hZ2VzLzk0NDM2NS0yZjUzMGU5NjRmZmFiOGQ3LnBuZz9pbWFnZU1vZ3IyL2F1dG8tb3JpZW50L3N0cmlwJTdDaW1hZ2VWaWV3Mi8yL3cvMTI0MA)

## Binder优点

![ç¤ºæå¾](https://imgconvert.csdnimg.cn/aHR0cDovL3VwbG9hZC1pbWFnZXMuamlhbnNodS5pby91cGxvYWRfaW1hZ2VzLzk0NDM2NS1jMzIxMTYxYmZlYTdlNzhkLnBuZz9pbWFnZU1vZ3IyL2F1dG8tb3JpZW50L3N0cmlwJTdDaW1hZ2VWaWV3Mi8yL3cvMTI0MA)



## 应用如何启动binder机制



从下面这个问题入手

- 什么时候启动binder机制的？

首先 开发中  在application的oncraete里就已经可以使用binder来通讯了，所以应该是在application之前。
而且 应用进程和ams的交互都是通过binder机制来进行的， 所以肯定是在zygote  frok 出进程的时候 就启动了binder的

是在zygote fork 出应用进程的后 应用进程的进程状态对象中打开的binder驱动。

然后应用进程映射内存， 分配缓冲区（**每个进程的内存映射都是由自己来完成的**）
接着注册应用进程的 binder线程， 
binder线程 进入loop 等待binder驱动的消息。

binder 线程进入loop之后  应用进程才会去和ams 通讯。


要点

- 打开binder驱动
- 映射内存，设置缓冲区
- 注册binder线程
- binder线程进入loop等待binder驱动的消息



## Binder的Oneway机制

- oneway的特性
- oneway的底层实现

 

### 特性



- oneway 不阻塞客户端
- oneway虽然不阻塞客户端，但是服务端是会阻塞的
  并且对于同一个服务端的oneway binder 对象下的接口 都是要排队的，是串行执行的。虽然binder线程是多个线程的。

binder 的接口调用 分两种 oneway 和非oneway的 
 默认是非oneway的
直接区别就是 oneway是非阻塞的， 直接返回。
非oneway的会阻塞。

oneway的 transfer 函数的 resply参数 传的是null， flag 传的是oneway










## 小结：

### binder的作用

android特有的跨进程通讯方式
采用的是CS的通讯模型， 虽然是跨进程的通讯方式，但是不一定要跨进程。

### binder的意义

对比起 linux的其他ipc通讯机制，  binder有如下优点

- 由内核统一管理， 更安全
- 通过内存映射 只需要 一次 用户空间 和内核空间之间的数据拷贝，比 socket和管道 更快
- 客户端和服务端有独立的内核空间 ，操作比共享内存要简单

### binder的原理

binder的通讯中 一共有四个角色。
客户端，服务端， binder驱动 以及ServiceManager

服务端 向 ServiceManager 注册服务，
客户端 向 ServiceManager 获取对应的ibinder的proxy.

binder驱动 负责服务端和客户端之间流转 消息。以及映射内存
binder的通讯都是通过binder驱动 运行在binder线程当中的。

ServiceManager中 管理着注册进来的binder对象。
内存是怎么映射的 就先不管了。

也就是ServiceManager 决定 找谁处理， 怎么处理。
binder驱动决定 在什么线程来处理。







