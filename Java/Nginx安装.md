### 准备

- 下载[openssl](https://www.openssl.org/source/old/1.0.2/)
- 下载[nginx](http://nginx.org/en/download.html)
- 下载[nginx-rtmp-module](https://github.com/arut/nginx-rtmp-module)

### 编译安装

在`nginx`目录下执行如下命令
```
./configure --add-module=/pathTo/nginx-rtmp-module --with-openssl=/pathTo/openssl --with-http_ssl_module
make install
```

### 修改conf支持rtmp

`nginx`默认只支持`http`，需要让`nginx`支持`rtmp`，找到`nginx.conf`文件，添加如下内容后重启即可；
`nginx.conf`在安装目录下，e:`/usr/local/nginx/conf/nginx.conf`。
```
rtmp {
    server {
            listen 1935;

        #点播配置
                application vod {
                    play /opt/media/nginxrtmp/flv;
                }

        #直播流配置
            application live {
                    live on;
            #为 rtmp 引擎设置最大连接数。默认为 off
            max_connections 1024;

                    # default recorder
                    record all;
                    record_path /var/rec;

                    recorder audio {
                         record audio;
                         record_suffix -%d-%b-%y-%T.flv;
                    } 

                    recorder chunked {
                        record all;
                         record_interval 15s;
                         record_path /var/rec/chunked;
                    }

            #on_publish http://localhost:8080/publish;  
            #on_play http://localhost:8080/play;  
            #on_record_done http://localhost:8080/record_done;

            #rtmp日志设置
             #access_log logs/rtmp_access.log new;
             #access_log logs/rtmp_access.log;
             #access_log off;

             }

        #HLS协议支持
        #application hls {  
            #live on;  
            #hls on;  
            #hls_path /tmp/app;  
            #hls_fragment 5s;  
        #} 

            application hls{

                    live on;
                    hls on;
                    hls_path /usr/local/nginx/html/app;
                    hls_fragment 1s;
            }
    }
}
```
[link](https://github.com/arut/nginx-rtmp-module#example-nginxconf)

### 配置nodejx反向代理

- 进入 `/usr/local/nginx/conf` 目录，在该目录下创建 `include` 文件下，新建 `nginx.node.conf ` 配置文件。

- 编辑 `nginx.node.conf` 文件
```
upstream nodejs {
    server 127.0.0.1:3000;
    keepalive 64;
}

server {
    listen 8081;
    server_name www.penguu.com penguu.com;
    access_log /usr/local/nginx/logs/node.log;
    location / {
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header Host  $http_host;
        proxy_set_header X-Nginx-Proxy true;
        proxy_set_header Connection "";
        proxy_pass      http://nodejs;
    }
}
```
- 进入 `/usr/local/nginx/conf`，打开 `nginx.conf`，在 http 里面添加 `include /usr/local/nginx/conf/include/*;`

- 重启 `nginx` , 输入 `./nginx -c /usr/local/nginx/conf/nginx.conf`

- 启动 `nodejx` 程序，分别用下面两个地址访问服务器

```
http://127.0.0.1:8081/
```
    ResponseHeaders
    ```
    Connection:keep-alive
    Date:Wed, 31 Aug 2016 06:27:22 GMT
    ETag:W/"cf-i1RI8kSxOkqNH6+yZ44AxA"
    Server:nginx/1.10.1
    X-Powered-By:Express
    ```

```
http://127.0.0.1:3000/
```
    ResponseHeaders
    ```
    Connection:keep-alive
    Date:Wed, 31 Aug 2016 06:27:45 GMT
    ETag:W/"cf-i1RI8kSxOkqNH6+yZ44AxA"
    X-Powered-By:Express
    ```

可以看到通过8081代理端口访问的时候，也能访问我们的node程序，并且响应的`Server`是`nginx/1.10.1`。


### 常用命令

```
nginx -s reload // 修改配置后重新加载生效
nginx -s reopen // 重新打开日志文件
nginx -t -c /path/to/nginx.conf // 测试nginx配置文件是否正确

nginx -s stop // 快速停止nginx
         quit // 完整有序的停止nginx

ps -ef | grep nginx

kill -QUIT 主进程号     // 从容停止Nginx
kill -TERM 主进程号     // 快速停止Nginx
pkill -9 nginx         // 强制停止Nginx

nginx -c /path/to/nginx.conf // 启动Nginx

kill -HUP 主进程号 // 平滑重启nginx
```
[link](http://www.cnblogs.com/apexchu/p/4119252.html)

### issues

q:
```
ld: symbol(s) not found for architecture x86_64
```
a:

```
在nginx/objs目录下，修改Makefile
找到: ./config --prefix=/pathTo/openssl no-shared 
把该段的 ./config 改成 ./Configure darwin64-x86_64-cc 
其他后面参数不变，保存后再次执行 make install
```
[link](http://www.bubuko.com/infodetail-1527902.html)

q:
```
open() "/usr/local/nginx/logs/nginx.pid" failed
```
a:
```
./nginx -c /usr/local/nginx/conf/nginx.conf
./nginx -s reload
```
[link](http://www.ithov.com/linux/130990.shtml)



