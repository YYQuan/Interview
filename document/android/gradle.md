# Gradle  



## Gradle构建

### 基础

gradle分为了三个部分

![img](https://img.mukewang.com/wiki/5f4e44d10976059b09060598.jpg)

![image-20201217112126632](https://i.loli.net/2020/12/17/xnt1hwkmzv8yqMK.png)

### 项目构建生命周期

![img](https://img.mukewang.com/wiki/5f4e44d909e0221615380324.jpg)

疑问
settings.gradle  、 project下的build.gradle、module下的build.gradle的作用是啥？

这几个文件的加载 就对应着gradle构建的生命周期流程。
tips: gradle中没有module的概念， android studio的每一个module对于gradle来说都是一个project.

- 初始化阶段
  执行根目录下的settings.gradle 分析哪些project(module)参与本次构建
- 配置阶段
  加载本次参与构建的project的build.gradle文件，
  解析并且实例化成一个Gradle的Project对象，然后分析Project之间的依赖关系， 分析Project下的Task的依赖关系，
  生成 有向无环拓扑结构图
- 执行阶段
  这个真正Task被执行的阶段， gradle会根据之前分析的结果来决定哪些Task要被执行， 应该以何种顺序被执行。



**Tips**

```Tip
Task 是 Gradle中的最小执行单元，我们的所有构建任务都是执行了一个个的task，一个Project中可以有多个Task，Task之间可以相互依赖。
```





gradle的任务命令  都会触发gradle的初始化阶段和配置阶段,区别只是执行Task任务。
包括Sync Gradle  /Gradle Clean 命令  都会。

也就是Gradle得执行了初始化 和配置 ，才能确定Task得怎么执行。



监听任务执行流程

在seettings.gradle的头部加上下列代码即可

//tips  对中文支持 不友好 ，用中文可能会报错

```gradle
// 1.在Settings.gradle配置如下代码监听构建生命周期阶段回调
gradle.buildStarted {
    println "项目构建开始..."
}
// 2.初始化阶段完成
gradle.projectsLoaded {
    println "从settings.gradle解析完成参与构建的所有项目"-->
}

//3.配置阶段开始 解析每一个project/build.gradle
gradle.beforeProject { proj ->
    println "${proj.name} build.gradle解析之前"
}
gradle.afterProject { proj ->
    println "${proj.name} build.gradle解析完成"
}
gradle.projectsEvaluated {
    println "所有项目的build.gradle解析配置完成"-->2.配置阶段完成
}

gradle.getTaskGraph().addTaskExecutionListener(
    new TaskExecutionListener(){
        //某任务开始执行前
        @Override
        void beforeExecute(Task task) {
            println("---20201217 before  task"+task.getName())
        }
        //某任务执行完成
        @Override
        void afterExecute(Task task, TaskState state) {
            println("---20201217 after task " +task.getName())

        }
    }
)


gradle.buildFinished {
    println "项目构建结束..."
}

```

打印task依赖

得到build.gradle下的build.gradle才行， 要不 project 会为空  settings.gradle是不会生成Project对象的
build.gradle才会生成对应 的project对象。

```groovy
afterEvaluate { project ->
    //收集所有project的 task集合
    Map<Project, Set<Task>> allTasks = project.getAllTasks(true)
    // 遍历每一个project下的task集合
    allTasks.entrySet().each { projTasks ->
        projTasks.value.each { task ->
            //输出task的名称 和dependOn依赖
            System.out.println(task.getName());
            for (Object o : task.getDependsOn()) {
                System.out.println("dependOn--> " + o.toString());
            }

//打印每个任务的输入 ， 输出
//for (File file : task.getInputs().getFiles().getFiles()) {
//    System.out.println("input--> " + file.getAbsolutePath());
//}
//
//for (File file : task.getOutputs().getFiles().getFiles()) {
//    System.out.println("output--> " + file.getAbsolutePath());
//}
            System.out.println("----------------------------------");
        }
    }
}

```

依赖的例子

![image-20201217121753334](https://i.loli.net/2020/12/17/NdUXp2QYabL6MWy.png)

![image-20201217121857789](https://i.loli.net/2020/12/17/x8bofVBGlCXO4dI.png)



各个build.gradle生成的project对象是有层级概念的。

![img](https://img.mukewang.com/wiki/5f4e450e0991ac7e18521070.jpg)



### Task & TaskTransform

Task是Gradle构建的最小单元，
Gradle通过把一个个的Task串联起来完成具体的构建任务。
每一个Task 都属于一个Project
Gradle在构建的过程当中会形成一个 依赖图，
这个依赖图会保证各个Task的执行顺序。

#### Task的配置项

![image-20201217142021819](https://i.loli.net/2020/12/17/5HfxawzdJ6rBo4C.png)



要注意Task的闭包代码块是在gradle的配置阶段就会被执行了的。并不是Task的执行阶段。如果想要在Task执行阶段来做处理的话， 得在Task中加入Task Actions来执行动作。
![image-20201217142404318](https://i.loli.net/2020/12/17/gXjJCVTvmFYuZkP.png)



#### Task的Action

gradle同一个闭包当中， 第二个doFirst 会比第一个doFirst先执行



```groovy
task tinyPngTask(group:'imooc',description:'compress images'){
    println 'this is TinyPngTask'   //1.直接写在闭包里面的，是在配置阶段就执行的
    doFirst {
        println 'task in do first1'  //3.运行任务时，后于first2执行
    }
    doFirst {
        println 'task in do first2' //2.运行任务时，所有actions第一个执行
    }
    doLast {
        println 'task in do last'   //4.运行任务时，会最后一个执行
    }
}

```

为什么会这样呢？
因为实际上doFirst 、doLast 最终都是添加到了同一个集合当中。
doFirst就是添加到集合的头部，
doLast就是添加到集合的尾部。
所以第二个添加的doFirst会比第一个先执行。



#### Task的依赖



#### dependsOn

dependsOn: 设置任务依赖关系。执行任务B需要任务A先执行

```groovy
ASProj/build.gradle---------------------task taskA {    doFirst {        println 'taskA'    }}task taskB {    doFirst {        println 'taskB'    }}taskB.dependsOn taskA   //B在执行时，会首先执行它的依赖任务A------------------------terminal:./gradlew taskB ===>taskA taskB------------------------terminal:./gradlew taskA taskB ===>taskA taskB
```



#### mustRunAfter  

设置任务执行顺序 
和 dependsOn不一样。
mustRunAfter 不需要执行任务A，但是任务A 、B都存在某一次的构建当中时，任务A必须先于任务B执行。



```groovy
ASProj/build.gradle---------------------task taskA {    doFirst {        println 'taskA'    }}task taskB {    doFirst {        println 'taskB'    }}taskB.mustRunAfter taskA //在一次任务执行流程中，A,B都存在，这里设置的执行顺序才有效----------------------terminal:./gradlew taskB ===>taskB只执行TaskB时 B不会被执行----------------------terminal:./gradlew taskA taskB ===>taskA taskB    同时执行 task  A 和B , A被先被执行
```



#### finalizedBy

和dependsOn是相反的

只执行任务B 时，A也会在B结束后执行

```groovy
ASProj/build.gradle---------------------task taskA {    doFirst {        println 'taskA'    }}task taskB {    doFirst {        println 'taskB'    }}taskB.finalizedBy taskA   //B执行完成，会执行A------------------------terminal:./gradlew taskA taskB ===>taskB taskA------------------------terminal:./gradlew taskB ===>taskB taskA
```



#### Transform

Transform的作用：
如果我们想对编译时产生的Class文件，在转换成Dex文件之前做些处理（字节码插桩，替换父类...）。

我们可以通过Gradle插件来注册我们编写的Transform,注册后的Transform 会被gradle包装成TransformTask，这个TransfromTask会在java compile Task 执行完毕后运行。

一些Transform的使用场景：

- Hilt中 替换掉 父类
- Hugo耗时统计库 会在每个方法中插入代码来统计方法耗时
- InstantPatch热修复，在所有方法前插入一个预留函数，可以将有bug的方法替换成下发的方法
- CodeCheck 代码审查

gradle的处理流程

![img](https://img.mukewang.com/wiki/5f4e4525098c4a8c51120952.jpg)

从这个处理流程可以看出 TransformTask和普通Task的区别
TransformTask是一定要有input  output的
而普通的Transform是不一定有输入  输出的。



##### 如何自定义Transform

```java
public class MyTransform extends Transform {
    @Override
    public String getName() {
        return "MyTransform";
    }
    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        //该transform接受那些内容作为输入参数
        //CLASSES(0x01),
        //RESOURCES(0x02)，assets/目录下的资源，而不是res下的资源
        return TransformManager.CONTENT_CLASS;
    }
    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        //该transform工作的作用域--->见下表
        return TransformManager.SCOPE_FULL_PROJECT;
    }
    @Override
    public boolean isIncremental() {
        //是否增量编译
        //基于Task的上次输出快照和这次输入快照对比，如果相同，则跳过
        return false;
    }
    @Override
    public void transform(TransformInvocation transformInvocation) {

      //注意：当前编译对于Transform是否是增量编译受两个方面的影响：
      //（1）isIncremental() 方法的返回值；
      //（2）当前编译是否有增量基础（clean之后的第一次编译没有增量基础，之后的编译有增量基础）
      def isIncremental =transformvocation.isIncremental&&!isIncremental()
       //获取一个能够获取输出路径的工具
      def outputProvider= transformInvocation.outputProvider
      if(!isIncremental){
        //不是增量更新则必须删除上一次构建产生的缓存文件
        outputProvider.deleteAll()
      }
transformInvocation.inputs.each { input ->
            //遍历所有目录
            input.directoryInputs.each { dirInput ->
                println("MyTransform:" + dirInput.file.absolutePath)


                File dest = outputProvider.getContentLocation(directoryInput.getName(),
directoryInput.getContentTypes(), directoryInput.getScopes(), Format.DIRECTORY);
                //将修改过的字节码copy到dest，就可以实现编译期间干预字节码的目的了
                FileUtils.copyDirectory(dirInput.getFile(), dest);
            }

            //遍历所有的jar包(包括aar)
           input.jarInputs.each{file,state->
                  println("MyTransform:" + file.absolutePath)

             File dest = outputProvider.getContentLocation(
                        jarInput.getFile().getAbsolutePath(),
                        jarInput.getContentTypes(),
                        jarInput.getScopes(),
                        Format.JAR);
                //将修改过的字节码copy到dest，就可以实现编译期间干预字节码的目的了
                FileUtils.copyFile(jarInput.getFile(), dest);
            }
        }
    }
}

```

##### Transform的输入输出概念

![image-20201217152025580](https://i.loli.net/2020/12/17/tvZkorFRYIhz5dX.png)

##### 自定义Transforms的工作流程

![img](https://img.mukewang.com/wiki/5f4e4538089a1af808811280.jpg)



##### Transform Scope的作用域

![image-20201217151141566](https://i.loli.net/2020/12/17/WkCZUmxsugyXlK4.png)



##### 配置流程

参考

https://www.imooc.com/wiki/mobilearchitect/gradleplugin.html



##### 字节码处理原理

最重要的原理就是

在Transform里面用字节码插桩技术进行操作
Transform里面可以拿到java文件生成的class，
transform操作这个class从而来处理字节码。

字节码的工具有AspectJ, apt ，javassist....

这里选择javassist （上手简单一些）。



自定义Transform插件的基础配置

在buildSrc下  要有两个

![image-20201218100410738](https://i.loli.net/2020/12/18/75y9vDAY2uTkZfQ.png)

apply上目标插件
其transform只会对该module的project对象生效

![image-20201218100631958](https://i.loli.net/2020/12/18/3nj6PSv1Gxzwc5y.png)

##### TinyPngPlugin.groovy

```groovy
import org.gradle.api.Plugin
import org.gradle.api.Project

class TinyPngPlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {

    }
}
```

##### TinyPngPTransform.groovy

```groovy
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import javassist.ClassPool
import sun.instrument.TransformerManager

class TinyPngPTransform extends  Transform{
    private ClassPool classPool = ClassPool.getDefault()

    TinyPngPTransform(Project project) {
        classPool.appendClassPath(project.android.bootClasspath[0].toString())

        classPool.importPackage("android.os.Bundle")
        classPool.importPackage("android.widget.Toast")
        classPool.importPackage("android.app.Activity")
    }

    @Override
    String getName() {
        return "TinyPngPTransform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }


    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        def outputProvider = transformInvocation.outputProvider
        //outputProvider.deleteAll()

        transformInvocation.inputs.each { input ->
            input.directoryInputs.each { dirInput ->
                //遍历处理没一个文件夹下面的class文件
                handleDirectory(dirInput.file)
                // 将输入的所有目录复制到output指定目录
                def dest = outputProvider.getContentLocation(dirInput.name, dirInput.contentTypes, dirInput.scopes, Format.DIRECTORY)
                FileUtils.copyDirectory(dirInput.file, dest)
            }

            input.jarInputs.each { jarInput ->
                // 重新计算输出文件jar的名字（同目录 copyFile 会冲突）
                def jarName = jarInput.name
                def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4)
                }

                // 生成输出路径
                def dest = outputProvider.getContentLocation(md5Name + jarName, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                //处理jar包
                def output = handleJar(jarInput.file)
                // 将输入内容复制到输出
                FileUtils.copyFile(output, dest)
            }
        }
        pool.clearImportedPackages()
    }

    File handleJar(File jarFile) {
        //添加类搜索路径,否则下面pool.get()查找类会找不到
        classPool.appendClassPath(jarFile.absolutePath)
        //通过JarFile，才能获取得到jar包里面一个个的子文件
        def inputJarFile = new JarFile(jarFile)
        //通过它进行迭代遍历
        Enumeration enumeration = inputJarFile.entries()

        //jar包里面的class修改之后，不能原路写入jar。会破坏jar文件结构
        //需要写入单独的一个jar中
        def outputJarFile = new File(jarFile.parentFile, "temp_" + jarFile.name)
        if (outputJarFile.exists()) outputJarFile.delete()
        JarOutputStream jarOutputStream = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(outputJarFile)))

        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) enumeration.nextElement()
            String entryName = jarEntry.getName()
            InputStream inputStream = inputJarFile.getInputStream(jarEntry)

            //构建一个个需要写入的JarEntry，并添加到jar包
            JarEntry zipEntry = new JarEntry(entryName)
            jarOutputStream.putNextEntry(zipEntry)

            println("entryName:" + entryName)
            if (!shouldModifyClass(entryName)) {
                //如果这个类不需要被修改，那也需要向output jar 写入数据，否则class文件会丢失
                jarOutputStream.write(IOUtils.toByteArray(inputStream))
                inputStream.close()
                continue
            }
            def ctClass = modifyClass(inputStream)
            def byteCode = ctClass.toBytecode()
            ctClass.detach()
            inputStream.close()

            println("code length:" + byteCode.length)
            //向output jar 写入数据
            jarOutputStream.write(byteCode)
            jarOutputStream.flush()
        }
        inputJarFile.close()
        jarOutputStream.closeEntry()
        jarOutputStream.flush()
        jarOutputStream.close()

        return outputJarFile
    }

    void handleDirectory(File dir) {
        //将当前路径加入类池,不然找不到这个类
        println("handleDirectory:" + project.rootDir)
        pool.appendClassPath(dir.absolutePath)
        if (dir.isDirectory()) {
            dir.eachFileRecurse { File file ->
                String filePath = file.name
                println("filePath:" + file.absolutePath)
                //确保当前文件是class文件，并且不是系统自动生成的class文件
                if (shouldModifyClass(filePath)) {
                    def inputStream = new FileInputStream(file)
                    def ctClass = modifyClass(inputStream)
                    ctClass.writeFile(filePath)
                    ctClass.detach()
                }
            }
        }
    }

    //通过文件输入流，使用javassist加载出ctclass类
    //从而操作字节码
    CtClass modifyClass(InputStream inputStream) {
        //之所以使用inputStream ，是为了兼顾jar包里面的class文件的处理场景
        //从jar包中获取一个个的文件，只能通过流
        ClassFile classFile = new ClassFile(new DataInputStream(new BufferedInputStream(inputStream)))
        println("found activity:" + classFile.name)
        CtClass ctClass = classPool.get(classFile.name)
        //解冻，意思是指如果该class之前已经被别人加载并修改过了。默认不允许再次被编辑修改
        if (ctClass.isFrozen()) {
            ctClass.defrost()
        }

        //构造onCreate方法的Bundle入参
        CtClass bundle = classPool.getCtClass("android.os.Bundle")
        CtClass[] params = Arrays.asList(bundle).toArray()
        //通过ctClass

        def method = ctClass.getDeclaredMethod("onCreate", params)

        def message = classFile.name
        //向方法的最后一行插入toast，message为当前类的全类名
        method.insertAfter("android.widget.Toast.makeText(this," + "\"" + message + "\"" + ",Toast.LENGTH_SHORT).show();")

        ctClass.removeMethod(method)
        ctClass.addMethod()
        return ctClass
    }

//检验文件的路径以判断是否应该对它修改
//不是我们包内的class，不是activity.class 都不修改
    static boolean shouldModifyClass(String filePath) {
        return (filePath.contains("org/devio/as/proj")
                && filePath.endsWith("Activity.class")
                && !filePath.contains('R$')
                && !filePath.contains('$')
                && !filePath.contains('R.class')
                && !filePath.contains("BuildConfig.class"))
    }

}
```



## Gradle打包流程



### Apk的文件结构

![img](https://img.mukewang.com/wiki/5f4e478d08a98c7816000295.jpg)

![image-20201221095547509](https://i.loli.net/2020/12/21/eO9UIaZ3NoV6Kn7.png)





### aapt打包流程

aapt是之前的打包流程，现在已经是用aapt2来进行打包的了。
但是也能用aapt的打包流程来帮助理解

- aapt

![img](https://img.mukewang.com/wiki/5f4e47a808ca9b3005360882.jpg)

- aapt2打包流程

![img](https://img.mukewang.com/wiki/5f4e47b20943175b25942022.jpg)

![image-20201221100056863](https://i.loli.net/2020/12/21/Ak1scdxaN7LbZW2.png)



#### 打包命令流程参考

https://www.imooc.com/wiki/mobilearchitect/aapt2.html

主体流程

- aapt2 compie 
  编译资源文件
- aapt2 link 
  分配资源ID，并生成R.java文件和resource.arsc资源索引表
- javac 
  把java文件变异成class文件
- d8
  把class文件转换成dex文件
- aapt2 add
  把 资源文件 ，资源索引表文件， d8生成的dex文件打包到一起 生成apk文件
- zipaligin
  使用apksigner对apk进行字节对齐
  字节对齐是为了提高指令的运行效率，毕竟虚拟机对int的数据处理速度是最快的。
- apksign
  签名



上面只是对基础的编译过程，
并没有涉及到 多模块的资源合并等处理。

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







