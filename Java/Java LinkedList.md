# Java LinkedList 源码解读

`LinkedList` 是采用链表储存，整个列表的数据在一条链表上，链表上的每一个节点除了真实的数据之外，还存放有上一个节点和下一个节点的索引(如果有的话)，插入删除不用拷贝移动数据，效率高，缺点是查询时需要从头开始一个一个往后查。

### Node

在 `LinkedList` 中有这样一个类，`Node` 就是链表上的一个个节点；
```
    private static class Node<E> {
        E item;
        Node<E> next; // 下一个节点
        Node<E> prev; // 上一个节点

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }
```

- 成员变量

```
public class LinkedList<E>
    extends AbstractSequentialList<E>
    implements List<E>, Deque<E>, Cloneable, java.io.Serializable
{
    transient int size = 0;

    /**
     * Pointer to first node.
     * Invariant: (first == null && last == null) ||
     *            (first.prev == null && first.item != null)
     */
    transient Node<E> first;

    /**
     * Pointer to last node.
     * Invariant: (first == null && last == null) ||
     *            (last.next == null && last.item != null)
     */
    transient Node<E> last;
}
```

- 构造方法

```
    /**
     * Constructs an empty list.
     */
    public LinkedList() {
    }

    /**
     * 从一个集合拷贝数据来构造一个列表，元素被添加到列表中的顺序是集合的iterator返回的数据顺序
     */
    public LinkedList(Collection<? extends E> c) {
        this();
        // 添加collection里面的元素到链表，此时size为0；
        addAll(c);
    }
```

- addAll

```
    public boolean addAll(Collection<? extends E> c) {
        return addAll(size, c);
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        checkPositionIndex(index); // 确保index在正确的范围，即 [0, size] 的区间范围

        Object[] a = c.toArray();
        int numNew = a.length;
        if (numNew == 0)
            return false;

        Node<E> pred, succ;
        // succ 为当前index位置上的节点;
        // pred 为index位置上的节点的上一个节点;
        if (index == size) { // 要插入的位置和当前链表长度相同，实际上就只需要直接在链表后面append节点
            succ = null; // @1
            pred = last;
        } else {
            succ = node(index); // 获取指定index位置上的Node节点
            pred = succ.prev; // pred则为index位置上的Node节点的上一个Node节点
        }

        for (Object o : a) {
            // @SuppressWarnings("unchecked") E e = (E) o;
            Node<E> newNode = new Node<>(pred, e, null);
            if (pred == null)
                first = newNode;
            else
                pred.next = newNode;
            pred = newNode;
        }

        if (succ == null) { // index = size
            last = pred; // @1
        } else { // 
            pred.next = succ;
            succ.prev = pred;
        }

        size += numNew;
        modCount++;
        return true;
    }
```

上面 `@1` 第一种情况下，在循环插入节点之后，把最后一个节点赋值给`last`节点；

- 添加元素

```
    public boolean add(E e) {
        linkLast(e);
        return true;
    }

    void linkLast(E e) { // 添加到链表最后面
        final Node<E> l = last;
        final Node<E> newNode = new Node<>(l, e, null);
        last = newNode;
        if (l == null) // 如果最后一个节点为null，则当前添加的node节点还需要赋值给第一个节点
            first = newNode;
        else // 将之前的 last 节点 的下一个节点 赋值为新添加的这个节点
            l.next = newNode;
        size++;
        modCount++;
    }
```

- 添加元素到指定位置

```
    public void add(int index, E element) {
        checkPositionIndex(index);

        if (index == size)
            linkLast(element); // 添加到链表最后面
        else
            linkBefore(element, node(index));
    }

    void linkBefore(E e, Node<E> succ) {
        // assert succ != null;
        final Node<E> pred = succ.prev;
        final Node<E> newNode = new Node<>(pred, e, succ);
        succ.prev = newNode;
        if (pred == null) // 原本的 succ 为第一个节点
            first = newNode;
        else // 将原 succ 的 next 赋值为newNode
            pred.next = newNode;
        size++;
        modCount++;
    }

    public void addFirst(E e) {
        linkFirst(e);
    }

    public void addLast(E e) {
        linkLast(e);
    }

    private void linkFirst(E e) {
        final Node<E> f = first;
        final Node<E> newNode = new Node<>(null, e, f);
        first = newNode;
        if (f == null)
            last = newNode;
        else
            f.prev = newNode;
        size++;
        modCount++;
    }
```

- 移除元素

```
    public E remove(int index) {
        checkElementIndex(index);
        return unlink(node(index));
    }

    E unlink(Node<E> x) { // 
        // assert x != null;
        final E element = x.item;
        final Node<E> next = x.next;
        final Node<E> prev = x.prev;

        // 处理prev
        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        // 处理next
        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

        x.item = null;
        size--;
        modCount++;
        return element;
    }

    // 移除第一个元素
    public E removeFirst() {
        final Node<E> f = first;
        if (f == null)
            throw new NoSuchElementException();
        return unlinkFirst(f);
    }

    private E unlinkFirst(Node<E> f) {
        // assert f == first && f != null;
        final E element = f.item;
        final Node<E> next = f.next;
        f.item = null;
        f.next = null; // help GC
        first = next;
        if (next == null)
            last = null;
        else
            next.prev = null;
        size--;
        modCount++;
        return element;
    }

    // 移除最后一个元素
    public E removeLast() {
        final Node<E> l = last;
        if (l == null)
            throw new NoSuchElementException();
        return unlinkLast(l);
    }

    private E unlinkLast(Node<E> l) {
        // assert l == last && l != null;
        final E element = l.item;
        final Node<E> prev = l.prev;
        l.item = null;
        l.prev = null; // help GC
        last = prev;
        if (prev == null)
            first = null;
        else
            prev.next = null;
        size--;
        modCount++;
        return element;
    }
```

- 获取元素

```
    public E getFirst() {
        final Node<E> f = first;
        if (f == null)
            throw new NoSuchElementException();
        return f.item;
    }

    public E getLast() {
        final Node<E> l = last;
        if (l == null)
            throw new NoSuchElementException();
        return l.item;
    }

    // 获取指定位置止的元素
    public E get(int index) {
        checkElementIndex(index);
        return node(index).item;
    }

    // 根据索引获取指定位置的节点
    Node<E> node(int index) {
        // assert isElementIndex(index);

        if (index < (size >> 1)) { // 二分法
            Node<E> x = first;
            for (int i = 0; i < index; i++)
                x = x.next;
            return x;
        } else {
            Node<E> x = last;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
            return x;
        }
    }
```

- 获取元素在列表中的索引

```
    public int indexOf(Object o) {
        int index = 0;
        if (o == null) {
            for (Node<E> x = first; x != null; x = x.next) {
                if (x.item == null)
                    return index;
                index++;
            }
        } else {
            for (Node<E> x = first; x != null; x = x.next) {
                if (o.equals(x.item))
                    return index;
                index++;
            }
        }
        return -1;
    }

    public int lastIndexOf(Object o) {
        int index = size;
        if (o == null) {
            for (Node<E> x = last; x != null; x = x.prev) {
                index--;
                if (x.item == null)
                    return index;
            }
        } else {
            for (Node<E> x = last; x != null; x = x.prev) {
                index--;
                if (o.equals(x.item))
                    return index;
            }
        }
        return -1;
    }
```

