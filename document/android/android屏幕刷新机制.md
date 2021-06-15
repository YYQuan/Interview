



# 屏幕刷新机制

[参考文章:屏幕刷新机制](https://www.jianshu.com/p/6958d3b11b6a)
[参考文章：屏幕刷新机制实现](https://blog.csdn.net/my_csdnboke/article/details/106685736)



## 原理分析


屏幕刷新当中，设计到3个硬件设备

- cpu : 计算好要显示的数据
- gpu: 对图形数据进行渲染
- 屏幕： 显示gpu渲染好的数据

简单来说过程如下:

1. cpu 计算好屏幕数据

2. gpu进一步处理和缓存  （blockBuffer 负责接收下一帧的数据，frameBuffer是提供给屏幕的buffer,   blockBuffer和frameBuffer的角色定期交换）

3. 屏幕定期把frameBuffer的数据显示出来

   

分步骤来分析

### cpu计算

所谓的cpu计算就是 执行activity下的onMeasure, onLayout ，onDrawable 这三步。
也就是view树的绘制过程。

### gpu渲染

gpu把cpu计算出来的数据 渲染成屏幕能直接使用的数据。

这要要注意
gpu渲染出来的数据 是要存到buffer里面去的。
buffer 分成了两个部分 blockBuffer(下一帧要显示的数据) 和 frameBuffer(显示着的数据)。
正常情况下，blockBuffer和fragmeBuffer的角色是随着Vsync信号来切换的。
但是blockBuffer在接受数据的时候 是会被锁住的。
如果收到了vsync信号，但是blockBuffer由于还在接受数据，而被锁住的话。
那么blockBuffer和FragmentBuffer就不会切换， 
屏幕就仍然使用上一帧的数据来处理。
这次就会产生卡顿。

所以说cpu 当前处理的绘制  和gpu的渲染的处理的都是blockBuffer的数据， 也就是下一帧的数据。
**并不是当前正在显示着的数据**

### 屏幕显示

就是把fragmeBuffer的数据显示出来。



### Vsync信号

前面提到了 blockBuffer和frameBuffer是根据vsync信号来同步进行切换的。
这个vsync从哪里来的呢？
是底层定期发出的。
也是60帧的底层控制。

底层的vsync信号 app层是控制不了的。
屏幕就根据vsync去切换画面。

实际上 app层控制的是cpu请求计算下一帧数据的动作。
本质是在 messageQueue中插入了一个同步屏障。
去等待vsync（也就是对vsync进行监听）
收到vsync信号之后， 就去执行view树的测绘。

如下图：
第一个vsync信号的时候，cpu开始测绘 1 帧的数据
gpu渲染的也是 1帧的数据。
第二个vsync信号来的时候，切换了block和frameBuffer  屏幕显示的是 1帧的数据.
cpu和gpu开始处理 2帧的数据。
假如说 这个1 帧的数据 在第2个 vsync信号来之前没处理完的话，那么屏幕就会仍然显示 0 帧的数据。

![img](https://upload-images.jianshu.io/upload_images/5815865-a449fb41ae9105f8.png?imageMogr2/auto-orient/strip|imageView2/2/w/1197/format/webp)



### 双缓冲机制

上面说到了 屏幕拿的是  frameBuffer的 数据。
cpu和gpu是给 blockBuffer写的数据。
这个就是 屏幕的双缓冲机制。

为啥要这样设计呢？
如果没有双缓冲的话，那么如果vsync信号一来就把缓冲数据显示出来。
由于只有一个缓冲， 如果cpu和gpu处理的慢的话，那缓冲里面可能有一半是这一帧的数据，一半是上一帧的数据。
这问题就大了。
所以才使用了双缓冲的机制来进行处理。

注意这个  双缓冲一般是gpu来进行管理。
和cpu没有关系。





## Android cpu 绘制流程

可以从熟悉的requestLayout（）开始分析。
分析ViewRootImpl的reqeustLayout ()   ViewRootImpl是最顶层的view。



![image-20210526113645778](https://i.loli.net/2021/05/26/Az4O38fQ1PiJavy.png)



![image-20210526115240966](https://i.loli.net/2021/05/26/9Q57JpsLPYd3iBx.png)

![image-20210526120033736](https://i.loli.net/2021/05/26/6KSfCDjNIx23Er8.png)

![image-20210526121303070](https://i.loli.net/2021/05/26/4hqOrExfmoWpBe6.png)



从上面的分析，还没有看出 同步屏障和 vsync 有关联。
只能看出 handler里面 最优先处理 同步屏障的消息。从而触发到屏幕刷新。
并没有提现出 是由vsync信号来触发绘制。

刚才对Choreographer的分析有点问题。
常规应该是走 scheeduleFrameLocked()。
从下面分析就能看出 刷新是由vsync来触发的。
收到 vsync消息之后  再往msgQueue里面 发送异步消息。
这样就能一定程度上保证 绘制是由vsync来触发的了。
但是并不严格。

![image-20210526141607945](https://i.loli.net/2021/05/26/x5uTizdWVp9cGDy.png)



vsync的信号的回调里  再去执行  传入的runnable

![image-20210526142925948](https://i.loli.net/2021/05/26/XN6ZCAKiGjv27R3.png)

通过这种方式 就实现了 每一帧的绘制都由vsync来触发。
所以只要保证在16.6ms内 完成了绘制，就能保证不卡顿。

但是如果说在一个vsync周期里面有大量的 刷新请求 ，就可能 gpu的blockBuffer一直在锁定。因为一直有界面刷新的消息。









## QA

### Q:android 每个16.6ms刷新一次屏幕是指每隔16.6ms调用一次 onDraw()来绘制吗？画面一直不变 还会调用onDraw吗?

不是的。
app并不是一直再监听底层了vsync信号的。
而是当有屏幕刷新的请求的时候，比如调用了requestLayout(),invalidate()等的时候，
才会监听vsync信号。在msgQueue中插入 同步屏障,然后快速的处理屏幕刷新请求。

在处理屏幕刷新请求的时候  cpu才会执行到onDraw等测绘函数。

所以说 是否调用onDraw()是由 app层是否有刷新屏幕的请求来决定的，并不是定期回调的。



### Q： activity的测绘完成之后，屏幕就会马上刷新吗？

不会的。
activity测绘完成后，还要等待gpu把数据写入blockbuffer中，接着等待下一个vsync信号。这是屏幕才会刷新。

### Q：主线程的耗时操作会导致丢帧，但是耗时的操作为什么会导致丢帧呢？它是如何导致丢帧发生的？

android的屏幕刷新是由handler机制来驱动的。
虽然android加入了同步屏障来保证优先执行屏幕刷新工作，但是如果说在同步屏障来之前，就已经在执行了一个耗时的主线程的任务的时候，那么主线程就一直在处理这个耗时的任务。
handler都没有到msgqueue拿msg这步， 同步屏障就没有什么效果了。
所以主线程就一直再做这个耗时任务，根本就没有执行到绘制任务。
自然 gpu里的frameBuffer就接着用上一帧。



### Q：如果一个vsync周期之内，有大量的各个子view的刷新消息 是不是就会导致cpu计算太久而产生卡顿？

从前面的绘制流程分析当中，可以看到。
一次的绘制请求 就会有一个消息屏障。
一次消息屏障只处理一个异步消息。

如果在一个vsync周期里面有大量的 绘制请求的话，那么cpu一直在测绘， gpu的blockBuffer就一直在锁定着，就不会被输出到屏幕当中。
所以 大量的view的刷新消息 是会导致 卡顿的。

甚至会导致anr。

### Q：一个vsync周期内 ，有大量的刷新请求，这些请求是全都被优先执行？还是只执行一个？

一次刷新请求就会有一个同步屏障，一个同步屏障就会优先执行一次屏幕绘制。
同步屏障会插入到消息队列的最前端。
所以说，大量的刷新请求 在一个个的同步屏障的作用下全部都会得到优先执行。



# Android View的绘制流程





前面已经讲到了 系统的刷新机制。
接下来讲讲vsync触发的cpu的绘制流程。








