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











# Android组件

## DataBinding

原理

![image-20201209154133470](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20201209154133470.png)















