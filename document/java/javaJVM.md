# Java JVM 相关





## 堆和栈



栈是配合执行程序的内存空间，栈存贮程序执行时的临时数据  是FIFO .



堆是用于存储数据的内存空间。



tips :  程序执行 是可以没有堆的 ，但是一定要有栈。



### 栈中是否能够 存贮对象？

据说java高版本中 对于 只在函数内可见的对象，也有可能直接存储在栈中，不再存放在堆中。
所以能不能存放对象 并不是栈和堆的本质区别。
本质区别是 栈一定程序运行过程中的临时数据。



## JVM的内存布局



各个版本的JVM 不一致的。
这里只讨论 应用最广的  HotSpot 的jvm.

内存模型如下。
这里的堆  指的是java 堆 ， 也就是会有GC 的部分。

![image-20210331181800582](https://i.loli.net/2021/03/31/ldEL8D5zZgehPmb.png)

注意 堆、栈、方法区这些是 jvm 规范里概念。 其他的概念并不是统一的。想一些jvm有永久代的概念， 是在堆中开辟也一块内存在存类的信息，代码 ，常量 ，但是hotspot中已经把永久代这个概念给移除了。
替换的原因是 由于永久代是在GC heap中的 会参与gc 影响到gc的效率，因此就移除了。

Tips:
HotSpot的元空间存储在哪里？
HotSpot 存放在  本地内存当中， 而不是JVM的虚拟机内存当中。
所以HotSpot的元空间是不参与虚拟机的GC计算的。



Q： native  对象存放在 堆中吗？
A： native对象存放在native堆中 ，不存在GC 堆 中。是两块区域。





Java整体架构

![image-20210401091303507](https://i.loli.net/2021/04/01/OqXHDT8ykBduiKs.png)



 要点：
classLoader在Method Area  中加载class.
JIT 编译器 是边翻译 边执行， 翻译过的指令不再进行翻译。 这是JIT 高效的原因。java  运行起来之后， 才能知道当前是什么cpu 才能选择最合适的cpu指令。

JIT 基本是java的标准方案， 用边翻译边执行的方式才能解决跨平台的问题。
向C，go这些都是先编译， 再执行。因此得对每一种芯片都出对应的版本。





## GC



### 什么是STW？



STW   : STOP  THE  WORLD 

这个是gc的能力。
也就是gc 拥有停止其他线程的能力。
就算是当前有几百个线程正在执行， gc也是能要求他们全部停下来的。
但是不一定是立即停， 各线程可能会停止在所谓的安全点。

### GC的指标

#### GC的Throughput （吞吐量）


gc的throughput就是指gc 没有占用cpu的时间 也就是没有STW的时间。

简单来说就是 吞吐量高 就是gc占用的时间少。



#### GC-Latency

 GC 造成的STW时间，也就是阻塞其他线程的时间。

Q: throughput 小 latency就高吗？
A: 这种说法 是不准确的。latency 是一次阻塞操作， throughput 小，可能造成了很多次stw 但是 每次阻塞的latency都很短。  throughput大，可能只造成了一次阻塞 ，但是latency 很大，这个是有可能的。





#### FootPrint

应用对内存的需求， 也就是占有的内存的峰值

想要吞吐量大， latency 小的话  那么 footPrint  就会大。



### GC算法

#### 引用计数



引用计数有什么问题?
两个问题：

1. 无法处理  多对象之间 循环持有， 但是外部已经不再引用的情况。
2. 没有纠错机制， 万一，多线程的情况下计数发生错误，那么该错误无法被纠正的



![image-20210401142659773](https://i.loli.net/2021/04/01/W27xpG8NVdyaPRs.png)

所以现在都不再使用 引用计数算法了。
代替的是 可达性分析算法。

#### 可达性分析算法

jvm中 维护着根集合 ，也就是可达性的起点。起点都有啥呢？
包括 方法区中的常量 ， 静态变量的引用， 栈中的引用。
从根出发， 如果能发现的对象就 标记成一种状态。
gc 根据这个状态来决定是否回收。

可达性算法就能够解决 引用计数的 循环依赖的问题。



tips
可达性分析算法当中   从根出发更新状态时， 要对环进行处理。



tips：
gc 是要分阶段的， 首先是要标记。
然后是 清除。
清除时还需要区分 该对象是否重写了finalize 如果没有重写的话，就可以直接回收。
重写了的话，就要延迟到 其finalize执行完之后再回收。





#### 三色标记





为啥需要三色 标记？
gc 不就只有 mark - sweep 两步么？  那两色标记就可以了哇。

其实实际场景中，gc mark了对象A后， 这时候如果另一个线程把对象A给改变了，那么之前的mark 可能就不准了。那么 这种mark后被改变对象 就需要被重新mark 才能保证 状态准确。 这个行为叫做remark

被标记成remark的对象 可以理解为 gc当中 一个不在跟集合里的跟。
也就是需要重新进行mark.



#### GC sweep 中 如何定位待回收对象



待回收对象 不是已经没有引用指向它了么？
gc怎么找到他呢？
java给gc 提供了遍历heap中所有对象的能力。 这个能力怎么来的先不管。



#### GC 赋值 整理  生代算法



java的堆中分成了几个区域。

新生代 ， 存活代 ，老年代。

老的java版本中还有 永久代的概念，用来存放常量  静态变量和class等，不过现在已经被移除了。



![image-20210402071435977](https://i.loli.net/2021/04/02/KHNpfuiZIkmb8Lc.png)


GC 时  会对堆中的各个部分的数据进行整理 然后进行生代。
从而整理出 更大的连续的内存。

但是这里存活代比较特殊，存活代有两个。
这样设计的好处，目的是 使得 老年代是更稳定的 存活期更长的对象。

另外，生代算法中还做了 一些优化， 对于存活代的两个部分。并不是指定某个的更加年长一些的。
而是在其中一个把数据copy到老年代之后， 另一个就成了年长些的 存活代。也就是下一个要生代的老年代的区域。

### GC策略

有哪些GC策略？
G1 ， CMS  , Serial, Parallel  ,Z  .....

简单分析一下 各个GC 策略的区别  以及 适合的场景。



#### Serial 

被废弃了

特点

- 单线程

-  STW  ： gc完了之后 其他线程才会继续执行
  tips :类似于 单线程 工作， 所以 对于SerialGC策略来说，可达性分析可以 用双色 标记法即可， 没必要三色标记。 
- 没有分代  也就是没有  compact (整理)  copy （复制）

#### Parallel

被废弃了

- 多线程
- 提供最大的吞吐量， 但是实际上 吞吐量大 和快不是绝对正比的。
  毕竟多线程 时 还是需要切换线程的开销的。
- STW 和Serial一样   一GC 就停掉别的线程
- 也是没有分代， 

#### CMS( ConcurrentMark Sweep)

GC的经典策略

- 多线程

- 是Concurrent的  也就是 GC和 其他线程可以 穿插着来处理。
- Mark 阶段 使用三色标记法进行，  是STW的
- 除了mark操作 其他GC的操作都是STW的
- 支持增量模式，  也就是可以约定每次mark占的工作量的比例



Q： 为啥在非mark的阶段可以不阻塞线程？

A： 因为在sweep阶段如果发生了remark，那么就只能是被标记成可达状态的对象被 置为了不可达的状态。这时 会产生浮动垃圾。可以等待下次垃圾的回收。不会对程序执行产生影响。
	 并不会存在 不可达的对象 变成可达的情况， 本来他都不可达了，除了GC 没其他人看得到他，没法把他变成可达的。所以GC 清除的时候不会清除掉 有用的对象， 只有可能产生浮动垃圾。

Sweep的时候 如果对象A申请了新的内存空间， 那么 就会打断gc 的sweep。
进入到对象A的remark.



Q： 那为啥mark的时候 需要阻塞线程呢？

A：原因是remark状态 可能就会被mark覆盖。 这样 可能会导致 GC的质量低下。
















#### G1 (Garbage First)

G1 算法是新的GC算法。
性能不一定比CMS好。

但是其思路很先进。

主体思路是将大内存 分成小段， 然后再每一段里面做GC。

G1的难点在于  各个小段之间 相互有引用在怎么处理。













#### ZGC

给GC 的STW 设置一个阈值。 从而达到低时延的目的。
但是不稳定， 可能会蹦。





## Java对象的生命周期



- **加载** ： 也就是类加载器去加载class文件 ，一般发生在 静态 成员被访问、new了新对象 。 执行了 静态代码块 和 静态成员的初始化。

- **创建**：在内存中分配了内存空间，执行了构造方法

- **存活**：就是创建完成后的状态。但是存活内部也是分状态的

  - 正在使用中的

  - 可达，但是没有在使用的
    比如

          ```
    void main(){
    	
    	ClassA cA   = new ClassA();
    	.....
    	
    	....
    	//cA不再被使用 这时内存中有cA 但是cA不会再被访问了。
    	
    	// 把cA 置空， 虽然 main函数还没有执行完，但是cA的对象的实例就变为不可达了。
    	// GC 就能回收它的了。 
    	cA = null;
    
    
    
    
    }
          ```

     

  - 不可达的， 但是还没被回收的

- 回收阶段 - GC

  -  回收标记阶段 -  mark
  - 在sweep前 如果对象重写了finalize 的，那么就先执行finalize
  - 回收内存















## Java对象在内存中的结构



可以简要的说
主要分为 对象头  对象实例数据， 以及8字节补齐。

对象头中 主要有mark  word。
其中主要是 锁的标志，配合锁的 monitor的地址 以及  gc的标志

![image-20210406093133036](https://i.loli.net/2021/04/06/u21xeQTKGUi4nRp.png)



**ps**

Q:一个空对象 栈多少字节？
A: 对象头就占8个字节 ，加上 补齐 最少也是16个字节。复杂的数据 比如 数组  可能就占24个。 如果关闭了压缩的话， 那么可能会变多，但是最少是占16个字节。





## ClassLoader双亲委派模式





### ClassLoader

运行时 ，加载类到JVM当中。

实际上 classLoader只做一件事情， 那就是把.class文件 转化成为 二进制， 并加载到内存当中。

![image-20210406104536574](https://i.loli.net/2021/04/06/PhrxIaTnGZ4MsEp.png)

### 只有一个classLoader 够用吗？

- 数据来源 不同， 缓存策略不同..
  class的来源不同，也就需要classLoader处理的数据源不一样。

  - 从文件

  - 从网络

  - 从其他库

  - 从内存

    针对数据源的不一样， classLoader需要做的处理也不一样。但是目的都是得到执行用的二进制码。

- 版本问题
  如果一个虚拟机里，有多个服务，这些服务都依赖于某一个class。
  但是他们的版本不一样。
  那么我们就可以用 不同的classLoader去加载不同的版本。从而在不同的服务内， 用不同的版本去处理
  这个是 java处理 库不同版本的重要方式。

  ![image-20210406105756111](https://i.loli.net/2021/04/06/vefrlQB4SR16nh5.png)

- 需要公用一部分类的情况
     委托给父类加载。
     父类加载的类 子类都能够使用。
    这样可以避免 多次无意义的多次加载。



从这三点触发， 所以classLoader应该具有如下的特性

- 具有树状关系， （具有父子关系）
- 拥有委托特性

因此 java设计了 双亲委派模型。

**PS**：双亲委派名字有点误导， 实际上就是 父亲委派。就是委派给父节点。





### 自定义一个ClassLoader



```java
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

public class TestClassLoader {


    public static void main(String[] args) throws InterruptedException {
        MyClassLoader myClassLoader = new MyClassLoader();
        Class mClass = null;
        try {

            // 調用多次 也只會调用到一次findClass
            // 缓存和双亲委派  内部已经实现好了。
            mClass = myClassLoader.loadClass("YYQTest");
            mClass = myClassLoader.loadClass("YYQTest");
            mClass = myClassLoader.loadClass("YYQTest");
            mClass = myClassLoader.loadClass("YYQTest");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("123456");
        try {
            try {
                Object  o = mClass.getConstructor().newInstance();
                o.getClass().getMethod("testMethod").invoke(o);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public  static byte[] genByte(){
        ClassPool  pool = ClassPool.getDefault();
//        pool.getOrNull()

        CtClass clazz =pool.makeClass("YYQTest");
        CtMethod method = new CtMethod(CtClass.voidType,"testMethod",new CtClass[]{},clazz);
        method.setModifiers(Modifier.PUBLIC);
        try {
            method.setBody("System.out.println(\"123456789\");");
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        try {
            clazz.addMethod(method);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        try {
            return clazz.toBytecode();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        return  null;
    }

    static class MyClassLoader extends  ClassLoader{
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            System.out.println("hello");
            // 不用考虑 先调用 super 还是先 判断类名。
			// findClass 只会调用进来一次。
            if(name.equals("YYQTest")){
                byte[] bytes = genByte();
                return  defineClass(name,bytes,0,bytes.length);
            }
            return super.findClass(name);
        }
    }
}

```



### 如何打破双亲委派？

复写classLoader的loadClass即可。









