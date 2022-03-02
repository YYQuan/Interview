# Mocktio 
[https://github.com/mockito/mockito](https://github.com/mockito/mockito) mock框架
​

# Powermock 
 [https://github.com/powermock/powermock](https://github.com/powermock/powermock) 对静态方案进行mock
如果与Mocktio一起使用，版本需要进行关联：[https://github.com/powermock/powermock/wiki/Mockito#supported-versions](https://github.com/powermock/powermock/wiki/Mockito#supported-versions)
# Robolectric
[http://robolectric.org](http://robolectric.org) 可以脱离android真机与模拟器进行测试
例子：[https://github.com/android/testing-samples](https://github.com/android/testing-samples)
​

# 其他
命名规则：RSpec-style
不要使用逻辑流关键字(If/else、for、do/while、switch/case)，在一个测试方法中，如果需要有这些，拆分到单独的每个测试方法里
测试真正需要测试的内容，需要覆盖的情况，一般情况只考虑验证输出（如某操作后，显示什么，值是什么）
不需要考虑测试private的方法，将private方法当做黑盒内部组件，测试对其引用的public方法即可；不考虑测试琐碎的代码，如getter或者setter
每个单元测试方法，应没有先后顺序；尽可能的解耦对于不同的测试方法，不应该存在Test A与Test B存在时序性的情况
