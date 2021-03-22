# Java并发相关

## 能力模型

![image-20201118234403705](https://voice-static.oss-accelerate.aliyuncs.com/9eac0bf1ca21c9004a65ead293191b8a.png)

![image-20210319033230054](https://i.loli.net/2021/03/19/AsGj3HmMaTewQ8P.png)



# 线程

linux中的解释是  线程是 轻量级的进程
这个轻量级提现在哪里呢？
体现在线程是不拥有资源的，只负责计算。

所以线程比进程要轻量级。

那为啥会有线程专属的资源呢？
比如ThreadLocal.
这个ThreadLocal本身是进程的资源，对于该进程下的所有线程都是可见的。
但是由于线程拿资源的时候是以自己这个线程作为Key去拿资源的 所以才会看起来像是线程专属资源。

## 线程的切换



线程的切换叫做  context  switch



上下文 是啥？ 是指cpu的上下文， 那cpu的上下文是啥呢？
cpu的上下文 就只有寄存器的值。



context  switch 流程图

![image-20210319044130888](https://i.loli.net/2021/03/19/x6c3UtLfhQnSojO.png)

## 线程的状态

操作系统的线程状态

- 运行态

- 就绪态：等待CPU 分配时间片

- 休眠 ： 阻塞态是休眠的一种情况

  下图： 线程要请求磁盘数据的时候 就会先把自己休眠， 等待请求好磁盘数据之后， 再由系统中断唤醒。
  ![image-20210319041815203](https://i.loli.net/2021/03/19/IC6Eu7B2p9Thgco.png)

Java的线程模型的状态
![image-20210319042318732](https://i.loli.net/2021/03/19/6fBWolwNxvCIqh5.png)

- java 特有的状态 ： new和terminated 
- java中线程执行完了new之后 就会到runnable   也就对应这  排队和执行， 但是不关心到底是在排队还是在执行，关心了也没意义，影响不了操作系统
  - time_wait ： 定时的休眠  比如 sleep
  - waiting:    线程之间在相互的等待sign
  - block ：  i/o请求 或者  lock
- 其他的 wai,t time_wait，  block 都是休眠态



QA:

- Thread.join 是哪种状态
  是在wait状态 ，在等待别 的线程的signal
- Thread.sleep是那种状态 
  在time_waiting,是定时唤醒
- 网络请求
  在linux中  网络请求 都是I/O文件操作， 在网络请求的时候 线程是在休眠状态。

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

todo



## java的线程是用户级的还是内核级的？

Java是采用了用户线程 映射 到内核线程的方案的。  一般映射关系是1比1 的
总体来说线程都是内核级的，在java早期版本的时候是有用户级线程的。
但是后面被修改了，用户级线程会有很大弊端。
主要是：在多核的情况下， 内核感知不到用户级线程，导致内核只能把时间片分给进程的主线程。
这样进程 多线程下 子线程只能去竞争主线程的时间片，就不能做到并行的执行多线程任务。
发挥不了多核cpu的优势。

## CAS和原子操作

### 原子操作

原子操作是 指CPU 不可分割的一个操作

```
i= 1; //是原子操作
i++;  // 不是 i++ 是三个原子操作结合而成的，
1.读取I ，
2,i+1
3,赋值给i
```



### CAS指令

CAS 的 指定cpu以原子的方式去设置一个地址的值。
函数体

```
cas(&oldValue,expectedValued,targetValue)
```



但是 CAS 指定成了原子操作之后， 拒绝了竞争条件，但是仍然没有解决拒绝竞争条件之后，要怎么解决赋值的问题。

那要怎么解决呢？
加上个外部循环 在操作被拒绝了之后，重新读取一次，然后再来一次即可。

```
while(!cas(&i,i,i+1)){
	// 什么都不用做
}
```



所以从这里也看出为啥   原子操作只能操作基本数据类型了。



### TAS指令

TAS 可以理解成为只有 0，1的CAS 操作 

TAS 可以用来做互斥操作。
全部线程都 指定从 0-》1
先竞争到的线程可以 把 内存 从0 改成1， 处理完之后 再改回0， 
这样其他的线程才可能继续往下执行。





Tips:
不是所有的cpu都支持 cas指令的。



### ABA问题

以栈的头节点为例子


stack: visuaHhead -> node1 ->node2 -> node3

Thread1 :   读 stack head   为 node1 
Thread2:    读stack head   为 node1 
Thread1： 准备移除node1 
（期望结果 ：  visualHead -> node2 -> node3 ）
Thread2  :  准备移除node1
（期望结果 ：  visualHead -> node2 -> node3 ）

Thread1:  执行 cas 操作 把 node1 移除了
此时：stack: visualHead -> node2 ->node3

Thread3:  读 stack head 为 node2
Thread3:  把 node1-> node3  插入 stack 头中
此时： stack :visualHead -> node1 -> node3 -> node2

Thread2:  发现内存里面 的头是 node1 就继续执行cas操作
此时： statck visualHead -> node3 ->node2

实际上和Thread2 的预期并不一致。
也不是Thread3的预期



### Java解决ABA问题的方式

java 提供了  类 在原子操作的后面加上执行序号。
以原子操作的序号来判定执行是否有效， 是否该被继续执行。












