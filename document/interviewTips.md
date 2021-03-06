# 问题点

## 性能优化

如果问到性能优化，就先往 大图触发的OOM 方面讲， 
由于有大量客户定制的图片，对于关键的图片，设置图片的时候，通过hook去获取图片的bitmap 对应的尺寸，如果尺寸出入比较大的话，那么就上报一下。



## 工作的亮点



1 可以把副号这种方案当中 亮点去说，
我们的业务场景中是有一个设备同时输出多个画面的需求的。
之前应对这个需求 是通过一个壳子里用多个板子来实现的。

为了节约设备成本 设计了这套方案。





之前的方案是 一台设备里面放了 多个板子来实现的。

需求是 要能同时看到同一个设备的多个视屏源。

当时有两个思路， 
一个是    对于一个用户 管理 自己的状态 发布给服务器，然后 远端设备根据自己的需求 来选择 要显示哪几个视屏源。
这样带来的问题就是  这一个设备的视频源之间有很多联动关系。

比如一些画面置顶的操作，如果用单个用户发多类型流的方式话，就需要对 流的类型 和成员进行额外的维护。
比如说设备A有类型1，2，3,我就要A1 画面显示在第一个位置，A2画面显示在第5个位置，A3画面显示在第8个位置，这样就要对用户的每种类型的流都要维护。

明显比做成统一的用户进行维护要复杂。
所以选择了process 属性来一个新进程 来做位单独的用户来处理的方案。





2  设计一套可以替换全部 string 和 drawable资源的方案。

用javassist 替换了 Resources的 getText 方法 和getDrawable 方法
修改id指向的资源。

这样运维人员 只要知道目标的资源的id就可以 在后台替换图片了。

之前是只能替换由特定的资源类去读出的资源。
但是代码里很多并没有使用特定的资源类去读资源。
有了这个方案之后，只要告诉运维的人员对应资源的id就能让他们自己在
后台配置了。
不再需要我们这边出版本来对图片进行操作。

3 
之前在做数字货币钱包的 时候，  在一些小币种的dapp没有开源的情况下， 通过反编译竞品，摸索出了 dapp的接口 完成了需求。



4 重点   工程sdk化

难点 去除 第三方库

比如  rxjava  butterknife    retrofit

5 seanet 网络的接入

### 补充：

工作最有价值的事情。
认为是两件事：
 一个是 完成了一个 通过fork多个进程来实现一个硬件能同时登录多个账户，打破了之前一个硬件对应一个账户的限制，从而缩减了一个终端需要同时输出多个画面的场景下的硬件成本，也增加了产品方案的灵活性，方案的提出者和主力开发。

另一个是接入中科院的SEANet, 之前我们会议的基础能力是用过sip来完成 媒体的传输的。会议的整体流程包括 入会离会 媒体流传输 都是通过sip来完成的。后面接了个需求，是要媒体流的传输替换为SEANet下，这时候之前的即时会议的流程就基本都要修改了。一直都是通过sip的注册和呼叫 来作为会议的基础的，接入seanet之后，就得改为了由终端和 会控服务器进行直接管理控制了。  而且他们还有一些很复杂的业务模型。和我们之前的很不一样。
比如为了实现 vr 效果，会有多路流 合并成为一路流的情景。 要有要去控制同一个网关下的其他设备。

在明心这块如果问 你做了什么的话  回答的整体逻辑是：

 主要做了两次项目架构重构，以及一次底层业务能力的拓展。
两次架构 第一次是把手机端和盒子端的代码整合到一块，做了更多共性模块的抽离。从而避免的很多时候 同一 个改动要在多个代码仓库里去改动。
一个是把项目给sdk化，重新定义sdk层的业务接口。使得项目的分离度更高

底层业务能力的扩展是 把底层实现从sip转为了 SEANet。

还有一些其他小的优化， 比如 能支持 市场人员和产品在不需要通过研发出包的情况下 自己去配置 想要的文案和图片。  比如一些启动流程的优化，通过线程池配合维护的启动任务的依赖关系， 做成了仿造gradle的任务式的启动流程；图片的大小不匹配的报警等等 





## 整合重构代码

把手机代码和盒子代码进行了整合

对于主体一直的界面 ，尽量都用viewstub来延迟加载。然后viewStub里面做设备类型的判断。
为啥用viewStub， 这样 xml文件里可以清晰一些， 修改的时候也比较好改。

然后对于各种硬件间的差异， 比如 视频源能力 和 声音方案能力等 都尽量用装饰者模式来包装。

尽量把相同逻辑整合起来，

### 









## 最近的工作

整个手机端和盒子端的代码

做了一个网络测试工具（看看这个能不能说些啥）
最近业务不多，是准备处理些多人协作时 相同的资源被重复的问题， 来提高代码的质量。



网络丢包 策略优化

在服务器带宽有限的情况下， 发生了丢包， 要求重传。
导致服务器带宽压力更大， 结果丢包更加严重，引起的恶性循环。

现在在做的优化方案是 准备在丢包越来越严重的情况下动态调整 重传次数。 一开始是3次。







## 数据库升级

数据库升级是 需要把每一个版本的改动都保留下来。
改动和版本一定要对应。而且需要按照版本号一步一步的来升级。

我们是用不带break的switch（）的形式来处理的。



如果是几个字段的改动的话， 那就直接添加上， 改下版本号，主要反序列化成对应对象的时候 要注意字段的顺序。
如果改动比较多的话，就直接迁移更加方便一点。

# 参考资料

## 面试题

https://www.jianshu.com/p/ccfdb32da717











自我介绍

16年毕业于深圳大学，至今有4年android开发经验。

刚毕业的时候是做了 物联网 音箱项目， 后来也开发过数字货币钱包，维护过数字货币交易所， 现在的主要工作是音视频即时通讯相关的开发。
个人觉得比较有优势的点 是 经手多种类型的项目，有从零开发新业务经验，也有维护陈年老项目的经验。
2C  2B的项目都做过。可以快速上手可以业务类型的开发。









项目： 音视频即时通讯项目 - 盒子端以及手机端

职责描述







主要成果

- 两次项目重构
  - 第一次是整合 盒子和手机端代码 ，避免重复接口
  - 盒子和手机的sdk化， 也就是把即时通讯部分抽离出来成为库
- 优化
  - 优化Jni锁处理逻辑
    用AQS 代替给Jni加上Class Synchronize大锁 ，减少的很多ANR的发生概率
  - 
    用注解处理实现会控事件的自动生成，避免大量手动添加模板代码
  - 优化启动逻辑
    参考gradle的编译流程，将应用初始化统一到任务管理器中进行加载，使得初始化流程更加清晰











