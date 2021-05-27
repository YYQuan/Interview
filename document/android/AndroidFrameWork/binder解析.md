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

整个过程 可以理解为 服务端 向 ServerManager注册（systemService进程中）时，
目的就是为了在binder在内核空间开辟服务端的接收缓存，然后映射到服务端的用户空间中的内存当中。
所以服务端的内核空间的接收缓存就确定下来了，并且是由binder驱动在维护。

然后 客户端 请求的时候， 就通过ServerManager 来找到服务端在内核空间的接收缓存。接着再给客户端一个内核空间（**不确定是不是真实的空间**），  客户端的内核空间映射到 服务端的内核空间中。
这样的映射关系下，客户端在往为客户端开辟的内核空间中写入信息的时候，就等于是直接在服务端的用户空间里写入数据了。
这样就完成的跨进程通讯。

##### QA

Q:为啥还要有客户端开辟内核空间，映射到服务端的内核空间呢？
都在内核空间， 直接把服务端的内核空间客户端用不就得了？

A：其实ipc中的 共享内存使用的就是这种方式。这种方式最大的问题就是客户端和服务端可能同时操作这一块内存， 这样在多线程场景下，就需要做额外的同步操作。
所以给客户端的独立的内存空间缓存。





#### 额外说明

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
