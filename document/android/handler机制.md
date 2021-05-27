# Handler机制

## 整体结构

android handler消息机制是由 以下四个组件 支撑起来的。

- Looper         消息驱动
- Handler       消息的发送和接收处理器
- MessageQueue   消息队列
- Mesage                消息体



### Looper

从消息队列MsgQueue中 取出msg 然后分发给对应的handler。
Looper最重要的两个作用是

- 决定了消息处理在哪个线程当中执行
- 驱动msg的分发

#### Looper的初始化

在当前线程执行
Looper.prepare()即可。

应用的主线程中 不需要执行Looper.prepare()是因为zygote在fork出应用进程的时候，应用进程就执行到ActivityThread.main()。里面就做了 对主线程的looper的初始化。

#### Looper的无限循环等待msg消息

Looper.loop在执行一个无限循环， 那不是很占用cpu的资源？
实际上Looper的无限循环是用了 linux的epool机制来实现的。
所以并不会耗费cpu资源。

在Looper.loop尝试去取MsgQueue中的消息的时候,如果没有消息。
就会执行epoll, 让这个线程休眠。直到休眠超时，或者有新的消息过来的时候唤醒线程。
也就是说 Looper在消息队列里没有消息的时候 ，是直接休眠了的。
所以并没有一直在无意义的跑。

android线程的消息来源 除了app之外，还有软键盘，屏幕等等。
epoll后，系统会把这些消息写入文件中。epoll机制就是通过监听这些文件，
如果文件有改动了，那么系统就给epoll的线程发送中断， 这样java的线程就被唤醒了。



### Handler



发送的方式

1. sendMessage(message)
2. post(runnable)
3. sendMessageAtTime



消息分发的优先级：

1. Message的回调方法： message.callback 
   也就是传入的runnable
2. Handle的回调方法：
   Handler.mCallback.handleMessage
3. Handler的默认方法：
   Handler.handleMessage

对应的对调

```kotlin
        //1. 直接在Runnable中处理任务
        handler.post (runnable= Runnable{
          
        })

        //2.使用Handler.Callback 来接收处理消息
        val handler =object :Handler(Callback {
            return@Callback true
          
           }
        )
          
        //3. 使用handlerMessage接收处理消息
        val handler =object :Handler(){
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
            }
        }
        val message = Message.obtain()
        handler.sendMessage(message)

```



#### Handler和Looper的绑定

Handler的能力需要依赖于Looper ，但是创建Handler可以不传Looper。
handler的默认looper是哪里来的呢？
看下图。

