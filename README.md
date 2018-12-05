# AndroidFrame

基于目前一些主流技术,整理的Android基础框架

如要了解详细的功能实现,请查看源代码!
* 源代码 : <a href="https://github.com/AcmenXD/AndroidFrame">AcmenXD/AndroidFrame</a>

### 依赖
---
- AndroidStudio
```
	allprojects {
            repositories {
                ...
                maven { url 'https://jitpack.io' }
            }
	}
```
```
	 compile 'com.github.AcmenXD:AndroidFrame:2.1'
```
### 混淆
---
```
     # 继承类混淆配置
     -keep interface com.acmenxd.frame.utils.proguard.** { *; }
     -keep class * implements com.acmenxd.frame.utils.proguard.IKeepClass
     -keepnames class * implements com.acmenxd.frame.utils.proguard.IKeepClassName
     -keepclassmembers class * implements com.acmenxd.frame.utils.proguard.IKeepFieldName {
         <fields>;
     }
     -keepclassmembers class * implements com.acmenxd.frame.utils.proguard.IKeepPublicFieldName {
         public <fields>;
     }
     -keepclassmembers class * implements com.acmenxd.frame.utils.proguard.IKeepMethodName {
         <methods>;
     }
     -keepclassmembers class * implements com.acmenxd.frame.utils.proguard.IKeepPublicMethodName {
         public <methods>;
     }

     # 依赖库混淆配置
     #rxjava & rxandroid
     -dontwarn sun.misc.**
     -keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
         long producerIndex;
         long consumerIndex;
     }
     -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
         rx.internal.util.atomic.LinkedQueueNode producerNode;
     }
     -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
         rx.internal.util.atomic.LinkedQueueNode consumerNode;
     }
     #okhttp3
     -dontwarn okio.**
     -dontwarn okhttp3.**
     -dontwarn com.squareup.okhttp3.**
     #retrofit2
     -dontwarn retrofit2.**
     -keep class retrofit2.** { *; }
     -keepattributes Signature
     -keepattributes Exceptions
```
### 功能
---
- 框架对<a href="https://github.com/AcmenXD">com.github.AcmenXD</a>:<a href="https://github.com/AcmenXD/Toaster">Toaster</a> | <a href="https://github.com/AcmenXD/SpTool">SpTool</a> | <a href="https://github.com/AcmenXD/Retrofit">Retrofit</a> | <a href="https://github.com/AcmenXD/Logger">Logger</a> | <a href="https://github.com/AcmenXD/Marketer">Marketer</a> | <a href="https://github.com/AcmenXD/RecyclerView">RecyclerView</a>做好配置支持
- 框架已集成并添加<a href="https://github.com/ReactiveX/RxJava">RxJava</a> | <a href="https://github.com/ReactiveX/RxAndroid">RxAndroid</a>支持
- 特别说明 -> 框架支持库请移步对应的github查看使用方法及源码

### 框架解析
---
**basis**
```java
-> impl包               : 框架层所需的接口基类
-> mvp包                : 实现MVP的接口基类
-> FrameApplication     : 框架层Application, 项目需继承此类做相关拓展!
-> FrameActivity        : 框架层Activity,实现Subscription | Presenter支持,内容 | 加载 | 错误视图,网络状态监控,Net支持,以及销毁等
-> FrameFragment        : 框架层Fragment,实现Subscription | Presenter支持,内容 | 加载 | 错误视图,网络状态监控,Net支持,以及销毁等
-> FrameService         : 框架层Service,实现Subscription | Presenter支持,网络状态监控,Net支持,以及销毁等
-> FramePresenter       : 框架层Presenter,实现Subscription支持,网络状态监控,Net支持,以及销毁等
-> FrameModel           : 框架层Model,实现Subscription支持,网络状态监控,Net支持,以及销毁等
-> FrameConfig    : 基础配置信息,项目配置必须继承此类,从而拓展其他配置!  例如:AppFrameConfig/OtherConfit(每个项目都有一份单独的配置清单)
-> ActivityStackManager : Activity堆栈管理器,提供exit | restartApp支持
```
---
**utils**
```java
-> code     : 编码加解密相关类
-> net      : 网络状态监控实现,以及网络状态工具类等
-> proguard : 混淆基类,各个混淆配置可直接继承对应接口,实现混淆配置
-> ...      : 各种工具类,具体功能请查看源码
```
---
**logger** / **retrofit** / **sptool** / **toaster** / **marketer** / **recyclerview**
```java
-> 功能组件,详情请移步对应的github查看使用方法及源码
```
---
有问题请与作者联系AcmenXD@163.com ^_^!
---
**gitHub** : https://github.com/AcmenXD   如对您有帮助,欢迎点Star支持,谢谢~

**技术博客** : http://blog.csdn.net/wxd_beijing

# END