# Jetpack的相关原理分析



## Navigation



详情看本目录下的

![image-20210417094702306](https://i.loli.net/2021/04/17/rHhUaEP9nGOcmFj.png)



## Lifecycle



lifecycler的作用就是对当前的 fragment,或者activity进行生命周期的监听。

基本用法

```java
public  class Fragment implements xxx,LifecycleOwner{
    LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);
    
    @Override
    public Lifecycle getLifecycle(){
        return mLifecycleRegistry;
    }    
}

class MyPresenter extends LifecycleObserver{
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
 	void onCreate(LifecycleOwner owner)   {}

     @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
.........
}

getLifecycle().addObserver(mPresenter);// 注册观察者 ，观察宿主生命周期状态变化
```



LifeCycle是怎么实现监听的呢？
分成两个部分： 对fragment的监听 以及  对activity的监听。



### Fragment

ilfecycler能对fragment起作用

1. fragment继承了lifecyclerOwner接口
2. 通过lifecyclerOwner的接口的返回类 LifecycleRegistetry 来进行生命周期分发



![image-20201201110622125](https://i.loli.net/2020/12/01/lMwG8dc6OFzAtq4.png)

![image-20201201110403308](https://i.loli.net/2020/12/01/6QroyceBfw8ASdg.png)

### Activity

activity的实现方式和 fragment的不一样。
是通过给activity添加一个空的fragment（ReportFragment.class） ,通过这个fragment的lifecyclerOnwer来进行生命周期消息的分发。

为啥会这样实现呢？
主要是为了兼容activity类,因为activity是没有实现LifeCycler接口的。
在AppCompactActivity中才实现了LifeCycler接口。



![image-20201201111755632](https://i.loli.net/2020/12/01/seOZjTQPyxHhDpt.png)

![image-20201201112005779](https://i.loli.net/2020/12/01/iBpaf5JjZ6d83EM.png)

![image-20201201112040096](https://i.loli.net/2020/12/01/ys3FLI6k2OGAnT7.png)

这里 注意：
Lifecycler的生命周期状态和生命周期事件并不是一回事。
注册的lifecycler的observable会**前进**到该宿主的当前生命周期状态。
注意是前进 是一步一步走的， 而不是一步到位。所以经过的生命周期的监听是能够接收到的。
也就是说，如果是在onResume时进行 lifecycler的监听，那么observable就会有从create-》start->resume 这三个事件。

![img](https://img.mukewang.com/wiki/5ee820ce0947f7e314000762.jpg)

## LiveData

liveData可以理解为一个具有生命周期感知能力的 数据监听。

LiveData发送数据有两种方式：

- set  ：只能在主线程中执行
- post : 可以在子线程中执行

最终回调都是在 **主线程** 中。



### LiveData的特点

特点

- 具有生命周期感知能力 -不再需要手动处理生命周期
- 在界面不可见时，不分发消息 ；界面可见之后会派发最新的消息给所有观察者，（包括消息产生之后的注册的观察者）。

LiveData为啥能够具有这种特点呢？
因为LiveData是依托于 LifeCycler的，LiveData的生命周期感知能力都是基于LifeCycler的。
特别是 对于消息产生后的观察者 是怎么同步消息？就是利用lifecycler的生命周期分发的回调进行触发，然后根据该观察者的状态位来判断要不要分发给他的。



![img](https://img.mukewang.com/wiki/5f0d186f09c853fd25490962.jpg)

![img](https://img.mukewang.com/wiki/5ee8243109c6b1c211470764.jpg)



### set  和post的区别

setValue
![image-20201201155255296](https://i.loli.net/2020/12/01/y5tQ1aVMImASJUd.png)

postValue:
 post其实就是发送了个mainHandler的消息把set放到了主线程的handler下 去执行。



![image-20201201154138558](https://i.loli.net/2020/12/01/vSIJwXYWFqRGnya.png)

![image-20201201154559863](https://i.loli.net/2020/12/01/SF54mnO8Tj3GJvd.png)



## ViewModel

viewModel是一个具有生命周期感知能力的 数据存储组件。

ViewModel的最大优势就是 ViewModel中的数据不会随着横竖屏，分辨率这些参数的改变而被销毁。
也就是不需要在onSaveInstanceState里面去恢复数据啥的了。

ViewModel 一般是配合LiveData来用的

先看基本用法

### 基本用法

![image-20210417110701766](https://i.loli.net/2021/04/17/JGThzs4Fv39b7AH.png)

```kotlin
class MyViewModel : ViewModel() {
    var liveData  = MutableLiveData<Int>()
}
```



下图旋转销毁重建activity后， viewModel还是同一个， 但是acitivity已经不是同一个了

![image-20210417112337381](https://i.loli.net/2021/04/17/lxuF75WnRKMVgLZ.png)

为啥 传入的 activity不是同一个了   拿到的viewmodel还是同一个呢？
先观察一下Provider是不是同一个

下图 可以判断 provider不是同一个 ，但是拿到viewModel是同一个。

![image-20210417112849010](https://i.loli.net/2021/04/17/XTkY4RV5MdCAJfm.png)



那就得跟一下 ViewModel的get

![image-20210417113152813](https://i.loli.net/2021/04/17/n3ovNLGjty9r17m.png)

从下图的get逻辑中 可以读出 重点是 viewModelStore中用key去获取ViewModel的实例

![image-20210417113331801](https://i.loli.net/2021/04/17/QdRabFHpOJr72sW.png)

接下来看看 ViewModel
下图可以看出 ViewModel 其实就是一个HashMap

![image-20210417113248300](https://i.loli.net/2021/04/17/VhqYPXfoMvetFbO.png)

所以接着来的重点就是
这个ViewModelStore从哪里来的？



### ViewModelStore

从ViewModelProvider的构造函数里 就可以找到是ViewModelStoreOnwer的接口提供了ViewModelStore对象

![image-20210417113730839](https://i.loli.net/2021/04/17/UivAzZh2Ltxqya7.png)

下图可以看出 activity和fragment都实现了这个接口

![image-20210417113913476](https://i.loli.net/2021/04/17/XfaJzpnYCux1PMs.png)

接着看看Activity的ViewModelStoreOwner是从哪里来的
看下Activity里 ViewModelStoreOnwer的接口的实现。

下图可以

activity中的ViewModelStore是从NonConfigurationInstances 中拿的。


![image-20201201181317601](https://i.loli.net/2020/12/01/24Kj8xzf7YeSPDC.png)

那现在就是要看这个NonConfigurationInstances是什么鬼了

#### NonConfigurationInstances

跟一下  getLastNonConfigurationInstance() 函数。
找到了下面这里。
从下图可以判断，viewModelStore 是由ams 在启动activity时 ，通过attach方法传入的。（attach方法由AMS 来执行 是acitivity启动流程中分析可以得知的）

所以说 viewModelStore 这个实例是ams传下来的。

![image-20210417114736113](https://i.loli.net/2021/04/17/FbhyAj6VSnB4esQ.png)

既然ams传下来的 ，那肯定有一个地方传到ams去。
直接用NonConfiguration做关键字 搜一下Activity的函数

![image-20210417115255991](https://i.loli.net/2021/04/17/LbJyf3DvkY9dFSO.png)

发现有个retainNonConfigurationInstance 函数。
这个名字就很像 保存NonConfiguration的地方。
看一下。

![image-20210417115519440](https://i.loli.net/2021/04/17/hco7TSdjFW8a3KZ.png)



此时可以来验证一下，旋转屏幕后 销毁重建Activity时，viewModelStore是不是同一个，从源码来看 应该是同一个。

从下图来看， 的确viewModelStore是同一个

![image-20210417115641528](https://i.loli.net/2021/04/17/zOJpAK7vx9mn6HS.png)

关于onRetainNonConfigInstance的调用时机 ，as代码里没找到线索。但是肯定是onDestory触发的。所以就不用需要再继续往下跟了。
看到这就差不多了。



### ViewModel进阶用法-savedState

- SavedStateHandle的数据恢复与存储，即使ViewModel对象不是同一个也能够恢复，
  所以在低电量和低内存的情况也能够完成数据的恢复。

用法

![image-20210417141514863](https://i.loli.net/2021/04/17/IQg3qX6l7edFLJT.png)

测试， 在开发者模式里 打开 不保留活动（也就是按home键 就销毁activity）

![image-20210417141342357](https://i.loli.net/2021/04/17/SvCqRa8ft6bYu9d.png)

MyViewModel.kt

```kotlin
class MyViewModel(h: SavedStateHandle) : ViewModel() {

    var liveData  = MutableLiveData<Int>()


    private var handle = h

    //初始化数据
    init {
        liveData.value = 0
    }
    //将数据加1
    public fun add() {
        getNumber().value = getNumber().value?.plus(1)
    }

    //获取数据
    public fun getNumber(): MutableLiveData<Int> {
        if (!handle.contains("KEY_NUMBER")) {
            handle.set("KEY_NUMBER", 0)
        }
        return handle.getLiveData("KEY_NUMBER")
    }
}
```

MyOtherViewModel .kt

```kotlin
package com.mingxin.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class MyOtherViewModel(h: SavedStateHandle) : ViewModel() {

    var liveData  = MutableLiveData<Int>()


    private var handle = h

    //初始化数据
    init {
        liveData.value = 0
    }
    //将数据加1
    public fun add() {
        getNumber().value = getNumber().value?.plus(1)
    }

    //获取数据
    public fun getNumber(): MutableLiveData<Int> {
//        if (!handle.contains("KEY_NUMBER")) {
//            handle.set("KEY_NUMBER", 0)
//        }
//        return handle.getLiveData("KEY_NUMBER")
       return liveData
    }
}		
```

不一样的地方只有

![image-20210417141804124](https://i.loli.net/2021/04/17/Mg9NCWumi3yjord.png)





#### ViewModel使用 SavedStateHandler

需要向demo里一样用

下面这种方式吗？

```kotlin
   var viewModelSave = ViewModelProviders.of(this, SavedStateViewModelFactory(application, this))
            .get(MyViewModel::class.java)

```

其实不需要 ，只要在Viewmodel的构造函数里 增加SavedHandler的参数即可。
为啥呢？
因为viewModel的工厂在创建viewModel的时候 就会检测 是否有带着SavedHandler的改造函数 ，有就直接把SavedHandler给加上去。

代码路径如下：

![image-20210417144158364](https://i.loli.net/2021/04/17/c1hLMrz7dGWplqs.png)

所以直接给ViewModel的构造函数加上SavedHandler就能用下面这种方式直接获取带着SavedHandler的ViewModel

```kotlin

var modelProvider  = ViewModelProvider(this)
val viewmodel  = modelProvider.get(MyOtherViewModel::class.java)
```



那接下来就要分析 为啥SavedStateHandler能做到 进程被杀死后 还能拿到之前的数据了

#### SavedStateHandler的原理

首先 先要看SaveStateHandler 从哪里来的。
根据前面分析 可以知道 要到ViewModel的工厂中去看。



![image-20210417151555783](https://i.loli.net/2021/04/17/B8Qva134DTGVeu2.png)

通过下图可以看出SavedStateHandler的数据来源是SavedStateRegistry , 并且是以viewModel的class name 作为key来获取的。

![image-20210417152850393](https://i.loli.net/2021/04/17/17JqmVhBlaZbLDr.png)

所以现在要分析的就是SavedStateRegistry

##### SavedStateRegistry

SavedStateRegistry和ViewModel的关系如下图
![img](https://img.mukewang.com/wiki/5ee8308409439aee15640642.jpg)



现在就要追溯一下 SavedStateViewModelRegistry的来源了

从下图来看 可以看出 就是Activity和fragment实现了SavedStateRegistryOnwer的接口，从接口实现那获取registry的实例的。

![image-20210417154115810](https://i.loli.net/2021/04/17/wiqOkrjaU95Bx8p.png)

往下跟



##### SavedStateHandler数据的存储时机

从下图来看，存储的时机就是在onSaveInstanceState当中

![image-20210417154352992](https://i.loli.net/2021/04/17/9ECOWIj7nktPH5d.png)

![image-20210417154803521](https://i.loli.net/2021/04/17/OKqkz9ri3FELCnD.png)

registry里面保存着全部的savedStateHandler的信息，因此就可以吧SavedStateHandle里的数据保存到bundle里面去了。
现在就知道SavedStateHandler怎么保存的了。

##### SavedStateHandler的恢复时机

猜测  既然是在onSaveInstanceState里做的存储 ，那么自然就应该是在restoreInstanceState里面恢复的啦。
找找线索。

从下图可以看出  恢复数据是在onCreate里恢复的，并不是像想的那样在restore里恢复的。
从这里也可以判断出，onSaveInstanceState里保存的数据 oncreate这就能获取到的

![image-20210417160452091](https://i.loli.net/2021/04/17/HB8p71ideQoluPw.png)

存的时候是以viewmodel的class name作为key  bundle 作为value的。
恢复的时候，有对应的videmodel 就把数据给回去即可。

### 小结：

android的界面临时数据的保存可以说是由两套机制。

1. onSaveInstanceState  时保存 到bundle中

2. onRetainNonConfigInstance 时保存到NonConfig实例的ViewModelStore参数中 ， 在activity attach函数调执行时 ，由ams传入。

   

ViewModel常规用的是NonConfig 这种方式。
但是对于第二种NonConfig的方式，进程整个都被重建的情况下，数据还是会被丢失的。第一中 onsaveInstance不会。
所以ViewModel还支持借助onSaveInstance这个通道的方式来保存数据。也就是SavedStateHandler的方式。

那为什么有了onSaveInstance这种方式更加可靠的方式  还存在这个nonConfig这种方式呢？
因为NonConfig这种方式是在进程中进行通讯的，而onSaveInstanceState是可以在进程间通讯的。
进程中的通讯效率比进程间的要高，所以两种方式都是由意义的







## Room  -后序补充


 轻量级orm数据库，本质上是一个SQLite的抽象层

暂时先不研究这个 ，先记住是SQLite的抽象层即可
面试直接说 我们现在用的是自己封装的SQLite.
并且也配套的实现了类似 Room和LiveData配合的数据监听。
虽然需要维护监听的生命周期 但是也不算大事。
没啥必要就没有迁移到room上。

### 创建数据库

```kotlin
@Database(entities = [Cache::class], version = 1)
abstract class CacheDatabase : RoomDatabase() {

    companion object {
        private var database: CacheDatabase
        fun get(): CacheDatabase {
            return database
        }

        init {
            val context = AppGlobals.get()!!.applicationContext
            database =
                Room.databaseBuilder(context, CacheDatabase::class.java, "howow_cache").build()
            
        }
    }

    abstract val cacheDao: CacheDao

}
```

### Room的ADUC

![image-20201128162329940](https://i.loli.net/2020/11/28/uqCV5h7ckWKxTS3.png)





Room 可以和LiveData  绑定使用， 从而可以直接实现对数据库中的数据进行监听。
![image-20201202145920386](https://i.loli.net/2020/12/02/q2mpQxTlFA5zNCv.png)

用法很简单 ，就在@Dao注解下的接口处，实现table_cache即可。



### Room支持内存数据库  （在进程被杀死之后  数据丢失）

```java
Room.inMemoryDatabaseBuilder()
                 Room.databaseBuilder(context, CacheDatabase::class.java, "howow_cache").build()

```



疑问：
Q1: 这个注解是怎么创建出SQLite表的？
通过注解在编译时， 构建出了对应的java类。

Q2: 这个LiveData  是怎么实现监听的？
当第一个LiveData的观察者被注册进来的时候，才触发查询数据库 发送数据更新。
那是怎么实现对数据库进行监听的呢？
实际上就是Room的内部 专门维护一个保存哪些表被更新了的表， 其他表的增删改操作会触发更新表的对应的tabID字段置为1

![image-20201202161101795](https://i.loli.net/2020/12/02/kxgnEUTXzHlL174.png)

总流程

1.LiveData的首个观察者进来的时候触发  查询数据库 更新LiveData
首次向LiveData注册Observer是利用LiveData的onActive回调来做的。
感觉这个omActive可以处理很多懒加载的场景。

2.当表被更新之后，会多加一个任务，就是遍历Room内部维护的更新表，然后找出被更新了的表单， 分发消息，接着接收到消息的观察者就去查询最新记录，然后通过LiveData发送数据出去。

## DataBinding -后序补充

数据与视图的双向绑定

在xml 中写入绑定逻辑， 可以减小activity和fragment的压力。

本质就是编译时生成响应代码。

只要找到对应生成的代码， 就并不存在网上有些人说的 不方便调试的问题了。

## WorkManager -后序补充

新一代的后台任务管理组件；

service能做的他都能做。

甚至app退出了仍然可以保证任务的执行

是一个系统级的服务调度。

workManager是可以支持 链式 调度的。

可以指定执行的顺序。

![image-20201128165423938](https://i.loli.net/2020/11/28/BXLwpSsndyjr2C5.png)



