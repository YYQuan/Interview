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