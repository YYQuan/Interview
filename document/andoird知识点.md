android机制



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



先来简析下ActivityThread的类的主要结构

##### ActivityThread 结构图

![image-20201012095552522](https://i.loli.net/2020/10/12/2ofZUAV1Wnm7xwM.png)



可以看出  ActivityThread里面 有 
**activity**的map、  **provider**的map、**service**的map 、和一个**BroadCastRecevier** 

也就是维护了 本进程中的四大组件，
比较特殊的是 广播broadCastRecevier   四大组件内 只有 broadCastRecevier 没有map ，那是因为 ActivityThread 只负责BroadCastRecevier 的转发 ，实际的处理和维护还是在AMS 当中的



**looper** :  启动消息循环 ，使能消息分发
**mH** :  一个主线程的handler
**mApplicationcation**:  进程的applicaiton对象  ，（也是由ActivityThread  创建和维护的）

**Instrumentation**: 一个辅助ActivityThread的来完成activity调度的工具类，比如完成activity对象的创建呀 ，执行oncreate函数等等。

ApplicationThread : 一个IBinder的接口，AMS 之所以能够调用四大组件的方法， 就是通过这个ApplicationThread

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

通过ActivityThread 传入的 ApplicationThread的iBinder对象 ，就能调用到 applicationThread .bindApplication()

接着分别来看 thread.bindApplication  和  mATMS的attachApplication


###### thread.bindApplication




![image-20201010160840664](https://i.loli.net/2020/10/10/XWE6aH57SMNGxZJ.png)

***Q:为啥要发消息呢？直接调用AcivityThread当中的目标函数不行么？***

***A:是因为ams 中 通过IBinder对象调用的方法 是在子线程当中， 需要通过mH来切换到主线程当中去执行逻辑***





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



对于launch 的事务，
activityStackSupervisor 会执行到LauncherActivityItem 的execute函数

如果是pause事务 那就会执行PauseActivityItem 的execute(),这个是状态机的设计模式。





![image-20201012105554798](https://i.loli.net/2020/10/12/nCylQxqcDXhrj7z.png)

sdk层源码看不出来，但是实际上 
**clientLifecycleManager.scheduleTransaction(clientTransaction)**调用了LaunchActivityItem.execute()
就是调用了ActivityThread的handleLauncherAcitivity()

![image-20201012105320332](https://i.loli.net/2020/10/12/nkJluzCWadMEigA.png)

ActivityThread.performLaunchActivity

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

然后呢 就ActivityThread 去发起 一个事务， 实际上就是调用ActivityThread的父类ClientTransactionHandler的方法（ClientTransactionHandler也是android 10 google重构 时新增的内容， 就是专门来用关系activity的）

最终会调用到 ActivityThread 里的performLauncher 方法

ActivityThread.performLauncher 就会创建出activity 和调用activity的create方法了。
这样launcher就算是启动起来了。















### 拓展

#### Q1:AMS 和zygote 的通讯干嘛不用binder呢？ 而使用socket?

A1： 因为使用 binder 需要讲本线程挂起， 如果服务端的binder 意外崩溃了，那么客服端这的binder就停住了； 
Q1_2: 那可以用个子线程来做呗？
A1_2:  不行 ，因为 linux fork 进程不允许多线程的。







## Activity核心


这部分内容主要包含如下部分：

1. View树的绘制
2. Ui刷新的机制
3. 手势分发的源头
4. Activity的任务栈

要解决的问题的总纲如下：

![image-20201012111430118](https://i.loli.net/2020/10/12/sFDM47uWJSwRhyb.png)

### Activity View树的测绘流程



从一个面试题入手：

**Q:如何在Activity的onCreate和onResume中获取一个view的height/width?**

**A:  直接拿是不行的， 因为 view的测绘流程还没有开始， 一般是通过view.post来获取，也可以通过给view添加上globallayoutListener的监听来获取**



那Activity的测绘流程是从哪里开始的呢？

下面开始分析 activity的测绘流程。

从分析启动流程的过程当中已经知道了，activity第一个被调用的方法是  attach函数 ，然后才是 onCreate()

先看下attach 函数



#### Activity.attach

![image-20201012113139447](https://i.loli.net/2020/10/12/WvufKNZd6POcMsA.png)



发现attach方法中 初始化了  成员变量 window

然后并没有发现其他和view的测绘相关的代码，
onCreate也是是。

所以回到 activity ，看setContentView(int id)



#### Activity.setContentView()



![image-20201012113850673](https://i.loli.net/2020/10/12/7KD5owS6Fkbmn1Z.png)



观察一下，




![image-20201012115734317](https://i.loli.net/2020/10/12/oHeVfrmbIljwTg4.png)

![image-20201012115844719](https://i.loli.net/2020/10/12/gTRlrvSAsmdXq28.png)

在activity的setContentView(view)中， 创建了一个viewGroup 并且 赋给了 window .

接着看看window.setContentView里面做了啥。

PhoneWindow是android中window的唯一实现类。



![image-20201012120421370](https://i.loli.net/2020/10/12/A9Jl8v3QSdIe5nW.png)

从上图可知，phoneWindow.setContentView
中创建了一个decorView ，并且和该window进程了绑定，

然后初始化了 mContentParent ，并且把activity传过来的view， 添加到 mContentParent当中。



ok， 现在就是已经把 用户指定的 view添加到了 
activity的window(由attach 方法创建)   然后再有setContentView触发，  new 一个decorView 和window相互绑定，   用户指定的 view 在添加到decorView的当中去。

但是这个过程并没有 涉及到绘制的流程。



所以得回到 activity的生命周期中。

onStart / onResume  中 都没有线索。
而且刚才提的问题中 也说到了 在activity的onResume方法中就获取不到view的 height和width的。 所以view的测绘肯定是在onResume方法之后。

ok ，那得啦。activity的显示的生命周期已经走完了，都没有触发测绘， 那测绘是在哪进行的啊。

那只能先去ActivityThread里面去找一下了。



#### ActivityThread.handlerResumeActivity

![image-20201012140525473](https://i.loli.net/2020/10/12/PqMNjsd32iWBzlH.png)

在ActivityThread 内 ，发现在handleResumeActivity中 ，在performResumeActivity之后， 还把 和window绑定了的 并且是包含了用户布局view的 decorView 添加到到了windowManager 当中。

这里会遇到一个问题 ，发现windowManger 是一个抽象方法， 并且找不到其实现类。

但是看 window的windowManage的赋值里有线索。

![image-20201012144328175](https://i.loli.net/2020/10/12/CD89txfl6T7bugs.png)



发现其实现类就是WindowManagerImpl

PS : android studio 中找不到实现类的时候一般可以看看 *Imple，比如WindowManager ,Context 



#### WindowManagerImpl

接下来查看 WindowManagerImlp ，

![image-20201012144810147](https://i.loli.net/2020/10/12/uePdx9sbnAOXTGr.png)

发现只是做了下转发。

转发给了WindowManagerGlobal
WindowManagerGlobal 其实就是应用的统一windwomanager入口。 
一个页面就有一个windowManager ,每一个windowManager都由WindowManagerGlobal来维护。

所以对于应用外部来说， 应用内的所有的增删改查 都是WindowManagerGlobal来完成的。

![image-20201012145627956](https://i.loli.net/2020/10/12/BtLkWm13VygZKoU.png)



#### ViewRootImpl

上面已经知道了viewRootImpl 是触发测绘的关键了。
先来对ViewRootImpl的做个整体的认识。

![image-20201012150058223](https://i.loli.net/2020/10/12/sAueMY96hg257Cb.png)



ViewRootImpl  



其中很重要的一个作用就是 监听 系统 
 vsync （垂直同步）信号 ，然后触发view的measure ,layout, draw这三大流程



ViewRootImpl 不仅仅是 view绘制的发起点， 还是手势事件接收的入口。

##### 注册手势监听

下面就是向windowManagerService 注册window的代码， 注册时还传入了 手势监听的回调。

![image-20201012170532408](https://i.loli.net/2020/10/12/iKa5fe3GxZ6oMXm.png)



##### 设置ViewParent

要注意ViewRootImpl 本身不是view ，只是实现了ViewParent的接口
把view和viewRootImpl关联起来，viewRootImpl 就是传入的decorView的ViewParent

![image-20201012171004960](https://i.loli.net/2020/10/12/JL2PvpaOQdytUuq.png)

![image-20201012171257636](https://i.loli.net/2020/10/12/9Anf3lp5kKGTNUq.png)



##### 注册屏幕点击实现的响应

![image-20201012172337038](https://i.loli.net/2020/10/12/I3LOlvYKmGpMqHD.png)

##### 测绘

requestLayout

![image-20201012162426645](https://i.loli.net/2020/10/12/5IJCbpdBDWGnS7g.png)

上图就是 著名的  只能在主线程更新的异常。

这个异常就是在ViewRootImpl 中被抛出的。

所以说  view 在更新数据的时候 ，如果没有执行到viewRootImpl的requestLayout()方法的话， 那么就算在子线程中，也是可以进行ui更新的。因为没有线程检查。



在子线程当中去更新UI 也是由应用的，比如 如果想要页面一展示出来就能立刻的把相关内容都显示出来，就可以采用在子线程当中更新的策略

比如 手机淘宝的首页， 基本loading界面一结束 内容都出来的。 为啥呢？
就是其实在 引导页的时候 子线程就已经在加载页面了。





线程检查完之后就进行事务处理

![image-20201012163523277](https://i.loli.net/2020/10/12/cEbUHedmKB1NsnA.png)

![image-20201012163633289](https://i.loli.net/2020/10/12/7phxiw6HAGC5kKP.png)



设置好垂直同步信号的监听之后，再收到垂直同步信号之后 就会调用mTravesalRunnable

![image-20201012163838951](https://i.loli.net/2020/10/12/vDZEjmNqsCaPAHp.png)



![image-20201012164122540](https://i.loli.net/2020/10/12/3jxnhgMVJfpbarG.png)

![image-20201012164306328](https://i.loli.net/2020/10/12/OZu5cqDCo8Mfkd9.png)

performTraversals 就是触发绘制的地方.
performTraversals 会根据 当前的view树是否发生变化 ，包括大小、 属性；来判断是否需要重新测绘，重新布局，重新绘制



performTraversals 中就会调用 performMeasure、 performLayout、 以及performDraw

他们内部就会分别调用 decorView的 measure 、layout、 和draw


这里提一下 handler的屏障消息

##### Hnadler之屏障消息

![image-20201012163142822](https://i.loli.net/2020/10/12/GKErabTiFO1c85l.png)

handler的同步消息和异步消息，在平常是没有区别的。但是再handler接收到 屏障消息之后就不一样了。 接收到屏障消息之后的handler就会优先处理 异步消息，而view的测绘任务就是一个异步消息。





#### 总结

![image-20201012164839207](https://i.loli.net/2020/10/12/PtI5fiDGJmnC8MO.png)

1. 在ATMS 调用了activity.attach   创建window
2. 在setContentView 中创建 decorView 和用户指定的view ，并且把用户指定的view添加到decorView中 ，然后把window和decorView关联起来；
3. 在ActivityThread 的handerResumeActiivty中，performResumeActivity之后  会把window添加到windowmanagerService当中
   
4. WindowManagerService中会创建 ViewRootImpl
   并且把ViewRootImlp 指定为 decorView的ViewParent(顶层节点);
   ViewRootImpl中会对view的测绘，以及向WindowManagerService 注册手势监听，以及点击事件监听。
   View的测试是通过在ViewRootImpl中添加系统垂直同步信号的监听来完成的。
   当接收到系统的垂直同步信号 后就会调用
   performTarversator()
   perfromTarversator中就会对 viewParent进行判断，看时候需要重新刷新；需要的话就会执行view的绘制的三大流程： measure ,layout,draw



### Activity页面刷新机制概述

前面讲到了在ViewRootImpl中  向Choreographer 注册了 垂直信号的 接收来刷新界面的。

来看看choreoGrapher

#### Choreographer 

ChoreoGrapher 有两个作用：

1. 根据系统的信号来刷新界面
2. 过滤掉同时有多个ViewRootImpl的requestLayout()请求，避免一帧的时间段内 多次刷新。



ChoreoGrapher 中维护了两个重要的成员变量：

1. displayEventReceiver :  接口native层的VSync的信号
2. CallbackQueues

CallbackQueues里面就对应着几种 Callback 类型
对应着几种事件类型的callback , 比如 屏幕输入，动画，requestLayout触发的测绘事件，以及Vsync的确认事件。



这个是CallbackQueue的数据模型。

![image-20201013155341175](https://i.loli.net/2020/10/13/2pLsaSVfWUcnJAy.png)



choreoGrapher 是如何达到 ，一帧内 的requestLayout请求只执行一次的呢？

其实很简单， 就是在接受到请求的时候，不是立即去执行的，而是先存其起来，然后在接收到vSync信号之后， 再去执行。

先整体的浏览一下ChoreoGrapher的整体结构

![image-20201013164115386](https://i.loli.net/2020/10/13/wJs1n7YIj69PGR5.png)

这几个 方法最终都是调用
ChoreoGrapher.postCallbackDelayedInternal（）

![image-20201013164216052](https://i.loli.net/2020/10/13/wdjP7Xb6caQtOYm.png)

可以把postCallbackDealyedInternal理解为刷新界面的唯一入口



![image-20201013164736582](https://i.loli.net/2020/10/13/FLpHGXfzeRhIjKY.png)

![image-20201013164751008](https://i.loli.net/2020/10/13/NkmZ5BaFuYOsnRr.png)



上面可以看出来，ChoreoGrapher实际上就是向DisplayEventReceiver 进行了调度。

等到显示服务发送了Vsync信号之后， DisplayEventReceiver就能够接收到了。

然后DisplayEventReceiver的实现类FrameDisplayEventRecevice的onVsync函数就会被调用


![image-20201013165643122](https://i.loli.net/2020/10/13/MkaBYiLVQFEvArp.png)

ok 现在就接收到了vsync消息了。
接着往下看， FrameDisplayEventRecevice的run里做了啥呢？

![image-20201013170254693](https://i.loli.net/2020/10/13/WFvLM4EbZ9U1aXH.png)

这个是 frameWork对掉帧的判断
ok 接着往下看逻辑的处理

![image-20201013171121284](https://i.loli.net/2020/10/13/sGBCnLoZv95jz6t.png)

上面这段 对动画 ，输入，测绘的逻辑 就是ChoreoGrapher协调 显示的逻辑



接下来 看CallbackRecord的run函数做了什么。

![image-20201013171208102](https://i.loli.net/2020/10/13/hlQsmWTuzFZXdOG.png)



那action.run()里面一般是做啥呢？

以ViewRootImpl中的测绘工作来看，
![image-20201013172405498](https://i.loli.net/2020/10/13/hIoF1kwlyvGxjMH.png)



这样第一帧的刷新就完成了。

view的所有刷新都会触发到ViewRootImpl的performTraversal()。 从而完成对 vsync的callback的添加， 以此完成刷新。

### 手势分发



前面说到 WindowInputEventReceiver
是接收手势的原点。

主流程

![image-20201013183209513](https://i.loli.net/2020/10/13/kBwJr5dn6H3fzTS.png)

为啥 流程中 要 decorView 分发给activity  ,然后再由activity分发会给decorView ，再传给ViewGroup呢?

是为了给activity 拦截事件的能力



### Activity任务管理

要想在程序的任意位置都获取到 当前activity任务栈顶部activity , android 中并没有直接的api， 但是可以通过application类下的ActivityLifecycleCallback 接口来获取。

#### ActivityLifecycleCallback

ActivityLifecycleCallback中包含了全部的activity的生命周期的回调。
![image-20201014095647393](https://i.loli.net/2020/10/14/Kch6oZqtbnluIT4.png)



通过这个ActivityLifecycleCallback ,从而就能构建



## Fragmetn核心

![image-20201014105106063](https://i.loli.net/2020/10/14/9PRVS1qohJjkyme.png)



常规用法
![image-20201014114554004](https://i.loli.net/2020/10/14/9u7Yaq2VKAts1kB.png)

#### Fragment的使用原理

![image-20201014111611463](https://i.loli.net/2020/10/14/Hyp1Kg2LiP5ZcmO.png)



为啥呢，FragmentActivity 不直接用activity来调用FragmentManager ，而是通过FrgmentController来处理呢？

![image-20201014112042278](https://i.loli.net/2020/10/14/AwqOTUPSRJcgbE1.png)





![image-20201014112410508](https://i.loli.net/2020/10/14/6hp1NivBxwg4rLA.png)

fragmentController是在activity的实例被创建的时候就被创建出来了的



这里就能看出来 Activity 管理Fragment的方式是


通过Activity 创建了一个FragmentControl ,并且把一个FragmentHostCallback接口的实现传给FragmentControl，

真正持有着FragmentManager的是 Activity new出来传给FragmentController的FragmentHostCallback接口的实现。

ok， 那搞的这么绕干嘛呀。
主要是为了屏蔽宿主对FragmentHostCallback的直接引用， 因为Fragment并不是只提供给activity使用的，可以拓展应用场景。

以后做设计也可以这样来考虑。
但是我是没看出来由啥好处



接着往下看。

![image-20201014114539253](https://i.loli.net/2020/10/14/6IBmkqvz7eRj9gx.png)

这个beginTransaction 是fragment操作的起点。

后面的一些列的操作  （到commit为止）构成了一个事务，  事务的一个特性就是 全部的操作 ，要么都成功，要么都失败。不会有一部分成功的情况。



接下来看BackStackRecord

##### BackStackRecord

![image-20201014114959699](https://i.loli.net/2020/10/14/K8ygCqeZTBMNxHA.png)

BackStackRecord继承至FragmentTransaction,
因此可以把backStackRecord 理解为 fragment的事务，
由google的命名的尿性 record的这个一般就是 一组概念里面的最小单位， 比如 对于activity任务栈来说 最小的单位就是 ActivityRecord；



为啥给FragmetnTransaction加多层包装呢？
是为了给FragmentManager逆向操作事务，完成出栈操作。逆向操作的能力是由backStackRecord还实现了BackStackEntry接口

ok 接着往下看

##### add

![image-20201014120234725](https://i.loli.net/2020/10/14/LsycuNFStkK27eU.png)

创建了一个事务，并且添加到一个列表当中



replace 和add类似
![image-20201014120405709](https://i.loli.net/2020/10/14/2hk1VWAuXBsKop4.png)





##### commit

commit是FragmentTransaction的abstract方法
真正的实现实在BackStackRecord当中

![image-20201014120706294](https://i.loli.net/2020/10/14/pgEk2sHoldShQVI.png)

![image-20201014121006358](https://i.loli.net/2020/10/14/hun4ICJAYUGSoPQ.png)


FragmentManager在执行事务之前会做状态检查。

![image-20201014121755603](https://i.loli.net/2020/10/14/68OCErLIuV2SlGR.png)



那怎么能避免状态检查呢？

##### FragmentManager的事务提交方式

![image-20201014121913404](https://i.loli.net/2020/10/14/JrpCwKiZ1SFUQqx.png)

说明：
同步 异步 都是在主线程执行的，只是异步是通过发送消息到消息队列当中， 在消息被轮询到的时候，才开始处理。

正常来说 用同步操作会更加安全一些， 不过只要不检查状态，那么就不会抛出异常了。



继续回到commit



![image-20201014122513648](https://i.loli.net/2020/10/14/ID9ztYed6bTcZCv.png)


![image-20201014142640213](https://i.loli.net/2020/10/14/a3OoRK5Sy2Vrbmn.png)



![image-20201014142653386](https://i.loli.net/2020/10/14/UF38jsvGAeCSEn6.png)

然后会调到FragmentTransition.startTransitions

![image-20201014143729764](https://i.loli.net/2020/10/14/nq6WA1mMtV352pK.png)

![image-20201014143833412](https://i.loli.net/2020/10/14/8P7lOgMZIX16HLh.png)





![image-20201014144456853](https://i.loli.net/2020/10/14/LJTN4Xlhqs6uyKP.png)

![image-20201014144723816](https://i.loli.net/2020/10/14/tevRxDZcJBKfbOz.png)

FragmentManager的moveToState就是处理 fragment生命周期回调的地方

无论是由于事务的执行 还是activity触发的fragment的生命周期的回调都是通过FragmentManager成moveToState来调用fragment的生命周期相关函数



#####  fragment的生命周期的回调

activity是通过在生命周期函数中调用FragmentController相关函数来控制fragment来声明周期的。
比如oncreate()

![image-20201014150444485](https://i.loli.net/2020/10/14/2S1ECmAB7hftrNl.png)



**PS： android studio中可能同时包含着几个版本的sdk，看源码的时候主要要对应sdk版本 ，否则可能会找不到相关函数**



### Fragment的数据是怎么存储起来的？



我们知道activity的数据存储是在 onSaveInstanceState当中， 试着跟一下。

![image-20201014151414330](https://i.loli.net/2020/10/14/hcEbeiCxj4uBmMl.png)

这就是fragment 的存储
这里也侧面说明了fragment相关信息的存储，也是在activity的onSaveInstanceState当中来执行的。

但要注意 fragment的restore 并不是发生在activity.onRestoreInstanceState当中的。
（可能是由于onSaveInstanceState和onRestoreInstanceState并不是成对出现而导致的）
fragment的恢复是在oncreate当中的

![image-20201014153228551](https://i.loli.net/2020/10/14/vN7FDiBwAHR6Xxu.png)

上面这个是数据恢复，生命周期的恢复也是在onCreate方法当中来完成的
![image-20201014153332971](https://i.loli.net/2020/10/14/jfiyIkELxecPdaR.png)





也正是由于activity在恢复的时候会尝试恢复fragment，但是并没有保存fragment中的view的状态，因此会调用fragment.onCreateView 
,所以会导致会出现fragment页面重叠的问题
比如：按下Home键后再回到前台

### Fragment的页面重叠



解决方法： 在添加fragment之前先判断一下该fragment是否已经存在了。

![image-20201014154416520](https://i.loli.net/2020/10/14/Ua69yfoNFcAQIMn.png)





PS： 给fragment添加tag的时候 最好不要用类相关的名称， 因为混淆后的名称是一样的



### Fragment新版懒加载

懒加载：Fragment的ui对用户可见时才加载数据。



![image-20201014154736373](https://i.loli.net/2020/10/14/hpVsakFveAbSgfn.png)



### 单Activity模式的探讨

![image-20201014164606412](https://i.loli.net/2020/10/14/pJU1zGQASbHxLKM.png)

**灵活**：单activity的话  就不需要管啥mainfest的注册什么的了，代码甚至都可以从服务器下发。
**响应速度**：fragment只是一个view ,而activity还需要走ams  进行ipc通讯，fragment的响应速度肯定是比activity快的

灵活和响应速度是单activity的优势，
但是对于fragment的支持 ，比如 多屏设下下， dialog和fragment的交互  这些都还有很多坑。
**稳定性和扩展性** 单Activity是比较差的





## RecyclerView核心知识点



![img](https://img.mukewang.com/wiki/5f2990d109207c2026041420.jpg)



RecyclerView的插拔式设计还是很牛逼的。



### RecycleView的基本用法

![image-20201014170611779](https://i.loli.net/2020/10/14/MpD3EbV6wWXArIu.png)



RecyclerView 的源码分析可以从setLayoutManager中开始

### RecyclerView.setLayoutManager

![image-20201014171230386](https://i.loli.net/2020/10/14/qX3oE7IOTHfpMDZ.png)



请求了刷新之后，来看下RecyclerView的测绘的三大流程

![image-20201014171402821](https://i.loli.net/2020/10/14/j8YFywOemzUQTKq.png)

![image-20201014172106683](https://i.loli.net/2020/10/14/ocjvPBhUJsA2aZ8.png)



这里我们得出结论  recyclerView的 测绘流程，基本都是交给了LayoutManager 来处理。
细节太多 也记不住。先不管了。

接着看RecyclerView的复用

### RecyclerView的复用

![img](https://img.mukewang.com/wiki/5f2990dd0940c7f113660534.jpg)





前面分析到了 RecyclerView的测绘都是交给了layoutManager.



因此RecyclerView的item的复用 都是交给了LayoutManager.

RecyclerView的复用是从
**LayoutManager.getViewForPosition()**开始



layoutManager每次在添加一个item的时候， 都会想Recycler 索取一个viewHolder

Recycler中就会按照优先级去拿。



### Recycler

```java
//Recycler 

public final class Recycler {
 //#1 不需要重新bindViewHolder
 ArrayList<ViewHolder> mAttachedScrap;
 ArrayList<ViewHolder> mChangedScrap;
  
 //#2 可通过setItemCacheSize调整，默认大小为2,
 ArrayList<ViewHolder> mCachedViews;
  
 //#3 自定义拓展View缓存
 ViewCacheExtension mViewCacheExtension;
  
 //#4 根据viewType存取ViewHolder，
 //   可通过setRecycledViewPool调整,每个类型容量默认为5
 RecycledViewPool mRecyclerPool;

}
```

![image-20201014184033108](https://i.loli.net/2020/10/14/SCyi8lQMTp7xUF6.png)

其中mAttachedScrp 、mCacheViews 是不需要重新onBind的

注意 recyclerPool的缓存   一般是一个 type 有5个缓存。

有些人说   缓存不止四级 ，他们是 一个 缓存list就当做一级。

但是分级应该是按照上图来分的。
所以 attachedScrap 和changeScrap 是同一级的。





但是mChacheViews中的缓存需要校验下位置，

从holderView的获取方式来看，就是从各级的缓存中拿，如果拿不到就调用createView

![image-20201014182633655](https://i.loli.net/2020/10/14/4dfxbBe1qFhk6KV.png)





![image-20201014183928238](https://i.loli.net/2020/10/14/q1TYBkafmF3irMC.png)





## Android 消息机制

### 整体结构

android的消息是由

- Handler
- Looper
- MessageQueue
- Message

这四个类支撑起来的。





### Looper

取出MessageQueue 出队， 分发msg

就是一个驱动器的作用
决定了消息处理在哪个线程当中。

### Message



数据结构

```JAVA
long  when  // 预期执行的时间戳  
Handler target // 给哪个handler处理
Runnable  callback //  该消息的处理回调
Object  obj		
Bundle  data
Message  next  // 下一个消息
```





Message消息类型

- 同步消息
- 异步消息
- 屏障消息 

这里可能会有误解， 这里的同步/异步消息 处理时并没有不同，都是异步处理的。Handler的消息处理都是异步处理的。
只和MessageQueue的出队顺序相关。





Message中有一个时间戳字段，MessageQueue就根据这个时间戳来决定出队顺序。

Handler的延时也是通过改这个时间戳来实现的 。
入队还是立刻入队的。

一般情况下MessageQueue的消息出队顺序是只和时间顺序有关。但是在队头是屏障消息的时候，就会跳过同步消息，去把第一个异步消息出队。

Tip：屏障消息 和异步消息 用户是不能触发的。都是系统触发的 。 系统应用场景有  对于刷新ui的垂直刷新信号就是异步消息，系统就是用屏障消息来触发垂直刷新信号消息来刷新UI的。 



Tip: Handler里有 根据消息立即唤醒对应线程的机制，这个立即唤醒是为了让异步消息更快的得到执行。
比如 messageQueue中下一条消息就是屏障消息，或这插入的消息是第一条异步消息，那么handler都会主动的唤醒其对应的线程。



### MessageQueue

其中MessageQueue是用来存储Message
是一个单向链表队列

出队的顺序是

- 如果队头是屏障消息，那么就跳过同步消息，取出第一个异步消息
- 如果队头不是屏障消息，那么就按照时间顺序来出队



### Handler

发送的方式

1. sendMessage(message)
2. post(runnable)
3. sendMessageAtTime



消息分发的优先级：

1. Message的回调方法： message.callback 
   也就是传入的runnable
2. Handle的回调方法：
   Handler.mCallback.handleMessage
3. Handler的默认方法：
   Handler.handleMessage

对应的对调

```kotlin
        //1. 直接在Runnable中处理任务
        handler.post (runnable= Runnable{
          
        })

        //2.使用Handler.Callback 来接收处理消息
        val handler =object :Handler(Callback {
            return@Callback true
          
           }
        )
          
        //3. 使用handlerMessage接收处理消息
        val handler =object :Handler(){
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
            }
        }
        val message = Message.obtain()
        handler.sendMessage(message)

```





创建Handler的时候并不需要传入Looper对象

那么Looper对象是哪来的呢？


![image-20201204105123320](https://i.loli.net/2020/12/04/xkea1ZdvGVzhorC.png)



需要在当前线程中调用Looper.prepare，才能拿到改线程的Looper 对象。

 在主线程内 ，之所以不需要调用Looper.prepare就能直接创建Handler，  是因为在ActivityThread里面就已经执行了Looper.prepare了。



线程的looper是通过threadLocal来存储的。

用ThreadLocal可以在线程的任意位置拿到线程的私有对象，不需要传来传去的。







Q1:

#### 如何让子线程拥有消息分发的能力？

在子线程当中 执行 Looper.prepare 以及Looper.loop（）即可。

```java
class HandlerThread extends Thread{
  private  Handler handler
   @overried
   public void run(){
       Looper.prepare()
       //面试题：如何在子线程中弹出toast  Toast.show() 
       //Toast.show() 
          
       createHandler()
   
       Looper.loop()
   }
  
   private void createHandler(){
     handler  = new Handler(){
        @overried
        public void handleMessage(Message msg){
            //处理主线程发送过来的消息
        }
     }
   }
}

```



Q2：

#### 如何在子线程中弹出Toast

```java
在子线程当中 执行Looper.prepare 以及Looper.loop
后即可 弹出Toast了。
    
也许会有个疑问， 这不是违反了 要在主线程更新UI的基本法则么？
    
实际上，view 并不是只能在主线程更新。
view的线程检查是在ViewRootImpl里面的checkThread来进行的。
源码看下图。
ViewRootImpl的线程检查并没有要求一定要在主线程当中。

而是要求checkThread和 创建ViewRootImpl必须要同一个线程当中。

所以 弹出toast并没有报错误。




```

![image-20201204145749534](https://i.loli.net/2020/12/04/75gCAZoXVRrKSTp.png)

Q3

发送了handler消息之后 ， 让手机立即进入休眠状态。在休眠中，这条handler消息会被执行吗？

答案是不会。休眠时期cpu是不处理handler消息的。

但是如果有这种场景，
post了一个msg ，延时 1小时， 然后让手机休眠两个小时后，之前post的这个msg会被触发吗？

理论上应该是会的。
应为handler用的时间戳是 SystemClock.uptimeMillis()
而不是SystemClock.currentTimeMillis()

1. SystemClock.uptimeMillis : 启动后运行 的事件， 不包含深度休眠的时间
2. SystemClock.currentTimeMillis:
   日期时间， 是可以被修改的。

由于时间戳是用uptimeMillis打上的。 比较的时候 用的也是uptimeMillis  所以休眠之后 消息由于未到指定的uptimeMillis 所以 不会被取出。 等待接触休眠之后 消息才有可能会被取出。



#### looper 的无限循环 不耗费资源的 关键 - Linux 的epoll机制


loop .next（） 函数中 如果没有msg，那么就会调用linux的epoll命令，让该线程进入休眠状态，直到超时，或者有新msg进入时才唤醒。

整是由于epoll把线程给休眠了， 所以才能使得loop中的无限循环 不会占用太多的系统资源。



android 线程的消息来源除了app之外 还有软键盘 、屏幕啊等等。
epoll 后， 系统会把这些消息写入一个文件当中。
epoll的机制就是会监听这些文件。
系统把消息写入文件后就会唤醒java线程，java线程就会被唤醒了。

### IdleHander

IdleHandler  监听 当前线程的Looper进入了空闲状态。
用于处理不重要的任务， 避免抢占系统资源

在MessageQueue 中，如果有空闲的话，就处理idleHandler

是否空闲的判断
![image-20201204163223949](https://i.loli.net/2020/12/04/HbWFI9fYvxLZEpd.png)

![image-20201204162412663](https://i.loli.net/2020/12/04/hTOoQ26Bxim1G3n.png)





### ThreadLocal

[参考文章](https://www.cnblogs.com/aobing/p/13382184.html)



threadLocal 提供了线程独有的局部变量存储能力。
可以在整个线程存活的过程中随时取用。

![img](https://img.mukewang.com/wiki/5f1f89eb09548b3f10110496.jpg)



![image-20201204165859587](https://i.loli.net/2020/12/04/Qky5VoCY6bIFxKi.png)

![image-20201204170040825](https://i.loli.net/2020/12/04/7PvwpJrhxzT25FD.png)

这源码看起来  一个ThreadLocal的实例 只能存一个内容呀。

Looper里有一个ThreadLocal 实例

一个Thread里有一个ThreadLocalMap

一个ThreadLocal 可以在一个ThreadLocapMap里面存一个对象

所以实际上就是Looper用自己维护threadLocal对象，
在各个Thread中的ThreadLocalMap中都保存了一份
以threadLocal作为key的数据

![image-20201204170633584](https://i.loli.net/2020/12/04/yGVqNHzxrkIb3un.png)

![image-20201204170430567](https://i.loli.net/2020/12/04/M7cmQhu3V1iFP54.png)

另外ThreadLocal是有可能内存泄露的。

存再ThreadLocalMap当中真正的数据对象是Entry
而且实际上并不是用map来存的 ，而是用Entry数组

而是通过threadLocal的哈希值  来算出改ThreadLocal在该ThreadLocalMap中对应的序号来处理的

那hash冲突了怎么办？
除了用hash序号之外，还会对key进行比对，如果不对则去看下一个元素。
所以说这个序号只是一个最早可能早的位置，并不是绝对准确的。

![image-20201204174311149](https://i.loli.net/2020/12/04/jZlJthsBMVqiNUu.png)

Entry 是同时持有着一个弱引用和一个强引用。
虽然Entry是集成只弱引用。
但是弱引用需要在释放了 其持有的强引用之后 才能被Gc清除掉。

ps: 所以说弱引用也不一定一来GC就释放的。如果没有弱应用内部没有强引用的话，那么就是gc扫描到了就会回收的

所以如果一个thread的threadLocalMap中存储了值，
那么这个threadLocal就不能被回收，
这时候需要手动的把 threadLocal的强引用给手动置空才能使得ThreadLocal被回收。

```
泄露场景：

就比如线程池里面的线程，线程都是复用的，那么之前的线程实例处理完之后，出于复用的目的线程依然存活，所以，ThreadLocal设定的value值被持有，导致内存泄露。

但是实际上 ThreadLocal关心的任务已经被执行完了。
但是由于Thread的复用 导致 ThreadLocalMap里面的数据 还被持有着 导致不能被GC。
```



![image-20201204172455230](https://i.loli.net/2020/12/04/8Rrs9lKv4tVz25E.png)

解决办法 

源码中 在ThreadLocalMap的set函数中有把key为空的entry给释放的操作，但是并不能百分百保证。
 执行ThreaLocal.remove 就能保证value的释放了。



另外 从网上的说法来推断
当GC 检测到是， Entry的key 是会被回收的。
但是value 没有被回收，导致的Entry没有被回收。
所以

![image-20201204181942534](https://i.loli.net/2020/12/04/mC8Y7BwXo2K1Zdk.png)

![image-20201204182230534](https://i.loli.net/2020/12/04/MEHu6w9fDSVyzjl.png)





### Handler常见面试题



- 为什么主线程不会因为Looper.loop里的死循环卡死？
  ![image-20201204183105931](https://i.loli.net/2020/12/04/b4ZGmA197syNng2.png)
- post和sendMessage 两类发送消息的方法有什么区别？
  ![image-20201204183133355](https://i.loli.net/2020/12/04/fJXtGIw6paxEmFY.png)
- 为什么要通过Message.obtain()方法获取Message对象？
  ![image-20201204183200969](https://i.loli.net/2020/12/04/z6KelyJt2f47RIF.png)
- Handler实现发送延迟消息的原理是什么？
  ![image-20201204183328714](https://i.loli.net/2020/12/04/DZ4BGkTWYjAzhq6.png)
- 同步屏障消息的作用？
  ![image-20201204183359036](https://i.loli.net/2020/12/04/WNrR23bh6fcjwP1.png)
- IdleHandler的作用？
  ![image-20201204183416570](https://i.loli.net/2020/12/04/Dnlgq28mhzPF6GT.png)
- 为什么非静态类的Handler导致内存泄露？怎么解决？

![image-20201204183451232](https://i.loli.net/2020/12/04/IQsbOSqhW2gw975.png)

- 如何让在子线程中弹出toast

![image-20201204183529942](https://i.loli.net/2020/12/04/KIiLRtScpha678k.png)

```java
在子线程当中 执行Looper.prepare 以及Looper.loop
后即可 弹出Toast了。
    
也许会有个疑问， 这不是违反了 要在主线程更新UI的基本法则么？
    
实际上，view 并不是只能在主线程更新。
view的线程检查是在ViewRootImpl里面的checkThread来进行的。
源码看下图。
ViewRootImpl的线程检查并没有要求一定要在主线程当中。

而是要求checkThread和 创建ViewRootImpl必须要同一个线程当中。

所以 弹出toast并没有报错误。

```





## Android类加载机制











#### 双亲委派

##### 什么是双亲委派

![image-20201207101425223](https://i.loli.net/2020/12/07/ysprbSBX6UtYVoG.png)



![img](https://img.mukewang.com/wiki/5f1f8a6309f6733603840620.jpg)

##### 双亲委派的作用

![image-20201207101558504](https://i.loli.net/2020/12/07/4Pi3dIkoc2b6fQy.png)

#### Dex文件的加载

##### android中主要的类加载器

- PathClassLoader
- DexClassLoader
- BaseDexClassLoader 



其实PathClassLoader和DexClassLoader都没有做啥实现， 主要的就是把BaseDexClass的构造参数暴露出来。
，但其实也没用这些参数。这么做的目的估计是为了向前兼容。
所以实际上都是BaseDexClassLoader在处理。

PathClassLoader 

![image-20201207102312917](https://i.loli.net/2020/12/07/rDxSigmce5TuO6p.png)



![image-20201207102352054](https://i.loli.net/2020/12/07/DARTVvX8k1ewjtf.png)

这几个参数的意义
1.dexPath   :要加载的dex文件的路径
2.optimizedDirectory :加载出来的dex文件的 缓存  存放在路径
3.librarySearchPath要加载的c++链接库的路径



一个dex文件 ，会被加载成DexFile对象。
（optimizedDirectory 在8.0之后都是null了）

```java
class DexPathList{
  public DexPathList(ClassLoader definingContext, String dexPath,String librarySearchPath, File optimizedDirectory, boolean isTrusted) {
     //dexPath="/data/data/**/classes.dex:/data/data/**/class1.dex/data/data/**/class2.dex"
      //根据传递的dexpath加载出所有dex文件路径
      this.dexElements = makeDexElements(splitDexPath(dexPath), optimizedDirectory, suppressedExceptions, definingContext, isTrusted);
      //加载APP的动态库
      this.nativeLibraryDirectories = splitPaths(librarySearchPath, false); 
      //加载系统的动态库
      this.systemNativeLibraryDirectories =splitPaths(System.getProperty("java.library.path"), true);
      ......
  }
    
    private static Element[] makeDexElements(List<File> files,  optimizedDirectory, List<IOException> suppressedExceptions, ClassLoader loader, boolean isTrusted) {
      Element[] elements = new Element[files.size()];
      int elementsPos = 0;
      for (File file : files) {
          if (file.isDirectory()) { 
              elements[elementsPos++] = new Element(file);
          } else if (file.isFile()) {
              String name = file.getName();
              DexFile dex = null;
              //如果文件路径以.dex结尾，则直接加载文件内容
              if (name.endsWith(DEX_SUFFIX)) {
                  try {
                      dex = loadDexFile(file, optimizedDirectory, loader, elements);//  加载dex文件到内存当中
                      if (dex != null) {
                          elements[elementsPos++] = new Element(dex, null);//用一个数组来维护dexFile对象
                      }
                  } catch (IOException suppressed) {
                      System.logE("Unable to load dex file: " + file, suppressed);
                      suppressedExceptions.add(suppressed);
                 }
              } else {
                  try {
                     //如果是jar,zip等文件类型，则需要先
                      dex = loadDexFile(file, optimizedDirectory, loader, elements);//  加载dex文件到内存当中
                  } catch (IOException suppressed) {
                      suppressedExceptions.add(suppressed);
                  }

                   if (dex == null) {
                      elements[elementsPos++] = new Element(file);
                  } else {
                      elements[elementsPos++] = new Element(dex, file);//用一个数组来维护dexFile对象
                  }
              }
              if (dex != null && isTrusted) {
                dex.setTrusted();
              }
          } else {
              System.logW("ClassLoader referenced unknown path: " + file);
          }
      }
      if (elementsPos != elements.length) {
          elements = Arrays.copyOf(elements, elementsPos);
      }
      return elements;
    }
    //从这里可以看出 optimizedDirectory 不同,  DexFile对象构造方式不同，我们继续看看 optimizedDirectory 在 DexFile 中的作用：
    private static DexFile loadDexFile(File file, File optimizedDirectory)
        throws IOException {
    if (optimizedDirectory == null) {
        return new DexFile(file);
    } else {
        String optimizedPath = optimizedPathFor(file, optimizedDirectory);
        return DexFile.loadDex(file.getPath(), optimizedPath, 0);
      }
   }  
}

```



![image-20201207105859737](https://i.loli.net/2020/12/07/1ZULAiDfdGYjuEO.png)



上面已经知道 DexPathList用来一个数据来维护 所以dexFiel对象。

然后加载的时候 就按顺序的去遍历 该数组，看 要加载的类在哪个DexFile对象下。
找到第一个包含该对象的Dex文件即可。
这里面包含两个点：

- 如果有很多个dex文件，那么如果要加载的类对应的DexFile越靠后，那么加载的速度就越慢，因此为了提高某些重要类的加载速度，可以指定要最前面的dex文件。
- 对于一个类，只加载最早找到的那个dex文件的对应实现（  热修复的基础方案。在原先的DexPathList的数组前前插入一个Dex，这样就可以屏蔽掉原先的类的实现了。）

但是注意DexPathList的作用只是加载了dexFile，还不是class加载。



#### Class文件的加载



类的加载指的是将类的class文件中的二进制数据读入内存当中，将其放在运行时的数据区的方法区内，然后再堆区创建一个java.lang.Class对象。用来封装类再方法去内的数据结构，并且提供了访问方法区内的数据结构的方法。

重点就两个：

- 把class的数据加载到方法区中
- class对象放入堆中， class和方法区中的数据关联起来

类的加载是 在用到的时候在会去dexPathList去遍历DexFile的。



##### 类加载的步骤

![img](https://img.mukewang.com/wiki/5f1f8aa6092a3eaf53241882.jpg)

![image-20201207114520368](https://i.loli.net/2020/12/07/TpwUgdr7Vcmz8no.png)

![image-20201207114533094](https://i.loli.net/2020/12/07/tylSid8Kabx7r3M.png)





![image-20201207114541851](https://i.loli.net/2020/12/07/hT57ZLAPb1sRgGt.png)

![image-20201207114558727](https://i.loli.net/2020/12/07/1cMGWdQjF4ZBJLe.png)

```java
class MainActivity{
   //在准备阶段他得值为默认值0，初始化阶段才会被赋值为3.
   
   //因为把value赋值为3的putlic static语句在编译后的指令是在类构造器<clinit>（）方法之中被调用的，所以把value赋值为3的动作将在初始化阶段才会执行。
   static int value = 3；//0x0001
   
   int value2=3;//随着对象实例化的时候，才会被赋值
  
  static void test(){
      value2 = 100;//静态方法为什么不能访问非静态变量？
  }
}

```

上面demo对应的class字节码

![image-20201207115615383](https://i.loli.net/2020/12/07/fo9s58bS4d6yKOz.png)





**为啥不能静态方法里面调用实例变量？**
因为静态方法，和静态变量是在类加载的时候就和class对象一样放在了方法区。是全局唯一的。
而实例变量是创建实例的时候才创建出来放在堆中的。

如果在静态方法里调用非静态的变量的话，就存在该变量没有被初始化，没有被赋值的情况 。
而且静态方法是可以被类直接调用的**，用类来调用的话，**
**就根本不知道该去调用哪一个实例的实例对象。**





类的初始化 
类的初始化 并不是必须的。
和类的加载并没有绑定在一起。
比如Class.forName 通过ClassLoader.loadClass是执行类的加载，然后会额外执行类的初始化。
ClassLoader.loadClass本身并不会执行类的初始化操作。



这个类的初始化 的点 可以和单例引起的初始化顺序异常的问题可以结合起来。

![image-20201207144142587](https://i.loli.net/2020/12/07/jATKUJtEasX6Fnv.png)

为什么饿汉式会有实例变量比静态变量先初始化的情况呢?
原因是 声明静态的INSTANCE的时候 在类的初始化的时候就进行了。
这个静态的INSTANCE 执行了构造函数，那么在构造函数中被赋值的成员变量就会被赋值。然后再继续往下执行其他的静态变量的赋值，静态代码块的执行。

而静态内部类的方式的单例，其SingletonInstance是在调用到SingletonInstance.INSTANCE 才开始加载SIngletonInstance类对象。此时其外部类的类的初始化已经进行完了（也就是静态变量，静态代码块已经处理了）。所以可以保证静态成员的赋值能在构造函数之前。



##### Class.forName & ClassLoader.loadClass 的不同

![image-20201207143148408](https://i.loli.net/2020/12/07/MhIRA2zEiYoNw3H.png)





## Android的热修复



主流方案：

![image-20201207151127298](https://i.loli.net/2020/12/07/6zeQg2qhcCyrTSA.png)





AndroidStudio的INSTANCE Run的实现原理是 类加载方案。



从底层替换的方案 andFix已经废弃，Sophix 收费。而且也比较复杂。

这里主要研究类加载方案的代表Tinker.

### 动态加载dex方案

![img](https://img.mukewang.com/wiki/5f1f8c3d095e254584503246.jpg)

学习类加载的时候就已经知道了 ClassLoader中的DexPathList中维护了一个dexFile的数组。 动态修复dex的方案的原理就是把要替换的dexFile插入到DexPathList的数组的头部。让修复好的class代替要修复的class.



- 找到ClassLoader.DexPathList 
- 执行DexPathList.makeDexElements方法生成包含dex的数组
- 向dexPathList合并新加载进来的dex数组



### Tinker工作原理简述



- 使用bsdiff对新旧apk做差分异，得到差量化产物patch.apk。
  bsdiff是基于二进制的差异比较得到的包，体积会很小。

- 把新得到的差异包和目标版本进行全量合并，得到新的apk文件
  class文件 、so文件 -》  tinker-NClass.apk
  res文件  -》 resource.apk

  为啥class和res要分成两个包呢？
  是因为 class和res 类和资源的热修复方式不一样。

- 类更新 apk 进行dex插队来修复
  资源更新 用反射去替换AssetManager来完成修复



这里要强调， 下发的只是差分包，全量合并是在本地执行的。





# 设计模式 







## 单例

主要讲讲DCL的实现

### DCL

java

```java
public Demo {
    //使用volatile 关键字 使其能立即同步数据到主存当中， 否则可能会出现 子线程已经创建了对象。但是由于没同步到驻村当中，下一个持有锁的线程，没检测到该对象已经被创建了。
	private volatile Demo INSTANCE  = NULL;
    
    public Demo getInstance(){
        if(INSTANCE == NULL){
            synchronized(Demo.class){
                
                if(INSTANCE ==NULL){
                    INSTANCE = new Demo();
                }
            }
        }
    }
    
    private Demo(){
        
    }



}
```





```kotlin
//无参数
class Singleton3Kotlin private constructor(){
    companion object{
        val INSTANCE: Singleton3Kotlin by lazy(mode == LazyThreadSagetyMode.SYNCHRONIZED){
        Singleton3Kotlin()
    }
}
实际上这个lazy的内部实现就是 DCL

```

lazy 实现
![image-20201208173020701](https://i.loli.net/2020/12/08/e4NVnGkJbW2BpEf.png)

![image-20201208174236880](https://i.loli.net/2020/12/08/qhAlNK7Rx4ibvIZ.png)





## 装饰者模式


用买咖啡 挺好说明的。
coffe有普通咖啡  、 摩卡 、卡布奇诺...

而且coffe还可以加料。

如果每一种料都用子类来复写的话，那么子类就非常非常多了。
这时候就可以考虑装饰者模式了。



kotlin的装饰者模式实现 比java的代码量可以少非常多。

```kotlin

class Coffe{
    fun price():BigDecimal{
        // 基础coffe
        return BigDecimal.valueOf(1)
    }

    // 描述是哪中coffe
    fun  description():String{
        return "基础"
    }

    companion object{

        @JvmStatic
        fun main(args: Array<String>) {

            var description = Coffe().run {
                var desc = mocaDesc(description())
                cabuqinoDesc(desc)
            }
            println(description)

            var price = Coffe().let {
                var price = it.mocaPrice(it.price())
                it.cabuqinoPrice(price)
            }
            println(price)



            var description1 = Coffe().run {
                mocaDesc(description())
            }
            println(description1)

            var price1 = Coffe().let {
                it.mocaPrice(it.price())
            }
            println(price1)
        }

    }
}


//加 moca
fun Coffe.mocaDesc(desc:String ) :String{
    return desc +"add moca coffe"
}
fun Coffe.mocaPrice(price:BigDecimal) :BigDecimal{
    return price.add(BigDecimal.valueOf(2))
}

//加 cabuqino
fun Coffe.cabuqinoPrice(price:BigDecimal) :BigDecimal{
    return price.add(BigDecimal.valueOf(2.5))
}
fun Coffe.cabuqinoDesc(desc:String) :String{
    return desc +" add  cabuqino "
}
```

打印 
![image-20201209105409040](https://i.loli.net/2020/12/09/laXt7RVx6crYIwM.png)

 可以看出来 ，通过闭包和扩展函数，kotlin可以不用像java一样声明一堆的实际装饰者类

java中要用接口继承 和传入被包装者来实现约束，而kotlin中直接用扩展函数就能够实现约束了。



## 建造者模式

java用建造者模式的最重要点就是为了只暴露想让用户操作的方法，然后调用起来优美一些。有build这一层的 话，具体类就不需要向外暴露那么多接口的了。
能够达到分层的目的。

感觉kotlin由于闭包的存在 对内部赋值本身就是比较优雅的了。 就算不用build 这一层包装一下，也是能接受的。
用多一层build接口来包一层  并不是必须的。但是少了build层，那么还是会有向外暴露太多的问题。

## 适配器模式


主要参考RecyclerView.Adapter。

























































# 架构

Mvc 、Mvp   、MVVM



mvc就是最理所当然的把业务逻辑，数据请求都放在了activity里面去实现。


为啥出现mvp ？
因为mvc下 复杂业务时，activity太臃肿了。

那为啥出现mvvm？
因为mvp 一个activity出来就要实现好几个相关类
麻烦。
而且mvvm可以完成 数据和视图的双向绑定。

**双向绑定**

- 数据驱动 ui - 数据改变是 自动改变ui
- ui同步数据 - ui改变是 自动去请求新的数据





### MVVM



#### 传统的MVVM



activity里面只需要做些绑定操作。

数据的绑定通过dataBind来完成。
这个DataBinding 就是数据绑定 放在了xml里面去做，
使得activity里面的模板代码更少一些。
DataBinding是能支持 view和 model的双向绑定的



```java
public class Scene2Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityScene2Binding binding = DataBindingUtil.setContentView(this, R.layout.activity_scene2);

        final HomeViewModel model = new HomeViewModel();
        binding.setViewModel(model);

        model.queryUserInfo();

        binding.editAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                //当输入框文本变更后，userFiled的address的数据会自动更新，变成输入框输入的内容。这就是双向绑定
                Log.e(TAG, "afterTextChanged: " + model.userFiled.get().address);
            }
        });
    }

```



```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <data>
        <variable
            name="viewModel"
type="org.devio.as.proj.hi_arch.scene2.HomeViewModel" />
    </data>
    <LinearLayout>
         //单向绑定@
        <TextView
            android:id="@+id/nick_name"
            android:text="@{viewModel.userFiled.nickName}" />

        //双向绑定@=
        <EditText
            android:id="@+id/edit_address"
            android:text="@={viewModel.userFiled.address}" />
    </LinearLayout>
</layout>

```



#### Jetpack的MVVM

![img](https://img.mukewang.com/wiki/5ee81fdd09b7963719221442.jpg)





### 插件化

google 对插件化的管理越来越严格，  而且对各个手机厂家都对rom做了定制，导致插件化不能适配所有手机。

像 阿里 插件化已经退出了插件化  回归标准化。

但是回归标准化并不意味着放弃动态化改变代码.

只是换了中实现方式， 不再从替换dex文件 这方面入手。
而是通过xml 动态下发代码配置 来做动态化处理。

![img](https://img.mukewang.com/wiki/5f335128097513e439720898.jpg)











# Android组件

## DataBinding

原理

![image-20201209154133470](https://i.loli.net/2020/12/10/XGAZclHBW9b18u3.png)



## Hilt

动态 对象 注入框架

Hilt 是基于dagger2 的 ，但是配置上比dagger2简单很多。
Dagger2需要 有很多module之间的配置关系。
Hilt不需要。

### 组件类型

![image-20201215105540367](https://i.loli.net/2020/12/15/PKs3XEzBULDfA4n.png)



### 关键字注解

![image-20201215105710823](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20201215105710823.png)





### 基本用法

Hilt从用法上能分两个部分：	 

- 提供对象的地方
- 接收对象的地方

#### 提供对象的地方

先用

@Module  ： 来表明提供对象的类

@InstallIn:     来声明能对哪个组件提供对象

@Provides/@Binds : 要向外提供的对象

@ActivityScoped/@FragmentScoped....: 要向外提供的对象的生命周期



#### 接收对象的地方

@AndroidEntryPoint:  赋予接收对象的能力

@Inject : 标记要注入的字段



Hilt比dagger2好的一点。
不再需要搞module的相关配置。

Hilt不再关心是哪个module 给哪个组件。
组件和moduel之间都是共同的。

module的能力能给所有的@AndroidEntryPoint来使用。



之前dagger做注入的时候， 最重要的注入是ViewModule的注入 ，但是在kotlin里面 viewModel 已经纳入android 组件当中， 并且声明也很方便。
所以感觉这个ViewModel的动态注入不是很有必要。



所以说这个动态注入的框架就算在MVVM框架里面 也不是那么的重要了。



Hilt有一个 很大的缺陷。
**不支持从外面传参**



### 原理

Hilt运行时依赖注入原理

主要是有一下几点要搞清楚

- 被注入对象是何时被创建的？
- 被注入对象是何时传递进去的？
- 全局单例是如何实现的





先要找到入口，
实际上就是通过字节码插桩技术，Hilt把
有@HiltAndroidApp 和@AndroidEntryPoint 的父类给换了。
然后其中调用了Hilt的inject.等函数
所以 我们才不需要手动的去调用Hilt.inject。

![image-20201215183038166](https://i.loli.net/2020/12/15/j7KHlZxvayUwmfQ.png)

![image-20201215183241327](https://i.loli.net/2020/12/15/kT9ys7SoKcnlHzr.png)

入口就在Hilt替换的父类上。



**关于问题1： 对象是何时被注入的呢？**

以activity为例， 字节码替换的Hilt_***Activity 
中在onCreate前执行了 inject

![image-20201216093410299](https://i.loli.net/2020/12/16/qSQzofCRVIAZKyO.png)

![image-20201216093805851](https://i.loli.net/2020/12/16/T2y3V8DKAjZL7PS.png)

这个component的概念就是dagger2里面的 component( 注入器 )  在dagger2里面需要手动添加注解，Hilt里面是自动生成的。

那为什么需要component呢？
因为对象的生命周期需要管理， component就可以对这些对象的统一的生命周期管理。

另外component  是怎么把 module和component关联起来的呢？

HiApplication_HileComponents是把ApplicationC 和Module关联起来

![image-20201216095247647](https://i.loli.net/2020/12/16/mp8olRL6IUKiCbT.png)



ApplicationC 是抽象类 ，具体实现是DaggerHiApplication_*******ApplicaitonC

![image-20201216095803629](https://i.loli.net/2020/12/16/M2w3bUFxVAejs7c.png)

Hilt 给Application的替换上的父类Hilt_Applicaiton中就实例化了一个AppliationC,也就是实例化了一个Components
然后通过 这个Components来完成的对象的注入工作。

![image-20201216100853644](https://i.loli.net/2020/12/16/BrVChXYjv9SyGc2.png)

![image-20201216101606006](https://i.loli.net/2020/12/16/eyHPlzExIJhajFm.png)

所以说Hilt_Application的作用其实就是实例化了一个Components来给Application使用。

要注意 HiApplication_HiltComponents 是关联了整个工程的module和comonents

比如在另一个模块下的

![image-20201216102121851](https://i.loli.net/2020/12/16/361FuSy9mn4NPdE.png)

![image-20201216102157015](https://i.loli.net/2020/12/16/itqFK1zAOD5xYPU.png)

所以说 其实Hilt比Dagger多的@InstallIn(....)注解就是关联起Hilts已经内置创建好的components的。



看看Activity的Hilt注入

![image-20201216103247437](https://i.loli.net/2020/12/16/AhzCVKTMQwatILc.png)

由于创建注入器的时候 activity用的是this这个参数，所以虽然activity用的component都全关联着 @InstallIn(Activity)的module  但是 并不会相互影响。

![image-20201216111259016](https://i.loli.net/2020/12/16/a31EMGmbNuLfC9F.png)





另外  Components是怎么保证 单例的？ 并且实现数据共享的呢？

可以看下图

组件剪存在嵌套关系， 生命周期长的组件称之为生命周期端的组件的父容器。
ApplicationComponents是顶层容器。

正是由于嵌套关系的存在，所以可以方便的从父容器获取实例对象 （比如Activity就能从Applicatin里面拿到数据， Actiivity下的View 和 Fragment就能从Activity中拿到数据） ，通过这种层级嵌套的关系，就能实现单例对象的获取和数据的共享单例。

![img](https://img.mukewang.com/wiki/5f44984a086ef7b509881226.jpg)



**全局单例是如何实现的**

上面只说了数据共享的实现， 作用域下的单例是怎么实现的呢？
以Application为例



也就是会在作用于下 的component中有一个成员变量， 该成员变量只会被赋值一次。 从而保证了 全局的单例

```java
public final class DaggerHiApplication_HiltComponents_ApplicationC extends HiApplication_HiltComponents.ApplicationC {
  private final ApplicationContextModule applicationContextModule;
  //MemoizedSentinel无意义，就是用于判断是否已创建过iLoginService的真实实例对象
  private volatile Object iLoginService = new MemoizedSentinel();

  private LoginServiceImpl getLoginServiceImpl() {
    return new LoginServiceImpl(ApplicationContextModule_ProvideContextFactory.provideContext(applicationContextModule));
  }

  private ILoginService getILoginService() {
    Object local = iLoginService;
    //1.如果在module的方法上标记了scope,那么在创建对象时，会首先判断
    //2.是否还没创建过了，那就创建它实例对象，并把applicationContext作为参数传递进去
    //3.如果已经创建过了，那就直接返回，就达到了组件生命周期内单一实例的目的
    //4. 由于组件（component）是在android类(application,activity,fragment)中开始被创建的。所以组件的对象的销毁，自然也和他们关联了起来。
    if (local instanceof MemoizedSentinel) {
      synchronized (local) {
        local = iLoginService;
        if (local instanceof MemoizedSentinel) {
          local = getLoginServiceImpl();
          iLoginService = DoubleCheck.reentrantCheck(iLoginService, local);
        }
      }
    }
    return (ILoginService) local;
  }

```







# Gradle  



## 多渠道打包



### 定义

![image-20201216145902110](https://i.loli.net/2020/12/16/G2ZF7613alMwzEd.png)

### 需求的由来

![image-20201216150055350](https://i.loli.net/2020/12/16/HPC2DKgXnb3ExMQ.png)



### 原生的多渠道打包原理

核心原理就是在编译时通过gradle修改AndroidManifest.xml中的meta-data 占位符的内容，执行N次的打包流程

![image-20201216150908893](https://i.loli.net/2020/12/16/b497RyMQgCVk1Tz.png)

基本流程

- Manifest配置渠道占位符

  ```xml
  <meta-data
              android:name="channel"
              android:value="${channel_value}"/> //yyb,360
  
  ```

  

- app模块的build.gradle

```groovy
android{
    //产品维度，没有实际意义，但gradle要求
    flavorDimensions 'default'
    //productFlavors是android节点的一个节点
    productFlavors {
        baidu {}
        xiaomi { }
        yyb{}
    }
    //如果需要在不同渠道统一配置，使用productFlavors.all字段
    //替换manifest里面channel_value的值
    productFlavors.all { flavor ->
        manifestPlaceholders = [channel_value: name]}

    //设置渠道包名称-> howow-xiaomi-release-1.0.apk
    applicationVariants.all { variant ->
        variant.outputs.all { output ->
            //output即为该渠道最终的打包产物对象
            //设置它的outputFileName属性
            outputFileName = "hwow_${variant.productFlavors[0].name}_${variant.buildType.name}_${variant.versionName}.apk"
        }
    }
}

```



- 运行时获取渠道信息

  

  ```java
  private String getChannel() {
         PackageManager pm = getPackageManager();
         ApplicationInfo appInfo = pm.getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
         return appInfo.metaData.getString("channel_value");
  }
  
  
  ```

  



很明显这种原生的方式有致命的缺点 要重复打包，而打包是很耗时的操作。

而且由于Gradle会为每一个渠道都生成对应的不同的Builder文件， 来记录渠道信息 会导致每一个渠道的dex的CRC校验值是不同的。

这个CRC校验值 不同会带来 一个问题。
Tinker的热修复是基于差分补丁来进行的合并成新的APK的。所以要出个热修复的补丁的话，那么就需要Tinker的补丁要区分渠道，并且每一个渠道都要打对应的包才行。



另一种多渠道打包的方案

### APKTool打包原理

 ApkTool来做的多渠道打包方案

![image-20201216152028155](https://i.loli.net/2020/12/16/hbzmdVIcKkgCUTO.png)

以上方案的缺点就是两个

- 要多次打包（原生模式）， 或者多次签名（APKTool模式）
- 都要重新签名 ，影响签名信息 导致校验信息不对，从而影响差分包合并。



痛点就是

1. 不要多次打包
2. 不要影响签名文件



下面就来分析一下签名流程

### 签名

签名就是 apk文件摘要信息使用keystore里面的私钥加密后的产物，签名是摘要的加密结果

签名和内容对应不上 系统就会认为内容被篡改了，从而拒绝安装。



在apk  META-INF 目录下的 *.RSA 文件下有签名信息。

![img](https://img-blog.csdn.net/20140212101830000?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMzYzNjkwNg==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)



这个证书指纹 就是  公钥



整体的逻辑就是

打包时
F（内容（摘要）  + 私钥 ） = 签名

验证时

系统 有的信息：新内容（摘要） 、新签名、公钥

新内容（摘要）  = F（ 新签名 +公钥）

![img](https://upload-images.jianshu.io/upload_images/2438937-0098ad79aef8cd4a.png?imageMogr2/auto-orient/strip|imageView2/2/format/webp)





所以你的私钥不对的时候，  公钥解密解出来的摘要和内容计算出来的就对不上了。从而完成对身份的校验。


这里要注意一下

和HTTPS 不同   两边的加密算法是不一样的。
android签名  是用私钥进行加密 ，公钥进行解密。
Https是公钥进行加密， 私钥进行解密。

android的场景是 要让大家知道，信息是我发布出来的。
Https的场景是  大家说的只有我一个人听得懂。
所以解密方是不一样的。



#### V1签名

![img](https://img.mukewang.com/wiki/5f4e43ec09a5c1ce17100650.jpg)



上图就说明V1 的签名流程了。



关键点

- 对apk 原本的所有文件都进行了摘要计算
- 做了二级的摘要计算
- 对二级摘要计算的结果 进行加密处理  得到 签名

感觉这个摘要计算没啥必要啊。
一级摘要已经可以保证完整性了呀。

v1签名的缺陷

- 速度慢， 因为要对全部的文件进行摘要计算 所以速度是比较慢的
- 摘要存储的文件夹并没有参与 签名计算

不过这个摘要存储文件没有参与签名文件计算是一些快速批量打包方案的基础。






#### V2签名

是android 7.0之后引入的。

V2签名是基于apk的存储结构进行的。
apk的存储其实就是zip的存储

##### zip的存储结构

![img](https://img.mukewang.com/wiki/5f4e441709b4585a23901890.jpg)



zip 存储分成的三个部分。

#### ![img](https://img.mukewang.com/wiki/5f4e44260959ae3834900246.jpg)



V2的校验不再对单独的文件进行校验，
而是直接对apk整体分块（1M） 计算摘要，
然后再做二级摘要计算。

再进行私钥加密。

![img](https://img.mukewang.com/wiki/5f4e443609e00ea445102034.jpg)



得到的结果插入到apk文件的 文件数据区和文件目录区中间。

![img](https://img.mukewang.com/wiki/5f4e4445093fefb144940246.jpg)



并且插入签名区时，里面包含了多个ID - 值 的键值对信息。
签名信息会记录在  id： 0x7109871a 处。
其他的id 是不对被校验的。
所以说  可以在这里做文章。让渠道信息保存在这里，就能不破坏签名信息的前提下进行渠道信息的写入。





由于V2签名不再是对原先apk解压后进行摘要计算，而是直接对apk进行分块的摘要计算，所以速度上会快很多。
而且也不会像V1一样创建出无效的摘要信息存储文件夹。



## Gradle构建

### 基础

gradle分为了三个部分

![img](https://img.mukewang.com/wiki/5f4e44d10976059b09060598.jpg)

![image-20201217112126632](https://i.loli.net/2020/12/17/xnt1hwkmzv8yqMK.png)

### 项目构建生命周期

![img](https://img.mukewang.com/wiki/5f4e44d909e0221615380324.jpg)

疑问
settings.gradle  、 project下的build.gradle、module下的build.gradle的作用是啥？

这几个文件的加载 就对应着gradle构建的生命周期流程。
tips: gradle中没有module的概念， android studio的每一个module对于gradle来说都是一个project.

- 初始化阶段
  执行根目录下的settings.gradle 分析哪些project(module)参与本次构建
- 配置阶段
  加载本次参与构建的project的build.gradle文件，
  解析并且实例化成一个Gradle的Project对象，然后分析Project之间的依赖关系， 分析Project下的Task的依赖关系，
  生成 有向无环拓扑结构图
- 执行阶段
  这个真正Task被执行的阶段， gradle会根据之前分析的结果来决定哪些Task要被执行， 应该以何种顺序被执行。



**Tips**

```Tip
Task 是 Gradle中的最小执行单元，我们的所有构建任务都是执行了一个个的task，
一个Project中可以有多个Task，Task之间可以相互依赖。
```





gradle的任务命令  都会触发gradle的初始化阶段和配置阶段,区别只是执行Task任务。
包括Sync Gradle  /Gradle Clean 命令  都会。

也就是Gradle得执行了初始化 和配置 ，才能确定Task得怎么执行。



监听任务执行流程

在seettings.gradle的头部加上下列代码即可

//tips  对中文支持 不友好 ，用中文可能会报错

```gradle
// 1.在Settings.gradle配置如下代码监听构建生命周期阶段回调
gradle.buildStarted {
    println "项目构建开始..."
}
// 2.初始化阶段完成
gradle.projectsLoaded {
    println "从settings.gradle解析完成参与构建的所有项目"-->
}

//3.配置阶段开始 解析每一个project/build.gradle
gradle.beforeProject { proj ->
    println "${proj.name} build.gradle解析之前"
}
gradle.afterProject { proj ->
    println "${proj.name} build.gradle解析完成"
}
gradle.projectsEvaluated {
    println "所有项目的build.gradle解析配置完成"-->2.配置阶段完成
}

gradle.getTaskGraph().addTaskExecutionListener(
    new TaskExecutionListener(){
        //某任务开始执行前
        @Override
        void beforeExecute(Task task) {
            println("---20201217 before  task"+task.getName())
        }
        //某任务执行完成
        @Override
        void afterExecute(Task task, TaskState state) {
            println("---20201217 after task " +task.getName())

        }
    }
)


gradle.buildFinished {
    println "项目构建结束..."
}

```

打印task依赖

得到build.gradle下的build.gradle才行， 要不 project 会为空  settings.gradle是不会生成Project对象的
build.gradle才会生成对应 的project对象。

```groovy
afterEvaluate { project ->
    //收集所有project的 task集合
    Map<Project, Set<Task>> allTasks = project.getAllTasks(true)
    // 遍历每一个project下的task集合
    allTasks.entrySet().each { projTasks ->
        projTasks.value.each { task ->
            //输出task的名称 和dependOn依赖
            System.out.println(task.getName());
            for (Object o : task.getDependsOn()) {
                System.out.println("dependOn--> " + o.toString());
            }

//打印每个任务的输入 ， 输出
//for (File file : task.getInputs().getFiles().getFiles()) {
//    System.out.println("input--> " + file.getAbsolutePath());
//}
//
//for (File file : task.getOutputs().getFiles().getFiles()) {
//    System.out.println("output--> " + file.getAbsolutePath());
//}
            System.out.println("----------------------------------");
        }
    }
}

```

依赖的例子

![image-20201217121753334](https://i.loli.net/2020/12/17/NdUXp2QYabL6MWy.png)

![image-20201217121857789](https://i.loli.net/2020/12/17/x8bofVBGlCXO4dI.png)



各个build.gradle生成的project对象是有层级概念的。

![img](https://img.mukewang.com/wiki/5f4e450e0991ac7e18521070.jpg)



### Task & TaskTransform

Task是Gradle构建的最小单元，
Gradle通过把一个个的Task串联起来完成具体的构建任务。
每一个Task 都属于一个Project
Gradle在构建的过程当中会形成一个 依赖图，
这个依赖图会保证各个Task的执行顺序。

#### Task的配置项

![image-20201217142021819](https://i.loli.net/2020/12/17/5HfxawzdJ6rBo4C.png)



要注意Task的闭包代码块是在gradle的配置阶段就会被执行了的。并不是Task的执行阶段。如果想要在Task执行阶段来做处理的话， 得在Task中加入Task Actions来执行动作。
![image-20201217142404318](https://i.loli.net/2020/12/17/gXjJCVTvmFYuZkP.png)



#### Task的Action

gradle同一个闭包当中， 第二个doFirst 会比第一个doFirst先执行



```groovy
task tinyPngTask(group:'imooc',description:'compress images'){
    println 'this is TinyPngTask'   //1.直接写在闭包里面的，是在配置阶段就执行的
    doFirst {
        println 'task in do first1'  //3.运行任务时，后于first2执行
    }
    doFirst {
        println 'task in do first2' //2.运行任务时，所有actions第一个执行
    }
    doLast {
        println 'task in do last'   //4.运行任务时，会最后一个执行
    }
}

```

为什么会这样呢？
因为实际上doFirst 、doLast 最终都是添加到了同一个集合当中。
doFirst就是添加到集合的头部，
doLast就是添加到集合的尾部。
所以第二个添加的doFirst会比第一个先执行。



#### Task的依赖



#### dependsOn

dependsOn: 设置任务依赖关系。执行任务B需要任务A先执行

```groovy
ASProj/build.gradle
---------------------
task taskA {
    doFirst {
        println 'taskA'
    }
}
task taskB {
    doFirst {
        println 'taskB'
    }
}
taskB.dependsOn taskA   //B在执行时，会首先执行它的依赖任务A
------------------------
terminal:./gradlew taskB ===>taskA taskB
------------------------
terminal:./gradlew taskA taskB ===>taskA taskB

```



#### mustRunAfter  

设置任务执行顺序 
和 dependsOn不一样。
mustRunAfter 不需要执行任务A，但是任务A 、B都存在某一次的构建当中时，任务A必须先于任务B执行。



```groovy
ASProj/build.gradle
---------------------
task taskA {
    doFirst {
        println 'taskA'
    }
}
task taskB {
    doFirst {
        println 'taskB'
    }
}
taskB.mustRunAfter taskA //在一次任务执行流程中，A,B都存在，这里设置的执行顺序才有效
----------------------
terminal:./gradlew taskB ===>taskB
只执行TaskB时 B不会被执行
----------------------
terminal:./gradlew taskA taskB ===>taskA taskB    同时执行 task  A 和B , A被先被执行

```



#### finalizedBy

和dependsOn是相反的

只执行任务B 时，A也会在B结束后执行

```groovy
ASProj/build.gradle
---------------------
task taskA {
    doFirst {
        println 'taskA'
    }
}
task taskB {
    doFirst {
        println 'taskB'
    }
}
taskB.finalizedBy taskA   //B执行完成，会执行A
------------------------
terminal:./gradlew taskA taskB ===>taskB taskA
------------------------
terminal:./gradlew taskB ===>taskB taskA

```



#### Transform

Transform的作用：
如果我们想对编译时产生的Class文件，在转换成Dex文件之前做些处理（字节码插桩，替换父类...）。

我们可以通过Gradle插件来注册我们编写的Transform,注册后的Transform 会被gradle包装成TransformTask，这个TransfromTask会在java compile Task 执行完毕后运行。

一些Transform的使用场景：

- Hilt中 替换掉 父类
- Hugo耗时统计库 会在每个方法中插入代码来统计方法耗时
- InstantPatch热修复，在所有方法前插入一个预留函数，可以将有bug的方法替换成下发的方法
- CodeCheck 代码审查

gradle的处理流程

![img](https://img.mukewang.com/wiki/5f4e4525098c4a8c51120952.jpg)

从这个处理流程可以看出 TransformTask和普通Task的区别
TransformTask是一定要有input  output的
而普通的Transform是不一定有输入  输出的。



##### 如何自定义Transform

```java
public class MyTransform extends Transform {
    @Override
    public String getName() {
        return "MyTransform";
    }
    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        //该transform接受那些内容作为输入参数
        //CLASSES(0x01),
        //RESOURCES(0x02)，assets/目录下的资源，而不是res下的资源
        return TransformManager.CONTENT_CLASS;
    }
    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        //该transform工作的作用域--->见下表
        return TransformManager.SCOPE_FULL_PROJECT;
    }
    @Override
    public boolean isIncremental() {
        //是否增量编译
        //基于Task的上次输出快照和这次输入快照对比，如果相同，则跳过
        return false;
    }
    @Override
    public void transform(TransformInvocation transformInvocation) {

      //注意：当前编译对于Transform是否是增量编译受两个方面的影响：
      //（1）isIncremental() 方法的返回值；
      //（2）当前编译是否有增量基础（clean之后的第一次编译没有增量基础，之后的编译有增量基础）
      def isIncremental =transformvocation.isIncremental&&!isIncremental()
       //获取一个能够获取输出路径的工具
      def outputProvider= transformInvocation.outputProvider
      if(!isIncremental){
        //不是增量更新则必须删除上一次构建产生的缓存文件
        outputProvider.deleteAll()
      }
transformInvocation.inputs.each { input ->
            //遍历所有目录
            input.directoryInputs.each { dirInput ->
                println("MyTransform:" + dirInput.file.absolutePath)


                File dest = outputProvider.getContentLocation(directoryInput.getName(),
directoryInput.getContentTypes(), directoryInput.getScopes(), Format.DIRECTORY);
                //将修改过的字节码copy到dest，就可以实现编译期间干预字节码的目的了
                FileUtils.copyDirectory(dirInput.getFile(), dest);
            }

            //遍历所有的jar包(包括aar)
           input.jarInputs.each{file,state->
                  println("MyTransform:" + file.absolutePath)

             File dest = outputProvider.getContentLocation(
                        jarInput.getFile().getAbsolutePath(),
                        jarInput.getContentTypes(),
                        jarInput.getScopes(),
                        Format.JAR);
                //将修改过的字节码copy到dest，就可以实现编译期间干预字节码的目的了
                FileUtils.copyFile(jarInput.getFile(), dest);
            }
        }
    }
}

```

##### Transform的输入输出概念

![image-20201217152025580](https://i.loli.net/2020/12/17/tvZkorFRYIhz5dX.png)

##### 自定义Transforms的工作流程

![img](https://img.mukewang.com/wiki/5f4e4538089a1af808811280.jpg)



##### Transform Scope的作用域

![image-20201217151141566](https://i.loli.net/2020/12/17/WkCZUmxsugyXlK4.png)



##### 配置流程

参考

https://www.imooc.com/wiki/mobilearchitect/gradleplugin.html



##### 字节码处理原理

最重要的原理就是

在Transform里面用字节码插桩技术进行操作
Transform里面可以拿到java文件生成的class，
transform操作这个class从而来处理字节码。

字节码的工具有AspectJ, apt ，javassist....

这里选择javassist （上手简单一些）。



自定义Transform插件的基础配置

在buildSrc下  要有两个

![image-20201218100410738](https://i.loli.net/2020/12/18/75y9vDAY2uTkZfQ.png)

apply上目标插件
其transform只会对该module的project对象生效

![image-20201218100631958](https://i.loli.net/2020/12/18/3nj6PSv1Gxzwc5y.png)

##### TinyPngPlugin.groovy

```groovy

import org.gradle.api.Plugin
import org.gradle.api.Project

class TinyPngPlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {

    }
}
```

##### TinyPngPTransform.groovy

```groovy
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import javassist.ClassPool
import sun.instrument.TransformerManager

class TinyPngPTransform extends  Transform{
    private ClassPool classPool = ClassPool.getDefault()

    TinyPngPTransform(Project project) {
        classPool.appendClassPath(project.android.bootClasspath[0].toString())

        classPool.importPackage("android.os.Bundle")
        classPool.importPackage("android.widget.Toast")
        classPool.importPackage("android.app.Activity")
    }

    @Override
    String getName() {
        return "TinyPngPTransform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }


    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        def outputProvider = transformInvocation.outputProvider
        //outputProvider.deleteAll()

        transformInvocation.inputs.each { input ->
            input.directoryInputs.each { dirInput ->
                //遍历处理没一个文件夹下面的class文件
                handleDirectory(dirInput.file)
                // 将输入的所有目录复制到output指定目录
                def dest = outputProvider.getContentLocation(dirInput.name, dirInput.contentTypes, dirInput.scopes, Format.DIRECTORY)
                FileUtils.copyDirectory(dirInput.file, dest)
            }

            input.jarInputs.each { jarInput ->
                // 重新计算输出文件jar的名字（同目录 copyFile 会冲突）
                def jarName = jarInput.name
                def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4)
                }

                // 生成输出路径
                def dest = outputProvider.getContentLocation(md5Name + jarName, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                //处理jar包
                def output = handleJar(jarInput.file)
                // 将输入内容复制到输出
                FileUtils.copyFile(output, dest)
            }
        }
        pool.clearImportedPackages()
    }

    File handleJar(File jarFile) {
        //添加类搜索路径,否则下面pool.get()查找类会找不到
        classPool.appendClassPath(jarFile.absolutePath)
        //通过JarFile，才能获取得到jar包里面一个个的子文件
        def inputJarFile = new JarFile(jarFile)
        //通过它进行迭代遍历
        Enumeration enumeration = inputJarFile.entries()

        //jar包里面的class修改之后，不能原路写入jar。会破坏jar文件结构
        //需要写入单独的一个jar中
        def outputJarFile = new File(jarFile.parentFile, "temp_" + jarFile.name)
        if (outputJarFile.exists()) outputJarFile.delete()
        JarOutputStream jarOutputStream = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(outputJarFile)))

        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) enumeration.nextElement()
            String entryName = jarEntry.getName()
            InputStream inputStream = inputJarFile.getInputStream(jarEntry)

            //构建一个个需要写入的JarEntry，并添加到jar包
            JarEntry zipEntry = new JarEntry(entryName)
            jarOutputStream.putNextEntry(zipEntry)

            println("entryName:" + entryName)
            if (!shouldModifyClass(entryName)) {
                //如果这个类不需要被修改，那也需要向output jar 写入数据，否则class文件会丢失
                jarOutputStream.write(IOUtils.toByteArray(inputStream))
                inputStream.close()
                continue
            }
            def ctClass = modifyClass(inputStream)
            def byteCode = ctClass.toBytecode()
            ctClass.detach()
            inputStream.close()

            println("code length:" + byteCode.length)
            //向output jar 写入数据
            jarOutputStream.write(byteCode)
            jarOutputStream.flush()
        }
        inputJarFile.close()
        jarOutputStream.closeEntry()
        jarOutputStream.flush()
        jarOutputStream.close()

        return outputJarFile
    }

    void handleDirectory(File dir) {
        //将当前路径加入类池,不然找不到这个类
        println("handleDirectory:" + project.rootDir)
        pool.appendClassPath(dir.absolutePath)
        if (dir.isDirectory()) {
            dir.eachFileRecurse { File file ->
                String filePath = file.name
                println("filePath:" + file.absolutePath)
                //确保当前文件是class文件，并且不是系统自动生成的class文件
                if (shouldModifyClass(filePath)) {
                    def inputStream = new FileInputStream(file)
                    def ctClass = modifyClass(inputStream)
                    ctClass.writeFile(filePath)
                    ctClass.detach()
                }
            }
        }
    }

    //通过文件输入流，使用javassist加载出ctclass类
    //从而操作字节码
    CtClass modifyClass(InputStream inputStream) {
        //之所以使用inputStream ，是为了兼顾jar包里面的class文件的处理场景
        //从jar包中获取一个个的文件，只能通过流
        ClassFile classFile = new ClassFile(new DataInputStream(new BufferedInputStream(inputStream)))
        println("found activity:" + classFile.name)
        CtClass ctClass = classPool.get(classFile.name)
        //解冻，意思是指如果该class之前已经被别人加载并修改过了。默认不允许再次被编辑修改
        if (ctClass.isFrozen()) {
            ctClass.defrost()
        }

        //构造onCreate方法的Bundle入参
        CtClass bundle = classPool.getCtClass("android.os.Bundle")
        CtClass[] params = Arrays.asList(bundle).toArray()
        //通过ctClass

        def method = ctClass.getDeclaredMethod("onCreate", params)

        def message = classFile.name
        //向方法的最后一行插入toast，message为当前类的全类名
        method.insertAfter("android.widget.Toast.makeText(this," + "\"" + message + "\"" + ",Toast.LENGTH_SHORT).show();")

        ctClass.removeMethod(method)
        ctClass.addMethod()
        return ctClass
    }

//检验文件的路径以判断是否应该对它修改
//不是我们包内的class，不是activity.class 都不修改
    static boolean shouldModifyClass(String filePath) {
        return (filePath.contains("org/devio/as/proj")
                && filePath.endsWith("Activity.class")
                && !filePath.contains('R$')
                && !filePath.contains('$')
                && !filePath.contains('R.class')
                && !filePath.contains("BuildConfig.class"))
    }

}
```



## Gradle打包流程



### Apk的文件结构

![img](https://img.mukewang.com/wiki/5f4e478d08a98c7816000295.jpg)

![image-20201221095547509](https://i.loli.net/2020/12/21/eO9UIaZ3NoV6Kn7.png)





### aapt打包流程

aapt是之前的打包流程，现在已经是用aapt2来进行打包的了。
但是也能用aapt的打包流程来帮助理解

- aapt

![img](https://img.mukewang.com/wiki/5f4e47a808ca9b3005360882.jpg)

- aapt2打包流程

![img](https://img.mukewang.com/wiki/5f4e47b20943175b25942022.jpg)

![image-20201221100056863](https://i.loli.net/2020/12/21/Ak1scdxaN7LbZW2.png)



#### 打包命令流程参考

https://www.imooc.com/wiki/mobilearchitect/aapt2.html

主体流程

- aapt2 compie 
  编译资源文件
- aapt2 link 
  分配资源ID，并生成R.java文件和resource.arsc资源索引表
- javac 
  把java文件变异成class文件
- d8
  把class文件转换成dex文件
- aapt2 add
  把 资源文件 ，资源索引表文件， d8生成的dex文件打包到一起 生成apk文件
- zipaligin
  使用apksigner对apk进行字节对齐
  字节对齐是为了提高指令的运行效率，毕竟虚拟机对int的数据处理速度是最快的。
- apksign
  签名



上面只是对基础的编译过程，
并没有涉及到 多模块的资源合并等处理。



# 性能、稳定性





## 稳定性优化



### 降级策略：

兜底策略： 首次安装时 网络请求失败的话， 可以用跟包数据；

缓存策略：  先展示缓存数据 ，再更新接口返回数据，

要有远程清除脏数据的功能





### 日志收集

bugly 和 友盟 都是对crash日志 进行手机

xlog 是已打点的形式来进行文件写入





### Code Review



利用 gitlab的 分支保护  和webhook 来接收 pull request的请求。 然后负责人 review了之后， 再由负责人提交





### Java & Native Crash监控



- 抛出异常为什么会导致程序崩溃
- 如何捕获java crash异常
- 如何捕获native crash异常





#### java -DispatchUncaughtExecption

 java 捕获到一个 异常 exception 之后  就会交给线程的dispatchUncaughtException() 去处理

如果没有给线程set上dispatchUnaughtException的话呢，
就会给System.err去处理。

另外虚拟机在 ThreadGroup中 默认已经加上了一个dispatchUncaughtExecption了

在RUntimeInit的main中 设置了KillApplicationHandler，作为全部Thread的dispatchUncaughtException。
这个KillapplicationHandler 顾名思义就是导致 程序被杀死的地方了。

```java
class RuntimeInit{
  public static final void main(String[] argv) {
      ......
      commonInit();
      ......
  }  
  protected static final void commonInit() {
    /*
 * set handlers; these apply to all threads in the VM. Apps can replace
     * the default handler, but not the pre handler.
 */
    LoggingHandler loggingHandler = new LoggingHandler();
    //可以发现这里调用了setDefaultUncaughtExceptionHandler方法，设置了默认的异常处理器
    Thread.setDefaultUncaughtExceptionHandler(new KillApplicationHandler(loggingHandler));
   }
}

```



```java
// killapplicationHandler 中 kill了进程 以及退出了 虚拟机

private static class KillApplicationHandler implements Thread.UncaughtExceptionHandler {
    private final LoggingHandler mLoggingHandler;

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        try {
            // Bring up crash dialog, wait for it to be dismissed
            ActivityManager.getService().handleApplicationCrash(
                    mApplicationObject, new ApplicationErrorReport.ParcelableCrashInfo(e));
        } catch (Throwable t2) {
           
        } finally {
            // Try everything to make sure this process goes away.       
            Process.killProcess(Process.myPid());
            System.exit(10);
        }
    }
}

```



如果我们主动的把 framework设置的dispatchUncaughtExecption给替换掉的话，那么就不会执行   killApplicationHandle了。

不过意义不大，  出现这种情况之后， 虚拟机的内存很可能已经紊乱了。 就有可能出现莫名其妙的 不可预知的问题。
反而会影响 问题的定位。



注意  DispatchUncaughtException 只能处理 java层的crash





#### Native

native crash的监控其实是开启了一个线程去监控的。

这个线程一直循环的读peerFd文件 如果存在了的话，那就 处理native crash， 然后上报，最终转给

handleApplicationCrashInner。



应用层是无法直接监听native  CRASH的



## 性能优化





### 页面显示速度优化

![image-20201228111116398](https://i.loli.net/2020/12/28/MhLs7UVFRX4HZWA.png)

### 启动优化

![image-20201228111124899](https://i.loli.net/2020/12/28/rV7UJYspKPcGzdx.png)





分3个问题来解决：

- 启动耗时统计
- 启动阶段白屏优化
- 异步并发启动框架

#### 启动耗时统计



冷启动流程

- 创建进程

  - 加载window
  - 创建进程
  - 启动应用

  完全是由系统控制，和我们的代码没关系

- 启动应用

  - 启动主线程
  - 创建Application
  - 创建MainActivity

  这里开始就和代码有关系了

- 绘制界面

  - 加载布局
  - 首帧绘制

![img](https://img.mukewang.com/wiki/5f9cfb6a09f6ac2d09760516.jpg)

application 的onAttachBaseContext 是应用的第一个生命周期。 可以从这里开始计时。



那该在哪 结束计时呢？
activity.onResume吗？
答案是 不行的。 因为activity.onResume时，view还没开始测绘。这也是在onResume时，不能拿到View的height,width的原因。

应该是activity.onwindowFocusChange方法。

看下onWindowFocusChange的注释

注释上说了， 这个回调是activity被user看到的最佳时间点。

![image-20201228161912701](https://i.loli.net/2020/12/28/yUTAPrROG3K4Yl8.png)

不过 onWindowFocusChange 回调了之后， 也只是说明能响应 写死在xml 里面的内容了。
那些依赖于接口 ，数据库的内容 还是没有加载的。

如果需要看到指定的view的话， 那么可以使用  predrawListener来处理。



onPreDraw()的调用时机是在 该ViewTree下的view的测量都完成了之后，准备开始绘制时来回调的。

```java


findViewById(R.id.root_container)
    .getViewTreeObserver()
    .addOnPreDrawListener();


    /**
     * Interface definition for a callback to be invoked when the view tree is about to be drawn.
     */
    public interface OnPreDrawListener {
        /**
         * Callback method to be invoked when the view tree is about to be drawn. At this point, all
         * views in the tree have been measured and given a frame. Clients can use this to adjust
         * their scroll bounds or even to request a new layout before drawing occurs.
         *
         * @return Return true to proceed with the current drawing pass, or false to cancel.
         *
         * @see android.view.View#onMeasure
         * @see android.view.View#onLayout
         * @see android.view.View#onDraw
         */
        public boolean onPreDraw();
    }
```







所以启动的完整流程 是从
appliation.onAttachBaseContext 开始，
一直到 activity.onWindowFocusChange

打点耗时统计eg.

```java
1.application:
attachBaseContext:           0ms
attachBaseContext-end:       13ms
onCreate:                    85ms
onCreate-end:                300ms

2.MainActivity:                   
onCreate:                    378ms
onCreate-end:                667ms
onResume-end:                777ms
onWindowFocusChanged-end:    2s215ms   //xml渲染成view tree.用户可交互

3.RecyclerView:BannerItem.onPreDrawCallback
onFirtDraw:                  2s425ms  //response数据返回，第一帧开始渲染

```





#### 耗时方法统计的手段



除了正常的 

```
System.nanoTime();
```

还可以利用TraceCompat.beginSection(...)

```java
class RecyclerView{
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
        TraceCompat.beginSection("sectin名称***");
        dispatchLayout();
        TraceCompat.endSection();
    }
}

```

然后用android sdk 内置的 python脚本来处理 

```
$ cd ANDROID_HOME/platform_tools/systrace //切换到systrace工作所在的目录

$ python systrace.py -t 5 sched gfx view wm am app -a "org.devio.as.proj.main" -o start.html

```

然后会生成 一个html 文件

html文件eg.

![img](https://img.mukewang.com/wiki/5f9cfb750926fccf28781430.jpg)

就可以用可视化的方式来观察启动信息了。





#### 启动阶段白屏优化

```xml
<!--  在主题里加入 windowBackground -->
    <style name="LaunchTheme" parent="AppTheme">
        <item name="android:windowFullscreen">true</item>
        <item name="android:windowBackground">@drawable/launch_splash</item>
    </style>
```



#### 异步并发启动框架



![img](https://img.mukewang.com/wiki/5f9cfb8d09904b6611480876.jpg)

对于初始化， 有些任务可能有依赖关系
这时的异步启动框架需要考虑进去。

![img](https://img.mukewang.com/wiki/5f9cfb96096d8b4311301040.jpg)

需求就是完成这样的启动器。
其中任务1,3,5完成之后 ，就拉起 activity.
其他的任务继续在后台执行。







### 响应速度优化

![image-20201228111741341](https://i.loli.net/2020/12/28/bpmBivY4yd5Dw93.png)



爱奇艺的xcrash的原理是在系统层 用c++去注册了sign的监听， 然后去获取堆栈信息 存储起来。



### 内存优化







# 其他

## Apk加固原理

![image-20201222113855520](https://i.loli.net/2020/12/22/tGnpDHKf9jSxIwA.png)





![image-20201222114253174](https://i.loli.net/2020/12/22/lmqzQX1kOdWvDAU.png)



![image-20201222113942507](https://i.loli.net/2020/12/22/hm78tICjsa4npqg.png)

![img](https://img.mukewang.com/wiki/5f4e485d0956a96046441278.jpg)



![image-20201222114214548](https://i.loli.net/2020/12/22/KzGDU7qQ1xClvLd.png)





