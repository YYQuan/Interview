## 1、基础
1.1 ==与equals的区别
1.2 final的作用
1.3 操作字符串的类有哪些，区别是什么？
1.4 什么是抽象类？与普通类的区别是什么？什么是接口？与抽象类什么区别？
1.5 volatile、synchronized、atomic、内存可见、同步、原子性
1.6 大小端
1.7 Kotlin的data class、open class分别有何意义
1.8 Kotlin的协程有几种调度器？协程与线程池有什么区别？挂起任务执行结束后，协程是否释放？协程的性能与线程池相比如何？
1.9 什么是Handler？为什么要使用Handler？如何使用Handler？Handler中Looper如何创建的？Handler中多线程如何工作的？
## 2、中级
2.1 java有哪几种容器？
2.2 Collection和Collections的区别？
2.3 List、Set、Map的区别
2.4 HashMap和HashTable区别
2.5 HashMap和HashSet的实现原理
2.6 ArrayList和LinkedList的区别
2.7 创建线程有哪几种方式
2.8 sleep和wait的区别，notify和notifyAll的区别
2.9 有哪几种线程池创建方式，线程池有几种状态，submit和execute的区别
2.10 ThreadLocal
2.11 java IO流：NIO（阻塞）、BIO、AIO（同步）
## 3、高级
3.1 多线程锁升级的原理


### 性能优化专题
#### 1) 关于崩溃优化需要采集的信息:
#### 1.1、崩溃信息

- 进程名、线程名. 崩溃的进程是前台进程还是后台进程,是否发生在UI线程.
- 崩溃堆栈和类型, 属于Java崩溃、Native崩溃还是ANR   
```java
Process Name: 'com.sample.crash'
Thread Name: 'MyThread'
java.lang.NullPointerException
		at ...TestsActivity.crashInJava(TestsActivity.java:275)
```
####  1.2、系统信息

- Logcat: 包含应用、系统的运行日志,其中系统的event logcat会记录App运行的一些基本情况、记录在文件

/system/etc/event-log-tags
```java
system logcat:
10-25 17:13:47.788 21430 21430 D dalvikvm: Trying to load lib ... 
event logcat:
10-25 17:13:47.788 21430 21430 I am_on_resume_called: 生命周期
10-25 17:13:47.788 21430 21430 I am_low_memory: 系统内存不足
10-25 17:13:47.788 21430 21430 I am_destroy_activity: 销毁 Activty
10-25 17:13:47.888 21430 21430 I am_anr: ANR 以及原因
10-25 17:13:47.888 21430 21430 I am_kill: APP 被杀以及原因
```

- 机型、系统、厂商、CPU、ABI、Linux版本等, 共性问题、
- 设备状态:是否Root、是否模拟器,一些问题是由Xposed或者多开软件造成的
####  1.3、内存信息
OOM、ANR、虚拟内存耗尽等很多崩溃都跟内存有直接关系.

- ==系统剩余内存==
   - 关于系统内存状态,可读取文件/proc/meminfo. 当系统可用内存很小(低于MemTotal的10%)时,OOM、大量GC、系统频繁自杀拉起等问题都非常容易出现、
- ==应用使用内存==
   - 包括Java内存、RSS(Resident Set Size)、PSS(Proportional Set Size), 可以得出应用本身内存的占用大小和分布、PSS和RSS通过/proc/self/smap计算
- ==虚拟内存==
   - 虚拟内存可以通过/proc/self/status得到,通过/proc/self/maps文件可以得到具体的内存分布情况.许多类似OOM、tgkill等问题都是虚拟内存不足导致的
   - 一般来说，对于 32 位进程，如果是 32 位的 CPU，虚拟内存达到 3GB 就可能会引起内存申请失败的问题。如果是 64 位的 CPU，虚拟内存一般在 3～4GB 之间。当然如果我们支持 64 位进程，虚拟内存就不会成为问题。Google Play 要求 2019 年 8 月一定要支持 64 位，在国内虽然支持 64 位的设备已经在 90% 以上了，但是商店都不支持区分 CPU 架构类型发布，普及起来需要更长的时间。
```java
Name: com.sample.name // 进程名
FDSize: 800 // 当前进程申请的文件句柄个数
VmPeak: 3004628 kB // 当前进程的虚拟内存峰值大小
VmSize: 2997032 kB // 当前进程的虚拟内存大小
Threads: 600 // 当前进程包含的线程个数
```
#### 1.4、资源信息
有的时候我们会发现应用堆内存和设备内存都非常充足，还是会出现内存分配失败的情况，这跟资源泄漏可能有比较大的关系。

