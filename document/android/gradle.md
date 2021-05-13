# Gradle  



## 多渠道打包



### 定义

![image-20201216145902110](https://i.loli.net/2020/12/16/G2ZF7613alMwzEd.png)

### 需求的由来

![image-20201216150055350](https://i.loli.net/2020/12/16/HPC2DKgXnb3ExMQ.png)





### 原生的多渠道打包原理

核心原理就是在编译时通过gradle修改AndroidManifest.xml中的meta-data 占位符的内容，执行N次的打包流程

![image-20201216150908893](https://i.loli.net/2020/12/16/b497RyMQgCVk1Tz.png)

基本流程

- Manifest配置渠道占位符

  ```xml
  <meta-data
              android:name="channel"
              android:value="${channel_value}"/> //yyb,360
  
  ```

  

- app模块的build.gradle

```groovy
android{
    //产品维度，没有实际意义，但gradle要求
    flavorDimensions 'default'
    //productFlavors是android节点的一个节点
    productFlavors {
        baidu {}
        xiaomi { }
        yyb{}
    }
    //如果需要在不同渠道统一配置，使用productFlavors.all字段
    //替换manifest里面channel_value的值
    productFlavors.all { flavor ->
        manifestPlaceholders = [channel_value: name]}

    //设置渠道包名称-> howow-xiaomi-release-1.0.apk
    applicationVariants.all { variant ->
        variant.outputs.all { output ->
            //output即为该渠道最终的打包产物对象
            //设置它的outputFileName属性
            outputFileName = "hwow_${variant.productFlavors[0].name}_${variant.buildType.name}_${variant.versionName}.apk"
        }
    }
}

```



- 运行时获取渠道信息

  

  ```java
  private String getChannel() {
         PackageManager pm = getPackageManager();
         ApplicationInfo appInfo = pm.getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
         return appInfo.metaData.getString("channel_value");
  }
  
  
  ```

  



很明显这种原生的方式有致命的缺点 要重复打包，而打包是很耗时的操作。

而且由于Gradle会为每一个渠道都生成对应的不同的Builder文件， 来记录渠道信息 会导致每一个渠道的dex的CRC校验值是不同的。

这个CRC校验值 不同会带来 一个问题。
Tinker的热修复是基于差分补丁来进行的合并成新的APK的。所以要出个热修复的补丁的话，那么就需要Tinker的补丁要区分渠道，并且每一个渠道都要打对应的包才行。



另一种多渠道打包的方案

### APKTool打包原理

 ApkTool来做的多渠道打包方案

![image-20201216152028155](https://i.loli.net/2020/12/16/hbzmdVIcKkgCUTO.png)

以上方案的缺点就是两个

- 要多次打包（原生模式）， 或者多次签名（APKTool模式）
- 都要重新签名 ，影响签名信息 导致校验信息不对，从而影响差分包合并。



痛点就是

1. 不要多次打包
2. 不要影响签名文件



下面就来分析一下签名流程

### 签名

签名就是 apk文件摘要信息使用keystore里面的私钥加密后的产物，签名是摘要的加密结果

签名和内容对应不上 系统就会认为内容被篡改了，从而拒绝安装。



在apk  META-INF 目录下的 *.RSA 文件下有签名信息。

![img](https://img-blog.csdn.net/20140212101830000?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMzYzNjkwNg==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)



这个证书指纹 就是  公钥



整体的逻辑就是

打包时
F（内容（摘要）  + 私钥 ） = 签名

验证时

系统 有的信息：新内容（摘要） 、新签名、公钥

新内容（摘要）  = F（ 新签名 +公钥）

![img](https://upload-images.jianshu.io/upload_images/2438937-0098ad79aef8cd4a.png?imageMogr2/auto-orient/strip|imageView2/2/format/webp)





所以你的私钥不对的时候，  公钥解密解出来的摘要和内容计算出来的就对不上了。从而完成对身份的校验。


这里要注意一下

和HTTPS 不同   两边的加密算法是不一样的。
android签名  是用私钥进行加密 ，公钥进行解密。
Https是公钥进行加密， 私钥进行解密。

android的场景是 要让大家知道，信息是我发布出来的。
Https的场景是  大家说的只有我一个人听得懂。
所以解密方是不一样的。



#### V1签名

![img](https://img.mukewang.com/wiki/5f4e43ec09a5c1ce17100650.jpg)



上图就说明V1 的签名流程了。



关键点

- 对apk 原本的所有文件都进行了摘要计算
- 做了二级的摘要计算
- 对二级摘要计算的结果 进行加密处理  得到 签名

感觉这个摘要计算没啥必要啊。
一级摘要已经可以保证完整性了呀。

v1签名的缺陷

- 速度慢， 因为要对全部的文件进行摘要计算 所以速度是比较慢的
- 摘要存储的文件夹并没有参与 签名计算

不过这个摘要存储文件没有参与签名文件计算是一些快速批量打包方案的基础。






#### V2签名

是android 7.0之后引入的。

V2签名是基于apk的存储结构进行的。
apk的存储其实就是zip的存储

##### zip的存储结构

![img](https://img.mukewang.com/wiki/5f4e441709b4585a23901890.jpg)



zip 存储分成的三个部分。

#### ![img](https://img.mukewang.com/wiki/5f4e44260959ae3834900246.jpg)



V2的校验不再对单独的文件进行校验，
而是直接对apk整体分块（1M） 计算摘要，
然后再做二级摘要计算。

再进行私钥加密。

![img](https://img.mukewang.com/wiki/5f4e443609e00ea445102034.jpg)



得到的结果插入到apk文件的 文件数据区和文件目录区中间。

![img](https://img.mukewang.com/wiki/5f4e4445093fefb144940246.jpg)



并且插入签名区时，里面包含了多个ID - 值 的键值对信息。
签名信息会记录在  id： 0x7109871a 处。
其他的id 是不对被校验的。
所以说  可以在这里做文章。让渠道信息保存在这里，就能不破坏签名信息的前提下进行渠道信息的写入。





由于V2签名不再是对原先apk解压后进行摘要计算，而是直接对apk进行分块的摘要计算，所以速度上会快很多。
而且也不会像V1一样创建出无效的摘要信息存储文件夹。







