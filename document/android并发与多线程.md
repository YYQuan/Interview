# Android并发与多线程

相关内容

![image-20201104071634933](https://i.loli.net/2020/11/04/mxG4UPCDAiZdylO.png)



![img](https://img.mukewang.com/wiki/5ee6e28709ec3ec019402170.jpg)





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



