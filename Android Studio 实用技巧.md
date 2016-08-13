# Android Studio 快捷键

## part 1

####  ⌘ : command
####  ⌃ : ctrl
####  ⇧ : shift
####  ⌥ : alt/option, 
####  ⏎ : enter/return 

- ⌘ + o : 在当前`project`中搜索`class`: 再按一次`⌘ + o`(会发现，右上角的选项勾选了)，搜索结果可包含非`project`中的`class`，如`external libraries`中的`Android.jar`里的`class`，还可以在搜索文本后跟 `:lineNumber` 定位到某一行
- ⌃ + h : 显示类层级 
- ⌘ + n : `getter`、`setter`、`toString`、`constructor`
- ⌘ + ⇧ + o : 在当前`project`中搜索`file`(包含上面的class结果)，再按一次`⌘ + ⇧ + o`，可以在搜索文本后跟 `:lineNumber` 从而定位到某行，如 输入文本`：mainacti:20`这时就会定位到`MainActivity`的第20行
- ⌘ + ⌥ + o : 在当前`project`中搜索属性(成员和静态，不论是否私有)，再按一次，搜索结果可包含非`project`中的`class`
- ⌥ + ⏎ : 快速导包，修复错误
- ⌘ + ⌥ + v : 自动声明变量 
- ⌘ + F12 : 查看当前类成员
- ⌘ + i : 显示/取消息匿名类
- ⌃ + o : 选择能重写(override)或实现(implement)的方法
- ⌥ + F7 : 查找方法/类在哪被使用
- F4 : 定位到属性、方法、类等它们的声明
- ⌘ + ⌥B / ⌘ + ⌥ + click光标在调用接口方法的方法名中 : 查看接口方法实现类
- ⌘ + ⌥ + T : Surround With: if、while、try-catch、synchronized 等等
- ⌃ + T : 重构面板
- ⌘ + ⌥ + M : Extract Method 抽取成方法
- ⌘ ⌥ F : Extract Field 抽取为成员属性
- ⌘ + ⌥ + P : Extract Parameter 将内部变量抽取成方法的参数
- ^ + ⌥ + O : 去除无效引用
- ⌘ + ⌥ + L : 格式化代码
- ⌘ + E : 最近打开过的文件 
- ⌘ + ⌥ + ⇧ + F7 : find usages 查找使用情况
- ⌘ + F，⌘ + R : 查找与替换，在查找后，使用 `⌘ + G` 定位到下一个text(find in path与replace in path：⌘⇧F，⌘⇧R)
- ⌘ + ⇧ + U : 大小写转换
- ⇧ + F6 : 重命名
- ⌘ + ⇧ + ⏎ : 光标换行  
- ⌘ + ⌥ + ⏎ : 在当前行上添加一行，光标定位到行首
- ⌘ + D : 复制整行
- ⌘ + delete : 删除整行
- ⇧ + ⌘ + ↑/↓ : 内容行上下移动，不会出方法体；或光标在方法体外且在方法行上时，移动整个方法
- ⌥ + ⇧ + ↑/↓ : 上下移动光标所在行
- ⌘ + X : 剪切，若无选中文本时会剪切整行
- F1 : 查看doc/文档注释
- F2 : 定位到未使用的声明

- ⌘ + j : 快捷代码片段
```
psf => public static final 
ifn => if (a == null)
inn=> if (a != null)
fori=> for(int i = 0; i < .....)
I(大写i)=> for(Object o : ) 
```
- ⌃ + ⇧ + F12 : 关闭或者恢复其他窗口
- ⌘ + P : 查看方法需要的参数

## part 2

#### Find Actions
```
cmd + shift + a 
```
对于没有设置快捷键或者忘记快捷键的菜单或者动作（Action），可能通过输入其名字快速调用。神技！！！
例如想要编译，只需要输入"release"，则列表框中就会出现"assembleRelease"选项，选择就可以进行编译。

