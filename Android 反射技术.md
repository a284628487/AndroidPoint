# Android中的反射技术

### 动态语言

一般认为在程序运行时，允许改变程序结构或变量类型，这种语言称为动态语言。从这个观点看，Perl，Python，Ruby是动态语言，C++，Java，C#不是动态语言。尽管这样，JAVA有着一个非常突出的动态相关机制：反射（Reflection）。运用反射我们可以于运行时加载、探知、使用编译期间完全未知的classes。换句话说，Java程序可以加载在运行时才得知名称的class，获悉其完整构造方法，并生成其对象实体、或对其属性设值、或唤起其成员方法。


### 反射

要让Java程序能够运行，就得让Java类被Java虚拟机加载。Java类如果不被Java虚拟机加载就不能正常运行。正常情况下，我们运行的所有的程序在编译期时候就已经把那个类被加载了。 Java的反射机制是在编译时并不确定是哪个类被加载了，而是在程序运行的时候才加载。使用的是在编译期并不知道的类。这样的编译特点就是java反射。

### 反射的本质

反射就是把Java类中的各种存在给解析成相应的Java类。要正确使用Java反射机制就得使用Class（C大写） 这个类。它是Java反射机制的起源。当一个类被加载以后，Java虚拟机就会自动产生一个 Class对象。通过这个 Class对象 我们就能获得加载到虚拟机当中这个Class对象 对应的方法、成员以及构造方法的声明和定义等 信息。

### ClassLoader与DexClassLoader

JAVA的动态加载的机制就是通过 ClassLoader 来实现的，ClassLoader 也是实现反射的基石。ClassLoader 是JAVA提供的一个类，顾名思义，它就是用来加载Class文件到JVM，以供程序使用的。ClassLoader加载文件到JVM，但是Android是基于DVM的，用ClassLoader 加载文件进DVM肯定是不行的。于是Android提供了另外一套加载机制，分别为 `dalvik.system.DexClassLoader` 和 `dalvik.system.PathClassLoader`，区别在于 PathClassLoader 不能直接从 zip 包中得到 dex，因此只支持直接操作 dex 文件或者已经安装过的 apk（因为安装过的 apk 在 cache 中存在缓存的 dex 文件）。而 DexClassLoader 可以加载外部的 apk、jar 或 dex文件，并且会在指定的 outpath 路径存放其 dex 文件。

### ClassLoader

Test.java
```
package com.ly;

public class Test {
    private String name;

    public Test(String name) {
        this.name = name;
    }

    public void sayHello() {
        System.out.println("Hello, I'm " + name);
    }
}
```

```
Class clazz = ClassLoader.getSystemClassLoader().loadClass("com.ly.Test");
System.out.println(clazz); // class com.ly.Test
Constructor constructor = clazz.getConstructor(String.class);
Object obj = constructor.newInstance("ccflying");
Method m = clazz.getMethod("sayHello", null);
m.invoke(obj, null); // Hello, I'm ccflying
```

### DexClassLoader在Android中的应用

- 准备两个apk，一个Response，一个Request

- Response.apk

Response.apk里面包含这样一个类，Response.java
```
package com.test.rej;

public class Response {
    public String name;

    public Response(String name) {
        this.name = name;
    }

    public void sayHello(){
        System.out.println("Hello, I'm " + name);
    }
}
```
为了快速找到该`Response.apk`，给其入口`Activity`定义一个特殊的`action`
```
<activity android:name=".MainActivity">
    <intent-filter>
        <action android:name="com.test.rej" />
        <action android:name="android.intent.action.MAIN" />

        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```
- Request.apk

发起调用请求
```
public void request() {
    Intent i = new Intent("com.test.rej");
    PackageManager pm = getPackageManager();
    List<ResolveInfo> resolveInfoList = pm.queryIntentActivities(i, 0);
    ResolveInfo resolveInfo = resolveInfoList.get(0);
    ActivityInfo activityInfo = resolveInfo.activityInfo;
    String packageName = activityInfo.packageName;
    // 获取目标类所在的apk或者jar存放的路径
    String dexPath = activityInfo.applicationInfo.sourceDir;
    // 获取调用者的路径
    String dexOutputDir = getApplicationInfo().dataDir;
    // 获取目标类所使用的C/C++库存放路径
    String nativeLibraryDir = activityInfo.applicationInfo.nativeLibraryDir;
    ClassLoader classLoader = getClassLoader();

    System.out.println(dexPath);
    System.out.println(dexOutputDir);
    System.out.println(nativeLibraryDir);

    DexClassLoader dcl = new DexClassLoader(dexPath, dexOutputDir, nativeLibraryDir, classLoader);
    try {
        Class clazz = dcl.loadClass(packageName + ".Response");
        Constructor constructor = clazz.getConstructor(String.class);
        Object o = constructor.newInstance("ccflying");
        Method method = clazz.getMethod("sayHello", null);
        method.invoke(o, null);
    } catch (Exception e) {
        Log.e(TAG, "req: " + e);
    }
}
```
在该调用方法中首先利用 Intent 查询到目标 activityInfo，然后利用查询到的 activityInfo 得到目标 Apk的包名，目标Apk所在的apk或者jar存放的路径 dexPath，目标Apk所使用的C/C++库存放路径 nativeLibraryDir。然后利用这些参数实例化 DexClassLoader 加载器。之后反射调用目标类中的方法。

输出结果
```
I/System.out: /data/app/com.test.rej-1.apk
I/System.out: /data/data/com.test.req
I/System.out: /data/data/com.test.rej/lib
I/System.out: Hello, I'm ccflying
```

上面使用的目标Apk是已经安装过的，因此采用Intent查询来拿到 dexPath 和 nativeLibraryDir，如果是未安装过的jar包或Apk，则直接传入该jar包或Apk的文件存放路径和C/C++库存放路径；

将上面的`dexPath`改为
```
dexPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/rej.apk";
nativeLibraryDir = dexOutputDir + "/lib";
```

输出结果
```
I/System.out: /mnt/sdcard/rej.apk
I/System.out: /data/data/com.test.req
I/System.out: /data/data/com.test.req/lib
I/System.out: Hello, I'm ccflying
```

### DexClassLoader使用场景

上面是是使用的已经安装过的Apk，如果采用未安装过的jar包或者Apk，则实例化 DexClassLoader 的时候把相应路径改为需要加载的jar包或者Apk路径亦可拿到结果。这就使得 DexClassLoader 可以应用在HotFix（热修复），动态加载框架等等 一些基于插件化的架构中。

[link](http://mp.weixin.qq.com/s?__biz=MzA5MzI3NjE2MA==&mid=2650236432&idx=1&sn=1da63ff74cb6082ffe3df5f344f9f5f1&scene=0#wechat_redirect)

### Note

在`Android 5.0`之后，弃用了'Dalvik'，所以，上述代码在`Android 5.0`及以上，不能得到预期的效果；
