# AMS核心原理解析



## android的启动



android 设备上电之后 做了什么呢？

### android 系统的启动流程



起点就是bootLoader。
上电后
启动bootloader.

也就是嵌入式设备的那一套。

流程如下

- 启动bootloader

- bootloader启动linux内核

- linux内核 找到 initRC脚本，执行init ,启动android系统

- android系统启动zygone

- zygone启动 systemService

- zygone启动launcher

  





![image-20210513214802987](https://i.loli.net/2021/05/13/18TeRwxJrDVXmqt.png)



### zygone的启动流程

前面说到了， linux内核会 执行到init 。


下面就是zygone的启动流程。
总体来说就是找到对应的脚本文件，完成启动。





![image-20210513220027801](https://i.loli.net/2021/05/13/9bqeE3KMAg2nHYR.png)



### SystemService的启动

zygone 进程启动好了之后， 就去执行软件运行的必要环境。
比如创建虚拟机等。
环境好了之后 ，就会启动SYstemServie进程。

![image-20210513220759547](https://i.loli.net/2021/05/13/E1upQmY6rcgFNOe.png)



QA:

```
Q：前面分析已经知道
init进程  fork zygone进程
zygone进程 fork  systemService

linux 的fork机制下， 子进程能够继承进程的资源。

为啥 应用进程要放在  zygone进程下进行fork？
而不是在init 进程 或者  systemservice进程下进行fork呢？


A:
init 进程都还没准备好  程序的运行环境， 如果从init进程进行孵化的话，那么要从头准备运行环境，那效率就太低了。
那为啥不从systemservice下进行孵化？
因为应用不需要 systemservice的那么多的系统服务  ，比如 ams pms, wms 不需要 每个应用都访问。
所以在systemService中fork的话  那就太臃肿了。

```



下面看下 SystemServer的源码。

![image-20210513223154063](https://i.loli.net/2021/05/13/xMK19SPD3wnAGrT.png)

![image-20210513223416289](https://i.loli.net/2021/05/13/IlQDkL32nu1ZjWa.png)



AMS 是在BootstrapServices中被调起的。
![image-20210513223740634](https://i.loli.net/2021/05/13/5zoEi92eqPamM4n.png)

![image-20210513224848556](https://i.loli.net/2021/05/13/sQODVgIvtUzM5Lw.png)

这里就 完成了ams对象的创建



tips:
launcher的启动页是通过ams来完成的。
但不是在启动ams的时候， 而是 系统必要服务都起来后， 再执行ams的执行函数， 完成launcher应用的启动的

![image-20210513231006878](https://i.loli.net/2021/05/13/W6FoVRxnG1h7wgZ.png)

## AMS

前面已经分析到ams对象的创建了。

接下来看ams 初始化是做了什么?



### AMS 的初始化



看看  ams的构造函数里干了什么？
捡看得懂的来看。







# 面试题





## system_server为什么要在Zygote中启动， 而不是由init直接启动呢？

linux中父进程 fork子进程 时， 子进程时继承了父进程的资源的 。
所以system_server由 zygote来孵化 可以直接使用 zygote加载到了系统资源。



## 为什么要专门使用Zygote进程去孵化应用进程，而不是让system_server去孵化呢？

fork 出的子进程时带着父进程的资源的。
system_server中 还带着  ams ，pms ，wms 这些系统级的服务。
对于应用来说是没用的。
没必要让子进程那么的臃肿， 在zygote进程里fork 就能继承常用的系统资源，因此直接在zygote 进程下fork是比较合适的。

另外fork机制对多线程不友好， 多线程状态下有可能会死锁。
而system_server 是多线程的。



## 多线程下 fork为啥会死锁？

根据标准， fork 的行为是  ：复制整个用户空间的数据 以及所有的系统对象， 然后仅复制当前线程到子进程当中。
也就是说 ，父进程中的除了执行fork的进程外， 到子进程时，其他的线程都是不存在的。

所以说， 在多线程状态下，如果其他线程持有着锁，但是在fork时，没有复制过来。这个锁永远无法被释放，那么fork过来的线程 就存在一直拿不到这个不存在线程持有着的锁。这就产生了死锁。



## Zygote 为什么不采用Binder机制进行IPC 通信？

binder 机制中 存在binder线程池。
而zygote 是进程的孵化器， fork在 多线程下 有可能存在死锁的情况。

但实际上虽然zygote没有使用 binder ，但是里面也是用了多线程的。
只是在fork前，把其他线程都停止了。fork完成之后 ，再恢复。 