# 使用JMeter进行压力测试

JMeter是Apache组织开发的基于Java的压力测试工具。用于对软件做压力测试，它最初被设计用于Web应用测试但后来扩展到其他测试领域。 它可以用于测试静态和动态资源例如静态文件、Java 小服务程序、CGI 脚本、Java 对象、数据库， FTP 服务器， 等等。JMeter 可以用于对服务器、网络或对象模拟巨大的负载，来在不同压力类别下测试它们的强度和分析整体性能。另外，JMeter能够对应用程序做功能/回归测试，通过创建带有断言的脚本来验证你的程序返回了你期望的结果。为了最大限度的灵活性，JMeter允许使用正则表达式创建断言。
Apache jmeter 可以用于对静态的和动态的资源（文件，Servlet，Perl脚本，java 对象，数据库和查询，FTP服务器等等）的性能进行测试。它可以用于对服务器，网络 或对象模拟繁重的负载来测试它们的强度或分析不同压力类型下的整体性能。你可以使用它做性能的图形分析或在大并发负载测试你的服务器/脚本/对象。

### 下载JMeter

官方网站下载最新版本： http://jmeter.apache.org/download_jmeter.cgi ,需要java环境才能运行，这里就不一一介绍了.

### 运行JMeter

进入JMeter主目录的bin子目录下 点击jmeter.bat文件即可运行（启动有点慢哦，耐心等待）

### 模拟高并发进行压力测试

测试计划的排列顺序的顺序是有讲究的，见最后的顺序解析

- 建立线程组

要想模拟高并发，线程组是必不可少的，所以首先我们建立一个线程组

[创建一个线程组](http://blinkcoder-img.qiniudn.com/Fs24M1Mxo7IIuinOlsMWHZH6562b)
```
测试计划 | 添加 | Thread (Users) | 线程组
```

[配置线程组](http://blinkcoder-img.qiniudn.com/Ft-Y1VDtIRcKiQBU2FV7txoIcUm2)

参数说明，我这里是建立了300个线程，`Ramp-Up Period`参数是为了防止一下子创建300个线程导致程序假死而设计的，意思是在多少秒之内建立起所有的线程，这里设置为10。

- 建立一个循环控制器

设置一个循环，30次，也可以设置为永远。

[新建循环控制器](http://blinkcoder-img.qiniudn.com/FghyYhBGz4xTrNHA1cSjP9HM4csP)
```
线程组 | 添加 | 逻辑控制器 | 循环控制器
```

[循环控制器配置](http://blinkcoder-img.qiniudn.com/Fm6ntIJf_QRs2MQRxtjhgBnI_HyF)

- 设置http信息头管理器
（这一步可以省去），比如网站做了访问限制，只有浏览器才能进行访问，所以必须指定User-Agent去模拟浏览器进行请求.

[新建http信息头管理器](http://blinkcoder-img.qiniudn.com/Foyz6eV8WRpJCnLTMQBQire0CFS5)
```
循环控制器 | 添加 | 配置元件 | HTTP信息头管理器
```

[http信息头管理器配置](http://blinkcoder-img.qiniudn.com/FudpOEngisxsSkrfbxhQLKnsoPzh)

- 设置一个固定定时器

这一步的作用是让线程每多少毫秒进行下一次操作，这里设置500毫秒也就是每一个线程1秒进行2次操作

[新建固定定时器](http://blinkcoder-img.qiniudn.com/FjV9AiyLUZP9c19emgP_KmSew9xW)
```
循环控制器 | 添加 | 定时器 | 固定定时器
```

[固定定时器配置](http://blinkcoder-img.qiniudn.com/FgnljyRd6KzuAw5Vz_MCLwUFFbRy)

- 设置一个http请求

这里的参数都很好理解，需要注意的是 服务器名称或ip这里我们也可以填写域名 还有路径这里不需要加域名或者服务器ip.

[添加HTTP请求](http://blinkcoder-img.qiniudn.com/FpFMdQWtDzrSFJlBhJjfH9sPE5Lo)
```
循环控制器 | 添加 | Sampler | HTTP请求
```

[HTTP请求配置](http://blinkcoder-img.qiniudn.com/FhxCpiyycU2ZmHsB0RUWD7QBKaj9)

```
配置相应的服务器信息：
这里是让所有线程一直请求同一个链接，也可以让所有线程随机去请求我们指定的链接
我们在菜单 选项->函数对话框->选择__StringFromFile（注意:是两个下划线，不是一个下划线的那个）
然后在第一个参数填写我们的链接文件的全路径，链接的文件格式如下
```

```
/command
/linuxrumen
/program
/jianzhan
/command/cat.html
/command/chmod.html
/command/lsattr.html
```

然后点击函数对话框右下角的生成，将生成的url(比如:${__StringFromFile(/Users/ccfyyn/Desktop/test.txt,,,)})复制到我们开始填写路径的地方就可以了，这时线程会随机对文件中的路径进行访问HTTP请求

- 图形结果

[图形结果监视器](http://blinkcoder-img.qiniudn.com/FuTw-y4tqqd_SKOS75rV4HPlDZen)
```
线程组 | 添加 | 监听器 | 图形结果
```

[图形结果](http://blinkcoder-img.qiniudn.com/FiOX1w48k6DD8XPcSmoUxdwF0_i3)

- 聚合报告
```
添加方法如添加图形结果
```

[聚合报告](http://blinkcoder-img.qiniudn.com/Fh8QysLpEM5dW-PilY8d-l844MmP)

- 用表格查看结果
```
添加方法如添加图形结果
```

[用表格查看结果](http://blinkcoder-img.qiniudn.com/Fh8QysLpEM5dW-PilY8d-l844MmP)

- 查看结果数
```
添加方法如添加图形结果
```

[查看结果数](http://blinkcoder-img.qiniudn.com/FlZF5QeCUgFCufa7DdHqD6INsBvC)

- 测试计划的排列顺序

[测试计划的排列顺序](http://blinkcoder-img.qiniudn.com/FlZF5QeCUgFCufa7DdHqD6INsBvC)

这里的顺序是有讲究的，从结构上可以看出，也可以这么类比

```
创建了一个线程组
for（循环多少次）{
	设置请求头
	设置sleep时间
	进行http请求
}
```

### 结果的分析

- 聚合报告

```
Samples -- 本次场景中一共完成了多少个Transaction
Average -- 平均响应时间
Median -- 统计意义上面的响应时间的中值
90% Line -- 所有transaction中90%的transaction的响应时间都小于xx
Min -- 最小响应时间
Max -- 最大响应时间
PS: 以上时间的单位均为ms
Error -- 出错率
Troughput -- 吞吐量，单位：transaction/sec
KB/sec -- 以流量做衡量的吞吐量
```

- 以树状列表查看结果

我们可以看到很详细的每个transaction它所返回的结果，其中红色是指出错的transaction，绿色则为通过的。
如果你测试的场景会有很多的transaction完成，建议在这个Listener中仅记录出错的transaction就可以了。要做到这样，你只需要将Log/Display:中的Errors勾中就可以了。

[link](https://www.devh.net/yidongnan/blog/user-jmeter-to-pressure-test)

