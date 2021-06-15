# WindowManagerService

Window 是应用的界面的最顶层的管理类。
WindowManagerService就是负责处理window管理的。

WindowManagerService 并不是直接管理window 而是通过WindowManager来进行管理。

Window的是一个抽象类， 其实现类是PhoneWIndow.

```
window 本质就是view的管理器。
并没有真正的存在window的实体。
```



WindowManger实际上是通过WindowManagerGlobal来进行管理。
最终view是通过WindowManagerGlobal.addView来把view添加上，显示出来的。



