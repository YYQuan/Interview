# Groovy





## 特性

groovy是运行在jvm上的。
所以和java的运行环境是一模一样的。

但是groovy是同时支持面向过程和面向对象的。
取决于开发者的用法。


groovy的主要用途
编写脚本
但是实际上groovy也是支持编写程序的。

groovy 和java的主要区别
可以从下面代码中 体会下

```groovy
// groovy  特点
println "hello groovy"

// 正常的java 和groovy 写法
//class HelloGroovy {
//
//    static void main(String[] args) {
//        println("Hello Groovy")
//    }
//}
```



## 基础语法





### 变量声明



groovy 支持弱类型声明



```groovy
//强类型
int x = 10

// 弱类型
def x = 10

```



强弱类型的使用建议：
如果是只在内部使用的变量，定义成 def 由编译器去推断即可。
否则还是使用 强类型来进行约束比较好

实际上groovy 的动态类型变量就是 Object类型

![image-20220409112938116](https://s2.loli.net/2022/04/09/AEgs9w6lqkbuR5U.png)



### 字符串

groovy  增加了一种GString 的字符串类型

```groovy

def s = 'asd'
//groovy 中有 三个单引号来声明字符串的方式
def s = '''asd'''



```

 一个单引号和三个单引号有什么区别呢？

三个单引号的字符串 可以指定格式



```groovy
def s = ''' 111
222
333'''

// 输出和写入是一样的， 如果是单引号的话
println s
：
{
   111
    222
    333
    
    
}
```



groovy 也可以用双引号来定义字符串

并且双引号的字符串  是可以动态改变的。



```groovy

def name = "YYQ"

def sayHello = "hello ${name}"
```



![image-20220409115131962](https://s2.loli.net/2022/04/09/4kU8A3B1LD5ZlTP.png)



从上图可以看出 ，groovy的字符串的动态特性是通过GString这个类来支持的

可扩展的内容是支持任意的表达式的

```groovy
def  sumStr = "sum of  2 and 3 equals ${2+3}"
println sumStr
{
sum of  2 and 3 equals 5
}
```



tips: GString  可以直接传给String 类型的。





### 逻辑控制



#### 顺序

#### 条件



if/else 和java 类似



switch/case

switch 可以同时匹配任意类型

```groovy
def x =1.23
def result
switch(x){
    case 'foo':
        result = 'foo'
        break
    case 1.23:
        result = '1.23'
        break
    case [4,5,6]:
        break
    case BigDecimal:
        result = "is BigDecimal"
        break
}

```



#### 循环

for的扩展

可以很容易的定义 map ，而且map的key和value的类型也不需要一致

![image-20220409140750646](https://s2.loli.net/2022/04/09/GhtvZLAs8MfQU1b.png)





### 闭包



闭包就是代码块







闭包的使用

```groovy
int x = fab(5)
println x


// 这种写法就不用写循环了
int fab(int number ){
    result = 1
    //1 到 number  每一步走 执行一次闭包的函数
    1.upto(number, { n -> result *= n })
    return result
}
```



闭包的关键变量

![image-20220409145131963](https://s2.loli.net/2022/04/09/9KPRLU6vNlBVgZr.png)



闭包的委托策略

```groovy
// 闭包策略


class Student{
    String name = ''

    def close = {
        "student is name ${name}"
    }

    @Override
    String toString() {
        return close.call()
    }
}

class Teacher{
    String name
}

def stu = new Student(name :"student")
def teacher = new Teacher(name:'Teacher')

println stu.toString()
// 手动设置 闭包的delegate的对象
stu.close.delegate = teacher
println stu.toString()

// 设置闭包的策略
stu.close.resolveStrategy = Closure.DELEGATE_FIRST
println stu.toString()

```

![image-20220409152351481](https://s2.loli.net/2022/04/09/sxPNt6yLKpQfwgm.png)



通过上面的代码可知，
闭包的 this ,onwer,delegate的作用是
闭包内部传参的数据来源。

可以通过配置策略，来控制参数的来源



### 数据结构

#### 列表



#### 映射



#### 范围





### 网络访问

```groovy

def response = getNetworkData('http://www.baidu.com')
println response

def getNetworkData(String url){
    def connection =new URL(url).openConnection()
    connection.setRequestMethod('GET')
    connection.connect()

    def response  =connection.content.text

    println  response.toString()
    return response
}
```





### Json解析



用groovy内置的 函数比 Gson啥的都要好用

内置了 JsonSlurper

```groovy
String response = ""
def jsonSluper = new JsonSlurper()
jsonSluper.parseText(response)
```



### XML 解析



- 如何解析一个xml
- 如何创建



XmlSlurper()

```groovy
def xmlSluper = new XmlSlurper()
def response = xmlSluper.parseText("****")
//获取 response 节点的value节点的第2个books节点下的第1个book节点
response. value.books[1].book[0]
//@AAA 为 该节点下的 AAA 属性
response. value.books[1].book[0].@AAA

// 查找 1
respinse.value.books.each{
    books ->
    books.book.each{
        
    }
}
//查找2 深度遍历
response.depthFirst().findAll{
    book  -> 
}
// 查找3  广度遍历
response.children().findAll{
    
}
```



xml 文件的生成

MarkupBuilder() 生成xml的核心类



![image-20220409162830703](https://s2.loli.net/2022/04/09/Rez25JXhZ7ksDHp.png)



### 文件处理

- java中的处理方式  groovy都支持
- groovy 扩展了更快捷的方法

```groovy
def  file = new File('GroovyDemo.iml')

file.eachLine{
    line -> println line
}

println file.getText()  // 读全部
println file.readLines()//一行一行读
//读一部分  读前一百个字符
file.withReader {reader ->
    char[] buffer = new char[100]
    reader.read(buffer)
    return buffer
}
```



groovy 实现文件复制

```groovy

def copyFile(String sourcePath ,String destationPath){

    try{
        //创建目标文件
        def desFile = new  File(destationPath)
        if(!desFile.exists()){
            desFile.createNewFile()
        }
        new File(sourcePath).withReader { reader ->
            def lines = reader.readLines()
            desFile.withWriter {write ->
                lines.each {line ->
                    write.append(line +"\r\n")
                }
            }

        }
        return true

    }catch (Exception e){
        e.printStackTrace()
    }
    return false

}
```





对象的文件读写

```groovy
def saveObject(Object o ,String path){
    try {
        def desFile  = new File(path)
        if(!desFile.exists()){
            desFile.createNewFile()
        }
        desFile.withObjectOutputStream {out->
            out.writeObject( o)
        }
        return  true
    }catch (Exception e){

    }
    return false
}

def readObject(String  path){

    def obj = null
    try {
        def file = new File(path)
        if(!file.exists()) return null
        file.withObjectInputStream {input->
            obj = input.readObject()
        }
    }catch (Exception e){

    }
    return obj
}

def list = [1,2,3,4]
saveObject(list, "list.o")
println list

def readO = readObject('list.o')
println readO
```





# Gradle



 gradle 使用的是 groovy的语法

但是有独立的api



### gradle的生命周期



load (初始化， 解析 构建所有的project的对象) 

configuration(解析project下面的所有 task对象)

exceute (执行阶段，执行某一任务前需要把前置依赖任务都执行完了才能执行)





#### 监听生命周期

```groovy
//配置阶段开始前的回调
this.deforeEvaluate{}
//配置阶段完成的回调
this.afterEvaluate{}
//gradle执行完毕后的回调监听
this.gradle.buildFinished{}

this.gradle.beforeProject{}

this.gradle.afterProject{}
```



java工程的gradle的build task的默认依赖度的前置task

![image-20220410100427331](https://s2.loli.net/2022/04/10/OWNTeLRQBlk1KZV.png)





## Project



```
// 查看当前工程有多少个project
./gradlew projects
```



目录下 是否有 build.gradle 文件 是该目录是否是gradle的project的标志

一个project会对应一个输出。
比如 

 java  module 对应的就是一个jar 包输出，

lib module对应的就是一个 aar的输出，

application对应的就是apk





### 常用api



```groovy

this.getProjects()

this.getRootProject()

//为所有工程 做统一的配置
allprojects{
	//todo 配置
}
```





### 默认属性

默认就只有下面这几个属性

![image-20220410110655081](https://s2.loli.net/2022/04/10/SjQhpUmlxCREvBL.png)



### 扩展属性

下面这种写法明显就是自定义了扩展属性

![image-20220410111107871](https://s2.loli.net/2022/04/10/RB8wbWTYOcDq7Gn.png)



但是 groovy是不支持这种 语法的。
为啥gradle能这样些呢？

如果把 build.gradle当作一个类来看的话， 这种写法很奇怪。



扩展属性有两种方法

1.在build.gradle中用ext去扩展

2.在gradle.properties中 去扩展

但是方式2只能扩展 key  = value的属性

方式1中还可以扩展去map类型等其他属性

```groovy
//gradle 中去扩展属性
ext{
    
}

//我们可以在根project下去遍历subProject
//然后给每一个subProject 统一的去扩展
//但其实  只要在跟project中去 扩展， 子Project去调用根工程的扩展属性就可以了
//但是gradle中规定  父project中的属性，子project中都会直接继承下来，所以子project中可以直接调用
```



```groovy
//判断是否有 xxx这个变量
if(hasProperty('XXX')){
}
```



### project下的文件操作

```groovy
//根工程的文件
getRootDir()
//build目录文件
getBuildDir()
//当前工程的文件
getProjectDir()
//根工程路径
getRootDir().absolutePath

//从根工程开始找对应文件
def file = file(fileName)
//输出内容
file.text

//复制文件 / 文件夹
  copy{
        from file('path')
        into getRootDir()
		//排除
        exclude{
            
        }
        //重命名
        rename{}
 }

// 对文件数进行遍历

    fileTree('app'){FileTree tree ->

        tree.visit{FileTreeElement details ->

            println  'the file name is :' + details.file.name

        }
    }


```



### 依赖相关API

android 工程中 gradle 里用的最多的就是 buildscript

跟进buildscript

![image-20220410152141829](https://s2.loli.net/2022/04/10/yaZ7jfSEMqxNDsc.png)

从类信息中就可以读出， 可以设置respsitories ，dependency..

这两个gradle 根Project中常用的。



跟进入下repositories

![image-20220410152333904](https://s2.loli.net/2022/04/10/Ng8S5PkdexHBjJM.png)

根据源码指引，找到下面类

![image-20220410152350775](https://s2.loli.net/2022/04/10/AZeMFLcuSVY7dil.png)

发现 以前常见的配置的 maven google 这些都是内置到类信息中的。
并不是混乱写都可以的



```groovy
buildscript{
  // buildscript 里的dependencies  是配置gradle插件的依赖
  // project 下的dependencies 是应用程序的依赖
    dependencies{
         
    }
}


dependencies{
    
    comile('xxxxxx'){
        //排除依赖
        exclude module:'xxxx'
        exclude group : 'xxxx''
		//是否要传递依赖
        transitive true
    }
    
}



```



### 外部命令



```groovy
//核心就是用 exce 去执行命令
// 只要配置好环境变量 ，正常来说exce可以实行本地cmd中能执行的所有命令
// 包括java gradle git  ...
task(name:'apkcopy'){
    doLast{
        def sourcePah = this.buildDir.path+'/output/apk'
        def desPath  = 'XXX'
        def command = "mv -f ${sourcePah}   ${desPath} "
        exec{
            try{
                excutable  'bash'
                args '-C' ,command
                println 'execute success'
            }catch (GradleException e){
                println  e
                println  'failure'
            }
        }

    }
}
```





## Task



![image-20220410155959468](https://s2.loli.net/2022/04/10/UGqfWlhYi6ywQ3H.png)

task 是project的一个方法。

平常的写法

```groovy
// 实际上 A 是传进task的一个参数
task  A{

}

// 所以说 直接 写 
task  A { }时
闭包是会直接被执行的

```



创建 task的方式

```

// 1 直接通过task函数创建
task  A {}

//2 用task的容器  TaskContainer 来创建
this.tasks.create(name: 'A'){}



```



task 的配置方式

```
// 在创建的时候就指定
task  A （group： 'Groupa',description: 'task study'）{

}


this.tasks.create(name:'a'){
	setGroup('GroupB')
	setDescription('task s')
}

```

TASK 能配置的参数

![image-20220410161222417](https://s2.loli.net/2022/04/10/JKBmLAv3CZyhb98.png)



```
task A {
// 执行在配置阶段
	println 'A'
	doLast{
// 执行在执行阶段结束
		printLn 'B'
	}

}


```

tips :

找task时  要在project的afterEvaluate 回调中去找。
避免有些task 配置完成了，能找到；有些找不到。



```
this。afterEvaluate{ Project project ->
	def targetTask =	project.tasks.getByName('preBuild')//传入task的名称
	//找到task 然后做处理
	targetTask.doLast{
	
	}
}
```



### task 的执行顺序



```groovy
// taskX 依赖 taskY,taskZ
// taskXm会等到taskZ， taskY 都执行完后才执行
task  taskX (dependsOn:[taskZ,taskY]) {
    doLast{
        println  'XXXX '
    }
}

task  taskY  {
    doLast{
        println  'YYY '
    }
}
task  taskZ  {
    doLast{
        println  'ZZZZZ '
    }
}

task taskA {
    // 依赖所有 以 lib开头的任务
    dependsOn this.task.findAll{
        task  -> task.name.startsWith('lib')
    }
    doLast{
        
    }
}


// 也可以用用 mustRunTask来指定
mustRunAfter




```



把自定义task挂接到 构建过程中

```groovy
this.project.afterEvaluate{ project ->

	def buildTask = project.tasks.getByName('build')

	if(buildTask == null)  throw GradleException()



	buildTask.doLast{

		//执行TaskXXXX
		// 这样TaskXXXX就挂接到构建过程中了
		TaskXXXX.execute()

	}

}



```



Setting类

和Setting类是通过 settings.gradle文件来进行初始化的。
