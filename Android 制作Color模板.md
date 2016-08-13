# Android 制作Color模板

`Color`值来源: `微信`, `QQ`

1. 生成适用于 `AndroidStudio` 的 `mycolors.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>

    <color name="_qq_list_title">#000000</color>
    <color name="_qq_list_subtitle">#777777</color>
    <color name="_qq_list_time">#777777</color>
    <color name="_qq_list_divider">#DEDEDE</color>
    <color name="_qq_list_item_bg_normal">#ffffff</color>
    <color name="_qq_list_item_bg_pressed">#e5e6e7</color>
    <color name="_qq_page_bg">#f7f7f7</color>
    <color name="_qq_theme_blue">#3ec1f2</color>
    <color name="_qq_notification_red">#f41200</color>
    <color name="_qq_tab_gray">#818181</color>
    <color name="_qq_login_btn_bg_normal">#2ebaf1</color>
    <color name="_qq_login_btn_bg_pressed">#29a8d8</color>

    <color name="_wx_list_title">#000000</color>
    <color name="_wx_list_subtitle">#909090</color>
    <color name="_wx_list_time">#848484</color>
    <color name="_wx_list_divider">#dcdcdc</color>
    <color name="_wx_list_item_bg_normal">#ffffff</color>
    <color name="_wx_list_item_bg_pressed">#d9d9d9</color>
    <color name="_wx_page_bg">#eeeef3</color>
    <color name="_wx_notification_red">#f13838</color>
    <color name="_wx_tab_gray">#929292</color>

</resources>
```
2. 找到`AndroidStudio`安装目录下的`plugins | android | lib | templates | gradle-projects | NewAndroidModule`目录,
将 `mycolors.xml` 拷贝到 `root | res | values`目录下;

3. 编辑 `NewAndroidModule` 目录下的 `recipe.xml.ftl` 文件

定位到:
```xml
<#if !(isLibraryProject??) || !isLibraryProject>
    <instantiate from="root/res/values/styles.xml.ftl"
                   to="${escapeXmlAttribute(resOut)}/values/styles.xml" />
```
新增一个配置项`copy`,修改之后如下:
```xml
<#if !(isLibraryProject??) || !isLibraryProject>
    <instantiate from="root/res/values/styles.xml.ftl"
                   to="${escapeXmlAttribute(resOut)}/values/styles.xml" />
    <copy from="root/res/values/mycolors.xml"
          to="${escapeXmlAttribute(resOut)}/values/mycolors.xml" />
```

4. 新建`Project`或者新建非`library`的`Module`的时候,将会自动在`Module`的`res | values`目录下生成`mycolors.xml`文件

