#  计划：

1. OKHttp
2. Retrofit
3. Rxjava
4. glide 
5. ButterKnife

# OkHttp



## 一.基础使用方法





### Get

```JAVA
OkHttpClient client = new OkHttpClient();
String run (String url) throws IOException{
	Request request = new Request.Builder()
        .url()
        .build();
    
    //同步
    try(Response response =  client.newCall(request).execute()){
        return response.body().string();
	}
    
    // 异步
    //
    /**
    try{
        client.newCall(request).enqueue(new Callback(){
            @Onverride
            public void onFailure(Call call,IOException e){
                
            }
            
            @Override
            public void onResponse(Call call, Response response)  throws IOException{
                
}
        })
    }
    */
}
```





### POST

```java
public static final MediaType JSON  = MediaType.get("application/json;charset=utf-8");

OkHttpClient client = new  OkHttpClient();
String post(String url,String json) throws IOExcetion{
    RequestBody request = new RequestBody.Builder()
        .url(url)
        .post(body)
        .build();
    
    try(Response response = client.newCall(request).execute()){
        return response.body().string();
}
    
}
```



从这两个用法可以看出来发起网络请求分为了以下的几步：

1. 获取OkHttpClient
2. 构建Request
3. 把Request传入OkHttpClient中构建出 Call
4. call调用execute ()或者enqueue()来完成同步或者异步的调用
   返回Response

总结下 涉及到的对象
OkHttpClient: 就是Call的工厂，
           主要作用就是构建出call（其实Client还管web socket ）
Request:用户的具体请求 
RequestBody:构建Post的Request请求所需的参数
Call: 实际执行网络请求的对象
Response: 网络请求的返回对象

## 二.整体流程图

