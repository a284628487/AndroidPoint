# Android gradle 实用技巧

### 动态赋值resValue
假如我们需要根据不同编译类型 (debug/release)或者不同渠道动态给 strings.xml 文件中的「url」赋值。只需要在「buildType」节点中根据不同的类型，或者「productFlavors」中根据不同的渠道加入一下代码：
```
buildTypes {
    release {
        // ...
        resValue("string", "name", "Hello");
    }
    debug {
        // ...
        resValue("string", "name", "World");
    }
}
```
### 自定义buildConfigField

默认情况下`BuildConfig`中有以下字段可以在`java`代码中直接调用
```
public final class BuildConfig {
  public static final boolean DEBUG = Boolean.parseBoolean("true");
  public static final String APPLICATION_ID = "com.xxx.xxx.xxx";
  public static final String BUILD_TYPE = "debug";
  public static final String FLAVOR = "";
  public static final int VERSION_CODE = 1;
  public static final String VERSION_NAME = "1.0";
}
```
如果我们希望根据不同的`buildType`或者不同的`flavor`动态加入一些新的`Field`我们只需要在对应的地方加入类似下面这样的代码:
```
buildTypes {
	debug {
		// ...
	    buildConfigField("boolean", "SHOW_TOAST", "true");
	}
}
```
