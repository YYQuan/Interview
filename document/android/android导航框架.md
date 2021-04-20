# Android导航框架

![img](https://img.mukewang.com/wiki/5f2991f809ea460716341524.jpg)



ps：
Q：为啥要有路由？
A：

三个原因

1. 不利于解耦
   传统的路由：
   显示intent： 存在直接的类的依赖，
   
   弊端：需要能够直接访问目标类才行。
   
   隐式intent：各个组件需要在相互调用的时候 主要想mainfest里面去注册才行。也就是需要统一的管理，不利于多人协作
   
   弊端： 需要在manifest下配置标签属性。
   
   
   
   
2. 原生的路由方式,无法插手启动的任何环节。在startActivity之后无法拦截，不能捕获，降级，只能交给系统，这是如果跳转失败的时候 就有可能发生异常；

3. 原生和H5之间的跳转
   同一 原生和H5的跳转









常规有两个方案：
Navigation  ：jetbrains的方案
ARouter		： ali的方案

![image-20201016100312669](https://i.loli.net/2020/10/16/ZLN7AvHYknw5chy.png)

从表格来看，
navigation  不支持模块间通讯 、不支持拦截器、不支持降级操作 是最大的缺点。
但是navigation是支持在android生成导航视图。
而且支持回退

## Navigation

### Navigation的用法

navigation是依托于NavController的。
NavController   是依托于NavHostFragment。 



#### 配置NavHostFragmetn

![image-20210414145458741](https://i.loli.net/2021/04/14/RL8ezNriBHqxyQE.png)

![image-20210414145602294](https://i.loli.net/2021/04/14/GyTaSLR98md5n3J.png)

![image-20210414162748264](https://i.loli.net/2021/04/14/HYmudPpSnaDJyZT.png)



#### 获取NavController对象

```java
NavHostFragment.findNavController(Fragment)
Navigation.findNavController(Activity, @IdRes int viewId/*NavHostFragment的id*/)
Navigation.findNavController(View/*NavHostFragment的view*/)

```



#### 跳转

```java
NavHostFragment.findNavController(FirstFragment.this).navigate(
    R.id.action_FirstFragment_to_NavgationActivity/*配置文件中的配置的id*/,
    Bundle.EMPTY/*传参*/);


```









### Navigation架构原理

![img](https://img.mukewang.com/wiki/5ef5fd8709f1047a40342042.jpg)





NavHostFragment 是全部fragment的宿主。
所有的跳转都是通过NavHostFragment来处理的。

主要需要分析的流程
NavHostFragment的解析是在哪里进行的？
NavHostFragment路由间的跳转是怎么完成的？


先来分析 NavHostFragment的解析是哪开始的？

#### NavHostFragment的解析

onFlate函数是NavHostFragment的路由xml的入口
**PS**：对于可以在xml文件中声明的类， 在创建完毕之后 都会回调到其onInflate函数。

![image-20201017045528448](https://i.loli.net/2020/10/17/lOqLBtU4jEucNMH.png)

![image-20201017045559841](https://i.loli.net/2020/10/17/5nHf6Je71iUwZmj.png)



onflate中拿到了自定义的属性  graphid 以及  defaultNavHost


然后 接着看NavHostFragment onCreate

![image-20201019071517126](https://i.loli.net/2020/10/19/vVDmURz4bWThOIS.png)



在onCreate 中初始化了 mNavController

![image-20201019071855067](https://i.loli.net/2020/10/19/ZxiYUDp8F9tKAzj.png)



为啥 fragment的navigation没有和activity类型的navigation 放在一起注册呢？
因为 activity是应用不可或缺的， 而fragment并不是不可或缺的

接着往下看 onCreate

![image-20201019164841705](https://i.loli.net/2020/10/19/vLhSmVUd6eRMKjl.png)

往里 跟一下

![image-20201019165654395](https://i.loli.net/2020/10/19/AmSbK1GeW8k2JZd.png)



接着跟 setGraph

![image-20210414182107758](https://i.loli.net/2021/04/14/fZGltT82OEib4m1.png)





### 

![image-20201019170439642](https://i.loli.net/2020/10/19/yEpjiFmMHgbKSq7.png)



在NavigartorProvider中 根据传入的NavDestination的类型来选择Navigator来完成跳转
要注意 传给Navigator的Destination 是NavGraph 类型的， 里面还包含着navigationd xml的全部节点



#### NavHostFragment的缺点



1. 全部的路由节点都需要在资源文件当中配置
2. Fragment的路由跳转是使用replace方法， 会导致生命周期的重新调度
3. 没有办法对路由跳转进行拦截
4.  而且路由跳转在 模块化 组件化 中 很多坑





#### NavHostFragment的优化

优化点

-  摒弃 navigation.xml , 支持开发时注解标记路由节点
- 自定义Fragment导航器 ，避免 原本导航器中 调用的fragment的replace 
  导致的重置fragment生命周期
- 配置化 APP 主页架构， （这样可以支持服务端下发）

#### NavHostFragment的优化

优化点 

-  不在需要在xml里对节点进行配置，用编码时加注解来代替
- 新增fragment navigator , 代替原来fragmentNavgator的由于replace导致的生命周期的调用
- 支持动态配置fragment页， 方便服务器下发配置，实现千人千面





##### 注解处理器 Proceesor

![image-20201023070739497](https://i.loli.net/2020/10/23/pNWFe1o3dmXBLAn.png)



这个注解器应该起到的作用是 把代码中的Destination节点信息都收集起来   然后输出成一个配置文件。 来代替navigation.xml的作用

主题逻辑很直接 ， 直接看源码即可。

##### 生成配置文件

![image-20201023072941107](https://i.loli.net/2020/10/23/oqS1pfvEzBlUPtF.png)

接着就是解析配置文件

##### 解析配置文件

![image-20201023073619098](https://i.loli.net/2020/10/23/GyaOU3PnM7Cs62Z.png)

通过输出的文件 ，创建一个NavGraph 来模拟 navigation.xml被加载的过程



![image-20201023074220987](https://i.loli.net/2020/10/23/EHpUYwDAePSiaT3.png)



怎么代替默认的fragmentNavigation的处理呢？

![image-20201023074717964](https://i.loli.net/2020/10/23/ujM2AJotaKIniQF.png)



把自定义的navigator 添加到NavgateProvider里面，用自定义的navigator来创建destination即可。



自定义的FragmentNavigator怎么弄呢？

由于原本的FragmentNavitor fragment的相关属性是私有的，不能通过继承得到。 因此我们可以把全部源码拷贝出来，然后针对 navigate() 中的replace来进行修改。

![image-20201023075109451](https://i.loli.net/2020/10/23/SJ3vbPZVwIWzg25.png)



前面说多Module下 navigation会有坑，啥坑呢？

### navigation 多模块支持

参考
https://itnext.io/android-multimodule-navigation-with-the-navigation-component-99f265de24

https://github.com/DDihanov/android-multimodule-navigation-example

理解一下：这个多模块的实现就能大致知道 多模块下 有什么坑了。

有5个module:

- app
  依赖

  - 其他四个module

- commonui
  mainActivity在这里
  依赖

  - navigation

- dashboard
  有DashBoardFragment
  依赖

  - navigation

- home
  有HomeFragment

  依赖

  - navigation

- navigation
  没有依赖其他module

MainActivity中需要跨  commonui(自己所在module),home module ,dashboard Module来做路由跳转。

commonui module 感知不到 home和 dashboard module的。

此时他的做法就是 用公共的 module  navigation里面去声明 路由关系。
为了完成编译， 还需要在公共的navigation module下，声明一份假的同名路由文件。
真正的路由实现在各自的module下面。

![image-20210415110611545](https://i.loli.net/2021/04/15/ldzQ6Kw2VERoigY.png)

公共module下的 路由 通过 id 来避免直接声明 类。

从而避开了 获取不到其他module的类名的问题。

![image-20210415112253840](https://i.loli.net/2021/04/15/qArveEuUmgIJtiO.png)

也就是说 这个navigation的 多模块方案是利用了  通过公共的 module 去预先声明出  子模块的 路由文件（假文件），然后公共模块中实现跨模块的路由配置文件。
配置文件中也不直接使用类名，而是使用id,从而避免编译时找不到类名的情况。
公共模块中预先定义好 跳转的id， 其他模块通过公共模块中定义好的id就能得到对应路由操作的映射。

从这里可以看出来，navigation在跨模块下的工作时， 最大的障碍就是 获取不到类名。
虽然说可以通过用些虚假的id 通过编译，然后再在子moduel中具体的去实现。但是需要做很多假文件， 还需要在公共模块中去做路由操作的映射，人工维护起来还是挺 麻烦的。



### 总结

navigation其实就分两个部分： **加载器**（Navigator） 和 **节点信息**(NavDestination)。
加载器 由 NavigatorProvider 统一管理
节点信息由 NavGraph  统一管理

navigation 就是把 配置文件加载下载  储存成 NavGraph。
由于NavGraph有activiyt,fragment dialog等类型,所以需要用不同的加载器去加载。



## ARouters原理分析

- ARouter配置与基本用法
- ARouter编译时原理
- ARouter运行时原理

![img](https://img.mukewang.com/wiki/5f29930209e8f4e815940972.jpg)



Arouter的功能分为了三个阶段：

1. 开发时   用注解来标识
2. 编译时 把开发时标志下来的注解给收集起来 并且记录到文件当中
3. 运行时 Arouter负责界面的跳转



因此 Arouter最重要的两步就是 

1. 编译时原理：是怎么收集注解信息的  
2. 运行时原理：是怎么完成界面跳转的





### 基本用法

![image-20201021072946494](https://i.loli.net/2020/10/21/Pz21oSgaQrByskY.png)





说明一下  IProvider  

IProvider 可以支持跨模块的服务调用， 这样可以避免无关模块之间的强耦合。

IProvider的用法。

https://blog.csdn.net/u010194271/article/details/105216495

ARouter的跨模块调用流程：
一共四个Moduel

- app
  依赖其他三个

- base
  

- library1

  有一个Activity1
  只依赖base

- library2
  有一个Activity2

  只依赖base

如果是跳转Activity的话，那么直接在各个moduel下加@ARouter就行了。

但是ARouter还支持 跨模块通讯功能。

### 跨模块通讯

跨模块通讯怎么弄呢？

比如library2中 Activity2中 要使用library1 下的某函数。
那么就可以在Base包(公共包)下 声明一个接口 A ，A继承ARouter的IProvider

然后library1中 实现一个A的实现类， 这个类加上@Router.

library2要使用的时候 ，直接使用Router的autoWired来注入即可。

![image-20210415164002389](https://i.loli.net/2021/04/15/qIEXlM4UwY1LHKS.png)



### 编译时原理

ARouter通过注解处理器，收集开发者 做的标记，同时做出处理。
ARouter里面一般只有这三种注解标记：

- @Router   : 路由节点声明
- @AutoWired ： 参数自动注入
- @Interceptor ： 路由拦截器声明

**三个注解处理器**

![image-20201021073407007](https://i.loli.net/2020/10/21/7InogbDNmKlPCu3.png)



#### BaseProcessor



这三个注解器都是继承至 BaseProcessor


![image-20201027072230865](https://i.loli.net/2020/10/27/MI1jNT5JGipoHkE.png)



#### RouterProcessor

![image-20201027073150130](https://i.loli.net/2020/10/27/ZAquVzay6Rkxiw7.png)



然后逐个解析Route注解

![image-20201027073340826](https://i.loli.net/2020/10/27/gGUvN8KwzJft7T1.png)

解析 Router的参数  解析成RouterMete 然后  分组存储到GroupMap当中。
GroupMpa是一个以group名为key的Map  

![image-20210415180921601](https://i.loli.net/2021/04/15/wpLdxJKce81Rba5.png)



创建.java文件

![image-20201027074213180](https://i.loli.net/2020/10/27/9AFRvrIgGt7EBST.png)

这里简单说明一个javapoet
javapoet的简单生成java文件的方法

![image-20201027074418211](https://i.loli.net/2020/10/27/f7H8AS1pEis3YCv.png)



判断当前被route注解的类的类型

![image-20201027075218740](https://i.loli.net/2020/10/27/kWeMUj5S14YAN7r.png)

对比下比较low的方法

![image-20201027075552067](https://i.loli.net/2020/10/27/ZFm2c3hS7faz1tT.png)



#### AutoWiredProcessor

这一个注解处理器 的功能主要就是创建 对应的 参数注入类

比如对于TradeDetailActivity 

![image-20201027172227614](https://i.loli.net/2020/10/27/8jLG7VhDoNdPOzA.png)

![image-20201027172656903](https://i.loli.net/2020/10/27/21GoKlpHOiTmAJU.png)





对于TradeDetailActivity来说 ，AutowiredProcessor这个注解的 作用就生成
TradeDetailActivity$$$$Arouter$$$$Autowired .这个类。

然后在TradeDetailActivity调用ARouter的inject()的时候 会通过反射去调用对应Autowired类的inject函数。 从而完成赋值。



接下来看看 AutoWiredProcessor 是怎么生成 java类的
还是通过JavaPoet来完成的

![image-20201027173105543](https://i.loli.net/2020/10/27/krPdV843QjoEeMD.png)

![image-20201027172953296](https://i.loli.net/2020/10/27/zA3FEti6GVdgQkI.png)



主体就是扫描全部的autowired注解  然后通过javaPoet来生成 java类。


#### InterceptorProcessor

和AutoWiredProcessor 类似。

#### ARouter编译时 生成的类分析

以前面跨模块的验证demo生成类来分析一下。

- app
  ![image-20210416103824085](https://i.loli.net/2021/04/16/pFLDAqMj8RSGkBW.png)

- base
  虽然继承了IProvider但是并无ARouter的生成的文件
  因为并没有标注这些注解 @Router ，@AutoWir*  ,@Interceptor 

  

- library1
  ![image-20210416104246281](https://i.loli.net/2021/04/16/5LKd8oJBiO2X7MT.png)

- library2
  ![image-20210416104258215](https://i.loli.net/2021/04/16/9LURocukjDs4ETM.png)



以library1 为例（因为library1里 有 IProvider的实现类）。 



![image-20210416105818210](https://i.loli.net/2021/04/16/yKEQISutZXWFAM8.png)

从上面这个图 ，来猜测流程
arouter 在进行 init的时候 就把各个module下的root 类给加载了。
这样就能获取到  路由组 对应的类。这是并没真正加载对应类，而是等待执行路由的时候再分组按需加载。

等待执行路由的时候， 就会能知道要去哪个类执行加载。
这是ARouter的分组加载的实现。

路由组的文件中 就存贮了各个节点的 路由路径和 路由节点类型和路由真正实现类的对应。

有了真正实现的class，就能通过反射来获取。

所以可以说 神秘的路由其实就是通过 注解 ，然后编译是收集信息（路由路径和实际实现类的对应），执行时 通过反射去获取实现类。
整体的思路应该就是这样的。













### 运行时原理

初始化

![img](https://img.mukewang.com/wiki/5f29933b09898b3313521030.jpg)





代码分析：

![image-20201027174935985](https://i.loli.net/2020/10/27/oBMAJOSpHr9nDUl.png)

![image-20201027175256594](https://i.loli.net/2020/10/27/c7Zxm5oj3S9YRnU.png)



先要来读取全部dex文件

这里的读取dex文件路径的方式以后可能用的到

##### TIPs:  读取dex文件路径

```java
 public static List<String> getSourcePaths(Context context) throws PackageManager.NameNotFoundException, IOException {
        ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
        File sourceApk = new File(applicationInfo.sourceDir);

        List<String> sourcePaths = new ArrayList<>();
        sourcePaths.add(applicationInfo.sourceDir); //add the default apk path

        //the prefix of extracted file, ie: test.classes
        String extractedFilePrefix = sourceApk.getName() + EXTRACTED_NAME_EXT;

//        如果VM已经支持了MultiDex，就不要去Secondary Folder加载 Classesx.zip了，那里已经么有了
//        通过是否存在sp中的multidex.version是不准确的，因为从低版本升级上来的用户，是包含这个sp配置的
        if (!isVMMultidexCapable()) {
            //the total dex numbers
            int totalDexNumber = getMultiDexPreferences(context).getInt(KEY_DEX_NUMBER, 1);
            File dexDir = new File(applicationInfo.dataDir, SECONDARY_FOLDER_NAME);

            for (int secondaryNumber = 2; secondaryNumber <= totalDexNumber; secondaryNumber++) {
                //for each dex file, ie: test.classes2.zip, test.classes3.zip...
                String fileName = extractedFilePrefix + secondaryNumber + EXTRACTED_SUFFIX;
                File extractedFile = new File(dexDir, fileName);
                if (extractedFile.isFile()) {
                    sourcePaths.add(extractedFile.getAbsolutePath());
                    //we ignore the verify zip part
                } else {
                    throw new IOException("Missing extracted secondary dex file '" + extractedFile.getPath() + "'");
                }
            }
        }

        if (ARouter.debuggable()) { // Search instant run support only debuggable
            sourcePaths.addAll(tryLoadInstantRunDexFile(applicationInfo));
        }
        return sourcePaths;
    }

```



ARouter里面读取类名是用了多个线程同时执行， 但是加了个同步锁来处理
以后遇到这种场景可以借鉴这种同步锁的方式。

##### TIPs: 用同步锁来处理 等待多个子任务完成的场景

```java
        List<String> paths = getSourcePaths(context);
		//同步锁
        final CountDownLatch parserCtl = new CountDownLatch(paths.size());

        for (final String path : paths) {
            DefaultPoolExecutor.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                   		...
                        parserCtl.countDown();
                    }
                }
            });
        }

        parserCtl.await();

```



得到全部ARouter包下的类后  对部分进行处理



![image-20201027181637406](https://i.loli.net/2020/10/27/VlS3HzRvwbfmIAp.png)

也就是调用了对应的ARouter生成类的 loadInto函数



![image-20201027182253207](https://i.loli.net/2020/10/27/67LbTSt4ZKdvzCw.png)



其实Root的loadInto只是把 生成的对应组的 相关的class传了进去。 并没有做处理。



路由主体流程

![img](https://img.mukewang.com/wiki/5f2993440953378f24881348.jpg)





代码流程 就和流程图很贴合。



主要说明了主要有几点

1. ARouter拦截点 ： 
   a. 开始时 的路径替换， 
   b.路由前预加载
   c.降级处理
2. 按需 按组加载



### ARouter的缺点 -对比 navigation

arouter 对于fragment的处理 只能获取fragment对象实例， 然后再自己维护。

![image-20210415165921136](https://i.loli.net/2021/04/15/e4pwy1AOlXHEKnb.png)

但是navigation在同module下的 fragment跳转的处理就 方便很多。
虽然说navigation在跨module的时候 实现很麻烦，但是 同module下还是很方便的。

### 总结

​	ARouter 就分 两个阶段

​	编译时， 
​	运行时

​	编译时就是收集 各个注解的信息 生成文件， 并且 通过javapoet去生成一些java类。来辅助加载。
​	

​	运行时的入口是 arouter.init。 这时候就会去加载 一些必要的组件，比如拦截器，service ,内容提供器等。
​	然后在执行router跳转的时候，在按照组来加载进Router 维护的 资源类当中。在加载的过程当中，ARouter 提供了三次拦截操作， 路径替换，预加载，加载失败的降级处理。
​    这个就是ARouter的简单流程。
​	感觉这里面最有价值的就是 javaPoet。  看了这才发现 还可以这样来写java类的。



## 路由方案选择

navigation在 处理同module 同一个activity下的fragment的跳转 很方便
ARouter在处理 activity之间的跳转， 以及跨模块的 通讯时，很方便。
所以可以选择两个路由框架都使用，在同一个activity下的fragment跳转时，就可以使用navigation.(一般情况下，同一个activitiy的使用的fragment应该都是同module下的)。
其他的情况就使用ARouter.
这样两种路由框架的优点都能够汲取到。





















