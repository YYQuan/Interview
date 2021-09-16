# Cmake

[参考文章](https://blog.csdn.net/afei__/article/details/81201039)

cmake是干嘛的？
编译性语言都需要经过编译和链接 两个步骤才能构建出可执行的程序。
cmake可以就先理解成 做这两件事情的先。
但是cmake好像作用还有更强大些。



## 常用命令

### cmake_minimum_required  - 指定cmake的最小版本

```cmake
cmake_minimum_required(VERSION  3.4.1)
```



### project  -  设置项目名称



```cmake
project(demo)
```



### add_xxxxx  -  设置编译类型

```cmake
add_executable(demo demo.cpp) # 生成可执行文件
add_library(common STATIC util.cpp) # 生成静态库
add_library(common SHARED util.cpp) # 生成动态库或共享库


```



### add_library  -  指定编译包含的源文件

```cmake
add_library(demo demo.cpp test.cpp util.cpp)
```



### aux_source_directory  -  搜索所有的cpp文件

```cmake
#把当前目录下的所有的cpp文件 存储到src_list这个变量当中
aux_source_directory(. SRC_LIST)  # . 就表示当前目录下的所有文件。 这个.就是用来指定路径的

add_library(demo $(SRC_LIST))  # 把SRC_LIST这个变量代表的文件  都包含到demo这个projection的编译当中



```



### file/FILE  -  自定义搜索规则

```cmake
file(GLOB SRC_LIST "*.cpp" "protocol/*.cpp")
add_library(demo ${SRC_LIST})
# 或者
file(GLOB SRC_LIST "*.cpp")
file(GLOB SRC_PROTOCOL_LIST "protocol/*.cpp")
add_library(demo ${SRC_LIST} ${SRC_PROTOCOL_LIST})
# 或者
file(GLOB_RECURSE SRC_LIST "*.cpp") #递归搜索
FILE(GLOB SRC_PROTOCOL RELATIVE "protocol" "*.cpp") # 相对protocol目录下搜索
add_library(demo ${SRC_LIST} ${SRC_PROTOCOL_LIST})
# 或者
aux_source_directory(. SRC_LIST)
aux_source_directory(protocol SRC_PROTOCOL_LIST)
add_library(demo ${SRC_LIST} ${SRC_PROTOCOL_LIST})
```





### include_directories  -  设置包含的目录

```cmake
include_directories(
    ${CMAKE_CURRENT_SOURCE_DIR}
    ${CMAKE_CURRENT_BINARY_DIR}
    ${CMAKE_CURRENT_SOURCE_DIR}/include
)

#linux下还可以通过下面的方式去设置 包含的目录
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -I${CMAKE_CURRENT_SOURCE_DIR}")

```



### 设置链接库搜索的目录

```cmake
link_directories(
    ${CMAKE_CURRENT_SOURCE_DIR}/libs
)

#Linux 下还可以通过如下方式设置包含的目录
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_CURRENT_SOURCE_DIR}/libs")
```





### 查找指定的库文件

find_library(VAR name path)查找到指定的预编译库，并将它的路径存储在变量中。
默认的搜索路径为 cmake 包含的系统库，因此如果是 NDK 的公共库只需要指定库的 name 即可。

```cmake
find_library( # Sets the name of the path variable.
              log-lib
 
              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )
```





### 设置target需要链接的库

查找到指定的库了之后， 就可以通过该命令来进行链接

```cmake
target_link_libraries( # 目标库
                       demo
 
                       # 目标库需要链接的库
                       # log-lib 是上面 find_library 指定的变量名
                       ${log-lib} )
                     
                     
#指定链接动态库或静态库                       
target_link_libraries(demo libface.a) # 链接libface.a
target_link_libraries(demo libface.so) # 链接libface.so    
#指定全路径
target_link_libraries(demo ${CMAKE_CURRENT_SOURCE_DIR}/libs/libface.a)
target_link_libraries(demo ${CMAKE_CURRENT_SOURCE_DIR}/libs/libface.so)
#指定链接多个库
target_link_libraries(demo
    ${CMAKE_CURRENT_SOURCE_DIR}/libs/libface.a
    boost_system.a
    boost_thread
    pthread)

```



### 设置变量



```cmake
#直接设置
set(SRC_LIST main.cpp test.cpp)
add_executable(demo ${SRC_LIST})
#追加
set(SRC_LIST main.cpp)
set(SRC_LIST ${SRC_LIST} test.cpp)
add_executable(demo ${SRC_LIST})
#追加或者删除
set(SRC_LIST main.cpp)
list(APPEND SRC_LIST test.cpp)
list(REMOVE_ITEM SRC_LIST main.cpp)
add_executable(demo ${SRC_LIST})
```



### 条件控制

```cmake
if(MSVC)
    set(LINK_LIBS common)
else()
    set(boost_thread boost_log.a boost_system.a)
endif()
target_link_libraries(demo ${LINK_LIBS})
# 或者
if(UNIX)
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -fpermissive -g")
else()
    add_definitions(-D_SCL_SECURE_NO_WARNINGS
    D_CRT_SECURE_NO_WARNINGS
    -D_WIN32_WINNT=0x601
    -D_WINSOCK_DEPRECATED_NO_WARNINGS)
endif()
 
if(${CMAKE_BUILD_TYPE} MATCHES "debug")
    ...
else()
    ...
endif()



while(condition)
    ...
endwhile()

#start 表示起始数，stop 表示终止数，step 表示步长

foreach(loop_var RANGE start stop [step])
    ...
endforeach(loop_var)


foreach(i RANGE 1 9 2)
    message(${i})
endforeach(i)


```



### 打印信息

```cmake
message(${PROJECT_SOURCE_DIR})
message("build with debug mode")
message(WARNING "this is warnning message")
message(FATAL_ERROR "this build has many error") # FATAL_ERROR 会导致编译失败
```





### 包含其他cmake文件

```cmake
include(./common.cmake) # 指定包含文件的全路径
include(def) # 在搜索路径中搜索def.cmake文件
set(CMAKE_MODULE_PATH ${CMAKE_CURRENT_SOURCE_DIR}/cmake) # 设置include的搜索路径
```





## 常用变量

### 预定义变量

```cmake
PROJECT_SOURCE_DIR #工程的根目录
PROJECT_BINARY_DIR#运行 cmake 命令的目录，通常是 ${PROJECT_SOURCE_DIR}/build
PROJECT_NAME#返回通过 project 命令定义的项目名称
CMAKE_CURRENT_SOURCE_DIR#当前处理的 CMakeLists.txt 所在的路径
CMAKE_CURRENT_BINARY_DIR#target 编译目录
CMAKE_CURRENT_LIST_DIR#CMakeLists.txt 的完整路径
CMAKE_CURRENT_LIST_LINE#当前所在的行
CMAKE_MODULE_PATH#定义自己的 cmake 模块所在的路径，SET(CMAKE_MODULE_PATH ${PROJECT_SOURCE_DIR}/cmake)，然后可以用INCLUDE命令来调用自己的模块
EXECUTABLE_OUTPUT_PATH#重新定义目标二进制可执行文件的存放位置
LIBRARY_OUTPUT_PATH#重新定义目标链接库文件的存放位置
```

### 环境变量

```cmake
$ENV{Name}

#写入环境变量
 set(ENV{Name} value) # 这里没有“$”符号
```

### 系统信息

```
CMAKE_MAJOR_VERSION：cmake 主版本号，比如 3.4.1 中的 3
CMAKE_MINOR_VERSION：cmake 次版本号，比如 3.4.1 中的 4
CMAKE_PATCH_VERSION：cmake 补丁等级，比如 3.4.1 中的 1
CMAKE_SYSTEM：系统名称，比如 Linux-­2.6.22
CMAKE_SYSTEM_NAME：不包含版本的系统名，比如 Linux
CMAKE_SYSTEM_VERSION：系统版本，比如 2.6.22
CMAKE_SYSTEM_PROCESSOR：处理器名称，比如 i686
UNIX：在所有的类 UNIX 平台下该值为 TRUE，包括 OS X 和 cygwin
WIN32：在所有的 win32 平台下该值为 TRUE，包括 cygwin
```



### 主要开关选项

```cmake
BUILD_SHARED_LIBS：这个开关用来控制默认的库编译方式，如果不进行设置，使用 add_library 又没有指定库类型的情况下，默认编译生成的库都是静态库。如果 set(BUILD_SHARED_LIBS ON) 后，默认生成的为动态库
CMAKE_C_FLAGS：设置 C 编译选项，也可以通过指令 add_definitions() 添加
CMAKE_CXX_FLAGS：设置 C++ 编译选项，也可以通过指令 add_definitions() 添加

add_definitions(-DENABLE_DEBUG -DABC) # 参数之间用空格分隔
```



## 项目实例

一个C程序 如何通过cmake来编译呢？


### 最小demo

#### 新建文件 main.c

```c
#include <stdio.h>
int main() {
    printf("Hello World!\n");
    return 0;
}
```

#### 新建文件CMakeLists.txt

```cmake
project(HELLO)
add_executable(hello main.c)#把main.c加入编译
```

#### 新创建build文件夹

在build中执行

- cmake ..  # .. 就回到了cmakeList所在的路径下
- make 

这样在build的目录下就 能得到可执行文件了

### 复杂项目 （ 多目录 、多源文件）

目录结构如下

![img](https://img-blog.csdn.net/20180725140917986?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2FmZWlfXw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)



demo路径下的cmakeList

```cmake
cmake_minimum_required (VERSION 2.8)
project(demo)
aux_source_directory(. DIR_SRCS)
# 添加math子目录
add_subdirectory(math)
# 指定生成目标
add_executable(demo ${DIR_SRCS})
# 添加链接库
target_link_libraries(demo MathFunctions) #把MathFunctions这个链接库加入到demo projection中。
```

math路径下的

```cmake
aux_source_directory(. DIR_LIB_SRCS)
# 生成链接库
add_library(MathFunctions ${DIR_LIB_SRCS}) #用当前路径下全部的c++文件生成一个名为MathFunctions的链接库
```



### 自定义编译选项

cmake 允许为项目增加编译选项，从而可以根据用户的环境和需求选择最合适的编译方案。
例如： 可以将MathFunctions 库设置为一个可选的库， 该选项为ON，就使用该库定义的数学函数来进行运算。

```cmake
# CMake 最低版本号要求
cmake_minimum_required (VERSION 2.8)
# 项目信息
project (Demo)
# 加入一个配置头文件，用于处理 CMake 对源码的设置
configure_file (
    "${PROJECT_SOURCE_DIR}/config.h.in"
    "${PROJECT_BINARY_DIR}/config.h"
    )
# 是否使用自己的 MathFunctions 库
option (USE_MYMATH
        "Use provided math implementation" ON)
# 是否加入 MathFunctions 库
if (USE_MYMATH)
    include_directories ("${PROJECT_SOURCE_DIR}/math")
    add_subdirectory (math)
    set (EXTRA_LIBS ${EXTRA_LIBS} MathFunctions)
endif (USE_MYMATH)
# 查找当前目录下的所有源文件
# 并将名称保存到 DIR_SRCS 变量
aux_source_directory(. DIR_SRCS)
# 指定生成目标
add_executable(Demo ${DIR_SRCS})
target_link_libraries (Demo ${EXTRA_LIBS})
```



- configure_file 命令用于加入一个配置头文件 config.h ，这个文件由 cmake 从 config.h.in 生成，通过这样的机制，将可以通过预定义一些参数和变量来控制代码的生成。
- option 命令添加了一个 USE_MYMATH 选项，并且默认值为 ON 。根据 USE_MYMATH 变量的值来决定是否使用我们自己编写的 MathFunctions 库。

修改main.cc文件

```cmake
#include "config.h"
#ifdef USE_MYMATH
    #include "math/MathFunctions.h"
#else
    #include <math.h>
#endif
 
int main(int argc, char *argv[])
{
    if (argc < 3){
        printf("Usage: %s base exponent \n", argv[0]);
        return 1;
    }
    double base = atof(argv[1]);
    int exponent = atoi(argv[2]);
 
#ifdef USE_MYMATH
    printf("Now we use our own Math library. \n");
    double result = power(base, exponent);
#else
    printf("Now we use the standard library. \n");
    double result = pow(base, exponent);
#endif
    printf("%g ^ %d is %g\n", base, exponent, result);
    return 0;
}
```



这个USE_MYMATH的来源是从 新加入的config.h中来的。
但是我们不直接写config.h文件 而是写config.h.in文件， 通过config.h.in文件，cmake就能创建出config.h

#### 编写config.h.in文件

```cmake
#cmakedefine USE_MYMATH
```

这样就能实现 通过配置 config.h.in文件来动态的调整工程中是否打入MathFunctions库了。





# Cmake实战之SeaNet工程分析



## 目录结构

![image-20210706171855791](https://i.loli.net/2021/07/06/JGW9Eb1HcFt43KO.png)



## 根目录cmakeLists分析

```cmake
cmake_minimum_required(VERSION 3.2) #指定最小版本

project(linphone-sdk VERSION 4.2 LANGUAGES NONE) #指定工程名称


set(LINPHONESDK_DIR "${CMAKE_CURRENT_LIST_DIR}")  #根据上面的记录  CMAEEK_CURRENT_LIST_DIR 指的是 Cmakelists.txt文件当前的完成目录
list(APPEND CMAKE_MODULE_PATH "${LINPHONESDK_DIR}/cmake") #给 CMAKE_MODULE_PATH追加

include(LinphoneSdkUtils)  # 虽然前面没讲到 include ,但是讲到了include_directories是包含路径  估计是先去处理了该路径下的cmakeLists.txt文件，这就是要包含LinphoneSdkUtils.cmake   找了下工程跟目录下 没有发现这个文件。结合 上面给 CMAKE_MODULE_PATH 追加了 路径的情况 去根路径下的/cmake路径下看看 果然有 <LinphoneSdkUtils.cmake>。


linphone_sdk_git_submodule_update() #这明显就不是默认有的内容 所以肯定是 LinphoneSdkUtils.cmake 里面的内容   详见 注释 1
linphone_sdk_compute_full_version(LINPHONESDK_VERSION)#也是在LinphoneSdkUtils中有实现， 效果就是把linphone的版本号 赋给 LINPHONESDK_VERSION 这个变量


set(LINPHONESDK_PREBUILD_DEPENDENCIES)#设置 LINPHONESDK_PREBUILD_DEPENDENCIES  的值为空
if(ENABLE_TUNNEL)#ENABLE_TUNNEL 这个哪来的？
	add_custom_target(tunnel-clone
		"${CMAKE_COMMAND}" "-DLINPHONESDK_DIR=${LINPHONESDK_DIR}" "-P" "${LINPHONESDK_DIR}/cmake/LinphoneSdkTunnelClone.cmake"
	)
	list(APPEND LINPHONESDK_PREBUILD_DEPENDENCIES tunnel-clone)
endif()


set(LINPHONESDK_PLATFORM "Android" CACHE STRING "Platform to build")# set cache ： 设置缓存条目
set_property(CACHE LINPHONESDK_PLATFORM PROPERTY STRINGS "Android" "Desktop" "IOS" "UWP") 


# TODO: This will be deletable once we get rid of cmake-builder
string(TOLOWER "${LINPHONESDK_PLATFORM}" _lowercase_platform)
include(cmake-builder/cmake/LinphoneOptions.cmake)
include(cmake-builder/configs/options-${_lowercase_platform}.cmake)
include(cmake-builder/options/common.cmake)
include(cmake-builder/options/bctoolbox.cmake)
#include(cmake-builder/options/bellesip.cmake)
include(cmake-builder/options/ms2.cmake)
include(cmake-builder/options/linphone.cmake)


# Give a feature summary
include(FeatureSummary)
feature_summary(FILENAME "${CMAKE_BINARY_DIR}/enabled_features.txt" WHAT ENABLED_FEATURES DESCRIPTION "Enabled features:")
feature_summary(WHAT ENABLED_FEATURES DESCRIPTION "Enabled features:")
feature_summary(FILENAME "${CMAKE_BINARY_DIR}/disabled_features.txt" WHAT ENABLED_FEATURES DESCRIPTION "Enabled features:")
feature_summary(WHAT DISABLED_FEATURES DESCRIPTION "Disabled features:")


include(LinphoneSdkPlatform${LINPHONESDK_PLATFORM})


if(ENABLE_GPL_THIRD_PARTIES)
	message(WARNING "
  ***************************************************************************
  ***************************************************************************
  ***** CAUTION, this Linphone SDK is built using third party GPL code  *****
  ***** Even if you acquired a proprietary license from Belledonne      *****
  ***** Communications, this SDK is GPL and GPL only.                   *****
  ***** To disable third party GPL code, set the                        *****
  ***** ENABLE_GPL_THIRD_PARTIES option to OFF                          *****
  ***************************************************************************
  ***************************************************************************")
else()
	message("
  ***************************************************************************
  ***************************************************************************
  ***** Linphone SDK without third party GPL software                   *****
  ***** If you acquired a proprietary license from Belledonne           *****
  ***** Communications, this SDK can be used to create                  *****
  ***** a proprietary Linphone-based application.                       *****
  ***************************************************************************
  ***************************************************************************
  ")
endif()

```



### 注释：

1. 在LinphoneSdkUtils.cmake里面 果然找到了linphone_sdk_git_submodule_update

   ![image-20210706174149100](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20210706174149100.png)





做屏幕共享时，相关cmakeList的修改有啥意义

加入library 就能 直接用include " " 来引用 add library的内容了
另外 它也能完成编译

![image-20210706183916518](https://i.loli.net/2021/07/06/aWvZCDU7oyKcqen.png)

