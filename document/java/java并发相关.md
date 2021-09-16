# Java并发相关

## 能力模型

![image-20201118234403705](https://voice-static.oss-accelerate.aliyuncs.com/9eac0bf1ca21c9004a65ead293191b8a.png)

![image-20210319033230054](https://i.loli.net/2021/03/19/AsGj3HmMaTewQ8P.png)



## 线程

linux中的解释是  线程是 轻量级的进程
这个轻量级提现在哪里呢？
体现在线程是不拥有资源的，只负责计算。

所以线程比进程要轻量级。

那为啥会有线程专属的资源呢？
比如ThreadLocal.
这个ThreadLocal本身是进程的资源，对于该进程下的所有线程都是可见的。
但是由于线程拿资源的时候是以自己这个线程作为Key去拿资源的 所以才会看起来像是线程专属资源。

### 线程的切换



线程的切换叫做  context  switch



上下文 是啥？ 是指cpu的上下文， 那cpu的上下文是啥呢？
cpu的上下文 就只有寄存器的值。



context  switch 流程图

![image-20210319044130888](https://i.loli.net/2021/03/19/x6c3UtLfhQnSojO.png)

### 线程的状态

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

### Java中的线程池共有几种？

四种：

- newCachedThreadPool: 不固定线程数量的线程池
-   newFixedThreadPool:
  一个固定线程数量的线程池
- newSingleThreadExecutor
  只有一个线程的 线程池，其他任务进入阻塞队列中等待
- newScheduledThreadPool
  支持定时以指定周期循环执行任务

周期的线程池和非周期的线程是 两种父类的实现。



### 线程池原理

todo



### java的线程是用户级的还是内核级的？

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











## Synchronize的本质/原理



wait / notify  是 object 提供的方法。
为啥object用着这种对线程状态操作的能力。
因为object对象都拥有者monitor 变量（C++层的变量）。

抢占锁实际上就是抢占 monitor的onwer变量。

monitor中  维护了保存这线程的两个集合 ：EntrySet,WaitSet。

EntrySet ：entry中的头元素 会去竞争 cpu时间片，其他线程都休眠；
WaitSet：  线程都在休眠 

Object的notify 实际就是把waitSet的元素移到EntrySet。



sychronize：实际上就是操作 object 对象的 monitor变量。

#### 锁的升级

锁的等级
偏向锁（java层） -》 轻量锁（java层） -》 重量锁（操作系统层）

##### 轻量级锁 -》 重量级锁

为啥要有轻量级锁

**Q:轻量级锁解决的是什么问题？**
A:

1. 减少无效的自旋操作占用CPU的时间片。
   线程在自旋一定次数之后，如果没有拿到锁的话，那么就直接休眠。不再继续自旋。
2. 减少未持有锁的线程的数目 在cpu上竞争时间片。没持有锁的线程 ，就算竞争到锁了锁，也没用。还不如增加持有锁的线程竞争到时间片的概率。

**轻量级锁是怎么实现的？**

其实就是minitor里面维护着一个集合EntrySet.
当竞争这个minitor锁失败的线程就会加入到这个集合当中。 在这个集合当中除了头部元素之外的其他线程都休眠。
这样就可以达到 前面说的两点。
减少拿不到锁的线程持有的cpu时间片以及增加拿着锁的线程竞争到实现片的几率。

![image-20210324155331843](https://i.loli.net/2021/03/24/wcXFfJAZqrEKoa7.png)



#### 偏向锁 -》 轻量锁

前提：是先有了轻量锁 才设计了偏向锁。

为啥要有偏向锁？
连CAS 的自旋的开销都不想消耗。

参考：https://blog.csdn.net/weixin_29798865/article/details/114217519

monitor当中有一个偏向标记位。
其实理解成一个 是否单线程访问的标记位即可。

如果有两个或者以上线程尝试访问这个monitor的话，那么偏向标记为就没有意义了。
偏向标记位的线程要升级成轻量级锁，和新的来访问的线程 一起进入到monitor的entrySet集合当中。

此时在entrySet中线程持有的锁升级成了轻量级锁，轻量级锁 用 Cas 去做自循环尝试获取锁。
并不涉及到操作系统的线程的阻塞。

直到满足某一条件的时候， 轻量级锁 升级成重量级锁，这样才会真正的通过操作系统的线程互斥来阻塞线程。



Q： 既然各种锁不是共存的。那么非公平锁是怎么实现的？
A:  公平有两个角度的公平， 一个是先到先得。 一个是大家公平竞争。无论是什么时候请求的，都和大家一样抢。
     轻量级锁的实现就是先到先得。
   todo





Q: 轻量级锁已经可以保证数据的同步了为啥还需要有重量级锁呢？
A：
[参考](https://www.bilibili.com/read/cv5161315/)
轻量级锁通过 等待队列就已经避免了大多数的线程的自旋的开销，开销已经是比较小了哇。

感觉参考文章里说的不太对。
核心应该是如果只有轻量级锁的话， 那么获取锁的顺序就固定了。
就是队列的顺序
轻量级锁升级为重量级锁的条件，有资料这样说 entryList的头元素在不断地自旋，当自旋的次数超过的阈值。
可在jvm参数中配置。就会膨胀成重量级锁。



感觉还是挺靠谱的，特别是都指出了配置参数。

所以轻量级锁膨胀的关键不是数量，而是等待的时长。





ps:

monitor的源码看起来 entryList是一个双向链表

![image-20210916211449887](https://i.loli.net/2021/09/16/L6TXHfmFbxhkaWn.png)

[monitor源码](http://hg.openjdk.java.net/jdk8u/jdk8u/hotspot/file/095e60e7fc8c/src/share/vm/runtime/objectMonitor.hpp)



#### 为啥重量级锁， 操作系统 的互斥消耗大？



操作系统中， 线程持有锁 就会进入用户态，其他的竞争着锁的线程就会进入内核态，阻塞住线程。
所以每一次锁的竞争都会有 该锁的竞争线程状态切换。
而轻量级锁只需要对 锁对象的monitor 的状态位进行标志即可。











## AQS的本质和理解



AQS 是java并发处理的半壁江山
另一半是 Synchronize . 剩下的小部分是高端用法，先不管。



为啥要有AQS，synchronize 不够用吗？
主要原因是 synchronize 的用法不够灵活。
 想AQS 还提供了 超时获取， 非阻塞获取，主动中等等操作。

另外做程序处理的时候synchronize是对接操作系统的



### AQS 原理

AQS 原理就是 在竞争锁前 加了一个 队列。然后通过这个队列去竞争锁。

1. 先尝试自旋获取锁

2. 如果获取不到就 休眠该线程，然后进入队列

3. 等待其他线程把锁释放，唤醒队列的头指针处的线程，再自旋

   

![image-20210329144254124](https://i.loli.net/2021/03/29/Sayi4Yt821hQVdM.png)

这个队列 头才会进入到临界区， 才会得到执行。
这样来保证数据的同步。

参考一下 ReentrantLock.FairSync当中的获取锁的实现。

```java
   protected final boolean tryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) {
                if (!hasQueuedPredecessors() &&
                    compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }
            else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0)
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            return false;
        }
    }

```





![image-20210329150640696](https://i.loli.net/2021/03/29/aZyc1sQAXUoB2kl.png)



这里可以看出来 ，AQS 并不是真正的去让线程去操作系统竞争互斥锁。而是通过CAS 去操作状态位 来达到 **‘锁’**的目的的。
当然 这个只是ReentranLock的实现。 AQS的tryAcquire 是开放出来给用户自定义的。

从这可以看出来，对于AQS  来说，只要实现 tryAcquire  和 tryRelease 就能完成 线程同步的能力。 

AQS 内部维护这CLH 队列。 
简单的来说 ，CLH队列 就是 高效的插入 删除队列。
删除、 插入 ，都只用一个cas 指令即可（分别处理  头指针和尾部指针）。
CLH队列是怎么实现高效的呢？
CLH队列通过尾插法 来实现的。

### CLH队列

```
尾插法

head                tail
node1  -> node2 -> node3

插入一个node4
有tail指针，  直接让 tail .next 指向node4 ，然后 tail 指向 node4即可。
只需要要有 tail 指向新的 node这一步  原子操作。
head                       tail
node1  -> node2 -> node3 ->node4

删除一个节点 node1
有head指针， 直接把 head 指向 head.next这一个原子操作即可。
head             tail
node2 -> node3 ->node4


```

```
头插法

前面说了 尾插法， 现在对比一下头插法。


head                tail
node1  -> node2 -> node3

插入一个node4
node4.next指向node1 
head 指向node4  只需要这一步原子操作。
head                        tail
node4 -> node1  -> node2 -> node3

删除一个node
在单向链表的情况下， 得先遍历到 node2 ，然后再把node2.next 置为null
tail 指向node2 (cas 操作)
这里就得需要先遍历链表了 。

如果这里用 双向链表呢？ 用双向链表就不需要遍历了。
但是多操作了一个指针。node指针不是Cas 操作，而是volitail

head                        tail
node4 -> node1  -> node2 -> node3


双指针的话 ，那也ok哇。
实际上 AQS 队列的节点 是双向的， 但是还是使用的尾插法。
这里没看出 头插法的优势。
```



原子操作 改变一个地址的值 ，所以得保证只能有一个指向的变化

![image-20210729175035551](https://i.loli.net/2021/07/29/THGecXlIYhmJoz6.png)

![image-20210729175444011](https://i.loli.net/2021/07/29/z6ncDWLFM4qfeNi.png)



tips: AQS 和synchronize都是由偏向设计的， 不过 AQS 可以手动设置， synchronize是只能开启偏向。synchronize 是通过monitor的偏向为进行判断，AQS 则是纯java层的逻辑判断。





#### AQS的原理总结 ： 

```
AQS 本质就是 维护着一个队列 配合cas指令来实现的纯java层的同步器。
AQS 只让队列头节点 在运行态，其他节点都休眠 来避免资源的抢占。
AQS的意图就是把 synchronize的对操作系统的锁的竞争 转成了 对 队列头的竞争。

AQS维护的队列  专属名称就是 CLH队列。
CLH队列本质就是一个尾进头出的队列，配合CAS 操作达到高效的同步队列的。

线程在尝试通过AQS 获取锁时 ，先会自旋尝试拿锁，要是拿不到的话，就会进入CLH队列当中，然后休眠。
等到锁被释放的时候， 当前的线程就会在CLH的队列中找到下一个节点，自旋判断当前节点的状态位是否已经释放了锁，如果释放了锁，那么就会把下一个节点给唤醒。
此时被唤醒的节点就会再次自旋的去拿锁。


```



















## AQS和synchronize的区别



synchronize的锁的能力 是基于c++层的monitor的变量的维护。
synchronize的锁 有升级的过程，AQS也有锁的能力 ，但只是自旋  -》 休眠 -》 自旋，并没有到操作系统的互斥。

AQS是纯Java的 不设计到 操作系统的互斥。
AQS 可以tryLock ,synchronize 没这能力
AQS 可以响应中断
AQS 可以配置公平/ 非公平



## 内存一致性



### Java中为什么会有内存不一致的情况

两个原因。

1. 各个cpu之间  有独立的缓存区域，而且 cpu的各级的缓存的速度是不一致的。
   约接近cpu的缓存越快。
2. jvm 和cpu都有可能做指令重排列， 重拍的逻辑是它判断这台指令会先被执行，而且这两条指令没有关系。



![image-20210330181151052](https://i.loli.net/2021/03/30/ulPbr9v8IGjohV6.png)

指令重排：

在 a == 0  和return a ；之间没有其他对a的操作的时候，可能jvm或者cpu就会把 a的读值 的操作先与 a ==0 来执行。

在cpu1 读了a的值之后，另一个cpu 把a 置为1，cpu1 在去判断a == 0;
这时候就会出现， 虽然程序返回a 返回的是0. 但是实际上 a ==0 这段代码被跳过了。 这就搞的很莫名其妙了， 搞的程序好像不可靠了。

![image-20210330181756960](https://i.loli.net/2021/03/30/TMZcWlaGA9wINm8.png)



### 怎么解决内存一致性问题 --Volatile

java 给的解决方案  volatile 关键字。



volatile的核心是 要有 happen -before 机制。
happen-before机制是指  a 若 happen -before b的话， a 发生的变化 ，b 处能够观察到。



volatail 并不是统一的方案 ，不同的cpu上的策略会不同。
策略主要针对的是 volatile 的读取 怎么读，一般都是要等待写同步完成后，再到公共的存储区域（比如L3  或者内存）

为了实现happen-before 机制，volatile 需要有如下功能。
volatile 做了啥

1.   禁止 volatile 那块代码 指令重排
2. 增加读写屏障 ，保证写入的值要可以马上同步到各级内存当中
3.  volatile的操作  添加happen-before关系， 保证程序前后的可见性





volatile 使用场景



```
public class Demo {
  // 这里 volatile 的作用 
  // demo =  new Demo(); 这一句
  // demo  和 new  Demo 这两个操作是没有严格的  happen-before关系的。
  // 所以可能出现 demo被赋值了 ，但是 指向的地址的 new Demo 还没创建， 这时线程可能被中断了。 就有可能出现  demo !=null了，但是 引用的地址还是没数据的情况。
  static volatile Demo demo;
  
  public Demo getInstance(){

	if(demo == null){
	
		sychronize(Demo.class){
			
			if(demo == null){
				demo =  new Demo();
			}
		}
	}
  	return demo;
  }
  
  private Demo(){
  
  }

}
```





### AtomicRefrence

volatile 是约束条件比较强，编译看到volatile之后，volatile周围的包括上下的顺序都是不可以重排的。 但是 实际上要解决内存一致性问题的时候，有时不需要那么严格的约束， 一般只要保证  volatile上文和下文的相对不重排就可以了（volatile 上部的内部可以重排， 下部的内部可以重排）。
所以java9 新给了一个方案：

```
AtmoicRefrence<Demo>  instance = new AtomicRefrence();

// getAcquire setRelease 就是表明 happen -before 的关系
// getAcquire 前的代码 会在 getAcquire 前执行 但是前面的代码的内部是可以重排序的
// setRelease 后的代码 会在 setRelease 后执行 但是后面的代码的内部是可以重排序的
instance.getAcquire()
instance.setRelease(new Demo());
```



### 怎么解决线程安全 

三个思路：

1. 用java提供的线程安全的 数据接口  各种 concurrent
2. 用ThreadLocal ，各个线程独立一份数据
3. 自己加锁 用aqs 或者synchronize都行

## 阻塞队列数据结构原理分析



阻塞队列 常见应用场景。

线程池。

![image-20210331142621719](https://i.loli.net/2021/03/31/1YMZ9yKhrFBOzmn.png)

![image-20210331142554482](https://i.loli.net/2021/03/31/olNJ24BFTznMKxL.png)



线程池的设计中有一个难点。
那就是怎么维护非核心线程的数量。

传统的方式是
生产者有足够空间就生产
消费者有足够元素就消费

![image-20210331143108978](https://i.loli.net/2021/03/31/1DEsM7UGqZ8JYfd.png)

而 现在的普遍的实现方案 
是下面这两个 （队列或者栈）

![image-20210331151901999](https://i.loli.net/2021/03/31/OWtYLTn4mb8okRE.png)



和传统的实现有什么区别呢？
传统的方式是不能做到消费者反向的刺激生产者进行生产的。

优化的方式是：
先是 生产者 生产生产 生产都一定程度之后，再开始消费。
消费 消费消费， 把产品全部消费完了之后，不要急着生产，而是把消费者积累到一定程度之后，再开始生产。

对应到线程池业务的话就是

先在queue中 ，创建 N 个线程， 然后  这个线程一个个的被拿出去用。
用完了就还回来。
这时突然 需要大量线程， 当时queue中没有那么多， 被用完了还不够。
queue中的请求线程的节点 过了某个阈值， 才开始 new新线程。
此时 一个执行完的线程 返回到队列当中， 也可以继续进行消费。



[阻塞队列参考](https://www.cnblogs.com/libin6505/p/12850830.html)

- SychronousQueue 得等添加的元素被消费后 才能添加新元素 ，在线程池里一般用于无非核心线程数量限制的场景
- ArrayBlockingQueue，阻塞数组队列  ,在线程池一般用于 在队列满了之后，在创建新线程的场景
- LinkedBlockingQueue， 阻塞队列没有长度限制， 一般用于 没有非核心线程的线程池中

### ArrayBlockingQueue和SynchronousQueue 的工作原理一样吗？

ArrayBlockQueue 和SynchroniousQueue的原理是不一样的
ArrayBolckQueue 只能实现 传统的生产者消费者关系，是没有支持 消费者反向增压生产者的。
而SynchroniousQueu 就实现了  offer/ take 这种消费者反向刺激生产者的能力。



### SynchroniousQueue和LinkedTransferQueued的区别

功能是一样的。
但是 api接口不一致。
LinkedTransferQueue有tryTransfer 接口， synchronious 没有。

另外 synchroniousQueue支持用stack的方式来支持  非公平实现，
而LinkedTransferQueue 只支持 功能的先进先出 这种实现。





### LinkedBlockingQueue也是双向队列和 LinkedTransferQueue 、synchronizeQueue这中双向队列有什么区别？

最主要的区别是 虽然是双向队列， 但是LinkedBlockingQueue 两端加个元素是一样的 没有状态的区别，而LinkedBlockingQueue / synchronioutQueue两端加的是有状态区别的， 正是由于有状态的区别，才能够有消费者反向增压生产者的能力。



### 以android的线程池为例子

![image-20210916223903175](https://i.loli.net/2021/09/16/RJ8QA9Z7gNqcIeY.png)



![image-20210916224007552](https://i.loli.net/2021/09/16/4vk7NPM8VLz2SGO.png)

![image-20210916224123455](https://i.loli.net/2021/09/16/MlT5Xseda1GFygP.png)
