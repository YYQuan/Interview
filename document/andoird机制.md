#  android机制



##  启动流程

启动流程： 系统系统 -> launcher启动 （launcher的启动其实就是应用的启动）



先看系统启动

### 系统启动

![img](https://img.mukewang.com/wiki/5f298fb9090604b418741390.jpg)

1. 按电源键通电后，启动bootloader

2. 然后kernel内核层加载驱动，以及启动内核核心进程

3. 硬件驱动与HAL层（硬件接口层是内核kernel和frameWork中间的一层）交互 启动 **Init进程**

4. Init进程就会fork出**zygote进程** ，zygote进程是C++ frameWork层和java frameWork层的唯一桥梁， ***zygote进程会通过反射调用 ZygoteInit 来进入java层***

5. zygote进程会启动相关的app进程， **第一个启动的就是launcher进程** ，**所有的app进程都是由 zygote 进程fork 出来的**。 

   

   

   PS:***zygote进程中会预加载上一些系统资源文件，通过zygote进程fork出的app进程共享了父进程的物理空间，就不需要重新的加载这些资源文件了。从而可以提高启动速度***


从上可知 ，java层的总入口就是在zygote进程中反射调用到的 zygoteInit.java了



C++是如何主动访问Java实例方法和静态方法的呢？ZygoteInit.java 

#### C++主动访问Java的方式



```C++
// 从classPath路径下找到ZygoteInit这个类
jClassclazz  = (*env)-> FindClass(env,"com/android/internal/os/ZugoteInit");
// 获取默认构造方法ID
jmethodID mid+construct = (*env)->GetMethodID(env,class,"<init>","()V");
// 创建该类的实例
jobject jobj = (*env)->NewObject(env,clazz,mid_construct);
// 查找实例方法的id
jmethodID mid_instance = (*env)->GetMethodID(env,clazz,"main","(Ljava/lang/String;...)")
// 调用实例方法   
jstring str_arg = (*env)->NewStringUTF(env,"我是实例方法")；
    （*env）->CallVoidMethod(env,jobj,mid_instance,str,arg,200);
```





接下来看看zygoteInit

#### ZygoteInit

ZygoteInit.java: Java层FrameWork的入口

从上一步发现，C++中是调用了 zygoteInit的main函数，所以我们从main函数开始查看。

```java

//ZygoteInit.java

public static void main(String argv[]){
  //1.预加载frameworks/base/preloaded-classes和framework_res.apk资源，linux在fork进程时，只是为子进程创建一个数据结构，使子进程地址空间映射到与父进程相同的物理内存空间。虚拟机的创建也是实例拷贝,共享系统资源，如果每创建一个虚拟机都加载一份系统资源，将会非常耗时和浪费。子进程在刚fork生成时，完全共享父进程的进程物理空间，采用写时复制的方式共享资源。
    preloadClasses();
    preloadResources();
    preloadSharedLibraries();
       
   // 2. 启动system_server进程。该进程是framework的核心。
    if(argv[1].equals("start-system-server")){
            startSystemServer(); 
     } 
  
   //3.创建Socket服务
    registerZygoteSocket();
  
   //4.进入阻塞状态，等待连接，用以处理来自AMS申请进程创建的请求
  runSelectLoopMode(); 
    } 
}

```

总结一下：
ZygoteInit.java 中 做了一下的事情：

1. 加载系统资源文件
2. fork SystemService
3. 启动一个socket  来监听来自AMS的创建新进程的请求



这样SystemService就启动起来了。

#### SystemServer

注意是SystemServer 不是 SystemService

```java
// systemServer  
SystemServer.java
public static void main(String argv[]){
  //创建系统的服务的管理者
  SystemServiceManager mSystemServiceManager = new SystemServiceManager(mSystemContext);
   //启动引导服务
   startBootstrapServices();
   //启动核心
   startCoreServices();
  //启动其他一般服务
   startOtherServices();
}

```

![img](https://img.mukewang.com/wiki/5f298fc90928cccd23161720.jpg)

SystemServer 会启动大大小小 100多个service ,这100多个server就构成了android的frameWork层。



其中关乎到应用启动主流程的服务有：
ActivityManagerService  : 四大组件调度服务，其中Activity调度交由ATMS  
ActivityTaskManagerService ： Activity的调度服务  （android 10 才拆分代码才出现的）



##### 当系统服务全部启动完成之后 ，程序又会往哪里执行呢？

![image-20201010071449994](https://i.loli.net/2020/10/10/EFwDpToxZeAnvub.png)

看源码，在SystemServer 的startOtherServices的末尾，systemServer会调用 ams 来启动第三方应用了（也就是launcher应用）



### launcher应用的启动

launcher的启动分两步： 

1 ams 发出请求 
2 zygote 进程接收到请求后进行处理



#### 发出启动Launcher的请求

相关类

相关服务

**ActivityManagerService** :  activity 生命周期调度的服务类 ，也是systemServer调度的app进程的入口
**ActivityTaskMangerService**： activity 实际管理服务， （android10中抽离出来的， 之前是放在ActivityMangerService当中处理的）
**RootActivityContainer** ： 获取符合launcher的标准的应用，得到一个intent对象
**ActivityStarter**  ： 把RootActivityCOntainer得到intent的对象启动起来



**ActivityRecord** ： 在server端对activity的映射 ，里面记录了activity的信息
**TaskRecord** ： 任务栈 里面记录了一个或者多个ActivityRecord的实例 ：
**ActivityStack** ： 任务栈的管理类
**ActivityStackSupervisor** ： activityStack的管理类
**ProcessList**： 负责启动进程的工作  android 10中从AMS 抽离出来的 ，
**ZygoteProcess **:  建立与zygote进程的链接，把创建进程所需要的参数发送给zygote 从而完成进程的创建





**任务栈模型**

![image-20201010073404870](https://i.loli.net/2020/10/10/QGSk9WP67MIrDFl.png)

ActivityRecord  是server端对activity的一一映射
ActivityRecord就会存放在TaskRecord当中
而一个应用可能会存在一个/多个任务栈（TaskRecord）
多个任务栈就交由ActivityStack来管理

系统会有多个应用，也就是有多个ActivityStack,系统通过ActivityStackSupervisor 来管理ActivityStack



**launcher应用启动流程**

![img](https://img.mukewang.com/wiki/5f298fe6091d7b6d36102242.jpg)



前面已经分析到了 systemServer.startOtherServices 中会调用 AMS.systemReady

从注释可知，systemReady就是第三方应用的入口。


AMS.systemReady()中逻辑有很多，但是关键在于 

```Java
mAtmInternal.startHomeOnAllDisplays(currentUserId,"systemReady")
```

是怎么知道这里是关键的呢？

没有直接线索，但是可以从注释中得出， 在系统之前的全部显示器上 开启 homeActivity

![image-20201010103615816](https://i.loli.net/2020/10/10/gAHV8XpsP5FdKCL.png)



可以看出 这个activityTaskMaangerInternal.startHomeOnAllDiplays 是一个abstract 函数 。
其具体实现在哪呢？
在android studio sdk 这层是看不到实现的。
但是有一个规律， frameWork层的代码，\*Internal的实现一般都是\*#LocalService 
 像ActivityTaskManagerInternal 的具体实现就在ActivityTaskManagerService#LocalService中；



ok 知道这个规律后接着往下跟。



![image-20201010104643939](https://i.loli.net/2020/10/10/XUwEKnYyC2WpOks.png)



这里就看到了RootActivityContainer 了。

##### RootActivityContainer .startHomeOnAllDisplays

前面说到 RootActivityContainer的主要作用就是找到 符合launcher要求的app,然后得到个intent对象 ,然后交给ActivityStart.startHomeActivity 来启动了

```java
RootActivityContainer.java
boolean startHomeOnDisplay(...) {
//构建一个category为CATEGORY_HOME的Intent，表明是Home Activity
 Intent homeIntent = mService.getHomeIntent();
//通过PKMS从系统所有已安装的应用中，找到一个符合HomeItent的Activity
ActivityInfo aInfo = resolveHomeActivity(userId, homeIntent); 
//启动Home Activity。下面就是启动Activity的流程了
ActivityStartController.startHomeActivity(homeIntent, aInfo, myReason,displayId);
    return true;
}

```

可以看下系统是怎么判定一个应用可以是launcher的

![image-20201010110008218](https://i.loli.net/2020/10/10/VlLhx2fYijMysPb.png)

![image-20201010110028912](https://i.loli.net/2020/10/10/K1tDMGVnIacqFZE.png)

也就是由HOME这个标签的就是 launcher 但是实际用时 只有HOME的category的是不够的，得加上DEFAULT才行



**ActivityStartController.startHomeActivity**

![image-20201010111014336](https://i.loli.net/2020/10/10/2sxtOrcy4AB8Ejq.png)

接着就交给activityStarter.execute()了

##### ActivityStarter

activitStarter中的 startActivity
ActivityStarter.startActivity()中处理的逻辑就是把intent  得到的activityRecord 加入到任务栈的管理当中，也就是
得到根据启动模式和现在任务栈中的状态等条件activityRecord 和 activityStack， 。

![image-20201010115025556](https://i.loli.net/2020/10/10/LrOzHsvq1WJNhZk.png)

得到activityStack 和activityRecord之后 就调用
RootActivityContainer.resumeFocuedStackeTopActivities()

##### RootActivityContainer.resumeFocuedStackeTopActivities

根据逻辑思路来推测， RootctivityContainer 应该是连接 ActivityStack 和ActivityRecord的

![image-20201010115953754](https://i.loli.net/2020/10/10/3EvpnPaJBUxScth.png)

调用了ActivityStack.resumeTopActivityUncheckedLocked();

##### ActivityStack

![image-20201010120930528](https://i.loli.net/2020/10/10/QWl2crIgJexohHw.png)

把 ActivityRecord  加入到ActivityStackSupervisor 中管理

mStackSupervisor.startSpecificActivityLocked()

##### ActivityStackSupervisor

![image-20201010141004963](https://i.loli.net/2020/10/10/sChHM3nfcFE8SoO.png)

通过post msg来执行 startProcess()
虽然没看出来 post里面的这个接口是怎么被执行的 ，但是不影响主流程。
activityStackSupervisor.startSpecificActivityLocked() 中通过ActivityManagerInternal.startProcess()来唤起ams 发起启动进程的请求

前面提到过ActivityManagerInternal的真正的实现是ActivityManagerService#LocalService

接下来看ActivityManagerService#LocalService的源码

##### ActivityManagerService#LocalService.startProcess

![image-20201010141722863](https://i.loli.net/2020/10/10/RQYl8xK37XFeoTy.png)

这里可以看出来了调用的是ProcessList .startProcessLocked()

前面也提到多 ProcessList是android10  googel重构 android frameWork层而新提出来的。
负责新进程的创建相关的请求。



![image-20201010143702109](https://i.loli.net/2020/10/10/1aJHIGLnPxwRt9U.png)
这个的 activityThread  指定了进程成功后的入口类，  所以ActivityThread才是Android 应用的主入口 （并不是常规理解的Application） 

![image-20201010143828292](https://i.loli.net/2020/10/10/kNtpg54vec9QM3l.png)

然后会调用到

appZygote.getProcess().start()

实际上就是调用ZygoteProcess.start()







##### ZygoteProcess

ZygoteProcess.start() 会调到下面的方法

![image-20201010144800204](https://i.loli.net/2020/10/10/aOiRnkCQPrhFmoV.png)

这个ZygoteState 是辅助进行socket通讯通讯的

![image-20201010145217161](https://i.loli.net/2020/10/10/qPKXksTiMWQ7xoh.png)



ZygoteProcess 发送socket消息给Zygote进程。 这样启动进程的请求就发出去了



#### Zygote接收到请求

主流程：

![img](https://img.mukewang.com/wiki/5f298ffd09e5877c34741346.jpg)



##### 应用的入口

之前提到的 应用的入口是ActivityThread，为啥是ActivityThread呢？

参数 ：ActivityThread 传入zygote 中

![image-20201010152126046](https://i.loli.net/2020/10/10/Xv3QDGUZ8K5CnFf.png)

zygote中通过socket把接收到的参数作为类型来找类对象， 由于AMS 中是写死ActivityThread 那么自然 ，ActivityThread 才是应用的入口

![image-20201010152552151](https://i.loli.net/2020/10/10/a48TGKH1owRW3dl.png)



下一步就是执行到ActivityThread.main()

##### ActivityThread.main()

进入到ActivityThread 之后 就到应用进程当中了

main函数中做了两件事

1. 启动了looper
2. 执行了ActivityThread.attach()



ActivityThread的main函数中， 启动了looper循环
这样应用的主线程就具有消息分发的能力了，也就能执行来自ams对四大组件的生命周期的调度执行工作了。

```java
ActivityThread.java
public static void main(String[] args) {
    .....
    //ActivityThread之所以称为主线程,就是因为在他的入口处就开始了MainLooper的初始化和loop工作,
    Looper.prepareMainLooper();
    ActivityThread thread = new ActivityThread();
    //该方法至关重要,
    thread.attach(false, startSeq);

    if (sMainThreadHandler == null) {
        sMainThreadHandler = thread.getHandler();
    }
    Looper.loop();
    .....
}

```



attach 这个方法很重要，那里面做了什么呢？

```java
ActivityThread.java
private void attach(boolean system, long startSeq) { 
  //IApplicationThread mAppThread是内部类实现了IBinder接口
  //向AMS注册自己,并且传递了mAppThread对象，以便AMS能够调度本进程的四大组件生命周期
   IActivityManager mgr = ActivityManager.getService();
   mgr.attachApplication(mAppThread,startSeq) 
}

```

AMS是集成了IActivityMaanger的binder接口的 

![image-20201010154834881](https://i.loli.net/2020/10/10/hJ9NYUQVGpoHjbg.png)

实际上 ActivityThread.attach()中的mgr就是ams





![image-20201010155232593](https://i.loli.net/2020/10/10/TaUDxJvcuXmtOq7.png)

**ActivityThread** 传给AMS 的参数mAppThread 是IApplicationThread的binder接口  具体实现类就是**ApplicationThread**

也就是通过这个**mAppThread** ，AMS 才能调度本进程的四大组件的生命周期函数

**注意:  是ActivityThread 中new了 applicationThread   传给了AMS ,**
**让ams能回调四大组件的生命周期，是activityThread 维护这applicationThread**

接着看AMS的attachApplication(ApplicationThread )

##### AMS.attachApplication()

```java
ActivityManagerService.java
//1是创建Application对象,2启动该进程的第一个Activity
boolean attachApplicationLocked(IApplicationThread thread,..) {

//该方法回调到ActivityThread的handleBindApplication方法 
//创建Application，并调用onCreate方法  
thread.bindApplication(...);
   
//执行ATMS的attachApplication,开始启动第一个Activity.
//会执行到上面提到的ActivityStackSupervisor#realStartActivityLocked()
//把刚才待启动的Activity继续启动  
mAtmInternal.attachApplication(...);
}

```

接着分别来看 thread.bindApplication  和  mATMS的attachApplication

###### thread.bindApplication

![image-20201010160840664](https://i.loli.net/2020/10/10/XWE6aH57SMNGxZJ.png)





发送了个消息

![image-20201010161032901](https://i.loli.net/2020/10/10/C4V8lcsfRnavKqx.png)

创建application

![image-20201010161649981](https://i.loli.net/2020/10/10/oxRzFwB2r3TDb1c.png)

![image-20201010162515515](https://i.loli.net/2020/10/10/lZW9Efu2TtRmONS.png)

这就创建了application 并且调用了 oncreate函数



###### ATMS的attachApplication

AMS  是怎么 启动第一个activity的呢？
源码看 相关Ams ->ATMS -> activityStackSupervisor

启动activity还是得通过任务栈相关类来启动的

![image-20201010162949022](https://i.loli.net/2020/10/10/du1FNgE7msDz9KX.png)



ActivityStackSupervisor.realStartActivityLocked

```java
ActivityStackSupervisor.java
boolean realStartActivityLocked(ActivityRecord r)  {
// 创建一个 launch 事务
// Create activity launch transaction.
ClientTransaction clientTransaction = ClientTransaction.obtain(proc.getThread(), r.appToken); 
  
//这里把启动启动Activity操作的Item添加到集合中  clientTransaction.addCallback(LaunchActivityItem.obtain(new Intent(r.intent),

//开始执行生命周期,流程就会再次进入到应用进程中的IApplicationThread的scheduleTransaction方法                                                     
clientLifecycleManager.scheduleTransaction(clientTransaction);
}

```

sdk层源码看不出来，但是实际上 
**clientLifecycleManager.scheduleTransaction(clientTransaction)**就是给ActivityThread的mH   handler 发送了个EXECUTE_TRANSACTION 消息

![image-20201010165047067](https://i.loli.net/2020/10/10/fvDqNEbBUnkMAPc.png)





![image-20201010165411821](https://i.loli.net/2020/10/10/ALYHhUsrd7gGe5a.png)

把事务提交到线程池当中，



![image-20201010165724429](https://i.loli.net/2020/10/10/O3JFTf69BKZVbGL.png)



然后 底层调调调 就会调用到 ActivityThread.performLaunchActivity

```java
ClientTransactionHandler.handleLaunchActivity
实际上ActivityThread 是ClientTransactionHandler的实现类
因此是加上就是调用到ActivityThread.handleLauncherActivity  
    
```



```java
ActivitityThread.java
public Activity performLaunchActivity(...) {
//通过反射构建出 Activity对象 
ClassLoader cl = appContext.getClassLoader();
Activity activity = mInstrumentation.newActivity(
         cl, component.getClassName(), r.intent);

  //调度Activity的attach()方法
  activity.attach（）
  //调度Activity的onCreate方法  
  mInstrumentation.callActivityOnCreate（）  
    
  //所以我们发现Activity也是反射创建出来的，因为有了ActivityThread的调度才具备了生命周期。  
  //至此Launcher应用的进程创建和HomeActivity的启动我们就分析完了  
}

```


![image-20201010171718197](https://i.loli.net/2020/10/10/wBNhrkAqeJP4dXx.png)

![image-20201010171801370](https://i.loli.net/2020/10/10/De7Kfm4wnqdXBR5.png)

![image-20201010171830823](https://i.loli.net/2020/10/10/V5uoMQIwX4xPYdO.png)

至此 ， activity的onCreate 就被执行了





**tips :  activity的attach 方法执行的比 activity.onCreate要更早一些**



这样launcher 的进程的创建和homeActivty的oncreate函数就执行完了。
launcher就被启动起来了



### 总结：

文字描述一下：

上电后 启动 bootloader ，然后启动内核， 内核会启动C++层的frameWork , 然后C++层会启动 java层的入口函数 zygoteInite.java

zygoteInit.java  启动了会加载系统资源 、启动一个socket,并且开启systemService服务


systemService 当中就会创建AMS  ，然后启动 其他各种进程

systemService 在启动 otherServices之后  , 
这时候就需要启动新进程啦，应用进程的入口都是AMS,所以肯定是去调用AMS啦。 实际上就是调用AMS.systemReady ，

ATMS是AMS 用来管理activity相关的，而我们这个时候是需要启动launcher进程，那么自然应该通过ATMS来启动launcher.
实际上AMS.systemReady中会调用给ATMS.startHomeOnAllDisplay

ATMS.startHomeOnAllDisplays中 就会 去查找符合条件的应用，然后由ActivityStarter来获取到要以何种方式来启动哪个AcivityRecord，为啥是获取ActivityRecord，因为android中的activity都要通过任务栈来管理，任务栈中的最小单元就是ActivityRecord ，需要先得到ActivityRecord，并且添加到任务栈当中去维护 ，这样任务栈才完整呀。

得到ActivityRecord ，并且拿到ActivityRecord 应该所属的ActivityStack ,这样才知道ActivityRecord 应该要放在哪个ActivityStack当中。

然后再通过ActivityStack获知了ActivityRecord 应该所属的ActivityStackSupervisor , 这样任务栈信息就完整了。



然后新的ActivityRecord就入栈了，ActivityStack 就去发起新的栈顶activityRecord 进来啦， 好更新界面的状态啦。
ActviityStack 是通过ActivityStackSupervisor 来通知AMS  我有新的activity小伙伴要加进来哦。
这里有个小疑问，为啥把通知给ams 放在activityStackSupervisor当中，而是不是actvityStack当中？
不考虑代码 ，只考虑概念也很好理解。
activityStack 是某个进程下维护的 而ams 是系统层面维护的 不是同一个层面的概念；明显让activityStack持有AMS 是不合理的
而activityStackSupervisor 是系统层面维护的  ，和ams是同一个层面的，activityStackSupervisor 持有ams挺正常的。

activityStack 持有着activityStackSupervisor ,也是合理，毕竟activityStackSupervisor是activityStack的管理类。



ok 继续。
上面说到 activityStackSupervisor  通知给ams 我有一个新的小兄弟要加入了， ams 收到后ok 。 通过ZygoteProcess 建立和zygote  进程的main函数中创建的socket建立连接。把相关消息发送出去。

这样zygote 进程就收到了 来自AMS 发送过来的信息了。
AMS 发送过来的信息当中包含了 进程的入口信息， 也就是写死了的
activityThread 。
所以zygote在处理了ams的socket消息之后  ，就去执行ActivityThread.main 函数了。 为啥执行main函数 ，并不是因为main函数是java函数的入口，而是代码里写的指定去执行main函数。



现在就到activityThread 的main函数了。

activityThread.main 里面 做了2件事

1.  new 一个 applicationThread 实例 并且把applicationThread对象注册到ams上
2. 执行looper  使能消息处理





这里有个要点 ，是把ActivityThread 是把 applicationThread 注册给ams ，并不是 把activityThread 注册给ams ；
设计上我是这样理解的， 毕竟 ams 不仅仅管理 activity 这一个组件的生命周期， 四大组件的生命周期都是由ams管理的。

那为啥设计上不直接用appliationThread 作为 应用的入口呢？
我想可能是由于application 并不是四大组件之一。 还有一点可能是applicationThread还是的通过activityThread来获取 main activity信息。
当然也有可能没有啥考虑。反正现状就是这样了。

所以现在就到了 ams 来对applicatioThread 进行注册了


ams 对application的注册分两个部分： application 和activity

总体来说 ams注册appliationThread 就做了两件事情

1. 创建application 并且调用appliation.oncreate
2. 执行ATMS的attachApplication



ATMS.attachApplication 需要说明一下。
ATMS.attachAppliction实际上调用到的是activityStackSupervisor的相关方法。 毕竟得告诉activityStackSupervisor    执行的结果到底怎么样嘛。

然后呢 就ActivityThread 去发起 一个事务， 实际上就是给ActivityThread 里维护的一个handler发送消息。

最终会调用到 ActivityThread 里的performLauncher 方法

ActivityThread.performLauncher 就会创建出activity 和调用activity的create方法了。
这样launcher就算是启动起来了。















### 拓展

#### Q1:AMS 和zygote 的通讯干嘛不用binder呢？ 而使用socket?

A1： 因为使用 binder 需要讲本线程挂起， 如果服务端的binder 意外崩溃了，那么客服端这的binder就停住了； 
Q1_2: 那可以用个子线程来做呗？
A1_2:  不行 ，因为 linux fork 进程不允许多线程的。







