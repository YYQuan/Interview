# Android网络编程



## Android网络编程核心技术



### HTTP报文

请求报文

![image-20201117111352423](https://i.loli.net/2020/11/17/M28PS5T6xWXHqir.png)

响应报文
![image-20201117111443160](https://i.loli.net/2020/11/17/LuXcI8oDJkUEa9K.png)



### HTTP1.1

HTTP1.1  最重要的优化就是 支持了 tcp 连接的复用

![image-20201117112259113](https://i.loli.net/2020/11/17/MgC1m6u3SDkzeJc.png)

### HTTPS

HTTPS  是为了解决 明文传输 和数据安全的 而做的优化

实际上就是HTTP1.1中加了一层处理加密的ssl层

![img](https://img.mukewang.com/wiki/5ef172620926253e06600590.jpg)

HTTPS 加解密过程

![img](https://img.mukewang.com/wiki/5ef1726c09b9e07b19241450.jpg)



描述一下：
SSL  就是服务器 维护着一套**公钥和私钥的证书**。
APP 端通过ssl去确认证书是否有效， 然后随机生成一个会话密钥。
然后APP端通过服务器给的公钥对会话密钥进行加密 传输给服务端，
服务端收到之后 用私钥解密得到会话密钥。
然后客户端和服务端之间的通讯都用 **会话密钥** 的加密进行传输。



ps: 免费的CA 证书 发布机构 letsencrypt.org

![image-20201117114547042](https://i.loli.net/2020/11/17/mOt8NiVql9FDb45.png)



HTTPS  由于增加了 加解密认证的过程， 传输速度会有影响
因此google开发spdy协议

### SPDY 

SPDY : google 开发的 应用层协议  可以理解为HTTPS协议的增强

1.用多个HTTP请求共享一个TCP通道， 来降低 HTTP的延时
2.对请求头进行压缩
3.

![image-20201117114857790](https://i.loli.net/2020/11/17/gnZJzT6NjRsawDU.png)

![img](https://img.mukewang.com/wiki/5ef172760985e9a805160708.jpg)



### HTTP2.0

HTTP2.0 借鉴了很多google的SPDY 协议的优点 比如 多路复用，  压缩请求头..
最重要的是 **重新定义了二进制格式**，不再是文本传输，而是用二进制传输。（前面的spdy ,https ,http1.1 .都是文本传输的）
二进制传输比文本传输更加健壮一些。

原本HTTP2.0会把消息分成更小单位的消息体 frame来发送
而且HTTP2.0在共享tcp连接的基础上 同时发送请求和响应，
由于HTTP2.0把分成了 frame ， 同一个消息不同的帧是可以在不同 的tcp通道上进行交错的发送，从而极大的提高了效率

![img](https://img.mukewang.com/wiki/5ef1727f097f87c220461136.jpg)



### HTTP3.0

基本没被商用。
最大的改变就是 把传输层 从tcp改成的udp,不再进行三次握手的处理。

![image-20201117142210726](https://i.loli.net/2020/11/17/fFTcJqUdPiSQn9w.png)

![img](https://img.mukewang.com/wiki/5ef172880968abd706680666.jpg)

## 网络层框架HiRestful的封装



目标实现一个类似Retrofit的网络框架。


声明接口的时候 就 创建一个 interface的接口。
不需要进行实现

![image-20201117144031181](https://i.loli.net/2020/11/17/JHAPSQrfw17MimK.png)

然后通过动态代理来实现

![image-20201117144234444](https://i.loli.net/2020/11/17/GmCW7uD4ZxqQn6R.png)



主体的话 自己看源码吧。



tip: 怎么 判断传入的参数是不是基本数据类型的方法？

```java
  // 利用反射来 判断是否是 基本数据类型， 基本数据类型是都有TYPE字段的
    // class 里面的isPrimitive 函数就是用于判断是否是 原始数据类型。
    private fun isPrimitive(value: Any): Boolean {
       
        try {
            //int byte short long boolean char double float
            val field = value.javaClass.getField("TYPE")
				
            val clazz = field[null] as Class<*>
            if (clazz.isPrimitive) {
                return true
            }
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }
        return false
    }
```



## 适配Retrofit

看源码去。











































