# Jni相关



# CPU结构适配需要注意哪些问题



![image-20210714101923512](https://i.loli.net/2021/07/14/gtYDw93QJAl5bOH.png)



armeabli  是兼容性最好的。
但是 一些优化的特性 armeabi就不支持了。

```
tips 像我们自己的sdk 手机端 只支持的 arm v7a。
```





系统会优先加载对应架构目录下的so库， 并且会在同一个目录下 读取所有的so库。
所以提供so库的时候， 要么直接提供一整套， 要么一个都别提供。
如果说 需要 a.so ，b.so 两个so, 两个在不同目录下的话，
那么就会有加载不到的问题。

那如果 一部分so库想用 arm的  一部分想用arm-v7a的 怎么处理呢？
可以如下图中一样， 给把v7a的so库放到  arm目录下，然后运行时通过对系统加载的判断来进行动态的加载  就能在arm目录下 选择加载哪个so了。

![image-20210714103632870](https://i.loli.net/2021/07/14/dnMxlLXO7yeHhCJ.png)



兼容性不绝对可靠



小结：

- 兼容性好的 性能就差
- 兼容性不是绝对可靠的 ，有时会出现莫名其妙的问题
- 系统优先加载对应架构目录下的 so 库， 并且so库都会在同一目录下进行加载



## so库动态下载

减少so库， 改成在app中不带so库， 等到 需要的时候再去 服务器上下载。

## 优化so库体积

通过一些配置项 来削减so库代码，  增加类似混淆的东西来达到减小so库体积。



## sdk 开发注意事项



尽量减少app层和sdk的交互。
尽量闭环一些。比如最近做的sdk 弄手机的屏幕共享， sdk需要系统提供的mediaserver，
但是资源的释放 等操作 上层虽然能完成， 但是 最好都用sdk来做。
sdk和app的交互要尽量的简单。 如果有接口之间的顺序依赖的话，很容易出问题。















# Java Native 方法和Native函数是如何绑定的？



![image-20210714111000703](https://i.loli.net/2021/07/14/T6QkvytWMIi9Je3.png)





![image-20210714111011626](https://i.loli.net/2021/07/14/ASjGI9xR41ZfNBW.png)

有两种绑定方式：

1. 静态绑定：命名规则映射 ，上面的例子就是这种
2. 动态绑定：通过jni函数注册



## 静态绑定

![image-20210714111315660](https://i.loli.net/2021/07/14/zHpOoR7bsQ6eCGl.png)

静态绑定中的  extern  "c"   是啥意义？
就是要告诉编译器  不要混淆我的名称。
编译器为了避免名称冲突等原因是由可能把函数名给改了的。
extern "C"就是指定了 用c的规则来处理， c的规则就是 不要改名字。



![image-20210714111636954](https://i.loli.net/2021/07/14/UOcfVAeCskiQHZW.png)

JNIEXPORT ： 指定要暴露出去， 要在显示在函数表中 默认就是会暴露的。

JNICALL :     在android平台上这个字段是无效的，但是别的平台可能会有作用。 用于约束一些 函数执行的规则。



## 动态绑定

![image-20210714112018477](https://i.loli.net/2021/07/14/Bf1ZpCoaQxrVIPW.png)

- 动态绑定是可以在任何时刻触发的。
- 动态绑定之前根据静态规则查找Native函数。
- 动态绑定可以在绑定后的任意时刻取消

所以也就是说jni可以通过动态绑定来屏蔽静态绑定的jni接口

## 两种方式的对比

![image-20210714112447610](https://i.loli.net/2021/07/14/LPH3BwWcZFnEC8k.png)

总体来说 就是 动态绑定 可以不出现在naive函数表中， 可以省些内存。
静态绑定的话， 可以更直观。



# JNI如何实现数据传递？





## 关联java和native层的对象

java层的对象和 native层的对象怎么对应起来？

通过java层只有native对象的指针就能够把java层的对象和native层的对象关联起来。



## 字符串操作

java层和 native层的 字符的编码 不一致。 字符串操作的时候 就要指定编码。

而且java和native读出来的字节序不一定是一致的

![image-20210714113103351](https://i.loli.net/2021/07/14/kantvupYUiV1oqL.png)

GetStringUTFChars/ReleaseStringUTFChars

![image-20210714113631711](https://i.loli.net/2021/07/14/AUFknql3HgXzwmR.png)

比如  \0  在c层是字符串的结尾，但是java 字节码中用的utf-8的\0 不会解析成 字符串的结尾 ，而是 编码成 0xC080 用两个字节来表示。

![image-20210714113508212](https://i.loli.net/2021/07/14/anVU2r3bgAxsG6Y.png)

![image-20210714113520110](https://i.loli.net/2021/07/14/uJmcFYgv41IAis7.png)

![image-20210714113558165](https://i.loli.net/2021/07/14/EpzovZJa9OLUkTD.png)

## 对象数组的传递

![image-20210714114530741](https://i.loli.net/2021/07/14/DTbIEU3cORPyQ21.png)

jni中  专门用了 objArrays来处理 对象数组。
返回的是一个localRef ，localref要尽可能及时释放掉。

```
tips:  LocalReference  有个数限制 ，用完要尽可能释放他。
函数调用完了之后，是会主动释放掉，LocalReferene的。
如果只是在函数里，用了几个LocalRef的话，也不用太紧张。
```

```
tips: 尽量直接给jni传要用的数据， 不要传一个对象，再让jni通过反射去拿变量。
这样效率会比较低。
```



注意事项：

- jvm和native 的字符编码不一致，
- jvm和native的 字节序 不一致
- java和native的对象的绑定 得通过 指针来关联。
- native层  的localref 的数量是有上限的。 要尽量及时释放。
  比如epic这个 插桩框架，  如果大量使用的话， 就会有这个localRef溢出的问题

- 尽量不要让native去处理 java obj ,而是去处理基本数据类型。
- 尽量减少java和native的交互， 接口尽量简单些。
  接口之间最好不要有太多强相关的顺序绑定。





# native如何捕获异常



![image-20210714143002475](https://i.loli.net/2021/07/14/cT5uElxsQvDN9GI.png)



通过设置 全局的 信号处理 函数来 捕获native异常。
信号触发的时候

捕获到信号之后，只要是jvm调起的线程都 可以通过javaVM ->GetEnv来获取到 env。
如果不是jvm 调起的线程的话， 那么就需要把线程绑定到jvm才能获得env。
通过env就能得到jclass ，然后就让java层去做处理了。

还要注意的是  findClass的时候 如果 env和 java层使用的classLoader不一致的话， 那还有可能会加载不到指定的类的。

```
tips: 如果要在native保存jObj的话， 就一定要通过newGlobalRef来保存， localRef出了函数就会被回收的。
```



