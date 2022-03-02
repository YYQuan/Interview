推荐一个很好用的 native 内存泄漏检测工具,git地址：[https://github.com/bytedance/memory-leak-detector](https://github.com/bytedance/memory-leak-detector)
​

#### 使用方法：
按照github里面步骤执行就行
对第5步进行一下解释

![image.png](native内存泄露.assets/1634008950823-ee8b79e3-cc56-4c88-a2ae-9628eb70d3a3.png)



## analysis report 
##   -r: report path   第四步得到的文件路径，在机器sdcard/raphael下面，需要pull到电脑上
##   -o: output file name   输出分析结果文件名
 ##   -s: symbol file dir  符号表的位置（这个我不太清楚，有点类似类似android的mapping文件，如何获取，等                                        有空研究一下）
python3 library/src/main/python/raphael.py -r report -o leak-doubts.txt -s ./symbol/



![image.png](native内存泄露.assets/1634009296352-92cff098-9da4-4f4e-920e-9edbd4b9fbc8-16462041664393.png)
下面是我使用工具得到的文件截图

#### report：

![image.png](https://cdn.nlark.com/yuque/0/2021/png/21559115/1634009394617-373ba963-392e-47bb-bb50-45d80e98854e.png?x-oss-process=image%2Fresize%2Cw_825%2Climit_0)


#### leak-doubts:

![image.png](native内存泄露.assets/1634009431578-b8bea560-075b-45ff-aee6-a14f78508101-16462041989375.png)

