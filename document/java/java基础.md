# Java



## 一 .面向对象



面向对象的三大特性
封装 ，继承 ，多态。

从一定程度来说，封装 继承 都是为了多态来进行服务的。

### 多态

多态是指：
允许不同类的对象对同一函数调用做出不同的响应。

实现多态的技术称为 动态绑定：
是指在执行期间判断所引用对象的**实际类型**来调用其相应的方法。

多态的好处：
接口设计的基础,可替换，可扩充，

```
public class Test {
    public static void main(String[] args) {
      show(new Cat());  // 以 Cat 对象调用 show 方法
      show(new Dog());  // 以 Dog 对象调用 show 方法
                
      Animal a = new Cat();  // 向上转型  
      a.eat();               // 调用的是 Cat 的 eat
      Cat c = (Cat)a;        // 向下转型  
      c.work();        // 调用的是 Cat 的 work
  }  
            
    public static void show(Animal a)  {
      a.eat();  
        // 类型判断
        if (a instanceof Cat)  {  // 猫做的事情 
            Cat c = (Cat)a;  
            c.work();  
        } else if (a instanceof Dog) { // 狗做的事情 
            Dog c = (Dog)a;  
            c.work();  
        }  
    }  
}
 
abstract class Animal {  
    abstract void eat();  
}  
  
class Cat extends Animal {  
    public void eat() {  
        System.out.println("吃鱼");  
    }  
    public void work() {  
        System.out.println("抓老鼠");  
    }  
}  
  
class Dog extends Animal {  
    public void eat() {  
        System.out.println("吃骨头");  
    }  
    public void work() {  
        System.out.println("看家");  
    }  
}
```



## 集合框架