![image-20201204105123320](https://i.loli.net/2020/12/04/xkea1ZdvGVzhorC.png)



![image-20210525105025260](https://i.loli.net/2021/05/25/WMjuBNm9OGAhvdP.png)



handler的默认Looper是从ThreadLocal里来的。
ThreadLocal后面接着分析。

### MessageQueue

其中MessageQueue是用来存储Message
是一个单向链表队列

出队的顺序是

- 如果队头是屏障消息，那么就跳过同步消息，取出第一个异步消息
- 如果队头不是屏障消息，那么就按照时间顺序来出



#### tips

##### 屏障消息

屏障消息 只有在frameWork层的 屏幕刷新的才有使用。
是用来同步vSync的屏幕刷新信号的。
用这种方式来保证屏幕刷新的频率。



### Message

消息体

数据结构

```JAVA
long  when  // 预期执行的时间戳  
Handler target // 给哪个handler处理
Runnable  callback //  该消息的处理回调
Object  obj		
Bundle  data
Message  next  // 下一个消息
```





Message消息类型

- 同步消息
- 异步消息
- 屏障消息 

这里可能会有误解， 这里的同步/异步消息 处理时并没有不同，都是异步处理的。Handler的消息处理都是异步处理的。
只和MessageQueue的出队顺序相关。





Message中有一个时间戳字段，MessageQueue就根据这个时间戳来决定出队顺序。

Handler的延时也是通过改这个时间戳来实现的 。
入队还是立刻入队的。

一般情况下MessageQueue的消息出队顺序是只和时间顺序有关。但是在队头是屏障消息的时候，就会跳过同步消息，去把第一个异步消息出队。

Tip：屏障消息 和异步消息 用户是不能触发的。都是系统触发的 。 系统应用场景有  对于刷新ui的垂直刷新信号就是异步消息，系统就是用屏障消息来触发垂直刷新信号消息来刷新UI的。 



Tip: Handler里有 根据消息立即唤醒对应线程的机制，这个立即唤醒是为了让异步消息更快的得到执行。
比如 messageQueue中下一条消息就是屏障消息，或这插入的消息是第一条异步消息，那么handler都会主动的唤醒其对应的线程。

## IdleHandler

dleHandler  监听 当前线程的Looper进入了空闲状态。
用于处理不重要的任务， 避免抢占系统资源

在MessageQueue 中，如果有空闲的话，就处理idleHandler

是否空闲的判断
![image-20201204163223949](https://i.loli.net/2020/12/04/HbWFI9fYvxLZEpd.png)

![image-20201204162412663](https://i.loli.net/2020/12/04/hTOoQ26Bxim1G3n.png)



## ThreadLocal

[参考文章](https://www.cnblogs.com/aobing/p/13382184.html)

threadLocal提供了线程独有的局部变量存储能力。
在整个线程的范围内 ，可以灵活存取。实现了线程之间的数据隔离。

Looper就是通过java的ThreadLocal来实现线程之间各自维护自己的looper。



![img](https://img.mukewang.com/wiki/5f1f89eb09548b3f10110496.jpg)





![image-20201204165859587](https://i.loli.net/2020/12/04/Qky5VoCY6bIFxKi.png)

![image-20201204170040825](https://i.loli.net/2020/12/04/7PvwpJrhxzT25FD.png)



![image-20210525115634387](https://i.loli.net/2021/05/25/YXALg1FisVfr7qJ.png)



上图 已经表明 looper是通过threadLocal来维护的。

### 线程间数据隔离

现在的问题是  ThreadLocal是怎么实现线程隔离的？
下图  每一个线程都维护着一个 ThreadLocalMap

![image-20201204165859587](https://i.loli.net/2020/12/04/Qky5VoCY6bIFxKi.png)

ThreadLocalMap实际上是一个数组。

![image-20210525115917498](https://i.loli.net/2021/05/25/NDKypQqiAu4IRrZ.png)

![image-20210525120215262](https://i.loli.net/2021/05/25/7bowOTyMXNkmvPp.png)

![](https://i.loli.net/2021/05/25/7bowOTyMXNkmvPp.png)



ThreadMapLocalMap 是通过数组来存储ThreadLocal。
那么各个ThreadLocal在数组中的序号怎么确定呢？
下图可以看出， 是通过ThreadLocal的hash值来确定 数组位置的其实位置， 然后往后找到第一个可用的位置进行填入。
这么看， 如果数据大的时候 hash冲突的概率还挺高， 效率偏低。
不过ThreadLocal这个 不怎么常用。

![image-20210525120550812](https://i.loli.net/2021/05/25/fTiAFIl5Ux6HKNt.png)

这样就解答了ThreadLocal的数据隔离的实现方式。
实际上就是**用相同的key在不用的map下取值**。



### ThreadLocal的内存泄露

ThreadLocal实际上是存在内存泄露的。
看看ThreadLocalMap中的弱引用。

![image-20210525121034600](https://i.loli.net/2021/05/25/uOeZ3VKdz2LMECo.png)

可以看出  当gc扫描到 Entry的key的时候， 那么这个key就会被回收了。
也就是ThreadLocalMap中 出现了 key为空 value不为空的 entry.

如果线程在复用场景下， 比如线程池中时。
那么这个线程就一直维护这一个没有任何意义的value.
这个就是内存泄露的点。

java的处理办法是：
在ThreadLocalMap.set的时候 发现了value为null的entry的时候，就把这个entry用新数据给覆盖了。

![image-20210525121752856](https://i.loli.net/2021/05/25/PwRJ8jVOt2HvcU1.png)

或者开发者手动的执行了remove也可以。
这样就能解决泄露的问题。



#### QA:

##### Q:为啥ThreadLocalMap.Entry的key做成弱引用，而value是强应用呢？

A: key做成弱应用是为了能回收内存。但是value却是强引用。
为啥value不做成弱引用呢？
因为value 值很可能已经在其他地方在使用。 如果突然回收掉，那么别的地方就数据错乱了。
比如下图的 looper.

![image-20210525122302424](https://i.loli.net/2021/05/25/V7A8643vSno5IlE.png)

在looper场景下，如果主线程的threadlocal的value被回收掉了话， 那么就没有looper去驱动msg了。
所以说value不能是弱引用。



## 常见问题:

### Q：为什么主线程不会因为Looper.loop里的死循环卡死？

![image-20201204183105931](https://i.loli.net/2020/12/04/b4ZGmA197syNng2.png)

### Q：post和sendMessage 两类发送消息的方法有什么区别？

![image-20201204183133355](https://i.loli.net/2020/12/04/fJXtGIw6paxEmFY.png)

### Q：为什么要通过Message.obtain()方法获取Message对象？

![image-20201204183200969](https://i.loli.net/2020/12/04/z6KelyJt2f47RIF.png)

### Q:Handler实现发送延迟消息的原理是什么？

![image-20201204183328714](https://i.loli.net/2020/12/04/DZ4BGkTWYjAzhq6.png)

### Q:同步屏障消息的作用？

![image-20201204183359036](https://i.loli.net/2020/12/04/WNrR23bh6fcjwP1.png)

### Q:IdleHandler的作用？

![image-20201204183416570](https://i.loli.net/2020/12/04/Dnlgq28mhzPF6GT.png)

### Q：为什么非静态类的Handler导致内存泄露？怎么解决？

![image-20201204183451232](https://i.loli.net/2020/12/04/IQsbOSqhW2gw975.png)

### Q:如何让在子线程中弹出toast

![image-20201204183529942](https://i.loli.net/2020/12/04/KIiLRtScpha678k.png)

```java
在子线程当中 执行Looper.prepare 以及Looper.loop
后即可 弹出Toast了。
    
也许会有个疑问， 这不是违反了 要在主线程更新UI的基本法则么？
    
实际上，view 并不是只能在主线程更新。
view的线程检查是在ViewRootImpl里面的checkThread来进行的。
源码看下图。
ViewRootImpl的线程检查并没有要求一定要在主线程当中。

而是要求checkThread和 创建ViewRootImpl必须要同一个线程当中。

所以 弹出toast并没有报错误。

```









