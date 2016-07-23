# Android Studio NDK 开发

#### 1. 创建`java`层源文件
```
package com.cf;

/**
 * Created by ccfyyn on 16/5/14.
 */
public class Test {
    public static native void changeArr(int[] arr);

    public static native String getString();

    static {
        System.loadLibrary("test"); // test为3中的moduleName
    }
}
```

#### 2. 在`main`目录下创建`jni`目录，并创建`test.h``，`test.c`文件

在`test.h`中添加方法的定义
```
#include <jni.h>

#ifndef MY_APPLICATION_TEST_H
#define MY_APPLICATION_TEST_H

JNIEXPORT void JNICALL Java_com_cf_Test_changeArr(JNIEnv *, jobject thiz, jintArray arr);

JNIEXPORT jstring JNICALL Java_com_cf_Test_getString(JNIEnv *, jclass clazz);

#endif //MY_APPLICATION_TEST_H
```
上面定义了两个方法，分别是改变`int[]`数组的值，和获取一个字符串，接下来在`test.c`文件中对方法进行具体实现
```
#include "test.h"
#include <android/log.h>

JNIEXPORT void JNICALL Java_com_cf_Test_changeArr(JNIEnv *env, jobject thiz, jintArray arr) {
    int len = (*env)->GetArrayLength(env, arr);
    jint *a = (*env)->GetIntArrayElements(env, arr, JNI_FALSE);
    int i = 0;
    for (; i < len; i++) {
        *(a + 1 * i) += 11;
    }
    __android_log_print(ANDROID_LOG_ERROR, "tag", "%d - %d", a, *a);
//    return arr;
}

JNIEXPORT jstring JNICALL Java_com_cf_Test_getString(JNIEnv *env, jclass clazz) {
    return (*env)->NewStringUTF(env, "static native string");
}

```
#### 3.修改`module`的`gradle`编译文件
```
apply plugin: 'com.android.application'

android {
    compileSdkVersion 23
    buildToolsVersion "23.0.2"

    defaultConfig {
        // ...

        ndk {
            moduleName "test"
            abiFilters "armeabi-v7a", "x86_64"
            // abiFilters "armeabi", "armeabi-v7a", "arm64-v8a", "x86", "x86_64", "mips", "mips64"
            ldLibs "log", "jnigraphics" // 添加log打印输出
        }

    }

    // ...

    sourceSets { main { jni.srcDirs = ['src/main/jni', 'src/main/jni/'] } }
}

dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    // ...
}
```
编译成功之后，在`build->intermediates->ndk`目录下就能看到生成的`so`文件

默认情况下，`as`会自动将`so`文件打包到`apk`文件中，但是，如果`so`文件过多，为了减小`apk`文件的体积，就需要动态的加载`so`文件了

#### 动态加载
动态加载`so`库对`so`文件存放位置有特殊的要求，需要所在的目录是可执行?的目录，于是通常把外部的`so`文件拷贝到`app`的安装目录下

- 为了测试方便把`so`文件放在`assets`目录下，然后拷贝到安装目录下:
```
    File f = new File(getDir("libs", Context.MODE_PRIVATE).getAbsolutePath() + "/libtest.so");
    // f 的路径为/data/packagename/app_libs/libtest.so
    if (!f.exists()) { // 文件不存在->拷贝文件到指定目录下;
        try {
            InputStream fis = getAssets().open("libtest.so");
            FileOutputStream fos = new FileOutputStream(f);
            byte[] bts = new byte[fis.available()];
            fis.read(bts);
            fos.write(bts);
            fis.close();
            fos.close();
        } catch (IOException e) {
        }
    }
    // 调用
    String nativeString = Test.getString();
```
- 修改`Test`文件
```
    static {
        // System.loadLibrary("test");
        System.load("/data/packagename/app_libs/libtest.so");
    }
```

获取`cpu`型号，参考自`hello-jni`
```
JNIEXPORT jstring Java_cn_asndk_Test_getAbi( JNIEnv* env, jobject thiz )
{
	#if defined(__arm__)
	#if defined(__ARM_ARCH_7A__)
	    #if defined(__ARM_NEON__)
	      #if defined(__ARM_PCS_VFP)
	        #define ABI "armeabi-v7a/NEON (hard-float)"
	      #else
	        #define ABI "armeabi-v7a/NEON"
	      #endif
	    #else
	      #if defined(__ARM_PCS_VFP)
	        #define ABI "armeabi-v7a (hard-float)"
	      #else
	        #define ABI "armeabi-v7a"
	      #endif
	    #endif
	#else
	#define ABI "armeabi"
	#endif
	#elif defined(__i386__)
	    #define ABI "x86"
	#elif defined(__x86_64__)
	   #define ABI "x86_64"
	#elif defined(__mips64)  /* mips64el-* toolchain defines __mips__ too */
	   #define ABI "mips64"
	#elif defined(__mips__)
	   #define ABI "mips"
	#elif defined(__aarch64__)
	   #define ABI "arm64-v8a"
	#else
	   #define ABI "unknown"
	#endif
	    return (*env)->NewStringUTF(env, ABI);
}
```

[关于load和lodaLibrary的分析](https://segmentfault.com/a/1190000004062899)