- ==文件句柄==
   - 文件句柄的限制可以通过/proc/self/limits获得,一般单个进程允许打开的最大文件句柄个数是1024
   - 如果文件句柄超过了800个就比较危险、需要将所有的fd已经对应的文件名输出到日志中,进一步排查是否出现了有文件或者线程的泄露
```java
opened files count 812:
0 -> /dev/null
1 -> /dev/log/main4
2 -> /dev/binder
3 -> /data/data/com.crash.sample/files/test.config
```


- ==线程数==
   - 当前线程数大小可以通过上面的status文件得到,一个线程可能占2MB的虚拟内存,过多线程会对虚拟内存和文件句柄带来压力.如果线程超过了400个就比较危险.需要将所有的线程id以及对应的线程名输出到日志中.进一步排查是否出现了线程相关的问题.
```java

 threads count 412:               
 1820 com.sample.crashsdk                         
 1844 ReferenceQueueD                                             
 1869 FinalizerDaemon   
 ...  
```

- ==JNI==
   - 使用JNI时候、如果不注意很容易出现引用失效、引用爆表等一些崩溃、通过DumpReferenceTables统计JNI的引用表、进一步分析是否出现了JNI泄露等问题



#### 1.5、应用信息

- 崩溃场景
   - 崩溃发生在哪个Activity或Fragment,发生在哪个业务中
- 关键操作路径
   - 记录关键的用户操作路径,复现崩溃会有较大的帮助
- 其他自定义信息
   - 不同的应用关注的重点可能不太一样, 比如关注的点有运行时间,是否加载了补丁,是否是全新安装或者升级等相关信息.
