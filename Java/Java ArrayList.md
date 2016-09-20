# Java ArrayList 源码解读

`ArrayList` 采用的是数组形式来保存对象的，这种方式将对象放在连续的位置中，优点是索引查询数据快，缺点就是插入删除时效率低；

### 成员变量

```
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 8683452581122892189L;

    /**
     * List默认的容量:10
     */
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * 静态的空数组，用于对空列表进行快捷赋值
     */
    private static final Object[] EMPTY_ELEMENTDATA = {};

    /**
     * Shared empty array instance used for default sized empty instances. We
     * distinguish this from EMPTY_ELEMENTDATA to know how much to inflate when
     * first element is added.
     */
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

    /**
     * 存放真实数据的数组，列表的容量是数组的长度，当添加了一个元素之后，任何一个空的列表都会被扩展成DEFAULT_CAPACITY大小的列表；
     */
    transient Object[] elementData; // non-private to simplify nested class access

    /**
     * 列表的真实大小
     */
    private int size;
}
```

### 构造方法

```
    /**
     * 构造一个指定容量的数组
     */
    public ArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: "+
                                               initialCapacity);
        }
    }

    /**
     * 默认构造方法，将elementData赋值为DEFAULTCAPACITY_EMPTY_ELEMENTDATA
     * 在添加元素的时候，会扩展数组的容量为10
     */
    public ArrayList() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }

    /**
     * 从一个集合拷贝数据来构造一个列表，元素被添加到列表中的顺序是集合的iterator返回的数据顺序
     */
    public ArrayList(Collection<? extends E> c) {
        elementData = c.toArray();
        if ((size = elementData.length) != 0) {
            // c.toArray might (incorrectly) not return Object[] (see 6260652)
            if (elementData.getClass() != Object[].class)
                elementData = Arrays.copyOf(elementData, size, Object[].class);
        } else {
            // replace with empty array.
            this.elementData = EMPTY_ELEMENTDATA;
        }
    }
```

### 添加元素

```
    public boolean add(E e) {
        ensureCapacityInternal(size + 1); // 确保数组的容量至少为 当前的size + 1，以容纳元素；
        elementData[size++] = e; // 赋值
        return true;
    }
```

```
	// 确保数组容量
    private void ensureCapacityInternal(int minCapacity) {
    	// 如果数组是空数组的话，则将数组的容量设置为10;
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
        }

        ensureExplicitCapacity(minCapacity);
    }

    private void ensureExplicitCapacity(int minCapacity) {
        modCount++;

        // overflow-conscious code
        // 如果所需要的数组容量大于数组的真实长度，则将数组长度自增0.5倍
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }

	// 增加数组的容量
    private void grow(int minCapacity) {
        // overflow-conscious code
        // 数组现长度
        int oldCapacity = elementData.length;
        // 数组扩容后的长度
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        // 确保计算的扩容长度至少大于所需要扩展到的容量大小
        // 为什么会有这一步？因为如果默认空数组的话，newCapacity为0，而传入的minCapacity为10；
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        // ...
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        // 拷贝数据并指定长度
        elementData = Arrays.copyOf(elementData, newCapacity);
    }
```

### 在指定位置添加元素

```
    public void add(int index, E element) {
    	// 确保添加的位置正确
        rangeCheckForAdd(index);
        // 同上
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        System.arraycopy(elementData, index, elementData, index + 1,
                         size - index);
        elementData[index] = element;
        size++;
    }
```

### 移除元素-指定位置

```
    public E remove(int index) {
        rangeCheck(index); // 确保是正确的索引

        modCount++;
        E oldValue = elementData(index); // 获取指定位置元素
        // 需要向前移的元素个数
        int numMoved = size - index - 1;
        if (numMoved > 0) {
        	// Object src,  int  srcPos, Object dest, int destPos, int length
        	// 从elementData的index+1位置开始拷贝，拷贝numMoved个元素到elementData中，从index位置开始存储
            System.arraycopy(elementData, index+1, elementData, index,
                             numMoved);
	    }
        elementData[--size] = null; // clear to let GC do its work

        return oldValue;
    }
```

### 移除元素-指定元素

```
    public boolean remove(Object o) {
        if (o == null) {
            for (int index = 0; index < size; index++)
                if (elementData[index] == null) {
                    fastRemove(index);
                    return true;
                }
        } else {
            for (int index = 0; index < size; index++)
                if (o.equals(elementData[index])) {
                    fastRemove(index); // 快速移除元素，和移除指定位置元素基本相同；
                    return true;
                }
        }
        return false;
    }
```

```
    private void fastRemove(int index) {
        modCount++;
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index+1, elementData, index,
                             numMoved);
        elementData[--size] = null; // clear to let GC do its work
    }
```

### 清空列表

```
    public void clear() {
        modCount++;

        // clear to let GC do its work
        for (int i = 0; i < size; i++)
            elementData[i] = null;

        size = 0;
    }
```

### 批量添加

```
    public boolean addAll(Collection<? extends E> c) {
        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacityInternal(size + numNew); // Increments modCount
        System.arraycopy(a, 0, elementData, size, numNew);
        size += numNew;
        return numNew != 0;
    }
```

### trimToSize

```
    public void trimToSize() {
        modCount++;
        if (size < elementData.length) {
            elementData = (size == 0)
              ? EMPTY_ELEMENTDATA
              : Arrays.copyOf(elementData, size);
        }
    }
```
将 `elementData` 缩减到真实大小。

