# 移动架构 





## Tips：

### 单位转换工具类：

sp转px
![image-20201119165241774](https://i.loli.net/2020/11/19/rjlR9X7OGZwngzc.png)

![image-20201119165233684](https://i.loli.net/2020/11/19/rCOUSPFKJgxiEz7.png)

```java

TypedValue.applyDimension(applyUnit, value, resources.displayMetrics).toInt()
```



### 全局任意位置拿到Application

ActivityThread的内部维护着application
可以通过反射来拿Application

```kotlin
object AppGlobals {
    private var application: Application? = null
    fun get(): Application? {
        if (application == null) {
            try {
                application = Class.forName("android.app.ActivityThread")
                    .getMethod("currentApplication")
                    .invoke(null) as Application
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        return application
    }
}
```





### 占位符Space

space 是不参与绘制的， view是会参数的。
如果只是为了占位的话，那么用space可以节省一些性能

```java
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="@dimen/dp_45"
        android:gravity="center_vertical"
        android:orientation="horizontal">

        <org.devio.as.proj.common.ui.view.IconFontTextView
            android:id="@+id/item_course"
            android:layout_width="wrap_content"
            android:layout_height="match_parent"
            android:gravity="center_vertical"
            android:text="@string/item_notify"
            android:textColor="@color/color_444"
            android:textSize="@dimen/sp_14" />

        <!--        <View-->
        <!--            android:layout_width="0dp"-->
        <!--            android:layout_weight="1"-->
        <!--            android:layout_height="1px"></View>-->
        <Space
            android:layout_width="0dp"
            android:layout_height="1px"
            android:layout_weight="1" />

        <TextView
            android:id="@+id/notify_count"
            android:layout_width="@dimen/dp_16"
            android:layout_height="@dimen/dp_16"
            android:background="@drawable/bg_red_circle"
            android:gravity="center"
            android:textColor="@color/color_white"
            android:textSize="@dimen/sp_10"
            android:visibility="gone"
            tools:text="10"
            tools:visibility="visible" />
    </LinearLayout>
```



### android支持的富文本

```
SpannableStringBuilder
SpannableString
```

eg.

```kotlin
private fun spannableTabItem(topText: Int, bottomText: String): CharSequence? {
    val spanStr = topText.toString()
    var ssb = SpannableStringBuilder()
    var ssTop = SpannableString(spanStr)

    val spanFlag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    ssTop.setSpan(
        ForegroundColorSpan(ContextCompat.getColor(context!!, R.color.color_000)),
        0,
        ssTop.length,
        spanFlag
    )
    ssTop.setSpan(AbsoluteSizeSpan(18, true), 0, ssTop.length, spanFlag)
    ssTop.setSpan(StyleSpan(Typeface.BOLD), 0, ssTop.length, spanFlag)

    ssb.append(ssTop)
    ssb.append(bottomText)

    return ssb
}
```





### Glide - 处理

圆图

```java
Glide.with(this).load(url)
        .transform(CircleCrop()).into(this)
```



圆角

```java
  
//之所以要加上CenterCrop() 那是因为
//和Imageview中scaleType有冲突
//先做了圆角处理之后， 被scaleType裁剪之后 圆角可能就看不见了。 所以先让glide裁剪一下 ，再做圆角处理
Glide.with(this).load(url).transform(CenterCrop(), RoundedCorners(corner)).into(this)
```



## RecyclerView的各个item 自定义宽度

```kotlin
1 adapter的  设置所占span


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerViewRef = WeakReference(recyclerView)
        /**
         * 为列表上的item 适配网格布局
         */
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            val spanCount = layoutManager.spanCount
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    if (isHeaderPosition(position) || isFooterPosition(position)) {
//                        return spanCount
                        HiLog.i("getSpanSize ",position)
                        return 3
                    }
                    val itemPosition = position - getHeaderSize()
                    if (itemPosition < dataSets.size) {
                        val dataItem = getItem(itemPosition)
                        if (dataItem != null) {
                            val spanSize = dataItem.getSpanSize()
                            return if (spanSize <= 0) spanCount else spanSize
                        }
                    }
                    return spanCount
                }
            }
        }
    }

```

核心就是layoutManager的spanSizeLookup

![image-20201123162514918](https://i.loli.net/2020/11/23/Woh4RVZ7ETFdax5.png)



## SpanSizeLookup

### SpanSizeLookup来全局控制GridLayoutManager的RecyclerView 宽度

![image-20201126113836216](https://i.loli.net/2020/11/26/dlKtsL1IhRo6vOW.png)





## RecyclerView.ItemDecoration  - 给RecycleView添加标题



1. getItemOffsets : 扩展Item额外的空间
2. onDrawOver: 绘制， 但是是会在VIEW的onDraw方式之后执行， 并且会在view的绘制的上层。

## 获得String的所需宽度和高度

通过paint 

```
paint.getTextBounds(groupName, 0, groupName.length, textBounds)
```





## HTTPS的抓包

![image-20201127180133546](https://i.loli.net/2020/11/27/vMGpKLJEoj5OQXC.png)



wireshark 是没法解析https的报文的。

这里的Https 的抓包 用的是charles

charles 抓Https包的原理是 charles在客户端和服务端之间做了个代理。

charles 只有有一套SSL证书的公私钥。
charles会把自己的公钥返回给客户端。

客户端生成会话密钥发送出去的时候，charles有ssl私钥能得到 会话密钥， 通过得到的会话密钥和服务端的ssl公钥就能够解析服务端的报文。 从而实现了https的抓包



android 6.0 以下的 对https的抓包是不需要安装证书的

7.0以上的都需要手动的添加charles的ssl证书才行



那是因为7.0 以上  就不再信任用户安装的证书了。

9.0以上甚至都不允许明文传输了

![image-20201128112213259](https://i.loli.net/2020/11/28/ZytF5pKaHkseXCM.png)

![image-20201128112413706](https://i.loli.net/2020/11/28/DOX3WSfBmiuaAp4.png)

charles 还可以改接口返回的数据， 这样可以不改代码的情况下 测试一些 不常规的数据





## 断网情况下 避免首页白屏



打包的时候内置上 跟包数据 （首次安装时才加载）。
然后请求到接口之后， 则缓存到本地。 接口优先拿缓存。



## 用弱引用来处理LiveData全局监听的自动解绑

![image-20201203102206890](https://i.loli.net/2020/12/03/JPwH1jeYpoWBs6x.png)

![image-20201203102231612](https://i.loli.net/2020/12/03/UEG6LPaDjQrIBAm.png)

## ViewPager相同数据 不应该触发detach

处理ViewPager  两次fragment 的内容是否相同 ，如果相同则不应该触发detach   重跑生命周期，；应该只有不相同的时候再detach。

主要是通过 Adapter 的这两个函数来处理。

```kotlin
// 这里判断 需不需要刷新
        override fun getItemPosition(`object`: Any): Int {
			// 对于PageViewAdapter来说 这个object就是fragment对象实例。
		   // 这里拿到的fragment实例 实际上是通过 下面的  getItemId 作为标识 获取的fragment
        }

// 这里是获取目标 fragment 实例 的唯一标识， 默认是以position作为标识的
// 所以在同一个页面 由于数据变动的情况下 position改变了，导致的fragment生命周期的重走，就可以通过这个GetItemId来处理 但是具体用什么来做fragment的标识要根据具体的业务来灵活处理。
        override fun getItemId(position: Int): Long {
        }
```



## Jetpack

jetpack就是一个组件集合， 是google主推的开发规范

![img](https://img.mukewang.com/wiki/5ee81ffc0984dad040020746.jpg)



暂时来我们只用用到了生命周期相关的

databinding  、leftcycle 、viewmodel、navigation...



### navigation

前面提到过了，
没ARouter好用， 最大的问题就是模块化，组件化开发中 各个模块相互不能相互感知。



### Lifecycle

能感知宿主(fragment、activity)的生命周期

经典用法
对宿主的生命周期的监听

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



#### lifecycler源码分析

##### Fragment 

Fragment是如何实现生命周期的监听的呢？

Fragment实现了LifecycleOwner接口，然后通过lifecycleOwner的Lifecycle对象来进行分发。

![image-20201201110622125](https://i.loli.net/2020/12/01/lMwG8dc6OFzAtq4.png)

![image-20201201110403308](https://i.loli.net/2020/12/01/6QroyceBfw8ASdg.png)

![image-20201201110427097](https://i.loli.net/2020/12/01/jTZAIVyP4Rg6rf1.png)

##### Activity

activity 并不是直接 注册 lifecycle的，而是创建了一个透明的fragment来通过fragment去控制。

![image-20201201111755632](https://i.loli.net/2020/12/01/seOZjTQPyxHhDpt.png)

![image-20201201112005779](https://i.loli.net/2020/12/01/iBpaf5JjZ6d83EM.png)

![image-20201201112040096](https://i.loli.net/2020/12/01/ys3FLI6k2OGAnT7.png)



之所以用这种通过fragment来控制，而不是直接去分发是为了兼顾  不是集成AppCompactActivity的场景。

Activity本身是没有集成LifeCycleOwner接口的，Activity类如果要集成LifeCycle功能的话
，得通过LifecycleDispatcher 来处理。

这里只需要知道Activity没有实现LifeCycle接口即可。



LifeCycle的宿主的生命周期 状态和生命周期事件并不是一回事。
留意下图

![img](https://img.mukewang.com/wiki/5ee820ce0947f7e314000762.jpg)



要注意，  注册的observable 会前进到该宿主的当前的生命周期状态。
比如  是在activity的resume状态下注册的话，那么就会把observable前进道resume状态。

比较需要注意的一点就是 LifecycleRegistery 里面不只有一个observable， LifecycleRegistery需要维护内部全部的observable和宿主状态一致



面试常问：
Q：lifecycle在activity里面是如何进行实践分发的？

A：是通过ReportFragment,ReportFragment中有LifeCycleRegister来进行分发。



Q2: 在resume中添加lifecycle observable能不能接收到完整的生命周期事件？
A2: 可以，在注册的时候 LifeCycleReigster会把observable一步一步的升级状态到和宿主的一致。升级状态的过程 会发送响应的消息



感觉这个LifecycleRegistery很值得研究呀。
类似的场景还是很常见的







### ViewModel & LiveData

viewModel  : 具备生命周期感知能力的数据存储组件。

viewModel中的数据 不会随着 横竖屏 分辨率变更这些参数改变导致的ui改变而销毁。
也就是不会onSaveInstanceState

viewModel 一般都会和liveData 一起使用的

这个viewModel用用法和dagger2的很相像。





![image-20201128155051844](https://i.loli.net/2020/11/28/kDfulqx4sFto8eN.png)

![image-20201128155109133](https://i.loli.net/2020/11/28/nBxOQ1ztIj9uva5.png)



类似dagger2 ，是通过 pageData.postValue来触发 observe的回调

liveData注册的时候就已经加上了 this 
因此能完成对宿主生命周期的监听

![image-20201128155403122](https://i.loli.net/2020/11/28/OVzcy6ISZoFdtUC.png)



#### LiveData

liveData的回调在什么线程执行呢？

setValue ： 只能在主线程执行，回调也是在主线程
postValue:   会在主线程回调

liveData的消息总线对比handler，eventBu,RXbus  有一个优点，只会对前台宿主进行消息分发。
避免后天用户不可见的页面抢占系统资源。

LiveData的特点
1.减少资源占用  ---- 页面不可见时 不派发消息
2.确保页面始终保持最新状态 --- 页面可见时会立刻派发最新一条消息给所有观察者，从而保证页面最新状态
3.不再需要手动处理生命周期 --- 有效避免NPE



LiveData  : 用于读
MutableLiveData  ： 用于写

实际上mutableLiveData只是把liveData 的post 和set的权限放开了。
这样多一层继承的设计的原因是 把读写给分开，避免读写操作的混乱。



LiveData的实现原理

![img](https://img.mukewang.com/wiki/5f0d186f09c853fd25490962.jpg)

![img](https://img.mukewang.com/wiki/5ee8243109c6b1c211470764.jpg)



从上面的流程中可以看出， LiveData的消息分发是依赖于LifeCycle的


刚注册时的数据同步：

LiveData刚注册的时候是利用LifeCycle的状态同步发送的消息来实现数据同步的。

setValue

![image-20201201155255296](https://i.loli.net/2020/12/01/y5tQ1aVMImASJUd.png)

postValue:
 post其实就是发送了个mainHandler的消息把set放到了主线程的handler下 去执行。

![image-20201201154138558](https://i.loli.net/2020/12/01/vSIJwXYWFqRGnya.png)

![image-20201201154559863](https://i.loli.net/2020/12/01/SF54mnO8Tj3GJvd.png)



#### ViewModel

 viewModel是怎么做到 页面销毁（**因为配置变更**）后，仍然存在的呢？
是因为ViewModel的实例是被保存下来了的。

常规情况下viewmodel只能在由于配置变更而导致的页面销毁下保存， 由内存不足，电量不足的情况还是会被销毁。
但是特殊处理一下之后， viewModel也能支持在内存不足、电量不足的情况下进行保存



##### 创建ViewModel

```kotlin
//让Application实现ViewModelStoreOwner 接口
class MyApp: Application(), ViewModelStoreOwner {
    private val appViewModelStore: ViewModelStore by lazy {
        ViewModelStore()
    }

    override fun getViewModelStore(): ViewModelStore {
        return appViewModelStore
    }
}

val viewmodel = ViewProvider(application).get(HiViewModel::class.java)

```



![image-20201201181140216](https://i.loli.net/2020/12/01/rezYBl5kMbwqS4h.png)

ViewModelStore本质就是一个HashMap

![image-20201201181852757](https://i.loli.net/2020/12/01/dl3B7Msg8ZqJzEv.png)

这个HashMap的key是 以class 来做区分的，所以页面销毁之后，用相同ViewModel的class 获取到的ViewModel还是同一个。

![image-20201201182655873](https://i.loli.net/2020/12/01/CwIT9WaKQfNEkpH.png)

![image-20201201181317601](https://i.loli.net/2020/12/01/24Kj8xzf7YeSPDC.png)

activity中的ViewModelStore是从NonConfigurationInstances 中拿的。
这个NonConfigurationInstances   是在activity.attach中赋值的。
也就是说明NonConfigutationInstances是在 ActivityThread中赋给activity的。
所以 ViewModelStore在页面销毁之后，是还存在的。

到这里就不需要往下跟了。

![image-20201201181439240](https://i.loli.net/2020/12/01/SaLpc3f5khCAq1e.png)

只需要知道  NonConfigutationInstances 是在ActivityThread。preformDestory 中调用了
activity的retainNonConfigurationInsatances 中保存下NonConfigutationInstances的 

##### ViewModel进阶用法-savedState

- SavedStateHandle的数据恢复与存储，即使ViewModel对象不是同一个也能够恢复，
  所以在低电量和低内存的情况也能够完成数据的恢复。



###### 整体结构



![img](https://img.mukewang.com/wiki/5ee8307409cd9c2118971359.jpg)





###### SavedStateRegistry的数据模型

- 一个activity只有一个SavedSateRegistry
- 一个SavedStateHandler对应一个ViewModel
- SavedStateRegistry不只有一个ViewModel

![img](https://img.mukewang.com/wiki/5ee8308409439aee15640642.jpg)



###### 存储的流程

![img](https://img.mukewang.com/wiki/5ee8309309666f0c22190609.jpg)

tips:
存储数据的时候是要把该activity对应的SavedStateRegister中的所有SavedStateHandle都添加上保存的数据

###### 恢复流程

![img](https://img.mukewang.com/wiki/5ee8309f0928b00417550489.jpg)



前面有说 viewModel的key是以Class来作为标识的 所以 能拿到保存的bundle中对应的数据



![img](https://img.mukewang.com/wiki/5ee830a709cb63df18150517.jpg)





###### ViewModel和onSaveInstanceState的区别

存储类时机不一样， 存储的位置不一样。
ViewModelStore 在页面重建的时候  是存储在ActivityThread的ViewModelStore里面的。
是在页面配置变更的情况下触发的 本质上还是在同一个进程当中。

而SavedState  是存储在了ActivityRecord当中， 是可以支持跨进程的。

同一个进程下的效率会比跨进程 效率高一些，所以ViewModelStore和SavedState都是有意义的。

### Room 

轻量级orm数据库，本质上是一个SQLite的抽象层

创建数据库

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

Room的ADUC

![image-20201128162329940](https://i.loli.net/2020/11/28/uqCV5h7ckWKxTS3.png)





Room 可以和LiveData  绑定使用， 从而可以直接实现对数据库中的数据进行监听。
![image-20201202145920386](https://i.loli.net/2020/12/02/q2mpQxTlFA5zNCv.png)

用法很简单 ，就在@Dao注解下的接口处，实现table_cache即可。



Room支持内存数据库  （在进程被杀死之后  数据丢失）

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

### DataBinding 

数据与视图的双向绑定

在xml 中写入绑定逻辑， 可以减小activity和fragment的压力。

本质就是编译时生成响应代码。

只要找到对应生成的代码， 就并不存在网上有些人说的 不方便调试的问题了。

### WorkManager

新一代的后台任务管理组件；

service能做的他都能做。

甚至app退出了仍然可以保证任务的执行

是一个系统级的服务调度。

workManager是可以支持 链式 调度的。

可以指定执行的顺序。

![image-20201128165423938](https://i.loli.net/2020/11/28/BXLwpSsndyjr2C5.png)





## 详情页

可能遇到的问题

![image-20201203111026362](https://i.loli.net/2020/12/03/Nj53lZr1iGvbw89.png)









### 流式布局 

常规流式布局 是用FlowLayout来处理的。


但是对于轻量一点的view来说 可以通过chip组件来实现























