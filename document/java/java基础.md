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

- char 是两个字节， utf-8是1~3个字节

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


































































