# Retrofit2 examples

### RxAndroid + Retrofit2

- 添加`adapter-rxjava`依赖
```
compile 'com.squareup.retrofit2:adapter-rxjava:2.0.0-beta4'
```
- 定义`Api`

```
Observable<ResponseBody> rxAndroidRetrofit(@Body RequestBody body);
```
- 设置`CallAdapterFactory`
```
addCallAdapterFactory(RxJavaCallAdapterFactory.create())
```

### Get
```
@GET("login")
Observable<PResult<User>> userLogin(@Query("platform") String platform, @Query("name") String name, @Query("pwd") String pwd);
```

### Post
- 使用`Field`
```
@FormUrlEncoded
@POST("user/add")
Call<User> postAge(@Field("userName") String name, @Field("age") int age);
```
- 使用`FieldMap`
```
@FormUrlEncoded
@POST("user/add")
Call<User> postMap(@FieldMap HashMap<String, String> map);
```
- 使用`Body`
```
public interface TaskService {  
    @POST("/create")
    Call<User> createUser(@Body User user);
}
```
User.java
```
public class User {  
    private String name;
    private String action;
    
    public User() {}

    public User(String name, String action) {
        this.name = name;
        this.action = action;
    }
}
```

### 上传文件
- 定义接口，使用`PartMap`注解
```
@Multipart
@POST("file?__type=1")
Observable<PResult<String>> fileUploadUser(@PartMap Map<String, RequestBody> params);
```
- 将文件包装成`Map<String, RequestBody>`
```
public Map<String, RequestBody> packBody(File file) {
    Map<String, RequestBody> map = new HashMap<>();
    RequestBody fileBody = RequestBody.create(MediaType.parse("application/excel"), file);
    map.put("file\"; filename=\"" + file.getName() + "", fileBody);
    return map;
}
```
- 发送请求
```
HttpHelper.getApi().fileUploadUser(map);
```

### 动态配置URL

在`Retrofit2`中使用`@Url`即可实现`URL`的动态配置
```
// 定义一个 LeanCloudApis 接口，定义 leanCloudLogin 方法
public interface LeanCloudApis {
    @POST
    Call<LoginResultBean> leanCloudLogin(
            @Body HashMap<String, Object> map,
            @Url String url
    );
}
```
使用定义的接口
```
// 使用https://api.leancloud.cn/1.1/login替换默认的URL，通过自定义拦截器可以看到请求的URL已经变成了传入的url地址。
Call<LoginResultBean> response = leanCloudApis.leanCloudLogin(map, "https://api.leancloud.cn/1.1/login");
```

## 默认依赖 OkHttp

当在 `Gradle` 中加入 `compile 'com.squareup.retrofit2:retrofit:2.0.0-beta4'` 的依赖后，会默认下载`OkHttp3`和`Okio`。`OkHttp`库是一款非常优秀的网络请求库有取代`Apache HttpClient`网络库的趋势，依赖于`OkHttp`库使得`Retrofit`不用自己去实现一些网络请求。
`OkHttp`的背后是一个叫做`Okio`的库，提供的`IO`支持。这是众多`IO`库更好的选择而且极度高效。

### 自定义拦截器
通过自定义拦截器，可以处理在发送请求或接受回复时对数据进行处理和判断。

大致的流程如下：
```
AppClient Request –> Retrofit 2 –> Intercepter handle request –> excute()–> WebService handle Request and return Response –> Intercepter handle response –> Retrofit2–>AppClient get Response
```
- MyInterceptors.java
```
import okhttp3.*;import java.io.IOException;

public class MyInterceptors implements Interceptor {
	
	@Override
	public Response intercept(Chain chain) throws IOException { // 封装headers
        Request request = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json") //添加请求头信息
                .build();
        Headers headers = request.headers();
        System.out.println("Content-Type is : " + headers.get("Content-Type"));
        String requestUrl = request.url().toString(); //获取请求url地址
        String methodStr = request.method(); //获取请求方式
        RequestBody body = request.body(); //获取请求body
        String bodyStr = (body==null?"":body.toString());
        //打印Request数据
        System.out.println("Request Url is :" + requestUrl + "\nMethod is : " + methodStr + "\nRequest Body is :" + bodyStr + "\n");

        Response response = chain.proceed(request); 

        if (response != null) {
            System.out.println("Response is not null");
        } else {
            System.out.println("Respong is null");
        }
        return response;
    }
}
```
- 使用拦截器
```
OkHttpClient client = new OkHttpClient().newBuilder()
                                        .addInterceptor(new MyInterceptors())
                                        .build();
Retrofit retrofit = new Retrofit.Builder()
    .baseUrl(API_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .client(client) //设置OkHttp Client
    .build();
```
对于拦截器的作用在于处理返回信息，对请求数据加密或者存储`cookies`等。在这种情况下，上面定义的拦截器基本上可以满足一般应用的需求。简单的应用开发只需要封装`access token`便于服务器验证即可。


### 请求 - 响应 流程
```
AppClient Request —> Retrofit 2 –> Conveters –> Retrofit 2 —> OkHttp3 –> WebService handle Request 
WebSevice Response –> OkHttp3 –> Retrofit2 –> Conveters –> Retrofit2 –> AppClient handle Response
```

[rel](http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0915/3460.html)
