# Java 多线程

## volatile

当多个线程操作同一个变量时，为了保证变量修改对于其他线程的可见性，必须使用同步，`volatile`对于可见性的实现是个不错的选择，但是我们代码中的`i--`也有可能因为并发造成一定的问题，毕竟`i--`不是原子操作，正常最好使用同步块或者`AtomicLong.decrementAndGet()`实现`--`操作。

## Semaphore

`Semaphore`中管理着一组虚拟的许可，许可的初始数量可通过构造函数来指定 `new Semaphore(1);`，执行操作时可以首先获得许可`semaphore.acquire();`，并在使用后释放许可`semaphore.release();`。
如果没有许可，那么`acquire()`方法将会一直阻塞直到有许可（或者直到被终端或者操作超时）。

作用：

可以用来控制同时访问某个特定资源的操作数量，或者某个操作的数量。

## CyclicBarrier

一个让一组线程同时阻塞到一个位置的同步辅助类。在包含固定线程且线程间必须相互等待的场景中非常有用。
`cyclic`的意思是`CyclicBarrier`当等待的线程全部释放之后，可以重复使用。`CyclicBarrier`类似一个闸门，指定数目的线程都必须到达这个闸门，闸门才会打开。
`CyclicBarrier`把所有的线程都阻塞在一个阀门位置，然后等到等待的线程数到达预设的值，就打开这个阀门。注意是阻塞线程，不是阻塞操作，在同一个线程多次调用`await`是没什么效果的。
```
final CyclicBarrier cb = new CyclicBarrier(4, new Runnable() {
    @Override
    public void run() {
    	System.out.println("cyclicBarrier over");
    }
});
System.out.println(System.currentTimeMillis());
for (int i = 0; i < 4; i++) {
    new Thread() {
        @Override
        public void run() {
            try {
                Thread.sleep(1500);
                System.out.println(Thread.currentThread().getName() + ":" + System.currentTimeMillis());
                cb.await();
            } catch (Exception e) {
            }
        }
    }.start();
}
```

## Timer、TimerTask

如果存在多个任务，且任务时间过长，超过了两个任务的间隔时间，会发生一些缺陷
`Timer`执行周期任务时依赖系统时间，如果当前系统时间发生变化会出现一些执行上的变化，`ScheduledExecutorService`基于时间的延迟，不会由于系统时间的改变发生执行变化。

```
final long start = System.currentTimeMillis();
TimerTask task1 = new TimerTask() {
	@Override
	public void run() {
		try {
			System.out.println("execute task1");
			Thread.sleep(500);
			System.out.println("task1 over " + (System.currentTimeMillis() - start));
		} catch (InterruptedException e) {
		}
	}
};
TimerTask task2 = new TimerTask() {
	@Override
	public void run() {
		try {
			System.out.println("execute task2");
			Thread.sleep(2000);
			System.out.println("task2 over " + (System.currentTimeMillis() - start));
		} catch (InterruptedException e) {
		}
	}
};
Timer timer = new Timer();
timer.schedule(task1, 1000);
timer.schedule(task2, 1000);
```
执行结果
```
execute task1
task1 over 1507
execute task2
task2 over 3512
```

## CountDowmLatch

`Latch`是闭锁的意思，是一种同步的工具类。类似于一扇门：在闭锁到达结束状态之前，这扇门一直是关闭着的，不允许任何线程通过，当到达结束状态时，这扇门会打开并允许所有的线程通过。且当门打开了，就永远保持打开状态。

作用：

可以用来确保某些活动直到其他活动都完成后才继续执行。

使用场景：

- 例如我们上例中所有人都到达饭店然后吃饭；
- 某个操作需要的资源初始化完毕
- 某个服务依赖的线程全部开启等等...

`CountDowmLatch`是一种灵活的闭锁实现，包含一个计数器，该计算器初始化为一个正数，表示需要等待事件的数量。`countDown()`方法递减计数器，表示有一个事件发生，`await`方法等待计数器到达0之后，表示所有需要等待的事情都已经完成，然后执行之后的代码。

