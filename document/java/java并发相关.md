# Java并发相关

## 什么是线程池？为什么要使用线程池？

线程池就是事先将多个线程对象放入一个容器当中，使用线程的时候 就不需要new一个Thread了。
这样带来的好处是方便管理线程以及减少了开辟新线程的时间。提高了运行速度。

## Java中的线程池共有几种？

四种：

- newCachedThreadPool: 不固定线程数量的线程池
-   newFixedThreadPool:
  一个固定线程数量的线程池
- newSingleThreadExecutor
  只有一个线程的 线程池，其他任务进入阻塞队列中等待
- newScheduledThreadPool
  支持定时以指定周期循环执行任务

周期的线程池和非周期的线程是 两种父类的实现。



## 线程池原理

























