# adb 命令

#### 查看设备
```
adb devices
```
查看当前连接的设备, 连接到计算机的android设备或者模拟器将会列出显示

#### 安装软件 卸载软件 
```
adb install [options] <apk文件路径>
adb uninstall [options] <PACKAGE>
```
`install`，`uninstall`还可以指定其它可选参数
```
adb install test.apk
adb install -l test.apk // forward lock application
adb install -r test.apk // replace existing application
adb install -t test.apk // allow test packages
adb install -s test.apk // install application on sdcard
adb install -d test.apk // allow version code downgrade
adb install -p test.apk // partial application install
//
adb uninstall -k com.test.app // Keep the data and cache directories around after package removal.
```

#### 登录设备shell
```
adb shell
adb shell <command命令>
```
这个命令将登录设备的shell
后面加<command命令>将是直接运行设备命令, 相当于执行远程命令

#### 从电脑上发送文件到设备
```
adb push <local> <remote>
adb push d:\test.apk /sdcard // Copies d:\test.apk to /sdcard directory.
```

#### 从设备上导出文件到电脑
```
adb pull <remote> <local>
```
```
adb pull /sdcard/demo.mp4 // download /sdcard/demo.mp4 to <android-sdk-path>/platform-tools directory.
adb pull /sdcard/demo.mp4 e:\ // download /sdcard/demo.mp4 to drive E.
```

#### 取得设备root权限
```
adb remount 
```

#### 打印输入logcat
打印输出命令一般使用比较少，通常我们一般都是直接连上开发软件如ADT，在开发软件里面就可以直接查看输出信息；
下面说一种特殊情况：
　　当调试那些使用了USB外设和主机特性的应用时，你很有可能把你的USB硬件连接到你的Android设备上，这将阻止你通过USB建立adb到Android设备的连接。但是我们可以通过网络访问adb。
通过网络连接adb：
- 通过USB将Android设备连接到电脑。
- 从SDK 的 platform-tools 目录，在命令行输入adb tcpip 5555
- 输入：adb connect <设备的IP地址>:5555；连接成功后将USB连接线和电脑断开，然后输入adb logcat 即可在命令行窗口和ADT窗口中查看到logcat信息；
*********************************************

如果想要断开无线调试，输入adb usb即可断开，切换到usb有线调试;

#### 关闭adb
```
adb kill-server
```

#### 查看已经安装软件
```
adb shell pm list packages [options] <FILTER> // 可以指定filter进行筛选
```
示例：
```
adb shell pm list packages
adb shell pm list packages -f // See their associated file.
adb shell pm list packages -d // Filter to only show disabled packages.
adb shell pm list packages -e // Filter to only show enabled packages.
adb shell pm list packages -s // Filter to only show system packages.
adb shell pm list packages -3 // Filter to only show third party packages.
adb shell pm list packages -i // See the installer for the packages.
adb shell pm list packages -u // Also include uninstalled packages.
adb shell pm list packages --user <USER_ID> // The user space to query.
```

#### 查看应用的apk文件存放路径
```
adb shell pm path <PACKAGE> // package:/data/app/com.tencent.mm-1.apk
```

#### 清空缓存文件
```
adb shell pm clear <PACKAGE>
```

#### 查看文件
```
ls [options] <directory>
```

```
adb shell // 1
ls // 2
ls -a // do not hide entries starting with
ls -i // print index number of each file
ls -s // print size of each file, in blocks
ls -n // list numeric UIDs and GIDs
ls -R // list subdirectories recursively
```

#### 删除文件
```
rm [options] <files or directory>
```

```
adb shell // 1
rm /sdcard/test.txt // 2
rm -f /sdcard/test.txt // force remove without prompt
rm -r /sdcard/tmp // remove the contents of directories recursively
rm -d /sdcard/tmp // remove directory, even if it is a non-empty directory
// rm -d equal rmdir command
rm -i /sdcard/test.txt // prompt before any removal
```

#### 创建文件夹
```
mkdir [options] <directory name>
//
mkdir /sdcard/tmp
mkdir -m 777 /sdcard/tmp // set permission mode
mkdir -p /sdcard/tmp/sub1/sub2 // create parent directories as needed
```

