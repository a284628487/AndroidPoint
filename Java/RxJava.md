# RxJava

### RxJava 的观察者模式

RxJava 有四个基本概念：Observable (可观察者，即被观察者)、 Observer (观察者)、 subscribe (订阅)、事件。Observable 和 Observer 通过 subscribe() 方法实现订阅关系，而 Observable 可以在需要的时候发出事件来通知 Observer。

### Observable(被观察者)

	```
	Observable.create / just / from
	```

### Observer/Subscriber(观察者/订阅者)

	Observer
	```
	public interface Observer<T> {

	    void onCompleted();

	    void onError(Throwable e);

	    void onNext(T t);
	}
	```

	Subscriber
	```
	public abstract class Subscriber<T> implements Observer<T>, Subscription {
	}
	```

	```
	Observable.subscribe(Observer / Subscriber / Action1)
	```

### Operators

	- map 转换对象
	- flatMap 平铺对象
	- filter 过滤
	- distinct 去重
	- take 从开始取出固定个数
	- doOnNext 输出元素之前的额外操作
	- toList 打包对象为集合

### 线程调度器

	- Schedulers.immediate() 默认线程
	- Schedulers.newThread() 每次都创建新线程
	- Schedulers.io() 包含线程池机制，线程个数无限，可复用空闲线程
	- Schedulers.computation() CPU密集计算线程，线程池线程数和CPU数一致，学用于大量运算场景
	- AndroidSchedulers.mainThread() Android

- subscribeOn 可执行多次，切换操作符的线程

- observeOn 只需要执行一次，指定订阅者执行的线程

[更多参考](http://gank.io/post/560e15be2dca930e00da1083)

