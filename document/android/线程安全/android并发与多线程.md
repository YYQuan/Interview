# Android并发与多线程

相关内容

![image-20201104071634933](https://i.loli.net/2020/11/04/mxG4UPCDAiZdylO.png)



![img](https://img.mukewang.com/wiki/5ee6e28709ec3ec019402170.jpg)

​                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       



## 线程的几种创建方式

### 1 new Thread 

![image-20201104072752062](https://i.loli.net/2020/11/04/gY2K8yPdSusF7C3.png)

```java
//传递Runnable对象
1.new Thread(new Runnable(){
     .....
}).start()

//复写Thread#run方法
2.class MyThread extends Thread{
        public void run(){
           ...... 
         }
   }
new MyThread().start()

```



### 2 AysncTask

默认是串行执行

![image-20201104072849902](https://i.loli.net/2020/11/04/caY4EXSTLlH1hWi.png)

 由于 生命周期和宿主的生命周期不同步， 因此最好定义成 静态类， 避免持有者外部类的引用导致了 内存泄漏

```java
class MyAsyncTask extends AsyncTask<String, Integer, String> {
          private static final String TAG = "MyAsyncTask";
          @Override
          protected String doInBackground(String... params) {
              for (int i = 0; i < 10; i++) {
                  publishProgress(i * 10);
              }
              return params[0];
          }
          @Override
          protected void onPostExecute(String result) {
              Log.e(TAG, "result: " + result);
          }
          @Override
          protected void onProgressUpdate(Integer... values) {
              Log.e(TAG, "onProgressUpdate: " + values[0].intValue());
          }
      }
      
AsyncTask asyncTask = new MyAsyncTask();
//AsyncTask所有任务默认串行执行
asyncTask.execute("execute MyAsyncTask");
//可以做到任务并发执行 
asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"execute MyAsyncTask")

//使用静态调用execute方法，同样串行执行
AsyncTask.execute(new Runnable() {
        @Override
        public void run() {
         ...... 
     }
});

//使用内置THREAD_POOL_EXECUTOR线程池 并发执行
AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
         @Override
         public void run() {
              
         }
 });

```

### 3 HandlerThread



![image-20201104073421629](https://i.loli.net/2020/11/04/XlmJFZrziOYWqPT.png)



串行执行，内部就是handler的轮询



```java
HandlerThread thread = new HandlerThread("concurrent-thread");
thread.start();
ThreadHandler handler = new ThreadHandler(thread.getLooper()) {
    @Override
    public void handleMessage(@NonNull Message msg) {
              switch (msg.what) {
                  case MSG_WHAT_FLAG_1:
                      break;
              }
          }
      };
handler.sendEmptyMessage(MSG_WHAT_FLAG_1);
//手动结束线程
thread.quitSafely();

//定义成静态,防止内存泄漏
static class ThreadHandler extends Handler{
public ThreadHandler(Looper looper){
  super(looper)
 }
}

```

### 4 IntentService

![image-20201104074421590](https://i.loli.net/2020/11/04/fmuKIFcgh7qVTt4.png)

```java
class MyIntentService extends IntentService{
@Override
protected void onHandleIntent(@Nullable Intent intent) {  
   int command = intent.getInt("command") 
    ......                                                       }
}
context.startService(new Intent())

```



### 5 ThreadPoolExecutor

```java
Executors.newCachedThreadPool();//线程可复用线程池
Executors.newFixedThreadPool();//固定线程数量的线程池
Executors.newScheduledThreadPool();//可指定定时任务的线程池
Executors.newSingleThreadExecutor();//线程数量为1的线程池
```



## 线程优先级

java和android 都提供了相应的api来设置线程的优先级，但是实际测试发现android的api效果要明显一些。

![image-20201104074801880](https://i.loli.net/2020/11/04/W6k3Of1vKycBajM.png)

## 线程安全

线程安全的定义：

![image-20201105170549147](https://i.loli.net/2020/11/05/rm8kFilwBQ3GKjz.png)



### 常见的锁

synchronized关键字

ReentrantLock锁

AtomicInteger..  原子类

### 锁的类别：

1. 悲观锁/乐观锁 ：线程要不要锁住同步资源
   ![image-20201105171643802](https://i.loli.net/2020/11/05/xKhIDkSodmqibuL.png)
2. 阻塞锁/自旋锁
   ![image-20201105171701446](https://i.loli.net/2020/11/05/DHScAimlvRb9BPg.png)
3. 公平锁/非公平锁
   ![image-20201105171708261](https://i.loli.net/2020/11/05/te8WN5KP3YXkOHf.png)
4. 可重入锁/不可重入锁
   ![image-20201105171717067](https://i.loli.net/2020/11/05/YldBhV1OHbTZmEA.png)
5. 共享锁/排它锁
   ![image-20201105175621690](https://i.loli.net/2020/11/05/lvIi9np2V4rx3BY.png)
6. JDK1.6 对synchronize 进行了优化 新增了锁的状态 ：无锁,偏向锁,轻量级锁，重量级锁

![image-20201105171733029](https://i.loli.net/2020/11/05/scIZE1d7QxwjkAe.png)



### 保证线程安全的方式

#### 对资源加锁

核心：阻塞住未持有锁的线程

场景：适合写操作多的地方，保证写的正确性

![img](https://img.mukewang.com/wiki/5ee6eb8f09aa7ae528880654.jpg)

#### 原子类自旋

核心： 原子类每次取值时 都取寄存器中对比，
			如果相等那么则说明同步资源没有被更新  直接用就可以了；
			如果不相等，那么就说明被其他线程更新过了，这时候就需要进去自旋
    状态了， 自旋状态其实就是个do while 循环。   把新的值更新到寄存器当中 
    do  while 循环再去寄存器里查一次，如果相等了，则说明没被其他线程更新
    就可以直接用了。 如果又不一样 ，那就再改一次，接着再查一次。
    直到 和寄存器中的值相等， 要是一直不相等咋办，自旋是由次数限制的，默 
   人是10次。
   还是不太懂，要是两个线程一直相互竞争 是怎么保证线程安全的。
   这不是还是各用各的么。



场景：读操作多的地方，避免加锁 ，释放锁导致的大量资源消耗

![img](https://img.mukewang.com/wiki/5ee6eb8809ec285426640574.jpg)



#### 实现类

##### 原子类：

 利用 自旋特性来保证线程同步，  适用于与 非高并发的情况

##### Volatile

volatile 只能保证单次的原子性操作 能同步

比如

```java
volatile int a ;
a  =1 ;// 原子性操作
a++; // 非原子性操作， java中 a++ 并不是 一条命令， 而是多条命令
a = a+1; // 非原子性操作

```



##### Synchronize

- 方法前加synchronize 锁的是类的对象
- static 成员 加synchronize锁的是 class对象



优点：
	jvm可以主动帮我们释放锁，能避免死锁的情况

缺点：

​	必须要等你获取锁对象的线程执行完毕、出现异常之后，才能释放掉锁。
不能中途中断；
​	另外 也不制度奥多个线程竞争锁的时候，是否获取锁成功
   每个锁只有单一条件， 也不能设定超时时间



##### ReentrantLock

正是由于synchronize 的缺点，所以引入了ReentrantLock
ReentrantLock是需要手动来释放锁的

![image-20201109115803374](https://i.loli.net/2020/11/09/oZFNKJL7zgynf4j.png)

利用公平锁特性 是可以达到多个线程交替执行的目的的。 
多个线程交替执行的话， 用thread.join（）也行。

ReentrantLock的话 是可重入的

```java
ReentrantLock lock = new ReentrantLock();
// ReentrantLock的 可重入特性 并不会造成死锁
public void doWork(){
  try{
  
  lock.lock()
  doWork();//递归调用,使得统一线程多次获得锁
}finally{
  lock.unLock()
}
}

```





ReentrantLock还 可以以条件来唤醒执行线程

```java
ReentrantLock lock = new ReentrantLock();
Condition worker1 = lock.newCondition();
Condition worker2 = lock.newCondition();

class Worker1{
  .....
    worker1.await()//进入阻塞,等待唤醒
  .....
}

class Worker2{
  .....
    worker2.await()//进入阻塞,等待唤醒
  .....
}

class Boss{
  
  if(...){
    worker1.signal()//指定唤醒线程1
  }else{
    worker2.signal()//指定唤醒线程2
  }
}

```



![image-20201110153358019](https://i.loli.net/2020/11/10/LSc9vW1zqPxRrpt.png)



```java
ReentrantReadWriteLock reentrantReadWriteLock;
ReentrantReadWriteLock.ReadLock readLock; // 读锁  共享
ReentrantReadWriteLock.WriteLock writeLock;// 写锁 排他

```



这里有点不明白 共享锁的意义， 直接不加锁不就得了。

这点可以通过这个表格来说明。

读锁去访问读锁是可以的， 但是一旦 资源被读锁持有着了的话， 那么写锁就拿不到资源了。 所以 读到的数据一定是正确的， 不会有边读边写的情况。
这个就是共享锁的意义。

|      | 读锁     | 写锁     |
| ---- | -------- | -------- |
| 读锁 | 可以访问 | 不可访问 |
| 写锁 | 不可访问 | 不可访问 |



## 锁的优化

1.  减少持有锁的时间
2. 把锁给分离 
   比如共享锁 和排他锁
   因为大多数都是读操作， 读操作是不没有线程安全问题的
3. 把锁粗化
   也就是说 连续多次加锁的时候，如果中间的操作的复杂度很轻的话，那么可以考虑多次加锁的操作合并成一个， 减少加锁 /释放锁的次数， 从而 提高效率。
4. 在基本都是读场景下， 用原子类来代替锁
   原子类的自旋特性，在客户端这种低并发的场景下，性能源高于加锁。



## 线程池

核心问题 

#### 实现原理

​	就是对Runnable进行一系列的封装。
   本质上还是 Thread和runnable

#### 线程池怎么复用的？

其实就是Thread启动后 run方法里有一个while循环 ，不直接结束，
任务直线完后就会阻塞住， 等待队列中的任务或者新任务

 对于非核心线程就挂起 等待保活市场， 如果这段时间内没有任务的话就销毁
如果是核心线程的话，那么就一直保活



## 自定义线程操作框架

为啥不用原来提供的呢
因为
不支持任务优先级、
不支持线程池暂停、恢复、关闭
不支持异步任务结果回调


自定义的框架应该要支持以上三个功能。



1. 任务优先级
   利用  优先队列来做等待队列 来解决
   ***但是要注意 这个只是优先级的处理，并不是按顺序来处理***
   而且当等待队列满了之后， 新来的任务都不会进到队列当中去的。

   

2. 线程的的暂停和恢复


   利用ReentrantLock 的条件锁来 处理
   也就是通过复写ThreadExecute的beforeExecute来实现的。
   暂停时就锁，阻塞住 然后等待恢复

   ![image-20201113151830660](https://i.loli.net/2020/11/13/DQHpnByWCLK7Gik.png)

   ![image-20201113151717594](https://i.loli.net/2020/11/13/4WT9ueRO5UHfrdI.png)
   ![](https://i.loli.net/2020/11/13/KYyDe6c4MSbECIG.png)

3. 异步回调
   用Callable来代替Runnable就能实现异步回调 逻辑很简单。

   ![image-20201113152842192](https://i.loli.net/2020/11/13/BZgWmpoUV3JPY6F.png)

   ![image-20201113152835186](https://i.loli.net/2020/11/13/iBaM28lNOotYwun.png)





## Kotlin协程机制

### 初衷

场景对比：

场景一：

![img](https://img.mukewang.com/wiki/5ee6ef5309bd2dc612220266.jpg)

描述： Request1 结束后 ，再执行Reqeust2 ...Request3结束后 再更新UI

用常规的回调的方式来处理
Tvlauncher中有很多这样的代码
一层嵌套一层

---对调地狱

```java
//客户端顺序进行三次网络异步请求，并用最终结果更新UI
request1(parameter) { value1 ->
	request2(value1) { value2 ->
		request3(value2) { value3 ->
			updateUI(value3)            
		} 
	}              
}

```





协程的写法

```kotlin
// 启动一个协程
GlobalScope.launch(Dispatchers.Main){

  //用同步的方式来 执行任务，并且拿到返回值  
  val value1 = request1()
  val value2 = request2(value1)
  val value3 = request2(value2）
  updateUI(value3)
}

//suspend  协程任务                        
suspend request1( )
suspend request2(..)
suspend request3(..)

```



场景2



![img](https://img.mukewang.com/wiki/5ee6ef5e091a09ab09960428.jpg)

描述： Request1 完成后， 并发请求2,3  2,3都完成之后 再去更新ui

常规代码

要加上 request3 、request2是否完成这种状态标志

```java
//客户端顺序并发三次网络异步请求，并用最终结果更新UI
fun request1(parameter) { value1 ->
	request2(value1) { value2 ->
      this.value2=value2   
	    if(request3){
         updateUI()       
      }
	} 
  request3(value2) { value3 ->
      this.value3=value3                
	    if(request2) {
        updateUI()
      }     
	}                                  
}

fun updateUI() 

```

协程写法

```kotlin
//发起一个协程任务
GlobalScope.launch(Dispatchers.Main/*指定代码块在主线程执行*/){
   val value1 =    request1()
   //request2 和request3 并发执行 
   // 用GlobalScope.async来包裹 request2 ,request3  
   val deferred2 = GlobalScope.async{request2(value1)}
   val deferred3 = GlobalScope.async{request3(value2)}
   // deferred2.await() 、 deferred3.await()  request2/request3都执行完了之后 调用updateUI
   updateUI(deferred2.await(),deferred3.await())
}

suspend request1( )
suspend request2(..)
suspend request3(..)

```



从两个场景可以看出来，协程可以避免异步任务之间的嵌套，和多任务之间依赖而需要多增代码的情况。
最重要就是增加了可读性， 把异步代码用同步的方式来写了。





### 依赖

```java
//在kotlin项目中配合jetpack架构引入协程
api 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0'
api 'androidx.lifecycle:lifecycle-runtime-ktx:2.2.0'
api 'androidx.lifecycle:lifecycle-livedata-ktx:2.2.0'
  
//在kotlin项目但非jetpack 架构项目中引入协程
api "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.2.1"
api 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.1.1'

```



### 创建协程

```kotlin
//创建协程时,可以通过Dispatchers.IO,MAIN,Unconfined指定协程运行的线程
val job:Job =GlobalScope.launch(Dispatchers.Main/IO...)
val deffered:Deffered=GlobalScope.async（Dispatchers.IO）

```

![image-20201113164627863](https://i.loli.net/2020/11/13/LhdDnJ7uPYFxiMq.png)


协程的启动就是启动了一个协程调度器

调度器会根据传入的DIspatchers来处理任务。

![img](https://img.mukewang.com/wiki/5ee6ef6d09ee1faa19621001.jpg)

![image-20201113170912483](https://i.loli.net/2020/11/13/GNob5ClaYBQuTEF.png)


用Main模式 ， 实际上就是 mainHandler.post  也是异步的
用unconfined模式， 实际上就是同步



协程调度器除了Dispatcher模式之外，还有一个重要属性，就是启动模式

![image-20201113171125922](https://i.loli.net/2020/11/13/fPev5imS3pdYwaz.png)

### suspend的作用

挂起函数 
![image-20201113165247987](https://i.loli.net/2020/11/13/t9G2as8TZlY4XAJ.png)

并不是suspend修饰  就一定有挂起效果
而是suspend修饰的函数  编译器会 自动转换成 return&callback的回调。

先说一下效果：

![image-20201116103050907](https://i.loli.net/2020/11/16/RbzYGwlaL5xXeCD.png)

这个reqeust2函数里 的写法是一个同步的写法，正常来说 应该执行完
delay（）之后 ， 就阻塞住，然后再打印
但是由于加了suspend 关键字，所以说实际上是会执行完delay之后 就立刻返回，并不会阻塞住线程。 然后2S后 再打印 request 2 ... 这个打印。

怎么回有这样的效果呢？
就是由于编译器在编译时 检测到了suspend关键字 ，然后给这段代码添加上了其他代码。



接下来看下例子

kotlin 代码  ： 没有 suspend

![image-20201116094527048](https://i.loli.net/2020/11/16/nO9XRSU7uAWPYMh.png)

反编译出的java  ：没有suspend

![image-20201116094544854](https://i.loli.net/2020/11/16/YDBZ9ybgS2E583V.png)

加上suspend 关键字之后 编译器生成的代码发生了变化

![image-20201116102657790](https://i.loli.net/2020/11/16/qKdAIN6LjrVyD53.png)

多了一个入参



如果给request2（）加上delay 函数（）生成的代码就会大变样， 

![image-20201116103050907](https://i.loli.net/2020/11/16/RbzYGwlaL5xXeCD.png)










也就是说 request2()加上delay函数之后， 才会使得 同步代码的写法有了异步的形式。

分析一下  加上delay之后

![image-20210416162452335](https://i.loli.net/2021/04/16/SVQx5epI9HbKnyR.png)

变化点 ：
1.增加了一个 Continuation的入参，先对入参进行包装
2.continuation 参数加入了 递归回到， 同步状态来分布执行
从而实现了 同步的写法， 有异步的调用

3



![image-20201116104642144](https://i.loli.net/2020/11/16/XF8WbfwhgPCiAxy.png)

![image-20201116104933842](https://i.loli.net/2020/11/16/gemic3yHokBbYzq.png)





delay函数 源码：

kotlin上看 delay函数是 没有回调参数 也没有返回值的， 需要 反编译成java来看。
以后遇到这种情况的话 也可以反编译一下。

![image-20201116123019434](https://i.loli.net/2020/11/16/iwXETD8bszlJMHg.png)

![image-20201116122941059](https://i.loli.net/2020/11/16/1UFPiSMLHGRJZ98.png)



delay 中传入的回调里面维护这一个状态机，用来标志delay任务的状态，然后根据状态来触发回调。
![image-20201116141853096](https://i.loli.net/2020/11/16/De5gycSLKAH8mnG.png)



所以 delayt只所以能挂起， 实际上是DelayKt.delay 决定的。



小结一下 suspend  关键字 是有可能把一个方法分成几部分去执行的。
是否分要取决于内部有没有类似delay这种方法，返回了SUSPENDED 这种状态。



加入有多个delay（或者类似delay这种返回了SUSPENDED 状态的行数）是怎么处理的

kotlin

![image-20201116150052112](https://i.loli.net/2020/11/16/he7AJngEcWL4y21.png)

java:

```java
   @Nullable
   public final Object request2(@NotNull Continuation $completion) {
      Object $continuation;
      label37: {
         if ($completion instanceof <undefinedtype>) {
            $continuation = (<undefinedtype>)$completion;
            if ((((<undefinedtype>)$continuation).label & Integer.MIN_VALUE) != 0) {
               ((<undefinedtype>)$continuation).label -= Integer.MIN_VALUE;
               break label37;
            }
         }

         $continuation = new ContinuationImpl($completion) {
            // $FF: synthetic field
            Object result;
            int label;
            Object L$0;

            @Nullable
            public final Object invokeSuspend(@NotNull Object $result) {
               this.result = $result;
               this.label |= Integer.MIN_VALUE;
               return CoroutineScene2.this.request2(this);
            }
         };
      }

      label31: {
         Object var4;
         label30: {
            Object $result = ((<undefinedtype>)$continuation).result;
            var4 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch(((<undefinedtype>)$continuation).label) {
            case 0:
               ResultKt.throwOnFailure($result);
               ((<undefinedtype>)$continuation).L$0 = this;
               ((<undefinedtype>)$continuation).label = 1;
               if (DelayKt.delay(2000L, (Continuation)$continuation) == var4) {
                  return var4;
               }
               break;
            case 1:
               this = (CoroutineScene2)((<undefinedtype>)$continuation).L$0;
               ResultKt.throwOnFailure($result);
               break;
            case 2:
               this = (CoroutineScene2)((<undefinedtype>)$continuation).L$0;
               ResultKt.throwOnFailure($result);
               break label30;
            case 3:
               CoroutineScene2 var5 = (CoroutineScene2)((<undefinedtype>)$continuation).L$0;
               ResultKt.throwOnFailure($result);
               break label31;
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }

            Log.e(TAG, "request2 completed11111111111");
            ((<undefinedtype>)$continuation).L$0 = this;
            ((<undefinedtype>)$continuation).label = 2;
            if (DelayKt.delay(2000L, (Continuation)$continuation) == var4) {
               return var4;
            }
         }

         Log.e(TAG, "request2 completed11111111111");
         ((<undefinedtype>)$continuation).L$0 = this;
         ((<undefinedtype>)$continuation).label = 3;
         if (DelayKt.delay(2000L, (Continuation)$continuation) == var4) {
            return var4;
         }
      }

      Log.e(TAG, "request2 completed222222222222");
      return "result from request2";
   }
```

![image-20201116150237824](https://i.loli.net/2020/11/16/YbX1cDTZ7HMJosq.png)

![image-20201116150405020](https://i.loli.net/2020/11/16/heDiNcyM1QGpdbE.png)

![image-20201116150543683](https://i.loli.net/2020/11/16/oJneQPVwbsDmWNx.png)



也就是 一样会分成不同不块去执行。
有x个delay   ，函数就会分成x+1个部分去执行。



另外 编译是怎么判断出需要不需要增加 状态机 那部分的代码的呢？
实际上是根据 内部调用的函数里有没有suspend 函数来判定的。

eg.

kotlin

![image-20201116151449462](https://i.loli.net/2020/11/16/eKrg9IoMzcNmHVs.png)

```java
  @Nullable
   public final Object request1(@NotNull Continuation $completion) {
      Object $continuation;
      label20: {
         if ($completion instanceof <undefinedtype>) {
            $continuation = (<undefinedtype>)$completion;
            if ((((<undefinedtype>)$continuation).label & Integer.MIN_VALUE) != 0) {
               ((<undefinedtype>)$continuation).label -= Integer.MIN_VALUE;
               break label20;
            }
         }

         $continuation = new ContinuationImpl($completion) {
            // $FF: synthetic field
            Object result;
            int label;
            Object L$0;

            @Nullable
            public final Object invokeSuspend(@NotNull Object $result) {
               this.result = $result;
               this.label |= Integer.MIN_VALUE;
               return CoroutineScene2.this.request1(this);
            }
         };
      }

      Object $result = ((<undefinedtype>)$continuation).result;
      Object var5 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      Object var10000;
      switch(((<undefinedtype>)$continuation).label) {
      case 0:
         ResultKt.throwOnFailure($result);
         ((<undefinedtype>)$continuation).L$0 = this;
         ((<undefinedtype>)$continuation).label = 1;
         var10000 = this.request2((Continuation)$continuation);
         if (var10000 == var5) {
            return var5;
         }
         break;
      case 1:
         CoroutineScene2 var6 = (CoroutineScene2)((<undefinedtype>)$continuation).L$0;
         ResultKt.throwOnFailure($result);
         var10000 = $result;
         break;
      default:
         throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      String request2 = (String)var10000;
      return "result from request1 " + request2;
   }

   @Nullable
   public final Object request2(@NotNull Continuation $completion) {
      Log.e(TAG, "request2 completed222222222222");
      return "result from request2";
   }

```



内部没有调用suspend修饰的request2  只加了个入参，没有状态机判断

![image-20201116151623181](https://i.loli.net/2020/11/16/YaKiF1R2LPe4tuE.png)

request1 中 有状态机代码
![image-20201116152009570](https://i.loli.net/2020/11/16/NODqyt45zKrICgx.png)



ps: delay这些是怎么触发回调的
是手动的调用 resumeWith(suspend)来完成回调的。

小结：

协程的挂起 本质:方法的挂起， 方法被分成的多个部分来分开执行。
也就是return  +callback

协程是线程框架吗？
 严格来说不是， 他只是提供了能在io线程运行的调度器，并不涉及线程的唤醒与阻塞。里面都是通过callback和return来完成异步任务的。

什么时候协程？
多任务并发的时候使用协程性能比java好， 因为并不涉及到线程的唤醒和阻塞。





## Kotlin协程应用

如何让普通函数适配协程， 成为 真正的挂起函数。即让调用方以同步的方式拿到异步任务的返回结果。

前面说了 并不是加上suspend关键字就能达到异步的目的的。
需要其返回了SUSPENDED这种状态才行。



![image-20201116162303448](https://i.loli.net/2020/11/16/UxTw7ySteIfkjCP.png)

应用：
![image-20201116163109802](https://i.loli.net/2020/11/16/WBcFvG5ITYoxSJ1.png)

lifeccleScope只能在activity/fragment当中使用

别的地方就只能用和application绑定的 GlobalScope了

![image-20201116164122952](https://i.loli.net/2020/11/16/knNA2bwsHtCWfc5.png)



小结：
kotlin的协程的性质（注意是性质）就是Java本身支持的FutureTask的用法外面再加一层回调的包装。使得协程代码不会阻塞住当前线程。

对比一下 协程代码 和 FutureTask的代码。




协程

![image-20210416173805506](https://i.loli.net/2021/04/16/gCPOpr9ZRVl1KXW.png)

futureTask

![image-20210416173839438](https://i.loli.net/2021/04/16/9fmwLX7atNrkB25.png)

futureTask的get和 协程中异步操作的await 是一个性质的。都是会阻塞的。

但是协程可以让 协程后面的代码先执行，然后触发到协程内部的逻辑的时候再阻塞，而FutureTask的get 执行后就阻塞了。

协程的本质就是 把看似同步的代码块， 用异步回调的方式去执行。







## 多线程优化

### 线程池

#### 通过等待队列来控制 -线程池的吞吐量 & 设置优先级

对线程设置优先级并不能保证 优先， 但是等待队列里设置优先级 ，在队列中时，是能保证优先被取出来执行的。

![image-20201116164632685](https://i.loli.net/2020/11/16/O8xSlpeAdNZvRXb.png)

#### 线程池性能监控

可以动态的调整 非核心线程的等待时间， 避免线程过多的销毁 /等待



### 并发安全

#### synchronized 

​	synchronized 在 jdk1.6进行了重构，  性能已经得到了很大的提升。
   并不和以前学的时候说的那样说 很重量。
  google 官方也是推荐使用synchronized的

  但是synchronized的确拿不到锁的细节， 要感知锁的细节的话 ，还是得用ReentrantLock.



#### 减少持锁时间



把耗时的 不需要同步的部分 抽离出来



#### 锁分离

读锁 和写锁分离，   业务中 基本都是读操作， 单独的读操作其实不需要锁住



#### 锁的粗化

假如两块同步操作， 这两个同步操作中间有一些耗时很短的操作，这样可以把这些耗时短的操作一并加上锁， 合成一个请求锁的操作。这样来避免多次请求锁，释放锁。
这个和减少锁持有的时间并不冲突



#### 原子类

在客户端读多写少的场景下  用原子类的性能会高一些



### 线程协作

![image-20201116170322985](https://i.loli.net/2020/11/16/ug9Xi4KLnrNzIV7.png)



CountDownLatchDemo:

![image-20201116170838309](https://i.loli.net/2020/11/16/QjUxeD9ZafER6nN.png)



#### Semephore :  限制最大访问数量

感觉客户端很少用到
用例demo

![image-20201116173454952](https://i.loli.net/2020/11/16/fb9mcJLMIEneYRv.png)

#### 协程

协程最重要的作用就是 可以把异步代码写成同步的形式。提高可读性

细节参考前面
