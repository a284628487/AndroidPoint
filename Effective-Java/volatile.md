## volatile关键字

volatile修饰符不会执行互斥访问，它可以保证任何一个线程在读取该变量的时候，都将看到变量被某个线程最新所写入的值。

考虑下面一段程序，它会一直执行，并不会在1s之后停止

```Java
	private static boolean flag;

	private static synchronized void stopLoop() {
		flag = true;
	}

	private static boolean isLoopNeedStop() {
		return flag;
	}

	private static void testLoopOrignal() throws InterruptedException {
		Thread t = new Thread() {
			@Override
			public void run() {
				int i = 0;
				while (!isLoopNeedStop()) {
					i++;
				}
			}
		};
		t.start();
		TimeUnit.SECONDS.sleep(1);
		flag = true; // 或者 stopLoop();
		System.out.println("flag = " + flag);
	}
```
> 外部线程对flag的修改，其它线程看不到。

下面两种方法将可避免这个问题。

### 使用synchronized，将isLoopNeedStop加上关键字synchronized

```Java
	private static synchronized boolean isLoopNeedStop() {
		return flag;
	}
```

### 使用volatile关键字修饰flag

```Java
	private static volatile boolean flag;

	private static boolean isLoopNeedStop() {
		return flag;
	}
```