![](https://raw.githubusercontent.com/sucese/computer-science/master/art/java_collection_structure.png)

### List

有序、 可重复； 

### Set

无序，不可重复

### Map

键值对  键唯一 ，值可以多个



List,Set 是继承自Collection接口，Map则不是
一般关注排序的时候 才会使用Tree 红黑树的结构，
要不使用Hash 效率会更高。

Hash的数据结构是 使用 数组 加单向链来实现的。
不过在java 8 中 链表数量大于阈值的时候 会转化成红黑树来存储。



### HashSet是如何保证不重复的？

先通过 hash值计算出 这个元素的存储位置，如果这个位置上为空，那么就添加进去；
如果该位置上已经有了的话，那么就用equals来计算。
如果不相等就找个空位置添加进去。



### HashMap和HashTable的区别

最主要的区别就是table是 线程同步的，底层的数据结构都是 数组加 单向链表来实现的（jdk8之前）



### ConcurrentHashMap 和HashMap

conCurrentHashMap 和 hashMap的主要区别就是 是否线程同步。
ConCurrentHashMap的各个版本也有一些时间上的差异。

ConCurrentHashMap在1.6中把重量锁转成了分阶段加不同的锁， 偏向锁 ，轻量锁，再到重量锁。但是到重量锁的时候性能仍比较低

ConCurrentHashMap在1.7的时候使用了分段（分成多个Segment）加锁的方式来提高效率，不对整个map进行加锁，可以减少锁持有的时间。

ConCurrentHashMap在1.8的时候 又抛弃的 segment分段锁的处理方式 ，但是 还是不能解决查询遍历链表的效率太低。改成了CAS（乐观锁） 和synchronized.

乐观锁和悲观锁
synchronized（ 悲观锁）：  线程一旦拿到这个锁 ，其他线程必须挂起
CAS (乐观锁):每次不加锁而是 假设没有冲突而去完成某项操作，如果因为冲突失败的话，则继续重试，知道成功为止。（感觉像是自旋）



```
CAS ( compare and swap ):
CAS机制当中，使用管理三个基本操作数：
内存地址V
旧的预期值A
要修改的新值B

主要思路就是：
线程1 要把V地址的值  从A改为B

这是发现 V地址里是不是A？如果是A，那么就改为B；
如果不是A 那么就从新去内存V中拿值，
然后 把预期值A 更新为A2，新值改为B2（重新计算）.
再去看内存中是否一致，一致的话，那么把值更新为B2.


CAS 的缺点就是 cpu 开销比较大，等于在做短期的无限循环。而且也不能绝对保证原子性，只能保证单一变量的原子性。
```



cas 原理代码解析(感觉就是自旋。)

```

public class AtomicBooleanTest implements Runnable {

    private static AtomicBoolean flag = new AtomicBoolean(true);

    public static void main(String[] args) {
        AtomicBooleanTest ast = new AtomicBooleanTest();
        Thread thread1 = new Thread(ast);
        Thread thread = new Thread(ast);
        thread1.start();
        thread.start();
    }
    @Override
    public void run() {
        System.out.println("thread:"+Thread.currentThread().getName()+";flag:"+flag.get());
        if (flag.compareAndSet(true,false)){
            System.out.println(Thread.currentThread().getName()+""+flag.get());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            flag.set(true);
        }else{
            System.out.println("重试机制thread:"+Thread.currentThread().getName()+";flag:"+flag.get());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            run();
        }

    }
}
```

### HashMap 如何解决散列碰撞

数组加链表中的链表就是存储hash值一致的元素的。
hash值来指定存储位置，当发生hash碰撞的时候 就会把元素加到 该位置的链表的头部或者尾部（根据sdk 版本的不同共有差异）

### HashMap 底层为啥是线程不安全的

这个是1.7的hashMap的源码
可以看出 迁移的时候 是一个倒序的插入。
**为啥设计成倒序的？**
**直接移过去不是更简单明了么**。先放放

    void resize(int newCapacity) {
        Entry[] oldTable = table;
        int oldCapacity = oldTable.length;
        if (oldCapacity == MAXIMUM_CAPACITY) {
        	//capacity 容量
            threshold = Integer.MAX_VALUE;
            return;
        }
    	// 创建一个新的对象
        Entry[] newTable = new Entry[newCapacity];
        // 将旧的对象内的值赋值到新的对象中
        transfer(newTable, initHashSeedAsNeeded(newCapacity));
        table = newTable;
        // 重新赋值新的临界点
        threshold = (int)Math.min(newCapacity * loadFactor, MAXIMUM_CAPACITY + 1);
    }
    
    void transfer(Entry[] newTable, boolean rehash) {
    	// 新table的长度
        int newCapacity = newTable.length;
        for (Entry<K,V> e : table) {
            while(null != e) {
                Entry<K,V> next = e.next;
                if (rehash) {
                    e.hash = null == e.key ? 0 : hash(e.key);
                }
                int i = indexFor(e.hash, newCapacity);
                e.next = newTable[i];
                newTable[i] = e;
                e = next;
            }
        }
    }
为啥这样的设计会有线程安全问题呢？

resize中的新table的赋值操作的核心有四步：


```
//缓存 next
next = e.next;
//倒序操作
e.next = newtable[i]
//更新newtable
newtable[i] = e;
//处理 next
e = next;
```

这时有两个线程

线程A，线程B

table状态为{1 -》2 -》3}

- 线程A跑完了
  缓存next这步时，线程被阻塞了。
  此时状态：{e:1  e.next :2 
  next:2}
- 此时 线程B也执行了resize操作，并且执行完了resize操作
  table的状态已经变成了{3 -》 2 -》1}

- cpu唤醒了线程A，继续执行。
  执行到 e.next = newTable[i];
  就出现了1 .next = 3；
- 此时链表的 关系就是
  {  1 -》 3 -》2 -》-》1 -》3....  }
  就行成了环

这点就是 HashMap线程不安全的体现

1.8之后就不再使用头插法了，改为了尾插法。
就避免了这个问题。（链表顺序不变）



### ArrayMap 跟SparseArray在HashMap上面的改进

arrayMap 基于两个数组实现
一个存Hash, 一直存键值对。

hash表的存放是有序的，
查找是通过二分查找。

1.8的代码里是指存 hash和 value的。
key是不存的。
但是 value的序号是通过key和hash来算出来的。
如果hash冲突了就向左找一个为被占用的位置

优势： 扩容时 直接arrayCopy即可，不需要重建 hash表。

缺点： 由于hash查找的时候 是二分查找（O(log^n^)），
 所以在数据规模大的时候 查找的效率是不如 hashmap(O(1))的

#### SparseArray

和ArrayMap 机制差不多。
value 也是用 一个数组来存。
但是 key只能是 int类型的。

SparseArray 对比起HashMap<Integer,Object>
减少了 拆箱装箱的开销





## 反射



反射是java的特性之一。
它允许获取到运行中 java成员的内部信息。
反射的核心是JVM在运行时才动态加载类或者调用方法/ 访问属性。

通过反射是可以 破坏封装性，越过权限检查的来调用任意方法的。

## 泛型

泛型是为了解决 在编译阶段更好的进行类型检查，方便开发人员在编码是进行代码检查。
代码编译时 编译器也能通过泛型来做类型检查。

注意java的泛型是假泛型，和C#等是不一样的。
并不是真实存在的类型。
像C#的话，List<String> 就是真实存在着List<String>这个类。
但是java当中 在字节码里只会保留List
到字节码当中 泛型是被擦除了的。

虽然说泛型是被擦除了的，但是还是可以通过反射来拿到泛型信息的。

java的假泛型 带来的结果是啥？

就是很多时候需要多此一举。
比如说gson

明明 返回值都传了泛型的类型
为啥还的传T.class 过去。
就是因为T 会被擦除成Object
其他真实泛型的高级语言估计就不会有这种问题。

```java
  public <T> T fromJson(JsonElement json, Class<T> classOfT) throws JsonSyntaxException {
    Object object = fromJson(json, (Type) classOfT);
    return Primitives.wrap(classOfT).cast(object);
  }
```





## 注解

注解其实就是一个标记。
可以在编译的时候 根据这个标记做相应的处理，
也可以在运行时，通过反射来获取注解，从而在运行时做相应的处理。

## 其他

### java当中为啥需要装箱 拆箱？

目的： 实现值类型和 引用类型的相互转换。

先来看  值类型和 引用类型的差异。
根本差异就是占用的 空间大小是不一样的。
boolean, char,byte,short,int ,long,float,  double,占的字节数是不一样的，
而引用类型占的内存是固定。
所以引用类型是可以 相互转换的。

为啥会出现这种值类型和引用各类型的转换需求呢？
是由于泛型的引入引起的。
java在实现泛型是 是转为了

Class<Obeject>,这个Obeject是引用类型， 是有固定长度的。

而如果泛型指定为了char
那么 Obeject和char的长度就对不上了。
就不能直接实现转换。
所以为了能够实现转换 所以要把值类型转成 统一长度的引用类型才可以。



### Java的char是两个字节，是怎么存Utf-8的字符的？

- char 是两个字节， utf-8是1~3个字节 utf-16 是2~4个字节 （java里存的是utf-16,也就是最少2个字节）

char里面存的是什么？
存的是字符集里的码点。
字符集 不是编码。
字符集： ascII , unicode

比如： utf-8的编码的'a' 占一个字节，但是它实际上使用的ascII码 占两个字节。



所以说 编码 3个字节 ，在字符集里面只有两个字节 所以 char才能表示。

而且对于unicode 的一些扩展字符 比如表情。是需要 一对char来表示的。
所以char 能否表示 字符。
只取决于字符集的码点。
而不是取决于编码格式。



另外有一点， 字符个数和  字符长度 不一定一致的。
比如字符当中 有emoji表情， 那么 字符长度有可能大于 字符个数。
但是各个语言不一样。



总结

- java char 存的是  字符集的码点， 而不是存编码。

- java 存的是utf-16编码的字符集

- 字符数和 char  的长度在java中是不一定对应的
  因为存在  两个char表示一个字符的情况， 比如emoji 表情

  

### Java String 可以有多长

这里 先要有第一个概念。

#### String 存储在哪里。

先来说Java中存储数据的位置。
java中一共有4总存储媒介 、6个形式可以存储数据。
**存储媒介**:  寄存器， ram rom ， 其他（磁盘 ，硬盘等 永久存储空间）

存储形式和存贮媒介是有一对多的对应关系的。
**存储形式**： 

- 寄存器 ，寄存器媒介
-  堆栈， ram中
  栈中只存 基本数据类型和对象的引用指针
  因为栈只能存数据大小确定的数据
- 堆 ，  ram中
  实际存放对象的地方
- 常量池，  可以存在rom中
- 静态区， 用来存静态变量的的地方。 
- 其他 

参考：https://www.cnblogs.com/xiohao/p/4296088.html

```栈的特殊性
栈有一个很重要的特殊性，就是存在栈中的数据可以共享。

假设我们同时定义   int a = 3;   int b = 3； 编 译器先处理int a = 3；首先它会在栈中创建一个变量为a的引用，然后查找有没有字面值为3的地址，没找到，就开辟一个存放3这个字面值的地址，然后将a指向3的地址。接着处 理int b = 3；在创建完b的引用变量后，由于在栈中已经有3这个字面值，便将b直接指向3的地址。这样，就出现了a与b同时均指向3的情况。 特 别注意的是，这种字面值的引用与类对象的引用不同。假定两个类对象的引用同时指向一个对象，如果一个对象引用变量修改了这个对象的内部状态，那么另一个对 象引用变量也即刻反映出这个变化。相反，通过字面值的引用来修改其值，不会导致另一个指向此字面值的引用的值也跟着改变的情况。如上例，我们定义完a与 b的值后，再令a=4；那么，b不会等于4，还是等于3。在编译器内部，遇到a=4；时，它就会重新搜索栈中是否有4的字面值，如果没有，重新开辟地址存 放4的值；如果已经有了，则直接将a指向这个地址。因此a值的改变不会影响到b的值。



```



一般情况下，我们只需要关心 堆栈， 堆 和常量池。

常量池和堆栈 一般用String来进行分析。

如果在编译期间就能够确定下来的String, 那么就是存放在 常量区当中的。
如果是在运行期 才能确定的就是存储在堆中的。
常量池中只有一份，而堆中有多份。

```QA
Q:String s = new String("abc");
这句代码 创建了 几个对象？
A：
一个或者两个。
"abc"是在遍历时就确定的对象，
如果常量池中 没有 "abc",那么就要在常量池中先创建 一个 常量池对象，然后再在对中创建一个常量池对象的拷贝。
如果常量池中已经有了的话，那么就只创建一个即可。

```

所以 回到问题。
String 是有可能存在 两个地方的。

1. 静态区（方法区）的常量池中
2. 堆中

```
tips：
严格来说 方法区也是在堆中的。
但是方法区的内存和常规的 存放对象实例的堆的内存是分隔的。

一般来说方法区不会说是在堆中
```





对于存放在 静态区的 String, 在字节码中是用byte[]来存储的，这个数组的长度是一个int值，所以 String的最大长度是 65535

当然由于这里的String是存放在方法区的，所以还受到方法区大小的约束。（对于这里 是不用管方法区是存在在永久代中，还是存在元空间当中。  ）

// 区分是 常量池在静态区 还是在堆中的方法
做个死循环 把常量池撑爆，
看报错是提示head space oom还是permGen space oom

```
拓展 :  永久代和 元空间

永久代 是啥？
永久代是 HotSpot 实现 存储方法区定义的概念。其他的jvm是没有永久代的。
并不是jvm 的定义， 方法区才是jvm的定义。

元空间： 元空间是1.7开始用来 代替 永久代的。
为啥要代替， 因为永久代的存在  影响到了GC的效率。 每次GC的时候 永久代都参与其中。
元空间和永久代都是用来处理jvm标准对方法区的实现的。
那么他们的区别在哪里呢？
最主要的区别就是
永久代是存在在jvm当中的，
元空间是直接存在本都内存当中的。
由于元空间不在jvm当中，所以有如下好处：
1.永久代大小难指定，大了容易 老年代移除， 小了容易永久代溢出；
2.提高了GC的效率；



```



回到正题：
对于不在方法区的String 的长度最大是多少呢？

new String(byte[]) 在堆上创建String ，对应的虚拟机的指令的长度参数的是int ，所以长度限制也是65535，但是由于虚拟机需要一些头部信息 所以长度会小于65535。



总结：

- 方法区里的常量池的 string的长度大小是65535
- 堆里的String的长度最大是接近65535,

### Java匿名内部类的限制

基础

- 编译后产生的类名 没有可读性

- 包名是外部类拼接匿名内部类的序号而成
- 只能继承一个父类 或者 实现一个接口。



进阶：

- kotlin的匿名内部类是同时继承父类，并且实现接口的。

高阶：

- 匿名内部类存在内存泄露的风险
  由于匿名内部类是由编译器自己产生的，并且编译器会自动的 让匿名内部类持有外部类的引用，有可能会导致 外部类不能够及时的回收，从而导致内存泄露



### Java中 对异常是如何进行分类的

Java中异常的定义有如下两种

- Exception
  exception是 程序可以处理的 异常状态， 
- Error
  程序无法处理的 ，比如 OOM，栈溢出这种



处理异常的基本原则：

- 捕获异常时要 指定异常，不要直接捕获Exception这样的

#### NoClassDefFoundError和ClassNotFoundException 的区别

NoClassDefFoundError是在jvm或者classLoader没有找到对应的类的时候抛出的。（编译的时候找的到，运行时找不到， 估计就是打包 打漏了。）

ClassNotFoundException在两种情况下会出现，

1.是反射时没有找到 传入的类，很可能是拼写错误

2.该类已经被加载进某个classLoader了， 然后又用另一个classLoader去加载该类。



### String为啥要设计成不可变的？

String 内部是一个final的 byte数组 
并且没也没暴露修改 数组内容的方法。
String的处理方式有点像 基础数据类型
这么设计的目的是啥

- 保证线程安全
- 实现jvm的规范的常量池 ，如果String 可变的话，常量池就会被改变，那就不是常量了。

### Java里的幂等性

实际就是 同一个参数 ，多次传入同一个方法 执行结果要一致。



幂等性最为常见的应用就是电商的客户付款，试想一下如果你在付款的时候因为网络等各种问题失败了，然后去重复的付了一次，是一种多么糟糕的体验。幂等性就是为了解决这样的问题。

实现幂等性可以使用Token机制。

核心思想是为每一次操作生成一个唯一性的凭证，也就是token。一个token在操作的每一个阶段只有一次执行权，一旦执行成功则保存执行结果。对重复的请求，返回同一个结果。

例如：电商平台上的订单id就是最适合的token。当用户下单时，会经历多个环节，比如生成订单，减库存，减优惠券等等。每一个环节执行时都先检测一下该订单id是否已经执行过这一步骤，对未执行的请求，执行操作并缓存结果，而对已经执行过的id，则直接返回之前的执行结果，不做任何操
作。这样可以在最大程度上避免操作的重复执行问题，缓存起来的执行结果也能用于事务的控制等。



### 为什么Java的匿名内部类只能访问Fatal修饰的外部变量

因为java 传的是引用。
如果不是fatal的话，那就会存在，外部类的变量，和内部类的变量 值不一致的情况。
这种情况对于开发者来说 挺难理解。
所以java就加上了这条规定。

### Java的编码方式

java.class里是utf-8 ，
但是java运行是 用的utf-16

编码是编码，
字符集是字符集。

unicode字符集现在规划的是有 0x10FFFF,
unicode指定的是字符的索引值

所以说 一个char并不能表示全部unicode字符。

带来的结果就是 java中string的长度和字符数不一定是相等的。

具体string里面的byte数组的存储的值是多少就和编码有关系了。

像utf-8的话编码都是

0XXXXXXX
10XXXXXX
110XXXXX

以0开头的编码 才是 一次编码的结束。

为啥这样设计呢？

加这些头不是浪费空间么。主要是为了兼容ascII码。



### String、StringBuffer、StringBuilder有哪些不同？



执行速度：
StringBuidler> StringBuffer>
String

String：存在方法区的常量池里，每次变化都需要 开辟一个新的内存空间
StringBuilder: 存在堆中，非线程安全
StringBuffer：和StringBuilder一致，但是是线程安全的



### 什么是内部类？内部类的作用









内部类有四种情况：
成员内部类，最常见的内部类,持有着外部类的引用
局部内部类，很少见这种用法，也是持有着外部类的引用
匿名内部类，只能继承或者实现一个接口，持有着外部类的引用
静态内部类：除了实现本身在类的内部，其他性质和普通类是一样的。并不持有外部类的引用。

持有外部类的引用的好处就是可以直接的访问外部类的成员变量，包括私有的。
但是同时也会带来内存泄露的风险。

内部类本身的价值就是 提供了更好的封装。可以做到除了外部类外不允许其他类进行访问。



### 抽象类和接口的区别



共同点：

- 都是抽象层，描述具备的功能，但是不进行实现。
- 都不能被实例化

区别：

- 抽象类只能继承，所以抽象类只能被继承一个
  而接口是可以同时实现多个接口的
- 接口函数 都是public的， 而抽象类的话 不一定
- 接口是没有构造函数的，
  抽象类是可以有的

### 接口的意义

规范，和扩展。
方便多人协作。

### 父类的静态函数能否被子类重写

不能。
静态方法是父类子类公用的。
如果强行的在子类用非静态的方式复写一个和父类静态函数的话，那么用子类的引用类型的时候，父类的静态函数就会被隐藏。

### 抽象类的意义

把共同属性都抽离出来，差异的地方交给子类各自去实现。
提高代码的可读性和易于维护。

### 静态内部类和非静态内部类的理解

核心 是否持有外部类的引用。

静态内部类的存在只是为了方便管理代码类结构的。
直接放在外部类当中也是可以的。
非静态内部类的话，是持有着外部类的引用的，所以有能够直接访问外部类的变量的能力的（包括私有成员变量）。

### 为什么复写equals方法的同事需要复写hashCode方法，前者相同后者是否相同，反过来呢，为什么？

hashcode 主要作用于 hashSet,HashMap等这种数据结构当中，hash值将决定存储的桶位置，如果说重写了equals 没有重写hashcode的话，就有可能存在 。 我们认为的相同的对象，由于hashcode的不同，导致了存在了不同的位置当中， 从而没有去比对到equals .结果就是在Hash结构中 存了两个相同的对象。

### java 为什么跨平台

因为java编译出来后，并不是直接机器执行的机器码，而是.class的字节码。
然后各种硬件设备上 有不同的专属硬件的jvm, 由jvm去执行字节码文件

也就是java变出的字节码只是一个中间层。



### 浮点数的精确计算



商业级别的 得用BigDecimal ，
不能简单的使用float 和double



### final、finally、 finalize的区别



final 是修饰符，
finally是 java 保证语句会被执行的机制， 一般用于 解锁，释放资源等操作；
finalize是 object的基础方法之一，设计的初衷是用于gc发生时在进行资源回收。
但是已经在jdk9后开始废弃，用了别的机制来代替。



### 静态内部类的设计意图

内部类能够有比较清晰的代码层架结构， 方便代码阅读。

但是内部类会隐式的持有着一个外部类的实例，这就导致内部类的创建是依赖于外部类的。
而静态内部类就打破了这个限制。静态内部类不持有外部类的引用， 是完全独立与外部类的。
所以也静态内部类就不能直接的访问外部类的非静态成员了。

### Java中对象的生命周期

在java中，对象的生命周期包括以下几个阶段：

- 创建阶段
  JVM加载类的class文件 此时所有的static 变量和static 代码块将被执行

- 应用阶段
  对象至少被一个强引用持有者

- 不可见阶段
  程序本身不再持有该对象的任何强引用
  虽然程序本身不持有，但是有可能被jni或者jvm线程持有着

- 不可达阶段
  该对象不被任何强引用持有

- 收集阶段
  当gc发现 该对象是不可达阶段时，该对象就进入了收集阶段。
  然后会执行finalize()方法。

- 终结阶段

  对象执行完finalized()方法后，仍然处于不可达状态时,该对象就等待GC 对其进行回收

- 对象控件重分配阶段
  gc对该内存已经进行回收或者再分配了。



### 静态属性和静态方法是否可以被继承？是否可以被重写？为啥这样设计





静态属性和方法可以被继承但是不能被重写。
如果子类中强行重写的话，那么父类的静态成员或者方法就会被隐藏。
这时使用子类的引用类型就调用不到父类的静态成员了。



为啥这样设计？
静态成员/方法的涉及本身就是对于类的固定属性，从这单上来看 本来就不应该有子类重写静态属性的情况。

但如果发生了重载的情况的话，那么根据多态 子类引用指向的自然就该是子类的实现。 所以 父类的静态便被隐藏了。



### == 、equals、hashCode的区别

默认情况下

==   和equals 是完全等价的。都是比较内存地址。

但如果重写了equals方法的话，那equals和== 就不一定相等了。

hashcode是快速的相等比较， 如果equals 为true, 那么hashcode就一定相等。

### java四种引用

- 强引用：最常用的引用类型
  在内存不足时，不会被回收

- 软引用：
  在内存不足时，会被回收

  弱引用：
  只要Gc扫描到就回收

- 虚引用
  对象回收前会被放入一个引用队列当中，
  而其他的引用会在GC回收之后才放到这个引用队列当中。
  这个引用队列用于对象被销毁之前的一些工作。 （没用过 见都没见过）



### 类的加载过程

以new 对象为例子

1. 类加载器加载.class到内存当中
2. 执行该类的static 代码块
3. 在堆中开辟内存空间，分配
4. 在堆中建立对象的特有属性，并且进行默认初始化
5. 对属性进行显式初始化
6. 执行对象的构造函数
7. 把开辟的堆地址返回给引用





### IO、NIO、OKIO

- IO是面向流的，处理的时候是一个字节一个字节的处理。
  NIO是面向缓冲区的，一次处理一个数据块。
- IO是阻塞的，NIO是非阻塞的

OKIO  不管了。 不了解。