#### 创建文件
```
touch [options] <file>
//
adb shell
touch /sdcard/tmp/test.txt
```

#### 查看当前路径
```
pwd
```

#### 文件复制
```
cp [options] <source> <dest>
//
adb shell
cp /sdcard/test.txt /sdcard/demo.txt
```

#### 文件移动
```
mv [options] <source> <dest>
//
adb shell
mv /sdcard/tmp /system/tmp // move
mv /sdcard/tmp /sdcard/test // rename
```

#### 查看网络
```
adb shell
netstat
```

#### ping
```
adb shell
ping www.baidu.com
ping www.baidu.com -c 4
```
#### netcfg
```
netcfg [<interface> {dhcp|up|down}]
//
adb shell
netcfg
```

#### ip
```
adb shell
ip -f inet addr show wlan0 show WiFi IP Address
```

#### logcat
```
adb logcat [options] [filter-specs]
//
adb logcat *:V lowest priority, filter to only show Verbose level
adb logcat *:D filter to only show Debug level
adb logcat *:I filter to only show Info level
adb logcat *:W filter to only show Warning level
adb logcat *:E filter to only show Error level
adb logcat *:F filter to only show Fatal level
adb logcat *:S Silent, highest priority, on which nothing is ever printed
```

#### 内存查看dumpsys 
// adb shell dumpsys [options]
```
adb shell dumpsys
```

```
adb shell dumpsys meminfo // 查看内存信息
adb shell dumpsys battery // 查看电池信息
adb shell dumpsys batterystats // collects battery data from your device
```

#### 截屏
```
adb shell screencap <filename>
//
adb shell screencap /sdcard/screen.png
```

#### 录屏(4.4+)
```
adb shell screenrecord [options] <filename>
adb shell screenrecord /sdcard/demo.mp4
```
使用 `Ctrl-C`停止录制，3分钟后录制会自动断开，或者录制时长到了时间限制；
```
adb shell screenrecord --size <WIDTHxHEIGHT>
```
Sets the video size: 1280x720. The default value is the device's native display resolution (if supported), 1280x720 if not. For best results, use a size supported by your device's Advanced Video Coding (AVC) encoder.

```
adb shell screenrecord --bit-rate <RATE>
```
Sets the video bit rate for the video, in megabits per second. The default value is 4Mbps. You can increase the bit rate to improve video quality, but doing so results in larger movie files. The following example sets the recording bit rate to 5Mbps: adb shell screenrecord --bit-rate 5000000 /sdcard/demo.mp4

```
adb shell screenrecord --time-limit <TIME>
```
Sets the maximum recording time, in seconds. The default and maximum value is 180 (3 minutes).

```
adb shell screenrecord --rotate
```
Rotates the output 90 degrees. This feature is experimental.

```
adb shell screenrecord --verbose
```
Displays log information on the command-line screen. If you do not set this option, the utility does not display any information while running.

#### adb root
```
adb root // restarts the adbd daemon with root permissions
```

#### 查看进程状态
```
adb shell
ps -p
```

#### 查看设备信息
```
adb shell
//
getprop
getprop ro.build.version.sdk
getprop ro.chipname
getprop | grep adb
```

#### sqlite
adb shell 获取root权限
sqlite3 xx.db 打开xx.db数据库
.table 查询数据库里面的表
pragma table_info(table_name); 查看表的数据结构
.mode line 切换显示模式
每个SQLite数据库中都有一个隐藏的sqlite_master表,记载了当前数据库中所有表的创建语句;
可以通过select * from sqlite_master where name='table_name'; 来对指定的表进行查询;

#### 启动应用
```
adb shell 
// am start -n ｛包(package)名｝/｛包名｝.{活动(activity)名称}
am start -W com.lc.paper/com.lc.paper.ui.LoadingActivity // -W 还可以是 -n
// am startservice -n ｛包(package)名｝/｛包名｝.{服务(service)名称}
am startservice -n com.android.traffic/com.android.traffic.maniservice
// am broadcast -a <广播动作>
am broadcast -a android.net.conn.CONNECTIVITY_CHANGE
```

[adb shell](http://adbshell.com/)