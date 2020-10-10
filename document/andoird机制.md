#  android机制



##  启动流程





![img](https://img.mukewang.com/wiki/5f298fb9090604b418741390.jpg)

1. 按电源键通电后，启动bootloader

2. 然后kernel内核层加载驱动，以及启动内核核心进程

3. 硬件驱动与HAL层（硬件接口层是内核kernel和frameWork中间的一层）交互 启动 **Init进程**

4. Init进程就会fork出**zygote进程** ，zygote进程是C++ frameWork层和java frameWork层的唯一桥梁， ***zygote进程会通过反射调用 ZygoteInit 来进入java层***

5. zygote进程会启动相关的app进程， **第一个启动的就是launcher进程** ，**所有的app进程都是由 zygote 进程fork 出来的**。 

   

   

   PS:***zygote进程中会预加载上一些系统资源文件，通过zygote进程fork出的app进程共享了父进程的物理空间，就不需要重新的加载这些资源文件了。从而可以提高启动速度***


从上可知 ，java层的总入口就是在zygote进程中反射调用到的 zygoteInit.java了

接下来看看zygoteInit

### ZygoteInit

ZygoteInit.java: Java层FrameWork的入口

```java
//java类的入口是main  zygoteInit.java 也是从 main函数开始执行的

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

### SystemServer

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

![image-20201009183924746](https://i.loli.net/2020/10/10/JEnbABkdPX4Ye6h.png)0