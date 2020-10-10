#  android机制



##  启动流程

启动流程： 系统系统 -> launcher启动 ->应用启动

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