#### 2) 关于内存优化
内存优化一般从设备分级、Bitmap优化和内存泄漏这三个方面入手:
#### 2.1、设备分级
内存优化首先需要根据设备环境来综合考虑. 其中的误区是:内存占用越少越好.
针对这个问题可以让高端设备使用更多的内存,做到针对设备性能的好坏使用不同的内存分配和回收策略.
举个🌰:

   - 设备分级:使用类似**device-year-class** 策略对设备分级,对于低端机用户可以关闭复杂的动画、或某些功能. 使用565格式的图片.使用更小的缓存内存等. 在开发过程中,需要思考功能是否需要对低端机开启,在资源吃紧的时候能不能进行降级等策略.

          [https://github.com/facebookarchive/device-year-class](https://github.com/facebookarchive/device-year-class)
device-year-class会根据手机的内存、CPU核心数和频率等信息决定设备属于哪一个年份.
```java
if(year>=2013){
	// Do advanced animation
}else if(year>=2010){
    //Do simple animation
}else{
	// Phone is too slow ,don't show any animation
}
```


   - 缓存管理

需要有一套统一的缓存管理机制,可以适当地使用内存,当“系统内存不足的”就需要立即归还. 可以使用**OnTrimMemory**回调,根据不同的状态决定释放多少内存,当项目逐渐成型,转变为大型项目的时候,可能存在几十上百个模块,统一缓存管理可以更好的监控每个模块的缓存大小.、


   - 进程模型

一个**空的进程**也会占用10MB的内存,有些应用启动就有十几个进程,甚至有些应用已经从双进程保活升级到四进程保活,所以减少应用启动的进程数,减少常驻进程、有节操的进行保活,对低端机内存优化非常重要.


   - 安装包大小

安装包中的代码、资源、图片以及so库的体积.跟它们占用的内存有很大的关系,一个80MB的应用很难在512MB内存的手机上流畅运行,可以适当针对低端机推出4MB的轻量版本应用.![截屏2020-12-30 15.48.00.png](https://cdn.nlark.com/yuque/0/2020/png/362377/1609314588456-1371a738-90a6-4c7a-8814-157ce523274f.png#height=188&id=Yujyq&margin=%5Bobject%20Object%5D&name=%E6%88%AA%E5%B1%8F2020-12-30%2015.48.00.png&originHeight=188&originWidth=537&originalType=binary&size=29576&status=done&style=none&width=537)


#### 2.2、Bitmap优化


#### 2.3、内存泄漏


### JVM专题
[https://www.toutiao.com/i6909308345472254476/?timestamp=1608749796&app=news_article&group_id=6909308345472254476&use_new_style=1&req_id=202012240256360100270570861539C835](https://www.toutiao.com/i6909308345472254476/?timestamp=1608749796&app=news_article&group_id=6909308345472254476&use_new_style=1&req_id=202012240256360100270570861539C835)
#### 1 获取class文件有哪些方式
从本地文件系统中加载.class文件
从jar包中或者war包中加载.class文件
通过网络或者从数据库中加载.class文件
把一个Java源文件动态编译，并加载，加载进来后就，系统为这个.class文件生成一个对应的Class对象。
#### 2 生成class对象有哪些方式
1.对象获取。调用person类的父类方法getClaass();
2.类名获取。每个类型(包括基本类型和引用)都有一个静态属性，class；
3.Class类的静态方法获取。forName("字符串的类名")写全名，要带包名。(包名.类名)
#### 3 方法区、堆、栈是什么关系
# 
![](https://cdn.nlark.com/yuque/0/2020/png/904744/1608809913737-82bf8842-f75f-4560-be30-464e5a12a442.png#height=481&id=XWzuQ&originHeight=481&originWidth=854&originalType=binary&size=0&status=done&style=none&width=854)
**栈指向堆**
如果在栈帧中有一个变量，类型为引用类型，比如
```
package com.tian.my_code.test;
public class JvmCodeDemo {
    public  Object testGC(){
        int op1 = 10;
        int op2 = 3;
        Object obj = new Object();
        Object result=obj;
        return result;
    }
}
```
这时候就是典型的栈中元素obj指向堆中的Object对象，result的指向和obj的指向为同一个对象。
![](https://cdn.nlark.com/yuque/0/2020/png/904744/1608809913880-13275baf-23c8-4433-9e4e-0a46bd18cc38.png#height=531&id=WR2L4&originHeight=531&originWidth=1080&originalType=binary&size=0&status=done&style=none&width=1080)
使用命令
javac -g:vars JvmCodeDemo.java
进行编译，然后再使用
javap -v JvmCodeDemo.class >log.txt
然后打开log.txt文件
![](https://cdn.nlark.com/yuque/0/2020/png/904744/1608809913870-bdd2abae-2d13-4947-8e26-4df99bb227eb.png#height=606&id=hpT6r&originHeight=606&originWidth=1080&originalType=binary&size=0&status=done&style=none&width=1080)


**方法区指向堆**
方法区中会存放静态变量，常量等数据。
如果是下面这种情况，就是典型的方法区中元素指向堆中的对象。【**「红线」**】
![](https://cdn.nlark.com/yuque/0/2020/png/904744/1608809913721-7bbb02b0-b3d0-4255-8124-20b7829a5bea.png#height=700&id=TObwK&originHeight=700&originWidth=969&originalType=binary&size=0&status=done&style=none&width=969)


**堆指向方法区**
方法区中会包含类的信息，对象保存在堆中，创建一个对象的前提是有对应的类信息，这个类信息就在方法区中。
![](https://cdn.nlark.com/yuque/0/2020/png/904744/1608809913969-290773c2-dffb-4c4a-9097-3e2aede98642.png#height=693&id=MQCge&originHeight=693&originWidth=923&originalType=binary&size=0&status=done&style=none&width=923)
# 
#### 4 java对象在内存中如何布局
![](https://cdn.nlark.com/yuque/0/2020/png/904744/1608810012614-d914d72b-816a-4afc-b91b-1b80452e2737.png#height=691&id=cA3TH&originHeight=691&originWidth=1080&originalType=binary&size=0&status=done&style=none&width=1080)
一个Java对象在内存中包括3个部分：对象头、实例数据和对齐填充。
![](https://cdn.nlark.com/yuque/0/2020/png/904744/1608810012646-1283c527-30e4-425c-a043-64f80693c23c.png#height=397&id=yK9JL&originHeight=397&originWidth=1080&originalType=binary&size=0&status=done&style=none&width=1080)
#### 5 如何理解Minor/Major/Full GC
请说一下Minor/Major/Full GC分别发生在哪个区域
Minor GC：发生在年轻代的 GC Major GC：发生在老年代的 GC。Full GC:新生代+老年代，比如 Metaspace 区引起年轻代和老年代的回收
#### 6 触发Full GC的条件有哪些
1）调用System.gc时，系统建议执行Full GC，但是不必然执行；
2）老年代空间不足；
3）方法去空间不足；
4）通过Minor GC后进入老年代的平均大小 > 老年代的可用内存；
5）由Eden区、From Space区向To Space区复制时，对象大小大于To Space可用内存，则把该对象转存到老年代，且老年代的可用内存小于该对象大小。即老年代无法存放下新年代过度到老年代的对象的时候，会触发Full GC。


### 高级UI专题


### 算法专题




