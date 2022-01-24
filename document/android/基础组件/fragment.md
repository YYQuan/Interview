# Fragment

## Fragment的使用

常规用法：
![image-20201014114554004](https://i.loli.net/2020/10/14/9u7Yaq2VKAts1kB.png)

### Fragment的使用分析

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

接着往下看
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