![http://ww3.sinaimg.cn/large/005BLbusjw1exb6q1pjkrg30dw06hdha.gif](http://ww3.sinaimg.cn/large/005BLbusjw1exb6q1pjkrg30dw06hdha.gif)

#### 条件断点

通过右键断点，可以对一个断点加入条件。只有当满足条件时，才会进入到断点中。调试神技，只对自己关心的情况进行调试，不浪费时间。

![http://ww4.sinaimg.cn/large/005BLbusjw1exb6shuwe1g30ea07ctap.gif](http://ww4.sinaimg.cn/large/005BLbusjw1exb6shuwe1g30ea07ctap.gif)
 
#### 进入调试模式

点击Attach Debugger(即绿色小虫旁边那个)可以快速进入调试而不需要重新部署和启动app。
可以选择为此功能设置一个快捷键或者通过前面提到的Find Actions(cmd+shift+a)输入"attach"进行调用。

![http://ww1.sinaimg.cn/large/005BLbusjw1exb6svknjxg30ai06k0v0.gif](http://ww1.sinaimg.cn/large/005BLbusjw1exb6svknjxg30ai06k0v0.gif)

#### 快速查看变量的值

按住Alt点击想要查看的变量或者语句。如果想查看更多，则可以按Alt+f8调出Evaluate Expression窗口来自行输入自定义的语句。

![http://ww1.sinaimg.cn/large/005BLbusjw1exb6t8orpzg30lr06ymzo.gif](http://ww1.sinaimg.cn/large/005BLbusjw1exb6t8orpzg30lr06ymzo.gif)

#### 分析堆栈信息

Find Actions(cmd+shift+a)输入"analyze stacktrace"即可查看堆栈信息。

![http://ww3.sinaimg.cn/large/005BLbusjw1exb6tlmxwng30is0awna1.gif](http://ww3.sinaimg.cn/large/005BLbusjw1exb6tlmxwng30is0awna1.gif) 

#### 分析某个值的来源

Find Actions(cmd+shift+a)输入"Analyze Data Flow to Here"，可以查看某个变量某个参数其值是如何一路赋值过来的。
对于分析代码非常有用。
![http://ww3.sinaimg.cn/large/005BLbusjw1exb6u0g2hsg30ij09odn3.gif](http://ww3.sinaimg.cn/large/005BLbusjw1exb6u0g2hsg30ij09odn3.gif)

#### 多行编辑

强大的神技之一，用过vim的vim-multiple-cursors或者Sublime Text的多行编辑都不会忘记那种快感！ 也许不是平时用得最多的技能，但是却是关键时刻提高效率的工具。
快捷键：Alt + J

![http://ww3.sinaimg.cn/large/005BLbusjw1exb6udtw0pg306c04274u.gif](http://ww3.sinaimg.cn/large/005BLbusjw1exb6udtw0pg306c04274u.gif)

#### 列编辑

在vim中叫作块编辑，同样神技！使用方法：按住Alt加鼠标左键拉框即可
PS：发现Ubuntu下不可用，代替方法为按Alt+Shift+Insert之后拖框选择。
但是经过这么操作之后，神技就大打折扣了。估计是与Ubuntu的快捷键冲突了。

![http://ww3.sinaimg.cn/large/005BLbusjw1exb6uqjczkg30dt03t0ux.gif](http://ww3.sinaimg.cn/large/005BLbusjw1exb6uqjczkg30dt03t0ux.gif)

#### Enter和Tab在代码提示时的区别

![http://ww3.sinaimg.cn/large/005BLbusjw1exb6v5xe65g307003fdgo.gif](http://ww3.sinaimg.cn/large/005BLbusjw1exb6v5xe65g307003fdgo.gif)

#### VCS Operations Popup

`Alt+``(是1左边的那个键) 
此快捷键会显示一个版本管理常用的一个命令，可以通过命令前面的数字或者模糊匹配来快速选择命令。
极大的提高了工作效率，快速提交代码、暂存代码、切分支等操作操作如鱼得水。

![http://ww2.sinaimg.cn/large/005BLbusjw1exb6qszjlyg30ci095aaj.gif](http://ww2.sinaimg.cn/large/005BLbusjw1exb6qszjlyg30ci095aaj.gif)

[rel](http://zlv.me/posts/2015/07/13/14_android-studio-tips/)

## part 3

#### 显示行号
```
Preferences | Editor | General | Appearance | Show line numbers
```

#### 驼峰选择

Android 开发中，我们通常会使用驼峰命名法对变量进行命名，但是当我们通过 Ctrl + Left / Right 键改变字符选择区域的时候 Android Studio 默认不支持‘驼峰’单词的选择。
```
Preferences | Editor | General | Smart Keys | Use "CameHumps" words
```

#### 自动导包
```
Preferences | Editor | General | Auto Import
```
选中
```
Optimize imports on the fly
Add unambiguous imports on the fly
```

#### Log color

```
Preferences | Editor | Color & Fonts | Android Logcat
```
点击 `on Save As…` 按钮创建一个新的配色`Scheme`
```
Assert:	#AA66CC
Debug:	#33B5E5
Error:	#FF4444
Info:	#99CC00
Verbose:	#FFFFFF
Warning:	#FFBB33
```

#### 代码配色

```
Preferences | Editor | Color & Fonts | Java
```
点击 `on Save As…` 按钮创建一个新的配色`Scheme`，展开下方的 Variables 选择 Local variable，设置右侧的 Foreground 颜色

#### 工程模板

`Android Studio`创建`Module`时并没有将 Android 开发中常用的文件目录全部生成，比如默认只生成了一个`drawable`文件夹，常用的`drawable-hdpi`等文件夹需要我们自己创建。

- 方法1
```
>> 进入 Android Studio 安装目录
>> 依次进入 plugins | android | lib | templates | gradle-projects | NewAndroidModule | root | res
>> 在res文件夹下创建 drawable-hdpi 等文件夹(可选：从对应的 mipmap文件夹中拷贝 iclauncher.png到创建的 drawable文件夹中)
>> 回到 NewAndroidModule 目录，用编辑器打 recipe.xml.ftl文件
```
找到如下的配置
```
<#if copyIcons && !isLibraryProject>
    <copy from="root/res/mipmap-hdpi"
            to="${escapeXmlAttribute(resOut)}/mipmap-hdpi" />
    <copy from="root/res/mipmap-mdpi"
            to="${escapeXmlAttribute(resOut)}/mipmap-mdpi" />
    <copy from="root/res/mipmap-xhdpi"
            to="${escapeXmlAttribute(resOut)}/mipmap-xhdpi" />
    <copy from="root/res/mipmap-xxhdpi"
            to="${escapeXmlAttribute(resOut)}/mipmap-xxhdpi" />
    <copy from="root/res/mipmap-xxxhdpi"
            to="${escapeXmlAttribute(resOut)}/mipmap-xxxhdpi" />
</#if>
```
在 `<#if... </#if>` 中加入新的项
```
<copy from="root/res/drawable-xhdpi"
        to="${escapeXmlAttribute(resOut)}/drawable-xhdpi" />
```
- 方法2
```
>> 进入 Android Studio 安装目录
>> 依次进入 plugins | android | lib | templates | gradle-projects | NewAndroidModule
>> 用编辑器打开 recipe.xml.ftl文件
```
找到
```
<mkdir at="${escapeXmlAttribute(resOut)}/drawable" />
```
在后面加入
```
<mkdir at="${escapeXmlAttribute(resOut)}/drawable-xxhdpi" />
```
两种方法的区别是: 第一种方式可以在文件夹中加入相应的图片，第二种方式只能创建目录，不能包含默认图片。

当然，通过类似的方式我们还可以在创建 Module 的时候做很多事情，比如：
```
	在 colors.xml 文件中生成常用颜色
	在 build.gradle 文件中生成自定义配置
	在 .gitignore 文件中生成自定义忽略配置
```

#### 活动模板

`Android Studio`中默认提供了很多非常方便的活动模板(Live Templates)，例如，我们输入 sout 后按 enter 键， Android Studio 会自动帮我们写入 System.out.println()；其实 sout 就是 AS 自带的一个活动模板。
```
Preferences | Editor | Live Templates 
```
在上面的路径下右侧列表中就可以看到`sout`

由此可以看出，活动模板就是我们常用代码的一个缩写。开发中有很多代码都会重复出现，因此自定义合适的活动模板能很大程度上避免我们很多重复的体力劳动。

下面是一个配置例子，当输入`mhd`就自动生成如下的代码；
```
private static class MyHandler extends Handler {
    private WeakReference<Activity> activityWeakReference;

    private MyHandler(Activity activity) {
        this.activityWeakReference = new WeakReference<Activity>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        Activity aty = activityWeakReference.get();
        if (null != aty) {
            // TODO
        }
    }
}
```

```
>> 选择 Editor | Code Style | Live Templates
>> 点击最右侧的加号并选择 Template Group
>> 在弹出的对话框中输入一个活动模板分组的名称，如 custom
>> 在左侧选中上一步中创建的 custom 分组，点击右边的加号
>> 选择 Live Template ，在 Abbreviation 中对输入 psh
>> 在 Description 中输入这个活动模板的描述
>> 在 Template text 中输入代码
```

```
private static class MyHandler extends Handler {
    private WeakReference<$className$> activityReference;

    private MyHandler($className$ activity) {
        this.activityReference = new WeakReference<$className$>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        $className$ aty = activityReference.get();
        if (null != aty) {
            // TODO
        }
    }
}
```

```
>> 点击下方的 Define 按钮，选中 java 表示这个模板用于java代码
>> 点击右侧的 Edit variables
>> 选择 Expression 下拉框中的 className 并勾选 Skip if…
```
这个操作的作用是，AS会自动将我们在上一步中用’$’符包裹的`className`自动替换为当前类不含包名的类名。

#### 活动模板延伸
- Android
```
fbc : ($cast$) findViewById(R.id.$resId$);
foreach : for ($i$ : $data$) {
    $cursor$
}
gone : $VIEW$.setVisibility(android.view.View.GONE);
visible : $VIEW$.setVisibility(View.VISIBLE);
key : private static final String KEY_$value$ = "$value$";
rgS : $resources$.getString(R.string.$stringId$)
Sfmt : String.format("$string$", $params$);
Toast : Toast.makeText($className$.this, "$text$", Toast.LENGTH_SHORT).show();
ViewConstructors : Adds generic view constructors
```
- AndroidXML
```
lw : android:layout_width=""
lh : android:layout_height=""

lww : android:layout_width="wrap_content"
lhw : android:layout_height="wrap_content"

lwm : android:layout_width="match_parent"
lhm : android:layout_height="match_parent"

toolsNs : adds tools namespace to Android xml file
appNs : xmlns:app="http://schemas.android.com/apk/res-auto"
```
