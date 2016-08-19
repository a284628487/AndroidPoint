# Android DecorView

- DecorView为整个Window界面的最顶层View。

- DecorView只有一个子元素为LinearLayout。代表整个Window界面，包含通知栏，标题栏，内容显示栏三块区域。

- LinearLayout里有两个FrameLayout子元素。

	- 第一个`FrameLayout`为标题栏显示界面。
	- 第二个`FrameLayout`为内容栏显示界面。就是setContentView()方法载入的布局界面，加入其中。

使用`SDK`中`tools`文件夹下`hierarchyviewer`查看`ViewTree`，可以查看`View`的层级关系；