```
	private static CountDownLatch latch = new CountDownLatch(2);
	
    private static void test1() {
        execute1();
        execute2();
        try {
            latch.await();
            System.out.println("latch over");
        } catch (InterruptedException e) {
        }
    }

    private static void execute1() {
        new Thread() {
            @Override
            public void run() {
                try {
                	System.out.println("execute 1");
                    Thread.sleep(500);
                    latch.countDown();
                } catch (InterruptedException e) {
                }
            }
        }.start();
    }

    private static void execute2() {
        new Thread() {
            @Override
            public void run() {
                try {
                	System.out.println("execute 2");
                    Thread.sleep(1500);
                    latch.countDown();
                } catch (InterruptedException e) {
                }
            }
        }.start();
    }

```

## ExecutorService # invokeAll()
`ExecutorService`的`invokeAll`方法也能批量执行任务，并批量返回结果，但是有个很致命的缺点，必须等待所有的任务执行完成后统一返回，内存持有的时间长；

```
ExecutorService es = Executors.newFixedThreadPool(4);
List<Callable<String>> list = new ArrayList<>();
for (int i = 0; i < 10; i++) {
	Callable<String> c = new Callable<String>() {
		@Override
		public String call() throws Exception {
			Random rd = new Random();
			int i1 = rd.nextInt(2000);
			Thread.sleep(i1);
            System.out.println('call');
			return Thread.currentThread().getName() + ":" + i1;
		}
	};
	list.add(c);
}
try {
	System.out.println("start-" + System.currentTimeMillis());
	List<Future<String>> results = es.invokeAll(list);
	for (Future<String> future : results) {
		System.out.println(future.get());
	}
	System.out.println("end-" + System.currentTimeMillis());
} catch (Exception e) {
}
```
执行结果：
```
start-1470742610644
call
 .
 .
 .
call
pool-1-thread-1:1487
 .
 .
 .
pool-1-thread-4:870
end-1470742614209
```

## CompletionService

`CompletionService`将`Executor`（线程池）和`BlockingQueue`（阻塞队列）结合在一起，同时使用`Callable`作为任务的基本单元，整个过程就是生产者不断把`Callable`任务放入阻塞队列，`Executor`作为消费者不断把任务取出来执行，并返回结果；使用`take()`方法获取已经执行完成的结果

优势：

- 阻塞队列防止了内存中排队等待的任务过多，造成内存溢出
- 使用CompletionService哪个任务先执行完成就返回，而不是按顺序返回，这样可以极大的提升效率；

```
BlockingDeque<Future<String>> queue = new LinkedBlockingDeque<>();
ExecutorService es = Executors.newFixedThreadPool(4);
CompletionService<String> cs = new ExecutorCompletionService<String>(es, queue);
for (int i = 0; i < 10; i++) {
    Callable<String> c = new Callable<String>() {
        @Override
        public String call() throws Exception {
            Random rd = new Random();
            int i1 = rd.nextInt(2000);
            Thread.sleep(i1);
            System.out.println('call');
            return Thread.currentThread().getName() + ":" + i1;
        }
    };
    cs.submit(c);
}
System.out.println("start-" + System.currentTimeMillis());
for (int i = 0; i < 10; i++) {
    try {
        // 谁最先执行完成，直接返回
        Future<String> future = cs.take();
        System.out.println(future.get());
    } catch (Exception e) {
    }
}
System.out.println("end-" + System.currentTimeMillis());
```
和`ExecutorService # invokeAll()`不同的是，`CompletionService`是开始执行之后，一个一个的执行，谁先执行完成先返回；

执行结果：
```
start-1470743099513
call
pool-1-thread-3:937
call
pool-1-thread-2:1114
 .
 .
 .
end-1470743103571
```

[线程池](https://imuhao.github.io/2016/08/19/Thread-Executors/)