![img](https://img-blog.csdn.net/20180824142557322?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzI5MTUyMjQx/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

  



整个流程还是很清晰的

1. 用户构建出Reuqest

2. 构建出call ： 具体的call由 用户传入的Request来决定

3. 执行call ：同步异步执行的方式是不同的。

4. 执行call的拦截器处理

   

## 三.流程详解

根据该流程来分析

1. 用户构建出Request
2. 构建出call ： 具体的call由 用户传入的Request来决定
3. 执行call ：同步异步执行的方式是不同的。
4. 执行call的拦截器处理

### 构建出Reuqest

![image-20200814115035793](E:/tools/Typora/res/andoirdCodeAnaly/image-20200814115035793.png)

没有太特别的就是通过Request的构建者Reuqest.Builder 存储了一个HTTP 请求 所需要的变量： 请求地址、请求头、请求体、请求方式（get/post）等等


### 构建出Call

先看看Call的源码
![image-20200814112947900](E:/tools/Typora/res/andoirdCodeAnaly/image-20200814112947900.png)

可以看出来Call是一个接口，接口中最重要的就是execute 和enqueue函数

另外其 request() 的返回值其实就是OkHttpClient.newCall(Request) 中传入的Request对象

Call的具体实现类是RealCall

![image-20200814113118484](E:/tools/Typora/res/andoirdCodeAnaly/image-20200814113118484.png)

按着流程从Call的入口开始看

```java
Call call =  client.newCall(Request);
```

实际上的调用的就是     ![image-20200814114038101](E:/tools/Typora/res/andoirdCodeAnaly/image-20200814114038101.png)![image-20200814114015394](https://i.loli.net/2020/09/24/YNAWEaLseHp5GJB.png)

这样Call的实现类就已经创建出来了

### 执行Call

Call创建出来之后， 接着来看执行Call的部分。
执行Call分为了同步 excecute 和 异步enqueue 两个部分。

#### 同步执行exceute

源码
![image-20200814120259213](E:/tools/Typora/res/andoirdCodeAnaly/image-20200814120259213.png)

 整段代码的核心在红色框内

client就是OkHttpClient

![image-20200814120639942](E:/tools/Typora/res/andoirdCodeAnaly/image-20200814120639942.png)

源码中可以看出，RealCall的execute的核心就是
用client的dispatcher 来分发自己。
然后通过getResponseWithInterceptorChain() 来得到返回的。

![image-20200814121026117](E:/tools/Typora/res/andoirdCodeAnaly/image-20200814121026117.png)

originalRequest就是用户传入的request。

所以说execute的关键就是
client的dispatcher的execute 和  拦截链的proceed 

先来看 client的Dispatcher 是啥，拦截链的process 放在拦截链中去讲。

Dispatcher是OkHttp3的任务调度核心类，负责管理同步和异步的请求，以及每一个请求的状态。并且其内部维护了一个线程池用于执行相应的请求


接下来看看源码， Dispatcher 内部维护了三个队列

![image-20200814122341878](E:/tools/Typora/res/andoirdCodeAnaly/image-20200814122341878.png)

一个正在跑着的同步队列， 一个正在跑着的异步队列，还有一个异步的等待队列（把未能及时执行的任务保存在其中，等到正在跑的异步队列有空闲的时候再执行）

PS： 这队列的实现方式都是 双向队列 ，支持从队列的两端检索和插入元素。

接着回到 dispatcher.execute(RealCall)；
![image-20200814122744480](E:/tools/Typora/res/andoirdCodeAnaly/image-20200814122744480.png)

原来现实就是把call添加到正在运行的同步队列中



dispatcher.execute() 运行完了之后
会调用dispatcher.finish();

![image-20200814141350977](E:/tools/Typora/res/andoirdCodeAnaly/image-20200814141350977.png)



![image-20200814141227750](E:/tools/Typora/res/andoirdCodeAnaly/image-20200814141227750.png)

dispatcher.finish的核心作用就是将 call 从队列中移除





#### 异步执行enqueue

接着来看异步任务

![image-20200814142315986](E:/tools/Typora/res/andoirdCodeAnaly/image-20200814142315986.png)

主流程和同步类似。 但是少了  拦截链的处理和 dispatcher.process
估计都放在回调当中去做了。

但是使用的不是RealCall了，而是AsyncCall

![image-20200814142718257](E:/tools/Typora/res/andoirdCodeAnaly/image-20200814142718257.png)

AsyncCall是RealCall的内部类。

![image-20200814142854444](E:/tools/Typora/res/andoirdCodeAnaly/image-20200814142854444.png)

AsyncCall继承了NamedRunnable，NamedRunnable是实现了Runnable接口的。
先继续往下看。



dispatcher.enqueue(AsyncCall)里面去做了啥

![image-20200814143452853](E:/tools/Typora/res/andoirdCodeAnaly/image-20200814143452853.png)

enqueue里就是把call 放在准备队列，或者异步队列当中然后执行

现在看看 excutorService().execute(call)是啥

excutoeService()就是获取dispatcher 维护的一个线程池

![image-20200814143626447](E:/tools/Typora/res/andoirdCodeAnaly/image-20200814143626447.png)

然后通过这个线程池去执行AsyncCall这个runnable.

那就需要回到AsyncCall的run函数里看做了什么了。

![image-20200814145441222](E:/tools/Typora/res/andoirdCodeAnaly/image-20200814145441222.png)

发现run没做什么特别的就只执行了execute函数，接下来看 AsyncCall的execute

![image-20200814144334582](E:/tools/Typora/res/andoirdCodeAnaly/image-20200814144334582.png)

看到这里逻辑就很清晰了。异步和同步的逻辑基本是一样的。只是异步放在了线程池里去执行了。




### 拦截器处理

现在来看真正得到返回值的地方了。
同步和异步都会调用到的

![image-20200814145734308](E:/tools/Typora/res/andoirdCodeAnaly/image-20200814145734308.png)

接下来看，getResponseWithInterceptorChain的源码

![image-20200814145931769](E:/tools/Typora/res/andoirdCodeAnaly/image-20200814145931769.png)

可以看出 拦截器是由client和request 构建出Chain ,最终的response是Chain.process中获取的。

接下来就该看看
Chain的情况。

先看看Chain的整体接口设计。
见下图：
从下图可以判断出，除了超时处理之外， chain的逻辑处理只有 request()和process()这两个函数。

![image-20210419101848933](https://i.loli.net/2021/04/19/WS9rgO4wMlsbFGQ.png)

接着看看具体实现。
chain接口只有一个实现：RealInterceptorChain
RealInterceptor 中只有proceed是做真正的逻辑处理。
看看RealInterceptorChain的数据处理是怎么实现的。

![image-20210419103526559](https://i.loli.net/2021/04/19/lgyhRo2ATBk1JpE.png)

从上图可以猜测出 通过chain的index 、和interceptor拦截器的的intercepte() 配合就能实现责任链的模型。

所以条用链条如下

![image-20210419104358858](https://i.loli.net/2021/04/19/3Uj6Yv2a5BgJfSM.png)

所以说一定是由一个最终的拦截器 做真正的网络通讯 拿到真正的网络请求的响应后，才能决定要做什么处理。



```
题外话：为啥 拦截器的拦截函数的返回值是 Respon 而不是boolean(是否拦截 不应该是 true/false么？)

这里要明白一个基本的概念。
这里拦截器是用来干嘛的？
是拦截下请求的吗？


并不是的。而是给你一个可以对网络请求进行中途改变的能力。
比如 某种情况下  要把某个字段给改了。
比如 某种情况下 要切换服务器地址等等。
所以 intercept 返回的是response. 而不是是否拦截这次的boolean

所以对于自定义拦截器来说， 一定要有以下 的最基础的操作。

public class LogInterceptor implements Interceptor {

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        //此三行代码是每个自定义拦截器中必须的
        Request request = chain.request();
        Response response = chain.proceed(request);
        return response;
    }
}


```

 

所以又要回到 构建拦截链这（下图），分析拦截链中 默认的必须的实现。

![image-20200814145931769](https://i.loli.net/2021/04/19/g45w3HfaZesLIjQ.png)

#### 拦截链

只看默认一定会被调用到的
重点有两个

1. 各个拦截器的 intercept()函数 和 chain.process()中间做了什么处理。
   因为调用chain.proceed后 流程就流转到下一个拦截器里了。
2. 调用完proceed之后 ，做了啥处理。



一共有如下几个：

- RetryAndFollowUpInterceptor
- BridgeInterceptor
- CacheInterceptor
- ConnectInterceptor
- CallServerInterceptor

一个一个往下看。

##### RetryAndFollowUpInterceptor

下图可以分析出， RetryAndFollowUpInterceptor  的目的就是要调整 chain 中的streamAllocation参数。

![sp20210419_111806_707](https://i.loli.net/2021/04/19/vHxLSlchXYsWg2r.png)

RealCalll在构建拦截链的时候  streamAllocation传的是空。

![image-20210419112458317](https://i.loli.net/2021/04/19/KH5xwq8prPvaJGE.png)

虽然还不知道这个StreamAllocation 有啥用 但是从拦截器的名称上来看， 应该就是设置重试次数之类的配置的。

先放放StreamAllocation ,对于RetryAndFollowUpInterceptor 就清楚了。
RetryAndFollowUpInterceptor拦截的目的就是为了填充 StreamAllocation 函数。

下图可以判断出重定向的触发逻辑是由响应码来判断出来的。

![image-20210419155455667](https://i.loli.net/2021/04/19/WQk452oXe3O81z6.png)





##### BridgeInterceptor

这个拦截的逻辑就比较简单了。
就是对传入的请求 加入 请求头出， 以及 对获取到的response做移除一些请求头参数的处理。

![image-20210419114518837](https://i.loli.net/2021/04/19/bEIoO5SiNxtrGsv.png)



##### CacheInterceptor

从名字分析估计是拿本地缓存。
要达到这个目的 至少要有两步。

1. 去查本地缓存， 把缓存的信息 带在请求当中，这样才服务器才有可能返回304 缓存未过程的状态
2. 服务器返回了304 就直接用缓存， 要是没返回304那么就服务器的返回继续往上流传。

代码就不贴了，除了上述流程之外。 里面大部分都是 逻辑判断，比如不允许用网络的时候，内存不足了放不下缓存的时候， 就不走网络  而是直接构造出异常的Response 回给上层了。



##### ConnectInterceptor

看名字就知道是管连接的。

前面分析  RetryAndFollowUpInterceptor的时候 就发现了  最开始创建的chain中，
StreamAllocation ,httpCode ，conection 传的都是空。
而RetryAndFollowUpInterceptor 是为了传入 StreamAllocation，
由此可以猜测httpcode 和 connection也是要有地方传的。
没错 就是在这个拦截器里传入的。
虽然还不知道这两个参数 具体其什么作用，但是ConnectInterceptor就是这个作用。
这两个参数是负责管理 Http网络连接的， 具体使用着两个参数是在后面的CallServerIntercept当中。
比如用的是http几呀， http请求的格式写入呀  如果http的返回的请求格式呀等等

ConnectionInterceptor.intercept在构建HttpCode和connect就把CallServerInterceptor要用的socket给准备好了。


![image-20210419181407320](https://i.loli.net/2021/04/19/PZGL4yO7N8wJ5Ka.png)

跟踪streamAllocation.newStream()

![image-20210419182627912](https://i.loli.net/2021/04/19/6pPaXIk72vjBtqd.png)

也就是说本次http请求的call对应的socket就已经找到了。

现在的重点是看 connetion中的socket是什么时候发起连接的。

跟踪到RealConnection的Socket

![image-20210419183523110](https://i.loli.net/2021/04/19/QRZf3laqE2FsNHk.png)

![image-20210419183815106](https://i.loli.net/2021/04/19/wTeMlFa5rsjy4uW.png)

基础观察下是在哪里连接socket

从下图可知 如果socket未连接  那就在ConnectInteceptor 中就启动了连接
虽然对socket的创建流程还未理清，大致流程是这样。
对于socket的复用 ，创建等流程 在后面的**OkHttp的连接池**专题中分析

![image-20210419184421241](https://i.loli.net/2021/04/19/167IXazwgfDEhB8.png)

###### OkHttp里面的socket

Q:Http 和socket 有啥关系？ 不是没关系的么
A：的确是没关系

![img](https://img-blog.csdnimg.cn/20190830163816120.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxcXEyNDU0MjUwNzA=,size_16,color_FFFFFF,t_70)

![img](https://img-blog.csdnimg.cn/20190830163625654.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxcXEyNDU0MjUwNzA=,size_16,color_FFFFFF,t_70)

OkHttp通过socket去构建的Http请求。

##### CallServerInterceptor

CallServerInterceptor 是最后调用的拦截器
也是真正的去获取网络数据流的拦截器。

![image-20200814153201782](E:/tools/Typora/res/andoirdCodeAnaly/image-20200814153201782.png)

这里通过http流取到了网络中的返回值。

在分析RetryAndFollowUpInterceptor时，得知了StreamAllocation是负责重定向/重试的。但是不知道具体是怎么生效的。

CallServer中就只有这几个地方用到了。

![image-20210419163902333](https://i.loli.net/2021/04/19/W16UaTSru2hinQy.png)

上图看出主要就是StreamAllocation.noNewStreams()和
streamAllocation.connection().handshake()
跟踪下代码，就看到 handshake 只是 返回了一个对象。

noNewStreams中才有逻辑处理。
来仔细看看StreamAllocation.noStreamStreams()

###### StreamAllocation

noStreamStreams
下图可以看出就是一个关闭socket连接的操作。

![image-20210419170545404](https://i.loli.net/2021/04/19/eYEMqdwViP1mjxR.png)

上图的作用就是关闭socket.
这个socket就是在ConnectInterceptor中和 streamAllocation绑定再一块的

![image-20210419170855715](https://i.loli.net/2021/04/19/ErOkWKbjlh91qTz.png)






##### 小结

tip :拦截器的小结

![image-20200814153552248](E:/tools/Typora/res/andoirdCodeAnaly/image-20200814153552248.png)



TIPS. 拦截器的生效是在  getResponseWithInterceptorChain里的，所以对于异步执行来说。拦截器的代码是执行在子线程当中的。

自己的理解：

RetryAndFollowUpinterceptor ： 负责重定向和重连的  里面其实就做了两件事情，
   1 给Http请求设置StreamAllocation参数，
    2while 循环 在下流的拦截器没有返回正常响应的时候，调整一下 StreamAllocation参数，

BrigeInterceptor : 添加请求头 和移除响应头  可以理解为 补充Request参数

CacheInterptor:    缓存处理 

ConnectInterceptor: 添加上Http 请求的相关参数 也就是添加 构建chain的httpCode 和Connect

CallServiceInterceptor: 利用前面拦截器 构建好的 request ，streamAllocation,HttpCode以及Connect  这四个参数来完成Http请求。
所以说 其实这些内置的拦截器就做了一件事情，根据用户的网络请求配置把请求给完善。 完善的过程包括 有缓存就用缓存这一点。



## 其他细节



### 等待队列的成员是怎么移动到异步队列当中的

asyncCall  finish的时候会检测，会通过promoteCalls函数尽量的把等待队列的成员放到异步队列中去。

### OKHttp的连接池

前面分析已经得到 ，在connectIntercept中 能够得到对应的socket。
http请求是通过socket来完成的。这个socket哪里来的呢？

是怎么实现复用的呢？
好好的Http 为啥用socket来实现呢？
OkHttp 连接池的肯定是和这个socket有关联。

从前面分析，触发socket连接的地方是在ConnectInterceptor的StreamAllocation的newStream函数当中。
现在接着分析，这个socket连接 是怎么实现复用的？



#### StreamAllocation.newStream()

从StreamAllocation.newStream()开始分析。


![image-20210420092544712](https://i.loli.net/2021/04/20/fNxrFPtHKDzQnqC.png)

上图分析，追踪到findHealthyConnection

![image-20210420092551877](https://i.loli.net/2021/04/20/Y6hODi9jMunsmXf.png)

上图分析， connection的创建和复用 重点就应该是在StreamAllocation的findConnection 和 RealConnect.isHealthy()当中。

现在来分析  StreamAllocation.findConnect

##### StreamAllocation.findConnect

![image-20210420093635080](https://i.loli.net/2021/04/20/ERPmpVQBzrxLgie.png)

分为三种方式获取connection：

- 拿已经存在的connection  也就是重定向的请求（之前已经绑定过了）
- 从连接池里拿
- 新创建一个connection

分析一下三种方式的

![image-20210420100316294](https://i.loli.net/2021/04/20/JAWMFjP6YIu8DNn.png)

![image-20210420100647639](https://i.loli.net/2021/04/20/y9vY18TbwodmXAk.png)

![image-20210420101043045](https://i.loli.net/2021/04/20/qX8p5xJuf9PUNcG.png)

从上面对findConnect函数的分析，可以得只  连接池的处理逻辑是在

```java
Internal.instance.get(connectionPool, address, this, null);
```

追踪一下。
发现具体的实现实在OkHttpClient中

![image-20210420102305348](https://i.loli.net/2021/04/20/2sv7cF4hzxD5XmT.png)

所以跟踪点 就来到了StreamAllocation的ConnectionPool.get（）这。

ConnectionPool  类头先浏览一下。
从下图可以知道， OkHttp 维护连接的手段是通过 线程池来实现的。

![image-20210420102952577](https://i.loli.net/2021/04/20/pBU46bLfvAaJ7rT.png)

![image-20210420103305008](https://i.loli.net/2021/04/20/9NjRecYO3GVzUZd.png)

![image-20210420113931697](https://i.loli.net/2021/04/20/1XpbsxlOAE2KLS3.png)



接着再
下图分析可知，其实就是在一个ConnectPool维护着的一个双端队列里面去找

![image-20210420104115167](https://i.loli.net/2021/04/20/aKxkJBEWojTX3Qm.png)

接着看是以什么规则在队列里找的呢？
跟踪下 connection.isEligible（）

![image-20210420104958915](https://i.loli.net/2021/04/20/avOFeNkm6i8lcuy.png)

那从这里来看，是不是只有Http2才支持连接池复用呢？
直观感觉上应该不是。
所以来跟踪下 这个StreamAllocation的size

![image-20210420105856836](https://i.loli.net/2021/04/20/NrE285xZAjLBYow.png)

从这里来看，在非Http2的情况 一个connect 只能有一个streamAllocation.
所以对于Http2的情况下， 连接池中只能返回一个当前已经把streamAllocation释放掉的connect.

那现在的问题就是 在什么情况下 connect会把streamAllocation释放掉。

释放的地方挺多的。
但是仔细查找一下 ，能够发现下面这里
![image-20210420111623783](https://i.loli.net/2021/04/20/FZSgOJ8nlchk6Xz.png)

也就是在RetryAndFollowUpInterceptor 这个自带的最上层的拦截器当中。
![image-20210420112120139](https://i.loli.net/2021/04/20/c8efIJYy7GzrQEk.png)

这样Connection 释放StreamAllocation的逻辑就比较清晰了。
就是在ConnectInterceptor中 在ConnectionPool中找到一个connect,
connect中维护上StreamAllocation的信息。
然后再网络请求完毕后，回到RetryAndFollowupInterceptor中，让connect把streamAllocation释放掉。
这样connect就有可能被其他请求给使用了。

前面有说到 ConnectPool是用一个线程池维护connection，但是pool.get是在一个双向队列里面去取的。
这里面是怎么关联起来的呢？
继续回到ConnectPool当中进行分析。

从Pool的get()使用的双向队列进行分析。
先看启动一种队列元素的移除情况。
就是拦截链中释放StreamAllocation资源时 如果不允许该connection空闲的话，那么就直接移除。

![image-20210420120446802](https://i.loli.net/2021/04/20/ZToG7pj6Kq31QOu.png)

第二种情况

在添加的时候 检测一下 没有要移除的空闲的线程。

![image-20210420120854036](https://i.loli.net/2021/04/20/t7IQcouDL5mkexW.png)

![image-20210420120826628](https://i.loli.net/2021/04/20/5EIrkgufoY1twWe.png)

这时候就需要保证 连接池添加的时机才能够达到优先使用，看看put的时机。

![image-20210420121836616](https://i.loli.net/2021/04/20/1xJdEB34wWbk8Ct.png)

上图分析可以得到 put是在连接池里没有connection的情况下 才会去新增。

现在看来  还没有看到ConnectionPool中的线程池和 这个双向队列的关系。
前面说的由线程池来实现connection的复用这个理解是不对的。

是由一个双向队列来实现当前connection的维护的
这个线程池的唯一作用就是
在ConnectionPool在加入元素之后，启动一个检查

![image-20210420122436743](https://i.loli.net/2021/04/20/8kF4GYnlVcLK7mq.png)

![image-20210420122641820](https://i.loli.net/2021/04/20/SnBV6GUpN5sHxXl.png)

![image-20210420122936451](https://i.loli.net/2021/04/20/rZhg9zOloVT38iy.png)



ConnectPool的清除线程一直在跑。
就能维护住 ConnectPool  的最大空闲线程数量了。可能短时间的 超出 限制， 但是很快就会恢复回去。

现在还有一个问题。
对于被ConnectPool线程池中执行的清除线程触发 移除 双向队列中 维护的Connection的时候 ，对应的socket是怎么关闭的？

根据下图 可以看出来 是之前看漏了。 清除线程那 在移除双向队列的connect后面会有关闭socket的操作。

![image-20210420144226160](https://i.loli.net/2021/04/20/vtdV1ul9H2ZW68G.png)





#### Q:线程池里的线程一直在休眠 会被回收吗

发现一个不太理解的点

ConnectPool 中put一次 就提交了一个死循环的任务，那么到后面不是有一大堆清除线程？

还是说 线程池中 设置的存活时间 配合 死循环当中的休眠操作 能够有 线程回收的作用?

![image-20210420151758943](https://i.loli.net/2021/04/20/spVNz1QjGrmLWdS.png)

所以这里的疑问就是 线程池中设置的存活时间是60S ，我设置它休眠61S，那么在线程休眠的时间里， 他会被销毁 回收吗？
根据KeepAlive的注释

![image-20210420152508701](https://i.loli.net/2021/04/20/Bd3pFx6squo5Gby.png)

这里是我sb了 ，这里是由标志位的。
只会启动一个线程。

![image-20210420160025475](https://i.loli.net/2021/04/20/tYel2kajzmqC37g.png)

但是对于问题，线程池里的线程一直在休眠，会被回收吗？
继续探讨一下。
用demo去模拟了场景 得到结果 和注释是一致的。
在runnable没执行完时 是不是做空闲的。哪怕他一直在休眠





#### 小结

OkHttp的连接是怎么实现的？

1. Okhttp是通过socket通讯 构造出Http请求 ，来完成http请求的
2. 这个socket的存在 可以使得客户端和服务器之间不需要频繁的进行握手
3. OkHttp有一个默认存在的连接池ConnectPool ，里面管理着 连接的socket ，默认情况下 空闲存活时间是5分钟，最大存活数量是5个
4. 在ConnectInterceptor中会根据如下优先级去获取socket连接， 
   - 当前这个call 有没有正在用着的connect    比如 是重定向回来的请求，那么本身就已经有connect了， 就不用再找了，就用这个
   - 如果当前call没有关联着connect ，那么就去 连接池里去找一找，看看合不合要求。
     一般来说就是connection没有关联着其他call 那么就可以用。
     但是对于Http2来说是允许被多路复用的，对于同一个地址 是可以复用的。
   - 如果在现有的连接池当中没有找到可用的连接的话，那么就得要新创建一个了
5. 上面是socket的获取流程，那ConnectPool是怎么维护这些Connect的呢？比如保证最大空闲数量
   - ConnecPool维护了一个线程池，在每次添加connect之前，都会执行一个异步任务，这个异步任务里面是一个死循环，会一直检测 ；如果有要移除的connect，就会移除队列以及关闭其socket, 并且计算出最近的下次检测时间，然后休眠到下次检测时间
   - 每次call的任务的完成 都会主动的唤醒 清除线程，去检查当前的连接的数量

















## 总结

new client -> new request -> new call(client,request)->

->同步-> dispatcher.execute->把realCall加入同步任务队列中
           -> 拦截链执行任务 获取数据
           -> 把realCall从同步任务队列中移除

->异步-> dispatcher.enqueue -> 异步任务队列是否满了
->没满-> 把asyncCall加入异步任务队列中
           -> 把任务加入到线程池当中
           -> 拦截链执行任务 获取数据
           -> 移除asyncCall
           -> 检测等待队列， 尽量把等待队列的元素移动到异步队列
->满了->把asyncCall 加入等待队列
          ->等待promoteCalls把asyncCall移到异步任务队列中





![image-20200814114220570](E:/tools/Typora/res/andoirdCodeAnaly/image-20200814114220570.png)





OkHttp的默认线程池：

![image-20200814155440108](E:/tools/Typora/res/andoirdCodeAnaly/image-20200814155440108.png)

SynchronousQueue 是同步队列，
OkHttp当中默认的队列是同步队列

OkHttp中有两个维护了两个线程池：
一个是Dispatcher用来分发执行异步请求
一个ConnectionPool中用来维护 当前使用的connection的最大空闲数量的









# Retrofit

## 纲领

***核心***： 用动态代理和注解来处理网络请求
***代理模式***：里面用很了很多代理对象， 是为了能够对各个流程进行监控
比如说 异步任务的回调要切回到主线程来执行， OKHTTP本身是不支持的。
所以需要一个代理对象来包装一层，在收到回复的时候切换到主线程去。
又比如说 拦截器， 如果没有代理类进行包装的话， execute 命令出去就直接发送了 
网络请求了。 没法实现拦截，因此 才出现的代理类。
***模板模式***：
retrofit 需要处理很多中字段，  retrofit为了避免出现很多了 解析字段，也就用了专门的类去
完成解析，这样主干代码能更加清爽

比如ParameterHandler 就用了这种设计模式

![image-20201118164836490](https://i.loli.net/2020/11/18/bV5xsfOa8gzP9Jr.png)



## 依赖

```java
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.0.2'
```

## 基础用法

![image-20200814170109965](E:/tools/Typora/res/andoirdCodeAnaly/image-20200814170109965.png)



![image-20200814170155679](E:/tools/Typora/res/andoirdCodeAnaly/image-20200814170155679.png)

![image-20200814170211832](E:/tools/Typora/res/andoirdCodeAnaly/image-20200814170211832.png)



可以看出来 只要吧retrofit的实例创建出来。
然后用retrofix.create(x.class),就能直接通过接口来得到
OkHttp的Call,得到call之后就能执行网络请求了

## 完整流程

一次通讯的流程如下：
![image-20200814171405150](https://i.loli.net/2020/09/24/byEGDZIFPANRMhc.png)

![image-20200817095309847](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817095309847.png)



## 源码解析

### Retrofit对象的构建

Retrofit的对象的构建是通过构建者模式 Build来创建。创建的过程中，需要传入各种的工厂来，帮助构建各种适配器来处理返回或者 处理各种平台的适配和返回值的转换等。

各种工厂和适配器先不看，先看主流程。

调用过程中，retrofit通过外观模式 统一的对 子类进行处理拿到子类对象。



### 接口实例

看看retrofit是怎么拿到接口实例的？

![image-20200814173147377](E:/tools/Typora/res/andoirdCodeAnaly/image-20200814173147377.png)



看看具体的实现

![image-20200817103442863](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817103442863.png)

看下 validateServiceInterface(Class<?> service)
从名字来看，是验证传进来的class ,是不是合法的。

![image-20200817105659007](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817105659007.png)

上面的代码可以分成两个部分
1： 检测参数是否合法
2：loadServiceMethod 获取服务函数

对于1的检测是否合法，没啥好说的 
接下看看 获取服务函数的核心：loadServiceMethod函数

![image-20200817110232819](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817110232819.png)

首先是在缓存cache里找对象，  DCL 双重检查锁来保证线程安全的。
并且 cahce的数据结构是用 ConcurrentHashMap 来实现的。COncurrentHashMap是线程安全数据结构。

![image-20200817111346960](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817111346960.png)

回到 retrofit.create(Class)
validateServiceInterface(Class) 就是把 该结构的函数都添加到 cache当中。

接着往下看。
![image-20200817111700630](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817111700630.png)

看名称应该是动态代理
首先这个Proxy 代理类是 java.lang.reflect 下的。 不是retrofit特有的。
![image-20200817113211826](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817113211826.png)

这个是Proxy.newProxyInstance 中的备注。
也就是说 这个类是java通用的得到接口类的实例的函数。

![image-20200817115543072](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817115543072.png)

这个个Proxy的newProxyInstance的参数的说明

loadser 是 类加载器，
interfaces 是要被代理的接口列表
invocationHandler  被代理的类的函数被执行后的回调

回到retrofit.create
![image-20200817120051432](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817120051432.png)

红框内 是被代理的对象的被代理的函数被执行后的回调。
已开始的例子来说

![image-20200817120400603](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817120400603.png)

该函数的返回其实是动态代理Proxy中的InvokeHandler.invoke()的返回。
所以现在的重点 是看 InvacationHandler中的invoke的实现。
![image-20200817122048034](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817122048034.png)

重点就是这三句。 
platForm.isDefaultMethod(method)的意思就是判断有没有被实现。对于retrofit来说 一般是没有被实现的。
所以一般也就是执行了  loadServiceMethod(method).invoke(args);

跟踪loadServiceMethod(method).invoke(args);
![image-20200817122850069](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817122850069.png)

发现真正的对象是通过红框类的这句实现出来的。
实际上调用的也是他的 invoke 方法。

![image-20200817141141051](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817141141051.png)

1是验证参数的合法性
2才是真正的处理



HttpServiceMethod.parseAnnotations()的源码

```java
  /**
   * Inspects the annotations on an interface method to construct a reusable service method that
   * speaks HTTP. This requires potentially-expensive reflection so it is best to build each service
   * method only once and reuse it.
   */
  static <ResponseT, ReturnT> HttpServiceMethod<ResponseT, ReturnT> parseAnnotations(
      Retrofit retrofit, Method method, RequestFactory requestFactory) {
    boolean isKotlinSuspendFunction = requestFactory.isKotlinSuspendFunction;
    boolean continuationWantsResponse = false;
    boolean continuationBodyNullable = false;

    Annotation[] annotations = method.getAnnotations();
    Type adapterType;
    if (isKotlinSuspendFunction) {
      Type[] parameterTypes = method.getGenericParameterTypes();
      Type responseType =
          Utils.getParameterLowerBound(
              0, (ParameterizedType) parameterTypes[parameterTypes.length - 1]);
      if (getRawType(responseType) == Response.class && responseType instanceof ParameterizedType) {
        // Unwrap the actual body type from Response<T>.
        responseType = Utils.getParameterUpperBound(0, (ParameterizedType) responseType);
        continuationWantsResponse = true;
      } else {
        // TODO figure out if type is nullable or not
        // Metadata metadata = method.getDeclaringClass().getAnnotation(Metadata.class)
        // Find the entry for method
        // Determine if return type is nullable or not
      }

      adapterType = new Utils.ParameterizedTypeImpl(null, Call.class, responseType);
      annotations = SkipCallbackExecutorImpl.ensurePresent(annotations);
    } else {
      adapterType = method.getGenericReturnType();
    }

    CallAdapter<ResponseT, ReturnT> callAdapter =
        createCallAdapter(retrofit, method, adapterType, annotations);
    Type responseType = callAdapter.responseType();
    if (responseType == okhttp3.Response.class) {
      throw methodError(
          method,
          "'"
              + getRawType(responseType).getName()
              + "' is not a valid response body type. Did you mean ResponseBody?");
    }
    if (responseType == Response.class) {
      throw methodError(method, "Response must include generic type (e.g., Response<String>)");
    }
    // TODO support Unit for Kotlin?
    if (requestFactory.httpMethod.equals("HEAD") && !Void.class.equals(responseType)) {
      throw methodError(method, "HEAD method must use Void as response type.");
    }

    Converter<ResponseBody, ResponseT> responseConverter =
        createResponseConverter(retrofit, method, responseType);

    okhttp3.Call.Factory callFactory = retrofit.callFactory;
    if (!isKotlinSuspendFunction) {
      return new CallAdapted<>(requestFactory, callFactory, responseConverter, callAdapter);
    } else if (continuationWantsResponse) {
      //noinspection unchecked Kotlin compiler guarantees ReturnT to be Object.
      return (HttpServiceMethod<ResponseT, ReturnT>)
          new SuspendForResponse<>(
              requestFactory,
              callFactory,
              responseConverter,
              (CallAdapter<ResponseT, Call<ResponseT>>) callAdapter);
    } else {
      //noinspection unchecked Kotlin compiler guarantees ReturnT to be Object.
      return (HttpServiceMethod<ResponseT, ReturnT>)
          new SuspendForBody<>(
              requestFactory,
              callFactory,
              responseConverter,
              (CallAdapter<ResponseT, Call<ResponseT>>) callAdapter,
              continuationBodyNullable);
    }
  }

```



HttpServiceMethod.parseAnnotations的注释

![image-20200817141439488](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817141439488.png)



由此能大致判断，这里就是处理Retrofit的注解的 并且声称对应函数的地方了。

通过HttpServiceMethod.parseAnnotations就把下面这个NetService的serviceApi这个接口给实现了。

![image-20200814170109965](E:/tools/Typora/res/andoirdCodeAnaly/image-20200814170109965.png)

简化下 HttpServiceMethod.parseAnnotations()

![image-20200817142716863](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817142716863.png)

发现主要流程就是3个：

1. 创建CallAdapter
2. 创建Converter
3. 根据calladapter 和 converter,以及 请求工厂和call工厂来创建要返回的 ServiceMethod 对象

现在已经得到了ServiceMethod,
CallAdapted 其实就是HttpServiceMethod的子类，CallAdapater的invoke函数的实现就是HttpServiceMethod
然后先不管CallFactory 和ConverFactory这两个点内部的具体行为，先接着主处理。
动态代理执行的是ServiceMethod的invoke()
接下来看ServiceMethod的invoke里做了什么。

ServiceMethod.invoke的源码
![image-20200817143552315](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817143552315.png)

![image-20200817143636073](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817143636073.png)

对于java的正常流程来说，只会执行到CallAdapted的。对于主流程的源码解析来说，只需要看CallAdapter即可

源码如下

![image-20200817144008201](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817144008201.png)



从这可以看出，这是ServiceMethod.parseAnnotation中传入的
![image-20200817144754281](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817144754281.png)

因此 往 HttpServiceMethod.parseAnnotations()中的createCallAdapter里面去看。

![image-20200817144944712](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817144944712.png)

![image-20200817145006446](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817145006446.png)

![image-20200817145310794](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817145310794.png)

![image-20200817145347697](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817145347697.png)

发现就是构建retrofit时传入的callFactory或者 默认的callFactory来完成的。

callAdapterFatories 在用户的callFactory后会追加默认的callFactory。

![image-20200817145752411](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817145752411.png)

另外 有多个callFactory时，只要有一个工厂能够创建出CallAdapater即可

![image-20200817145708821](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817145708821.png)



接下来看默认的CallFactory中是怎么创建出CallAdapter的。

![image-20200817150526590](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817150526590.png)



找到了默认情况下的 CallAdapter的adapt的处理的地方了。



再回到动态代理中
Proxy中的InvocationHandler()的invoke的实现里执行的  loadServiceMethod(method).invoke(args)
在默认情况下就是执行 

![image-20200817151537024](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817151537024.png)

![image-20200817151654908](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817151654908.png)

adapt(call) 的就是从HttpServiceMethod.invoke中得到的。

PS：这里的Call和OKHttp里的是不一样的。
CallAdapater中 无论是返回 call ,还是   executorCallbackCall 主流程都是类似的，先接着看主流程。
以返回call 来继续。



***也就是Retrofit会得到一个内部包含这OKHTTP.call的ExecutorCallBackbackCall对象。
这个ExecutorCallBackCall的最重要能力就是把异步回到切回到主线程执行***。



回到主流程

retrofit.create返回的是一个动态代理。netApi.serviceApi执行后，返回的其实是HttpServiceMethod.parseAnnotations().invoke()的返回 OkHttpCall<T>了

![image-20200817154600852](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817154600852.png)



到此接口的实例就已经创建出来了。
虽然前面还有个坑还没有讲。conver 工厂 的创建。conver工厂在解析 okHttp3.execute的按返回的时候会用到，放在那讲。







接下来看OkHttpCall的 同步和异步的执行。

### 接口的执行

![image-20200817155705182](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817155705182.png)

接口的执行在这里。
先分同步和异步。

#### 同步

看OkHttpCall的同步

![image-20200817155802194](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817155802194.png)

可以看到 ，这里最终调用的还是OKHttp的call.execute，且OkHttpCall是通过getRawCall来转化成OkHttp.Call的

![image-20200817155959951](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817155959951.png)

![image-20200817160109358](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817160109358.png)

![image-20200817160230229](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817160230229.png)

![image-20200817160547085](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817160547085.png)



所以OkhttpCall里面有 构建retrofit时的callFactory和requestFactory  callFactory 构建成OKhttp3.call,
requestFactory 构建OkHttp3.reqeust
实际上OkHttpCall就是一个 OkHttp3.Call的代理。

CallFactory 构建OkHttp3.call 比较简单，实际上就是调用OKHttp3.Client.newCall

但Request的构建就麻烦一些。

看reqeustFactory .create构建OkHttp3.Request的源码

![image-20200817173450562](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817173450562.png)



![image-20200817172725553](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817172725553.png)

![image-20200817172839534](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817172839534.png)

构建OkHttp.Request的时候 是借助ParameterHandler来添加参数的

![image-20200817175224245](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817175224245.png)

![image-20200817175317672](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817175317672.png)

ParamterHandler是设置Retrofit.RequestBuilder的相关参数的抽象类。其包括 body ，header等参数。
通过ParamterHandler把接口影响的参数都设置进来之后，Retrofit.RequestBuilder.get()才能完成的构建出OkHttp3.Request。



那现在的疑问就是ParameterHandler是怎么构建出来的

![image-20200817175906798](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817175906798.png)

![image-20200817180005038](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817180005038.png)

看起来和注解的参数有关系。

![image-20200817180326958](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817180326958.png)

Method.getParameterAnnotations:是拿参数的注解的。

例如：

![image-20200817180525956](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817180525956.png)

![image-20200817181025600](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817181025600.png)

这一步就是得到注解的参数的对应值。内部应该是通过反射来获取的。这不是重点。先不看了。
这样就能得到用户传入的参数，然后通过Retrofit.RequestBuild 来构建OkHttp3.Request了。





再看OkHttp3.Call.execute的返回

![image-20200817161730695](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817161730695.png)

![image-20200817161806322](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817161806322.png)

这个Response.success是Retrofit的Response不是OkHttp的。

![image-20200817162401400](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817162401400.png)



![image-20200817162244806](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817162244806.png)



通过源码可以看出
responseConverter.convert就是泛型转换的关键。

而这个responseConverter 就是HttpServiceMethod
中创建出来的

![image-20200817162954689](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817162954689.png)



接着往下看：
![image-20200817163018910](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817163018910.png)

![image-20200817163041303](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817163041303.png)

![image-20200817163130811](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817163130811.png)

这些过程和callFactory很类似。

GsonConverterFactory 是比较常用的 ConvertFactory。

看下 GsonConverterFactory 的

![image-20200817163744115](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817163744115.png)

![image-20200817163842672](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817163842672.png)

以上就是同步的流程
一般情况 ， CallFactory和CoverFactory都是别人实现好了的。



#### 异步



![image-20200817161007068](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817161007068.png)

类似同步， 最终也是通过OkHttp3.call来执行enqueue的。

![image-20200817164232405](E:/tools/Typora/res/andoirdCodeAnaly/image-20200817164232405.png)



异步和同步的返回值的泛型转换是一样的。不需要单独分析。

***重点 返回之后 会根据返回值的类型 去选择转换器
比如 如果是Observable的话就会选Rxjava 
如果是Response的话，就用默认的
如果是协程的话， 就是根据请求的最后一个参数是不是 Continuetion
反正就是做转换***。

## 总结

retrofit当中重点就是三点：

1. 怎么把接口类转换成为具体的实现类。
2. 怎么把注解 解析成实际要使用的网络请求，默认是OkHttp的call
3. 怎么把网络的响应转换成为 接口中定义的泛型 一般通GsonFactory来处理

第一点 是通过动态代理的方式来完成
第二点 默认是通过CallFactory来实现的 也就是构建Retrofit的client的时候传入的
第三点 也是在构建Retrofit的时候传入的



具体实现

***retrofit就是用动态代理的方式去运行时解析 接口的注解，从而发起网络请求。
重要的两步就是 calladapter 和 convert
calladadapter只可以控制网络的发起 和 回调的线程的切换
convert可以对返回值进行转换
多个calladater和convert时 retrofit都是通过类型判断来选择需要用哪个adatper
只要有一个符合就可以了。
只有协程的适配有有些差异， 是利用suspend的特性的判断的。
suspend修饰的函数的最后一个参数一定是continuation***。



retrofit 先通过构造者模式 创建出retrofit对象。并且设置好配置，比如CallFactoty, ConvertFactory 等参数。
CallFactory  选择怎么new出call对象 ， 默认是OkHttpCall   // 还有一个重要功能就是 切换回调的线程
ConvertFactory可以转换返回的值
CallFactory 和ConvertFactory 传入多个的时候，只有一个会生效，优先级是先加入的优先。

然后通过retrofig对象的create函数来创建出接口动态代理。
等调用动态代理的函数时，实际上返回的是retrofit的invokeHandle的invoke函数。
一般默认情况下返回的就是OkHttpCall<T>。
这样就得到接口的实例了。
当接口的execute / enqueue 被执行的时候，实际上是被转化成为了OKhttp3.call去执行了。
然后把从OKHttp3中得到的Response 通过ConverFactory的conver函数来解析。解析出来的就是最终retrofit返回来的数据.





PS：对于 CallAdapter的 适配。其实最重要的就是适配把回调函数回到主线的方法。
在默认CallAdapter时，用的是Executor来切换。
而在Rx的Adapter的话就会用别的方式在切换。
但功能都是一样的都只是把OkHttpCall的异步回调切换为主线程中执行。

默认情况下的CallAdapter

![image-20200818115144287](https://raw.githubusercontent.com/YYQuan/MyTypora/master/img/20200818115144.png)

![image-20200818115230125](https://raw.githubusercontent.com/YYQuan/MyTypora/master/img/20200818115230.png)

![image-20200818115327401](https://raw.githubusercontent.com/YYQuan/MyTypora/master/img/20200818115327.png)

这部分就是 把持有主线程handle的Executor来实现主线程的切换。



***Retrofit的 协程的适配***

Retrofit的 协程的适配    要适配协程那么 该函数一定是被suspend关键字修饰的
所以经过编译处理之后 ，最后一个参数一定是Continuation 类型的，Retrofit 就是用过这个来判断的。
然后Retrofit对协程支持的转换过程的闭包代码块中就是直接包含了execute函数
所以协程启动 网络请求就发起了。不需要显式的执行execute。





## 相关面试题



### Retrofig是怎么解析泛型的？

先来看看常规的解析泛型。
首先 要获取用于反射的Method 对象。
然后通过Method去  反射拿泛型

```java
 //通过方法来获取参数的返回值类型 Call<User>
                        ParameterizedType parameterizedType = (ParameterizedType) method.getGenericReturnType();
                        //关键步骤，通过返回类型Call<User>,提取User的Type。Retrofit是在ExecutorCallAdapterFactory中的 Utils.getCallResponseType(returnType)方法获取，原理一样
                        Type returnType = Utils.getParameterUpperBound(0,parameterizedType);
T user = gson.fromJson((String)args[0],returnType);  
```

但是从上图可知 ，拿方法的泛型返回的是一个列表  有多处的泛型 有返回值里的 ，有传入的参数的，是怎么对应的呢？
实际上 method是有分 retureType和paramsType的 也就是 返回值类型和参数类型的。

以下是用接口的实现类的实例去获取 方法体的返回值类型和 参数类型

![image-20210422111153089](https://i.loli.net/2021/04/22/uno4BawqmNsvgFb.png)

发现是有泛型的实际类型的。

接下来看看动态代理的时候有没有
![image-20210422112059877](https://i.loli.net/2021/04/22/2ZizCQXqdxLktbH.png)

所以说反射拿到的返回值信息里是有泛型信息的。

那怎么获取到 里面的泛型信息呢？

从Retrofit.create处开始跟踪 就可以得到其获取返回值泛型的方式
![image-20210422141716617](https://i.loli.net/2021/04/22/jDdoYAQXhe5Lan3.png)

尝试用retrofit里面这中方法来试试。

![image-20210422143732877](https://i.loli.net/2021/04/22/dMPKUmxr6LhVCFk.png)

发现type里面的actualType里就存储着泛型的信息。
那多个泛型的话会怎么样的，试试

![image-20210422144128201](https://i.loli.net/2021/04/22/1RZcJihQMmraIS6.png)

![image-20210422144251386](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20210422144251386.png)

同样的从type.actualType里面可以按顺序获取到。

r如果泛型是通配符怎么处理呢？
参考retorfit里的处理 就是 用 type.upper  和type.lower 返回的数组进行处理即可。

```java
//tips:
upperBounds 是直接拿到上界 不是一级一级的返回。  所以不太清楚 为啥返回值设计成数组。

```

![image-20210422145740230](https://i.loli.net/2021/04/22/AChVQqUt57oF2OD.png)

从上面几步就清除了， java泛型要怎么解析了。

#### 小结

以函数返回值中的泛型为例子。
首先要通过反射获取到Method对象
然后获取其返回值信息的类型type。
接着从type中的actualType里获取到全部泛型信息。
这里泛型信息分成 两种情况

1. 是通配符类型的
2. 不是通配符类型的

是不是通配符类型可以通过是不是WildcardType的子类的判断。

如果不是WildCardType,那只用用该类即可。
如果是WildCardType的话，那么就是通配符类型了。
这个时候直接读类信息意义不大。
在意的应该是通配符的上界和下界。
可以通过wildCard的upbound和lowBound来获取 上下界。



# RxJava

参考：https://segmentfault.com/a/1190000019243389
作用：简化异步步骤。

## 基础使用

```java
//被观察
Observable<Integer> observable = Obserbable.create(new ObservableOnSubscribe<Integer>()){
	@Override 
    public void subscribe(IbservableEmitter<Integer> emitter throws Exception{
        
        emitter.onNext(1);
        emitter.onNext(2);
        emitter.onComplete();
    }
}
        
Obserber<Integer> obser = new Obserber<Integer>(){
    public void onSubscribe(Disposable d){
        
    }
    
    
    public  void onNext(Integer integer){
        
    }
    
    public void onError(Throwable e){
		
    }
    
    public void onComplete(){
        
    }
    
}
                          
observable.subscribe(observer);
```



整体的流程就是上流 observable  发出数据，
然后下游observer  进行观测。
observable 和observer 通过subscribe 函数联系起来



所以说，Observable 和observer 都是 负责定义的。 而subscirbe函数才是真正有执行动作的地方。 
所以我们从subscribe进去看一看.

## 源码解析

### Observable.subscribe

从Observable.subscribe(Observer) 入手来看源码。

![image-20200827110244651](https://raw.githubusercontent.com/YYQuan/MyTypora/master/img/20200827110245.png)

很容易就看到 , Observable的subscribe函数的核心
就是这两句。

RxJavaPlugins.onSubscribe(this, observer)这句对observer进行了一些处理之后，重新把返回值赋给了observer。

接着看RxJavaPlugin.onSubscribe()中做了什么



![image-20200827111714313](https://raw.githubusercontent.com/YYQuan/MyTypora/master/img/20200827111714.png)

看这个RxJavaPlugins.onSubscribe()的注释

看起来这是hook的工具类的入口
但是要RxJavaPlugins.onSubscribe() 要做处理的话，那么就需要onObservableSubscribe 不为null,

![image-20200827112811044](https://raw.githubusercontent.com/YYQuan/MyTypora/master/img/20200827112811.png)

这里可以看出来  RxJavaPlugins.onObservableSubscribe 是通过 setOnObserbableSubscribe来赋值的。
但是我们一般都不会调用这个。所以可以认为RxJavaPlugins.onSubscribe 就是直接返回了Observer。

回到Observable.subscribe()。

![image-20200827110244651](https://raw.githubusercontent.com/YYQuan/MyTypora/master/img/20200827110245.png)

接着看 subscribeActual

![image-20200827114932199](https://i.loli.net/2020/08/27/8eHXWoRdPZLaIOw.png)

从这 可以看出来， Observable.subscribe 实际上就是调用了 其实现类的subscribeActual（Observer ）

ps： Observable.subscribe不仅仅可以传 Observer,还可以传Consumer

![image-20200827115253953](https://i.loli.net/2020/08/27/tFgwuqD7oKmRePj.png)



![image-20200827115437401](https://i.loli.net/2020/08/27/Arq9u6dhlZRyvP4.png)

从这可以看出 Observable.subscribe(consumer )实际上也是执行了 Observable.subscribe(Observer)



### Observable.subscribeActual(Obserber)

上面以及分析出 Observable.subscribeActual(Observer) 取决于具体的实现类，所以要回到创建Obserbable的实例

```java
//被观察
Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>()){
	@Override 
    public void subscribe(IbservableEmitter<Integer> emitter throws Exception{
        
        emitter.onNext(1);
        emitter.onNext(2);
        emitter.onComplete();
    }
}
```

所以要来看Observable.create()

![image-20200827120631948](https://i.loli.net/2020/08/27/P3a9FCnBU61WYk2.png)

--> RxJavaPlugins.onAssembly

![image-20200827121109931](https://i.loli.net/2020/08/27/g3s9T8rXGZ6Pnvm.png)

也是一个hook

![image-20200827121143421](https://i.loli.net/2020/08/27/8tjae6Sl1mPKRAi.png)

但是我们一般都没有额外设置 ，所以就是直接返回了source

所以说Observable的真正实现类就是 ObservableCreate

插一句： Observable.just 的具体实现类是 ObservableJust 其他创建Observable实例的方法可能有其他的对应的实现类。

所以，现在要知道Observable.subscribeActual()具体做了啥 ，对于Observable.create 来说 就是看，ObservableCreate.subscribeActual的实现。

接下来来看ObservableCreate.subscribeActual的具体实现。
先从整体的看下ObservableCreate

![image-20200827122828249](https://i.loli.net/2020/08/27/3g1eSrNU6wsBypD.png)



可以看出 ObservableCreate 唯一做了的事情就是 实现了subscribeActual
CreateEmitter 只是辅助ObservableCreate的



专注于主线

![image-20200827142050171](https://i.loli.net/2020/08/27/jpPcIdlrXxAfDiO.png)

源码中：

先把observer包装成对应的emitter
接着调用了下游的 observer.onSubscribe( emitter) 
这个是用户实现的
然后再调用了上游的 source(也就是上游的实例)的subscribe()
也就是用户自定义实现的部分。

source.subscribe(emitter)
所以对于ObservableCreate 这个实例来说， subscribeActual就做了两件事情，
1.执行用户实现的下游的observer的onSubScribe方法
2.执行用户实现的上游的observable的subScribe(emitter)
然后剩下的事情就交给emitter去处理。
对于Observable的emitter是CreateEmitter



因此需要看看CreateEmitter的实现

![image-20200827144319469](https://i.loli.net/2020/08/27/CiGTP6fpFySAcq7.png)

从结构来看，都是很熟悉的函数， 基本上从函数名称都能够知道函数的作用

看几个常用用的函数的实现

![image-20200827145736533](https://i.loli.net/2020/08/27/MBfpHKLscxjUP9z.png)其实就是调用了 下游Observer的对应函数。
subscribeActual 就解析到这里。

## 线程切换

上下游的基础使用只是RXjava的主题，线程切换才是RxJava的亮点和核心。

先来看看RxJava的线程切换的基础用法

```java
observable.subscribeOn(Scheddulers.newThread())
		  .observeOn(AndroidSchedulers.mainThread())
    	  .subscribe( observer );
```



调用了Observable的subscribeOn（Schedulers.newThread()）之后,上游的Observable.subscribe()代码就会切换到子线程中去执行，而 observeOn(AndroidScheduelers.mainThread()), 又会把下游代码切换到 主线程中执行。



接下来就从上游相关的切换开始分析

### 上游线程切换 subScribeOn

看看传入的这个subscribeOn(Schedulers.newThread())  这个scheduler 调度器是怎么起作用的

先看

![image-20200827153312373](https://i.loli.net/2020/08/27/3qoknEif5sWabGA.png)

![image-20200827153351420](https://i.loli.net/2020/08/27/vV1N94GuwftHaXR.png)

和之前的类似，一般我们并没设置onNewThreadHandler。
所以Schedulers.newThread() 就是返回了Schedulers的静态成员NEW_THREAD

![image-20200827153910726](https://i.loli.net/2020/08/27/c8DpW2liLUegk1Y.png)

![image-20200827154040487](https://i.loli.net/2020/08/27/c8DpW2liLUegk1Y.png)

![image-20200827154111653](https://i.loli.net/2020/08/27/54NFjn8eZ67mxf3.png)

![image-20200827154132243](https://i.loli.net/2020/08/27/FCbLJIPKHruMej9.png)

从上面的流程可以看出，Schedulers的静态变量NEW_THREAD 实际上就是
new NewThreadTask（）.call();

![image-20200827154246794](https://i.loli.net/2020/08/27/7rc9l6xqtAvF53w.png)

![image-20200827154310276](https://i.loli.net/2020/08/27/7rc9l6xqtAvF53w.png)

所以Schdulers.newThread（） 实际上就是创建了一个NewThreadScheduler;

![image-20200827154702670](https://i.loli.net/2020/08/27/a4RjQIGhkpD65Tu.png)

这个NewThreadSchduler起什么作用呢？
暂时还不清楚。

先回到RxJava的线程调度
subscribeOn(Schedulers.newThread())

![image-20200827151834602](https://i.loli.net/2020/08/27/8PKSxkyQUNJ3hlE.png)



RxJavaPlugins.onAssembly 不用管，subscribeOn得到的就是
ObservableSubscribeOn()

![image-20200827160441294](https://i.loli.net/2020/08/27/sgEPAuWLbZH35fx.png)

![image-20200827160454038](https://i.loli.net/2020/08/27/zTZ2eU9QIO3bv8j.png)

scheduler 传入到了ObservableSubscribeOn当中 能在哪里其作用呢？
上面的基础分析当中，已经知道。Observable.subcribe(observer)里会先调用obserbable具体实现类的subscribeActual  所以说ObservableSubscribeOn 的subscribeOnActual 会被执行。



回顾下ObservableSubscribeOn 的创建

![image-20200827161654284](https://i.loli.net/2020/08/27/R1SpxA47Cw5DIfz.png)

![image-20200827162335092](https://i.loli.net/2020/08/27/uFUOrZSDPqbwh4f.png)

![image-20200827162342439](https://i.loli.net/2020/08/27/i2ubQs3JevYLWlt.png)

看到上面的源码 ，发现 RxJava的链式调用其实是把每一个对象都层层包装成Observable ,每层都持有者上一层的引用。这样来达到分层处理的效果。也就是使用了装饰器模式。

回到源码分析
ObservableSubscribeOn 的subscribeOnActual 

![image-20200827171702539](https://i.loli.net/2020/08/27/oNJnQS8vwDhdbLy.png)



回顾一下ObservableCreate的subscribeActual(). 是先执行下游的onSubscribe,然后再执行上游Observable.subscribe();
感觉ObservableSubscribeOn 也应该类似。

![image-20200827173404233](https://i.loli.net/2020/08/27/OL4UNdYbyf5zqIl.png)

这部分是比较熟悉的， 也就是执行下游的onSubscribe方法。
但是看不到显示的调用上流的subscribe方法。

猜测可能和parent.setDisposable 有关
但是看名字 像是设置 上游Observable的dispose
所以猜测执行上游的subscribe的地方在

```java
scheduler.schedulerDirect(new SubscribeTask(parent));
```

从内往外看， 先看SubscribeTask
![image-20200827174411601](https://i.loli.net/2020/08/27/X98DJPvQNzO5dLx.png)

看到了SubscribeTask 是一个runnable 。看到了 runnable,那就是终于看到了和线程操作相关的部分了。
SubscribeTask的run方法就是执行了上游的subscribe()

在回顾一下，
![image-20200827174736847](https://i.loli.net/2020/08/27/LuBbF8pkEjN4sgf.png)

![](https://i.loli.net/2020/08/27/E64XHMG53oZfdlb.png)


![image-20200827175159220](https://i.loli.net/2020/08/27/vqFTzregx8AaEnl.png)



![image-20200827174803048](https://i.loli.net/2020/08/27/RY6NOoCsupzEWJG.png)

从上面的源码得知， 这是通过subscribeOn()传入的scheduler分发器来分发SubscribeTask(),SubscribeTask 实际就是一个runnable, 他的run函数中就会执行上游的subscribe();

现在知道了subscribeOn是把上游的subscribe() 封装到runnable当中，然后交给schedule去分发从而实现了切换线程的目的了。
具体内部是怎么实现的呢？

看源码：
![image-20200827180143739](https://i.loli.net/2020/08/27/i7NDAIrkUKdTY1n.png)

![image-20200827180158064](https://i.loli.net/2020/08/27/OdtJD4ScF3gxPbz.png)

![image-20200827180215013](https://i.loli.net/2020/08/27/aEr8UQntXcHzsmg.png)

![image-20200827180252986](https://i.loli.net/2020/08/27/KoFHxQX8nTjM3OI.png)

![image-20200827180307054](https://i.loli.net/2020/08/27/ygv64QGNCISqsDk.png)

![image-20200827181136701](https://i.loli.net/2020/08/27/OZIxoNh5Udema24.png)



从上面这段源码 可以看出， subsribeOn 中传入的 schedule就是重载了createWork函数 ，然后创建出不同的线程池，然后分发到线程当中去。
中间还加了些封装，实现了dispose ,这个接口。提供给外层来控制整个过程。

这个就是subscribeOn 切换 上游 Observable的subscribe的整个过程：

接下来 下游线程切换

### 下游线程切换 observeOn

![image-20200827181600441](https://i.loli.net/2020/08/27/io7nwvXWK4UjY83.png)

熟悉的模式
schedule的创建， 讲上游的时候已经讲过这里不再啰嗦

直奔ObservableObserveOn的subscribeActual函数
![image-20200827181719051](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20200827181719051.png)

![image-20200827182305067](https://i.loli.net/2020/08/27/72pqKeI5sJHkX9U.png)



 红框内有点 不一样了，之前都是包装了 上游Observable,但是这里是包装了下游Observer。
那现在就可以猜测，RxJava是重写了下游的一系列函数通过 传入的scheduler 来达到切换线程的

先看onSubscribe()
![image-20200831152432594](https://i.loli.net/2020/08/31/rFgRoYE6s4BDuIM.png)

从这里看出，downStream的onSubscribe是执行在当前线程的 因为它不需要通过schedule .

挑的来看
![image-20200827183046069](https://i.loli.net/2020/08/27/IvsKAtSEZLpkrcd.png)

![image-20200827183115787](https://i.loli.net/2020/08/27/C76BaONVFxMDvTz.png)

![image-20200831150015879](https://i.loli.net/2020/08/31/yBFIKo578gRXqaA.png)

可以看出下游的全部操作都和schedule（）有关系。

![image-20200827183126579](https://i.loli.net/2020/08/27/y1bITkHXwGxCt4p.png)

​	run中的drainFused 看名字就觉得是和背压相关。先不管。



来看 drainNormal的源码

![image-20200831151124068](https://i.loli.net/2020/08/31/soNHcWhzQvAEZXB.png)

![image-20200831151215566](https://i.loli.net/2020/08/31/FIeoasMyL6PTf47.png)



checkTerminated 是检测任务是否结束，然后从queue中得到 待处理的元素然后执行downStream.onNext()

![image-20200831151327575](https://i.loli.net/2020/08/31/dOG1tLCYMkmHsgD.png)

downStream.onComplete()和 downStream.onError() 是在线程任务完成的时候执行。

下游的线程切换的实质，其实也是通过scheduler来执行runable 来处理。



## 总结

来总结一下线程切换：

1. 其实RXjava的整个链式调用就是一种责任链的模式，也就中下层的执行中包含链上层的执行；
2. 下游downStream.onSubscribe()是执行在当前线程的
3. subScribeOn指定的是上游的线程，其实就是用schedule去执行一个runnable
4. observeOn指定的是下游的 onNext ,onComplete ，onError的线程
5. 默认情况下，下游接收时间的线程和上游发送的线程是同一个线程



这第五点是怎么得到的呢？
首先下游除了onSubscribe 之外，其他的方法都是在上游的subscribe里面触发的。
如果上游的subscribe在线程A里被执行，在没有其他线程切换的干预下，那么下游的剩余的操作自然也就是在线程A里被执行。

Ps:
多次指定上游线程，只有第一次指定是生效这个结论是错误的。

来分析一下，下面的流程

```java
Observable.create()
          .subscribeOn( threadA)// ObservableA
          .subscribeOn( threadB)// ObservableB
          
```

一开始是得到Observable  其 subscribe () 是执行在当前线程。
经过subscribeOn( threadA )之后，Observable 就执行在线程A了，并且返回了ObservableA,
接着执行了subscribeOn(threadB),这个时候 ObservableA的subscribe就是执行在thradB当中了。
整体流程就是

ObservableB 在threadB 执行了ObservableA的subscribe()
ObservableA在threadA中执行了Observable的subscribe()
所以用户写在Observable的代码就被执行在了threadA当中了。
但threadB是起了作用的

所以subScribeOn是全部都会起作用的， 但是对于用户的代码是链下最近的那个subscribeOn才起作用



## 面试题





### RXJava怎么切换线程

核心是利用装饰者模式去把上游或者下游的操作包装成runnable 放在相应的线程池中去执行。
用 onSubscribe 去控制  上游，  onObservable 去控制下游

用这种方式去控制线程。
所以每一个线程切换的操作都是有效的。
并不是一些网上的理解的 只有 最接近的一个生效。
只是业务代码 被包裹到最接近的那个切换的装饰者里了。
但是整体还是在前一个线程切换的操作当中。



### 你了解协程吗？协程有什么作用？可以完全取代rxjava吗？

协程的本质就是用语法糖提供的 同步的写法去实现异步的调用，内部实现的机理就是用回调的方式来响应协程中的语句。
也就是说 协程就是一种 回调写法的语法糖。
本身其用法就很类似 java提供的future 的用法。
但是future的get是阻塞的。
可以通过future 配合回调 就能实现类似协程的效果了。

rxjava 直接的作用也是为了处理异步的调用。
但是内部用的是 装饰者模式 把操作包装成runnable分发给不同的线程池去执行来完成的线程切换， 和协程的实现机制是不一样的。

更重要的是rxjava  还提供了很多灵活的操作符来应对业务场景。
比如背压，  比如计时  这些在协程上 都得自己的做处理。不如rxjava便捷。

所以我认为 rxjava不会被协程取代。





# Glide

参考 ：https://blog.csdn.net/github_33304260/article/details/78116312?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.channel_param&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.channel_param

优势  可以与activity的生命周期绑定，加载Gif ，还可以配置相应的网络请求框架。



基础用法：

![image-20200831170512909](https://i.loli.net/2020/08/31/flVFp3SosNJQ2jW.png)

## with

![image-20200831170525832](https://i.loli.net/2020/08/31/jGP4LKkA9Ighb7O.png)

![image-20200831170840000](https://i.loli.net/2020/08/31/sUYKR3zjyIuFODe.png)

可以看出 with 函数有很多重载。

但是 目的只有一个  那就是 拿到 RequestManager
怎么拿到RequestManager呢？
得通过RequestManagerRetriever

### RequestManagerRetriever

​	with 怎么拿到RequestManager呢？

​    看with的代码  都是先借助 getRetriever()来处理的

​    

![image-20200918165147409](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20200918165147409.png)

从 RequestManagerRetriever 的名字来看， 就是RequestManager的 发现器 Retriever的意思是 寻物犬
![image-20200918165840666](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20200918165840666.png)

看ReqeustManagerRetriever 的注释， 意思很明显就是维护RequestManager的管理类。 去创建或者去获取。

看看RequestManagerRetriever的get函数

![image-20200918170059046](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20200918170059046.png)

get的重载  基本对应着 Glide.with()的重载。

![image-20200918170229195](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20200918170229195.png)

但是RequestManagerRetriever 的重载最终都会调用到get(Context) 这个函数 或者向当前的Activity添加一个fragment

![image-20200918171057125](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20200918171057125.png)



![image-20200918171604350](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20200918171604350.png)



application的ReuqestManger  实际上是RequestManagerRetriever的一个单例的静态变量。

为啥要向Activity 添加一个 fragment。
Glide 是通过这种手段来监听activity的生命周期的。
这样当图片还未被加载，activity就被销毁的时候，Glide就能直接监听到，并且不再处理该图片。

为什么不用android 提供的registerActivityLifecycleCallbacks接口来监听activity的生命周期呢？

因为registerActivityLifecycleCallbacks接口监听的是全部activity的，不是指定activity的。你得去重新去维护 Glide和关联的activity.很麻烦。并且由于监听的是全部activity，有的activity不一定会用到glide ,这会造成性能的浪费。





总结下：
Glide.with()是为了拿到RequestManager ,
RequestManager是由RequestManagerRetriever来维护的。
为啥要特地弄个RequestManger的维护类呢？

因为RequestManger并不是全局唯一的。
这种管理类做成唯一的不行吗？
可以是可以，但是Glide为了监听activity的生命周期，
在Glide.with()执行在子线程的时候，是会去给activity添加一个fragment的。有了这个fragment就能监听activity的生命周期了。然后就能及时的释放资源。

要点：

1. 通过 Glide.with()  来拿到 RequestManager
2. RequestManager通过RequetManagerRetriever来管理
3. RequestManagerRetriever管理的RequestManager有两种：
   a. 全局的和application 绑定的实例 
   b.  通过创建fragment 依附在当前activity上的RequestManager
4. RequestManagerRetriever  通过依附在activity上的fragment,从而可以监听activity的生命周期



## load

通过Glide.with() ,就可以获取到RequestManager
接下来就是执行RequestManager.load(str);



看下源码

![image-20200918175621382](https://i.loli.net/2020/09/18/rFORmbSVIwLq21C.png)



asDrwable() 是为了构建 RequestBuilder对象  
重点是RequestBuilder对象的laod(string) 函数

接下来看下ReuqestBuildre的load (String)的源码

![image-20200918180806268](https://i.loli.net/2020/09/18/rFORmbSVIwLq21C.png)

![image-20200918180836479](https://i.loli.net/2020/09/18/2afh3mLS9QXgVDM.png)

可以看出 load的核心就是 RequestBuilder 的构造函数了。因为loadGeneric（） 中只是做了些赋值操作。

RequestBuilder的构造函数如下

![image-20200918181144074](https://i.loli.net/2020/09/18/JO1CSm2eRgQYb5f.png)

发现RequestBuilder的构造函数 也只是在 做些赋值操作。
所以可以判定 RequestManager.load() 主要作用就是做了些赋值操作。真正做处理的地方是  into 这个函数。

## into



源码

```java
  /**
   * Sets the {@link ImageView} the resource will be loaded into, cancels any existing loads into
   * the view, and frees any resources Glide may have previously loaded into the view so they may be
   * reused.
   *
   * @see RequestManager#clear(Target)
   * @param view The view to cancel previous loads for and load the new resource into.
   * @return The {@link com.bumptech.glide.request.target.Target} used to wrap the given {@link
   *     ImageView}.
   */
  @NonNull
  public ViewTarget<ImageView, TranscodeType> into(@NonNull ImageView view) {
    Util.assertMainThread();
    Preconditions.checkNotNull(view);

    BaseRequestOptions<?> requestOptions = this;
    if (!requestOptions.isTransformationSet()
        && requestOptions.isTransformationAllowed()
        && view.getScaleType() != null) {
      // Clone in this method so that if we use this RequestBuilder to load into a View and then
      // into a different target, we don't retain the transformation applied based on the previous
      // View's scale type.
      switch (view.getScaleType()) {
        case CENTER_CROP:
          requestOptions = requestOptions.clone().optionalCenterCrop();
          break;
        case CENTER_INSIDE:
          requestOptions = requestOptions.clone().optionalCenterInside();
          break;
        case FIT_CENTER:
        case FIT_START:
        case FIT_END:
          requestOptions = requestOptions.clone().optionalFitCenter();
          break;
        case FIT_XY:
          requestOptions = requestOptions.clone().optionalCenterInside();
          break;
        case CENTER:
        case MATRIX:
        default:
          // Do nothing.
      }
    }

    return into(
        glideContext.buildImageViewTarget(view, transcodeClass),
        /*targetListener=*/ null,
        requestOptions,
        Executors.mainThreadExecutor());
  }

```



关键部分

![image-20200924103217506](https://i.loli.net/2020/09/24/b4toCOAmDMizG5P.png)

可以发现 只是 调用了带其他参数的into函数



看看多参数 的into 的各个参数是怎么把图片资源加载到imageView当中去的.

![image-20200924103949485](https://i.loli.net/2020/09/24/TsEG78ncb6tMBF3.png)

参数分别是Target , targetListener , requestOptions,Executor 
从名称上来看，   RequestOptions 应该是保存了相关的一些配置， executor 应该是指定了一些逻辑需要执行在哪个线程。Glide的改变UI这一步，肯定是在主线程的当中执行的。

Target和TargetListener 是啥的， 从名称上来看，知道了Target就应该知道Target了，而且这里TargetListener传的是null，所以先看Target即可。



### Target

![image-20200924104526713](https://i.loli.net/2020/09/24/t4R7ZBDOAHnaeLY.png)

![image-20200924104154309](https://i.loli.net/2020/09/24/8PfKtFLcIg6oAOU.png)

![image-20200924104208109](https://i.loli.net/2020/09/24/3mERZ8zkW9UDGNw.png)

看出Target是一个回调接口，  
和ImageView关联在一块的回调接口，那自然联想到是onResourceReady之后就把图片资源赋给 ImageView的啦。
那就接着往下看看.

Target是怎么构建的

### buildImageViewTarget-构建Target

![image-20200924104819224](https://i.loli.net/2020/09/24/Kv8X5uW4yscxp6l.png)

![image-20200924104846044](https://i.loli.net/2020/09/24/pHfnYbkdSALRBvF.png)

![image-20200924104938624](https://i.loli.net/2020/09/24/N4PfIj5xpBiHCZK.png)

![image-20200924105107655](https://i.loli.net/2020/09/24/6VCESaBmPOxg2KH.png)

这里看到了 给imageView设置资源的地方了
但是没其他的  还是的看下DrawableImageViewTarget的父类ImageViewTarget。看了下 只是实现了各个接口。

所以Target的创建只是实现了各个回调接口。

因此现在可以回到RequestBuilder的into（）中。



回到into

![image-20200924110617670](https://i.loli.net/2020/09/24/XuUBNFVI2YL4srt.png)

终于看到RequestManager了

看到这里 ，就感觉 RequestManager.trace函数是重点。不过先一步步的往下看。
猜测是  buildRequest 来构建 Request ，然后通过RequestManager的track 去执行，再接着track中会触发Target中的相关回调， 从而完成对ImageView的赋值



看着猜测来看，
先看buildRequest

### buildRequest-构建Request



![image-20200924111546310](https://i.loli.net/2020/09/24/abLBCrd5neIhiJZ.png)



buildRequestRecursive的实际就是
看有没有错误处理，没有的话就直接返回mainRequest,有的话，那么就把mainRequest和errorRequest打包在一起封装成一个新的。再返回

![image-20200924111718568](https://i.loli.net/2020/09/24/oX7WHswemBdPNEF.png)

![image-20200924111750264](https://i.loli.net/2020/09/24/Onh5PFUfZlYb9RK.png)





先抓住主干， 看MainRequest的构建，errorRequest先不管。

#### buildThumbnailRequestRecursive- 构建MainReqeust

buildThumbnailRequestRecursive中分了很多中情况 ，还包含了很多 缩略图的处理等。但是核心只有一句。

![image-20200924120020534](https://i.loli.net/2020/09/24/8DVPYd5xZpOMR6u.png)

![image-20200924120041790](https://i.loli.net/2020/09/24/wYCgEAXRUinfK4t.png)

要用的参数巨多 ，但是 实际上也只是new 了一个 SingleRequest

![image-20200924120452433](https://i.loli.net/2020/09/24/uyoA4idvPnwsbxC.png)





所以buildeRequest的实际就是new了一个singleRequest对象



接着回到 into中

![image-20200924120736051](https://i.loli.net/2020/09/24/QNO4fAnLZi7jPh8.png)

先不管缓存相关的，往下走。

```java
requestManager.clear(target); // 顾名思义
target.setRequest（request）; // 缓存相关

requestManager.trace( target,request);

return  target;
```

那就只剩下requestManger.trace(target, request)了

接着看RequestManager.trace

### RequestManager.trace

![image-20200924121302108](https://i.loli.net/2020/09/24/zarshnZgDVYpmEC.png)



先看TargetTracker.track

#### Targetracker（Target的追踪器）

![image-20200924121542038](https://i.loli.net/2020/09/24/DrbQtT7gzipuSn2.png)

targetTracker就是触发 target 和activity相关的生命周期回调的地方。

![image-20200924121831406](https://i.loli.net/2020/09/24/ePkHciWUdoLO8wm.png)

TargetTracker并没有关联到资源加载的回调， 所以资源加载应该是在 RequestManager.track中的 requestTracker.runRequest

#### RequestTracker.runRequest

![image-20200924122353282](https://i.loli.net/2020/09/24/CpQtB7f19zIAXHU.png)



执行的是Request.begin() , 对于我们的分析中， 也就是SingleRequest.begin()

#### Request.begin()

![image-20200924122955990](https://i.loli.net/2020/09/24/mX6xAKhnskD1cYM.png)

可以看出  begin就是  glide 读取资源的核心 .标红的三个函数 应该就是对应着glide的target对应的三个状态  start， ready ,failure



先来看fail 

##### Target- Failed状态 onLoadFailed

![image-20200924142501369](https://i.loli.net/2020/09/24/vbfuJHwrsmRWQd8.png)

都是些 状态设置和 触发回调
而target的onfail 方法在 setErrorPlaceholder（）中执行

![image-20200924142723315](https://i.loli.net/2020/09/24/xqSlrABmv5Cngh9.png)

以ImageViewTarget 为例子
onLoadFailed

![image-20200924142820036](https://i.loli.net/2020/09/24/F8Q7Ei9e15vDVmt.png)

![image-20200924142833153](https://i.loli.net/2020/09/24/81mKUN9Wcf4YLl5.png)

图片就被设置进去了。

接着看start 状态

##### Target- Start状态

没啥好说的 就只直接调用了target的onLoadStarted()

![image-20200924143301962](https://i.loli.net/2020/09/24/Ntz2Xj6bV8EaiwW.png)

![image-20200924143214898](https://i.loli.net/2020/09/24/G8AZTktDgXqCV7o.png)

##### Target- Ready状态

![image-20200924143524916](https://i.loli.net/2020/09/24/Q4PlANa8GIe7uqJ.png)

这可以可以观察下 glide的数据来源有哪些 -- DataSource 这个枚举类型

![image-20200924143700789](https://i.loli.net/2020/09/24/tv4VYnRcwKhq6mN.png)

总体三类 local， remote  ,cache

ok 接着ready状态往下看。



![image-20200924144105564](https://i.loli.net/2020/09/24/qo5jFiJlkTWf6QH.png)

![image-20200924144508858](https://i.loli.net/2020/09/24/WbSsE8L1tO76eRc.png)

 这里把result 就传过去了。
那么这个result是怎么得来的呢？

回到 两个参数的 onResourceReady

![image-20200924144820838](https://i.loli.net/2020/09/24/AgHvKh4OtXEMS2p.png)

可以看出是通过resource.get来得到的。

![image-20200924144919292](https://i.loli.net/2020/09/24/wPX6lxKeL1hWTBz.png)

get的实现有这么多种， 

挑一个来看，
![image-20200924145310416](https://i.loli.net/2020/09/24/rDcVmtMbuj19PiU.png)

 发现是Resource的构造函数中带进去的



那就要回到 singleRequest.begin中看 resource是怎么来的了.

![image-20200924145455842](https://i.loli.net/2020/09/24/GTZkwUOtK54pBaA.png)

resource是个成员变量

![image-20200924145548961](https://i.loli.net/2020/09/24/aHEXvFUZVRWT8jd.png)

只有一个赋有效值的地方

那就 3个参数的 onResourceReady  


![image-20200924145809835](https://i.loli.net/2020/09/24/3waeSYRtnNUMkzO.png)

卧槽 ，这不是套娃了吗？
一直都是空了呀。

这肯定是有问题的。

所以这个三个参数的onResourceReady 一定是有别的入口.
而是正常来说这个入口应该是由 start状态触发的

追踪一下

三个参数的onResourceReady 只被 两个参数的onResourceReady调用

![image-20200924150344809](https://i.loli.net/2020/09/24/NvClcwADtg8BaR9.png)



![image-20200924150409148](https://i.loli.net/2020/09/24/OJdXjTSvIycEBxt.png)

![image-20200924150433107](https://i.loli.net/2020/09/24/Dq7sC1AuBQMXboV.png)

这就追踪到了， 还是由begin 触发的

![image-20200924150517494](https://i.loli.net/2020/09/24/Oh2qcbC8GgjlyVU.png)

所以说 resource 资源的加载 是在Request的begin中 的onSizeReady中触发的

![image-20200924150732286](https://i.loli.net/2020/09/24/Fb4A7mGkdvaNrMI.png)

onsizeReady的代码很长， 但是核心的就是这句。
调用engine.load();

所以这个engine 就是 读取图片资源的关键了

##### Engine  -  负责开始读取和管理活动、以及内存资源 

![image-20200924150953322](https://i.loli.net/2020/09/24/O13SfRGy6EZmpgb.png)

看注释 ： 负责开始读取和管理活动、以及内存资源 。

大致知道了engine的作用。
再细看下代码



发现个问题 可以观察一下

![image-20200924151406776](https://i.loli.net/2020/09/24/RamleygfE8XtUnM.png)

![image-20200924152207312](https://i.loli.net/2020/09/24/NiOHwL8bc6WefBR.png)

核心就是 waitForExistingOrStartNewJob

![image-20200924153415812](https://i.loli.net/2020/09/24/GvhlZy5wjD39z4N.png)

先看下返回值。



![image-20200924153223710](https://i.loli.net/2020/09/24/GyZzpTwPu14QoAv.png)

看的出来 这个返回值，只是用来cancel操作的



所以核心就是EngineJob的 start

##### EngineJob.start

![image-20200924153525218](https://i.loli.net/2020/09/24/3juKYsi1DqMHeBN.png)

![image-20200924153817169](https://i.loli.net/2020/09/24/ch86NTPjqAF1IoV.png)





所以现在要看的是这个异步任务DecodeJob 做了啥。

##### DevideJob

DevideJob .run

![image-20200924154010525](https://i.loli.net/2020/09/24/FRYWUSH4wqdivrC.png)

核心是 runWrapped()函数

![image-20200924160906021](https://i.loli.net/2020/09/24/RquLPiUo1BAQ4Yy.png)

先看INITIALIZE状态的 起执行了 runGenerators()

![image-20200924161001030](https://i.loli.net/2020/09/24/PbRFtk7K5pB3WGA.png)

runGenerators中重点是红框的两句

![image-20200925141647092](https://i.loli.net/2020/09/25/RruDzcOfx7mWT2C.png)

看看个currentGenerator 是怎么初始化的。

![image-20200925142001194](https://i.loli.net/2020/09/25/FndJ6qewujbhpYR.png)

![image-20200925142021802](https://i.loli.net/2020/09/25/SvBy3znbPG57dZF.png)

其实就是对应了 三种类型 的数据源 



回到startNext函数

![image-20200925142129915](https://i.loli.net/2020/09/25/B61YeynzrHqNjV8.png)

有三种重载。

对于首次的情况，肯定是直接去拿源。SourceGenerator

接着来看SourceGenerator的startNext函数

![image-20200925143008693](https://i.loli.net/2020/09/25/nRvGZ5f1sVqbr7z.png)



![image-20200925143209174](https://i.loli.net/2020/09/25/5tvMhAklRxUwZQJ.png)

所以 判定 资源的加载就是在 这个  loadData.fetcher.loadData 这里处理了。

![image-20200925143402268](https://i.loli.net/2020/09/25/uHBWR94KGpiq13X.png)

![image-20200925143421768](https://i.loli.net/2020/09/25/Zw2pMBV7jyGnaSR.png)

来看看这两个类

![image-20200925143733976](https://i.loli.net/2020/09/25/H3a5MsCx1JROfjG.png)

![image-20200925143932515](https://i.loli.net/2020/09/25/ZWtlXjuLrFHziaN.png)

![image-20200925144040846](https://i.loli.net/2020/09/25/4P5GUvbXyEikDjF.png)

这样大体类的功能就知道了。

真正加载资源的就是DataFetcher这个接口了

这个DataFetcher的实现这么多， 该用哪一个呢？

![image-20200925144244542](https://i.loli.net/2020/09/25/LrESQ9ImtRiq26F.png)

从名称上来看 ， 以例子来说 应该是HtppUrlFetcher 。 但是是怎么对应到HttpUrlFetcher的 大概看了下。感觉很复杂。就先不管，先直接去看主流程。

也就是HttpUrlFetcher

##### DataFetcher  - 真正加载资源的地方



接下来看HttpUrlFetcher 的loadData的源码

![image-20200925145120341](https://i.loli.net/2020/09/25/mpbwTaJtMedFHS8.png)





![image-20200925150602781](https://i.loli.net/2020/09/25/jyUq96JDOLBac1t.png)

![image-20200925150724757](https://i.loli.net/2020/09/25/qlpUAhjfM564xi3.png)

终于看到HTTP的东西了
那现在就拿到了  流。 接着看流是怎么处理的

![image-20200925151352271](https://i.loli.net/2020/09/25/wTeQpCOYcGzLjWs.png)

![image-20200925151411926](https://i.loli.net/2020/09/25/XwkYfL2RJMHo4Nb.png)



流给到了SourceGenerator .onDataReadyInternal来处理

![image-20200925152403480](https://i.loli.net/2020/09/25/2sV9nPulIQMTxgJ.png)

![image-20200925152803892](https://i.loli.net/2020/09/25/jrSm8YvVXB1PgyI.png)

SourceGenerator 中的FetcherReadyCallback  传的是DecodeJob对象

所以调用的是DecodeJob.onDataFetcherReady

![image-20200925153133600](https://i.loli.net/2020/09/25/yZI1hT4VP3Lai2b.png)

![image-20200925153254444](https://i.loli.net/2020/09/25/LIcSxvBo6gdH7qD.png)



先看decode资源

![image-20200925153730315](https://i.loli.net/2020/09/25/n2eCkuyZXAiEYSx.png)

![image-20200925153840347](https://i.loli.net/2020/09/25/Wwv3CjsAmqPVorQ.png)

![image-20200925153938909](https://i.loli.net/2020/09/25/32sbuJdfwvtMeQE.png)

![image-20200925154028613](https://i.loli.net/2020/09/25/6Y9jmWHpD1Gaxed.png)

![image-20200925154234144](https://i.loli.net/2020/09/25/jUhnI6WF3c41SDr.png)

返回了一个LoadPath 实例 

接着往下看



![image-20200925154722445](https://i.loli.net/2020/09/25/rVtvYuilgP3OATX.png)

![image-20200925154832509](https://i.loli.net/2020/09/25/5gnfqxmprhaKsWb.png)

然后经过Registry的处理 和 一系列的调用 （涉及到好几个类，  晕乎乎的 先不管）



![image-20200925155556879](https://i.loli.net/2020/09/25/lFcZGXfKrS9IPWi.png)



![image-20200925155644101](https://i.loli.net/2020/09/25/lFcZGXfKrS9IPWi.png)



这几句先停一下，不往下跟了。
这里可以判断出是 返回的值Resource<Transcode>了  ，先返回去。看看Resource中的这个泛型是怎么转成能设置给imageView的类型的

![image-20200925160436052](https://i.loli.net/2020/09/25/wbr6j8x1s9CNATJ.png)



经过一系列 让我头大的 调用  会回到

![image-20200925161234606](https://i.loli.net/2020/09/25/T7dAJmjefQ5cRO8.png)



![image-20200925161537244](https://i.loli.net/2020/09/25/RpFdHTnQ2jELNvM.png)

![image-20200925161600687](https://i.loli.net/2020/09/25/pPguAN9GY4z3Q8r.png)

![image-20200925161637873](https://i.loli.net/2020/09/25/TWcfM5Vkwb4K1Rm.png)

![image-20200925161857799](https://i.loli.net/2020/09/25/bv2yktcGsEFap8q.png)

而后会执行到这里。







接着回到

![image-20200927095922449](https://i.loli.net/2020/09/27/y6JhSmdwpsfUbCI.png)

这里就是

![image-20200927100505528](https://i.loli.net/2020/09/27/zV6G1ra5WkquEKd.png)



![image-20200927141513622](https://i.loli.net/2020/09/27/feYCD6IGXABP7uL.png)

![image-20200927141551282](https://i.loli.net/2020/09/27/USIW86Jvjapo2LK.png)





#### 总结Into流程

重头再整理一遍主流程

![](https://i.loli.net/2021/04/23/dO4Sob7DaYj1uQs.png)





主流程

RequestBuilder 构建ViewTarget   然后 构建Request
接着通过RequestManager来执行request
然后就开始异步执行加载任务， 真正执行加载任务是通过EngineJob来执行的，EngineJob就是一个异步线程启动器
engineJob 是启动DecodeJob这个runnable来启动任务的。
DecodeJob 这个任务有分三个部分 

1. 读取未解析的数据， 
2.  解码未解析的数据 
3.  把解码后的数据转换成需要的格式

读取数据通过 DataFetcher来完成
解码数据通过 ResourceDecode 来完成
转换格式通过ResourceTranscoder 来完成



Glide 对数据资源的支持就是通过DtaFetcher ,ResourceDecode ，ResourceTranscoder的大量重载来实现 对各种输入的资源类型（比如http,byte数组， file ，stream等等）以及各种解码类型 和各种的目标类型进行适配和转换。

转换完之后 再通过 一层一层的回调 最终回调到ViewTarget的ready回调用，viewTarget内部维护了用户传入的view,由其完成贴图操作
转换完成 - > DecodeJob.callback --> EngineJob.callback --> 
Request.callback --> viewTarget.callback





现在接着把 非主线 但是也是很重要的逻辑（主要就是缓存逻辑）也理一下。



DataFetcherGenerator  是怎么获取 LoadData的?



![image-20200930180403641](https://i.loli.net/2020/09/30/mWFG4AxkIjEzl3U.png)

![image-20200930181632623](https://i.loli.net/2020/09/30/92Sg45XTHFrtPam.png)







大致的主流程是这样。
还有很多其他的要求 没有说到  他的三级缓存，以及各个泛指的指定的意义等都没有说。这看到头疼先看到这里。后面再继续补充



GlideContext 是全局唯一的



## Glide 总流程补充

Glide.with(context) 去获取requestManager。
这个RequestManager对于 一个activity来说是唯一的。
也就是Glide中activity 多次调用with(this)。
获取到的是同一个RequestManager.

是怎么做到的呢？
下图所示
对于同一个activity 会去找是否有glide绑定activity的fragment.如果有没有就创建一个， 如果有那么就从里面拿RequestManager.

![image-20210423114633702](https://i.loli.net/2021/04/23/jU9xtZ4JLiGabkC.png)

![image-20210423114757747](https://i.loli.net/2021/04/23/x4Y7i1pInJS9WO3.png)

所以同一个activity 里用的with(activity)， 用的requestManager是同一个。

RequestManager.load()

实际调用的是RequestBuilder .load()
也就是RequestManager.as().load
![image-20210423115603612](https://i.loli.net/2021/04/23/4WSsnq71bIQF8RL.png)

上图看出，  每次requestManager没有load ,都是new 出了一个新的requestBuilder.

Engine 是负责读取 和管理 缓存的。
是Glide全局唯一的。在Glide初始化的时候 就构建出来的。



## Glide 面试题

### Glide的优点

1. 链式调用 流程清晰
2. 代码侵入性小
3. 默认情况下 使用的是 565 编码 ，比8888编码的省一半的内存
4. 绑定activity生命周期，避免护内存泄露
5. 内部维护着三级lru缓存，而且还对不同尺寸的view 缓存不同的尺寸的图片





### Glide生命周期绑定原理

通过给activity 添加一个空的fragment ,然后通过fragment的lifecycler监听 fragment的生命周期，再生命周期函数的回调中进行相应的处理。从而实现了生命周期的绑定。

这个实现和Androidx的LifeCycler的实现是思路是一致的。
都是通过加入一个空的fragment来监听activity。
这样做的主要目的就是为了适配老版本的activity.
因为activity是在appcompatActivity之后才实现了LifecyclerOnwer这个接口的。否则就不需要解除fragment去处理了。



### Glide怎么保证绑定activity的空Fragment的唯一性的

给在activity 的fm找固定tag的fragment ，如果有就说明绑定上了。没有再加上。 fragment manager 的tag是唯一的 那么自然 activity对应发fragment就是唯一的了。



### 缓存原理

从glide的真正开始加载 ，管理的 Engine类开始分析。

从Engine.load开始

![image-20210423143059644](https://i.loli.net/2021/04/23/FpdMPRYWaUVAKCD.png)

#### 内存缓存 -ActivityResource & MemoryCache

先看看内存缓存。
内存缓存内两块，
 一个是活动缓存  随着activity销毁会把里面的内容转移到内存缓存当中。
另一个内存缓存。

下图是 活动缓存 ， 使用的是以弱引用由为value 的map来维护的
这是一个没有限制数量的 map   和MemoryCache 不一样， MemoryCache作为LruCache 是有数量限制的

![image-20210423143528287](https://i.loli.net/2021/04/23/jDeSsitnOzfLRTk.png)

下图是 内存缓存 ， 使用的是map来维护的  不是弱应用

![image-20210423143855658](https://i.loli.net/2021/04/23/VyEigcr5NARvpWh.png)

这里是对于内存缓存怎么取的内容

接着再往下看。
如果在ActivityResource和MemmoryCache中都找不到的话

就会进入下图。

![image-20210423145006481](https://i.loli.net/2021/04/23/Lqdgoy3jw86vkSm.png)

根据前面的分析  可以看出 核心就会执行到
DecodeJob当中的run里 跟下代码 就能找到关键。

![image-20210423145429984](https://i.loli.net/2021/04/23/RFeLE3oUbpVwa9W.png)



根据上图 分析 内存缓存中拿不到的话， 还有三个数据来源。

- DataCahcheGenerator
- ResourceCacheGenerator
- SourceGenerator

根据缓存优先级顺序来看。

#### ResourceCacheGenerator

下图可以判断 是磁盘缓存

![image-20210423152054155](https://i.loli.net/2021/04/23/2JRrqndMjeZXHga.png)

#### DataCacheGenerator

下图看出DataCache也是磁盘缓存

![image-20210423152219239](https://i.loli.net/2021/04/23/cbtvEAYzmZIlpMW.png)


#### SourceGenerator

这里就是没有缓存直接从源 出读取。



可以发现网上说的三级缓存 这说法不太对哇。
应该是三级数据来源。
内存缓存 -》  磁盘缓存 -》 原数据

内存缓存有两级：

ActivityResource 和 MemoryCache
为啥要弄成两级呢？

lru算法 是没有管正在被使用着的图片的。
如果只有一级 cache的话，那么就可能会存在 正在被使用的相片被移除了。

如果加多一级 ，然后配合下图的逻辑  ， 就可以 先把正在使用的图片用一个非lru的map给存储起来,然后activity被销毁时 ，再转移到MemoryCache里面。

活动缓存中用的图片存储起来 用的是弱应用， 用弱应用这样可以有效的避免 内存溢出。
但是用弱应用也不是绝对可靠的。
毕竟GC的线程优先级不高。

![image-20210423161854574](https://i.loli.net/2021/04/23/M3OYfaFkKHEjBxe.png)

另外磁盘 缓存也分成了两步：
ResourceCache 和 DataResource

这两个都是 磁盘缓存  但是 ResourceCache是缓存编码后的数据，DataCache缓存的是实际的源数据。
比如说
同一张图片 应用了 两个尺寸。
这个时候的缓存状态应该是怎么样的呢？

从下面的图可以看出 在 ActivityResource ,MemoryCache，ResourceCache当中  的key都是包含 宽高信息的， 而DataCache是不包含宽高信息的。
所以可以判断出同一张图片两个尺寸在非源数据缓存中 只有一份，但是其他缓存中是由两份的。

![image-20210423175810277](https://i.loli.net/2021/04/23/rwyN5xYAOiT1aB3.png)

![image-20210423175732898](https://i.loli.net/2021/04/23/xJUFCfKEWMX94ew.png)





### Glide的存在内存泄露的情况吗

严格来说  应该是不存在内存泄露。
但是他的缓存机制是由可能造成 内存溢出的。

Glide 用了两点 来保证 内存不泄露

- Glide能绑定activity的生命周期 ，及时的释放 活动缓存，另外活动缓存里还用的是弱应用来处理，避免内存溢出
- Glide的内存缓存 用的是LRU算法 来保持缓存区的大小

但是为啥还会有 内存泄露的情况呢？
分两种情况 

1. 绑定的是application 而不是 activity 这样的话就不能及时的手动释放 活动内存了
2. 绑定的是activity的话  活动内存导致的内存溢出问题出现的概率就降低了 ，但是并不是就不可能了。 因为导致内存溢出的原因是 活动内存里 维护的 弱应用对象 没有被GC 扫描到， 没有被回收。所以导致的内存溢出； 如果说gc的够快的话， 理论上绑定在Application上的话， 也是不会有内存溢出的情况发生的。

所以 Glide 虽然没有设计上的内存泄露  但是 实际上还是有可能会导致内存溢出的。



### 使用Glide时 with 在子线程中执行会有问题吗？

Glide内部只有在主线程中构建RequestManager的时候 才会去绑定生命周期的。比如给activity添加空白Fragment的操作。
如果在子线程构建RequestManager的话，就不能自动释放 活动内存了。



### BitmapPool

Glide当中 bitmapPool的作用  对bitmap 进行复用。
从下图的引用来看  就是在DecoderJob中的   格式转换 中用的比较多。

![image-20210423182835182](https://i.loli.net/2021/04/23/oEnfeQs8AR9rKPj.png)

### App 内存紧张时 如何避免Glide 引发的OOM?

监听onLowMemory、onTrimMemory回调，及时释放memoryCache、bitmapPool、arrayPool

