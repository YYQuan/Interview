# 小功能Tips



## 可以在主线程访问网络吗？

关闭严苛模式就可以了。



```java
StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
StrictMode.setThreadPolicy(policy);

```



StrictMode（严苛模式）Android2.3引入，用于检测两大问题：ThreadPolicy（线程策略）和VmPolicy(VM策略)，这里把严苛模式的网络检测关了，就可以在主线程中执行网络操作了，一般是不建议这么做的。关于严苛模式可以查看[这里](https://ericchows.github.io/Android-StrictMode-Analysis/)。



## UI绘制的 垂直同步信号是怎么保证能优先得到执行的？


通过同时发送 消息屏障和 异步消息 来提高该消息的优先级的。使其能尽早的被执行。



## 字体大小 SP 与 DP 的区别

sp会随着系统字体大小改变而改变， dp则不会。





## 获取类列带的泛型



那MVP模式里Activity创建presenter的例子

```java
public class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView {
    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof ParameterizedType) {
            Type[] arguments = ((ParameterizedType) superclass).getActualTypeArguments();
            if (arguments.length > 0 && arguments[0] instanceof BasePresenter) {
                try {
                    mPresenter = (P) arguments[0].getClass().newInstance();
                    mPresenter.attach(this);
                } catch (IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detach();
        }
    }
}
```

核心代码

```java
 Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof ParameterizedType) {
            Type[] arguments = ((ParameterizedType) superclass).getActualTypeArguments();
            if (arguments.length > 0 && arguments[0] instanceof BasePresenter) {
              //BasePresenter 替换成 泛型的约束类
			  //如果有的话 就能说明arguments[0].getClass（） 就是泛型的实际类型了。
            }
        }
```

