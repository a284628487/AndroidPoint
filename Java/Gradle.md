# Gradle Groovy

### task

任务使用`task`定义

- 定义一个task
	
有2种方法

```
task hello0 {
	println 'hello world 0'
}

task hello1 {
	doLast {
		println 'hello world 1'
	} // doFirst || doSecond
}

task hello2 << {
	println 'hello world 2'
}
```

- 调用常用函数

```
task upper << {
	String name = "Ccf"
	println 'Upper case: ' + name.toUpperCase()
}
```

- dependsOn

两种写法
```
task intro(dependsOn: hello) << {
	println 'I\'m intro, dependsOn hello'
}

// 未定义所依赖的任务情况下
task intro2(dependsOn: 'hello3') << {
	println 'I\'m intro, dependsOn hello3'
}

task hello3 << {
	println 'Hello3'
}
```

- 导入`Java`类

```
import java.lang.String as KString

task importJava << {
	println(new KString("JavaString"))
}
```

- 默认执行函数

```
defaultTasks 'hello', 'hello2'
```
在命令行下运行`gradle`命令，就会执行`hello`和`hello2`。

- finalizedBy 完成后执行下一个任务

```
task a << {
	println 'a'
}

task b << {
	println 'b'
}

a.finalizedBy b
```

### 函数

函数使用`def`定义

- def
```
def abc() {
	println 'abc'
}

abc()
```
直接运行`gradle`命令该函数就自动执行了;

- Collections
	1. 
	```
	def playList() {
	    def lst = ["a", 2, true] // 支持不同类型元素
	    println(lst)
	}
	```

	2. 
	```
	def playArray() {
	    def intArr = [1, 2, 3] as int[] // 显示声明
	    String[] strArr = ["a", "b"] // 另外一种方式

	    println(intArr)
	    println(strArr)
	}
	```

	3. 
	```
	def playMap() {
	    def map = [a: "a", b: "b"]
	    println(map)
	    def key = "name"
	    def map2 = [key: 'a'] // 未使用key
	    def map3 = [(key): 'a'] // 使用了key

	    println(map2) // {key=a}
	    println(map3) // {name=a}
	}
	```

- Closure

```

def defaultIt() {
    3.times {
        println it
    }
}

defaultIt()

println 'after defaultIt()'
def closureObj() {
    def obj = { a ->
        ++a
    }
    println obj.call(1)
}

closureObj()
```
执行结果
```
0
1
2
after defaultIt()
2
```

- var 

```
def varAndMethod() {
    def a = 1 // 不显式声明变量类型
    a = "abc" // 运行时改变类型

    println a // 无需；结束一行代码
    a = 4 // 最后一行作为返回值
}
def ret = varAndMethod() // 文件内运行方法
println ret
```

- quoted

```
def quoted() {
    def singleQ = 'hello, single quot'
    // 声明为java.lang.String
    def doubleQ = "hello, double quot ${singleQ}"
    // 如果有${},则为groovy.lang.GString，支持变量替换;否则为java.lang.String
    def tripleQ = '''hello,
triple quot'''
	// 允许多行，而不需要+号

    println singleQ
    println doubleQ
    println tripleQ
}
```

### class trait

```
trait Fly {
    void fly() {
        println("fly")
    }
}

trait Walk {
    void walk() {
        println("walk")
    }
}

class Duck implements Fly, Walk {
}

Duck duck = new Duck()
duck.fly() // fly
duck.walk() // walk
```

```
class People {
    String name
    int age
}

People p1 = new People();
People p2 = new People(name: "Luis", age: 29) // 通过类似 map 的方式赋值参数

def foo(String p1, int p2 = 1) {
    println(p1)
    println(p2)
}

foo("hello")

```

