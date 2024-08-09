package org.example;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * MyList
 */
public class MyList<T> extends AbstractList<T> implements List<T> {

  private static final int DEFAULT_CAPACITY = 10;

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("[size=%d, capacity=%d, elements={", size, array.length));

    for (int i = 0; i < size; i++) {
      sb.append(array[i]);
      if (i != size - 1)
        sb.append(", ");
    }
    sb.append("}]");
    return sb.toString();
  }

  int size = 0;
  T[] array;

  public MyList() {
    this(DEFAULT_CAPACITY);
  }

  @SuppressWarnings("unchecked")
  public MyList(int capacity) {
    if (capacity <= 0)
      throw new IllegalArgumentException("capacity must be > 0");
    array = (T[]) new Object[capacity];

  }

  private MyList(T[] array) {
    this.array = array;
    size = array.length;

  }

  private void expandArray() {
    array = Arrays.copyOf(array, array.length / 2 * 3 + 1);
  }

  @Override
  public boolean add(T arg0) {
    add(size, arg0);
    return true;
  }

  @Override
  public void add(int index, T value) {
    if (index < 0 || index > size)
      throw new IndexOutOfBoundsException();

    if (size == array.length)
      expandArray();

    if (index != size)
      for (int i = size; i > index; i--)
        array[i] = array[i - 1];

    array[index] = value;
    size++;
    modCount++;
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    return addAll(0, c);
  }

  @Override
  public boolean addAll(int index, Collection<? extends T> c) {
    int new_size = c.size() + size;
    while (new_size > array.length)
      expandArray();

    for (int i = new_size; i > index + c.size(); i--)
      array[i] = array[i - c.size()];

    var iterator = c.iterator();

    for (int i = 0; i < c.size(); i++)
      array[i + index] = iterator.next();
    modCount++;

    return true;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void clear() {
    size = 0;
    modCount++;
    array = (T[]) new Object[DEFAULT_CAPACITY];
  }

  @Override
  public boolean contains(Object o) {
    return indexOf(o) > 0 ? true : false;
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    var iterator = c.iterator();
    while (iterator.hasNext())
      if (!contains(iterator.next()))
        return false;

    return true;
  }

  @Override
  public T get(int index) {
    if (index < 0 || index > size)
      throw new IndexOutOfBoundsException();
    return array[index];
  }

  @Override
  public int indexOf(Object o) {
    for (int i = 0; i < size; i++)
      if (array[i].equals(o))
        return i;
    return -1;
  }

  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  @Override
  public Iterator<T> iterator() {
    return new Itr();
  }

  private class Itr implements Iterator<T> {
    int cursor; // index of next element to return
    int lastRet = -1; // index of last element returned; -1 if no such
    int expectedModCount = modCount;

    Itr() {
    }

    public boolean hasNext() {
      return cursor != size;
    }

    @SuppressWarnings("unchecked")
    public T next() {
      checkForComodification();
      int i = cursor;
      if (i >= size)
        throw new NoSuchElementException();
      Object[] elementData = MyList.this.array;
      if (i >= elementData.length)
        throw new ConcurrentModificationException();
      cursor = i + 1;
      return (T) elementData[lastRet = i];
    }

    public void remove() {
      if (lastRet < 0)
        throw new IllegalStateException();
      checkForComodification();

      try {
        MyList.this.remove(lastRet);
        cursor = lastRet;
        lastRet = -1;
        expectedModCount = modCount;
      } catch (IndexOutOfBoundsException ex) {
        throw new ConcurrentModificationException();
      }
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
      Objects.requireNonNull(action);
      final int size = MyList.this.size;
      int i = cursor;
      if (i < size) {
        final Object[] es = MyList.this.array;
        if (i >= es.length)
          throw new ConcurrentModificationException();
        for (; i < size && modCount == expectedModCount; i++)
          action.accept(elementAt(es, i));
        // update once at end to reduce heap write traffic
        cursor = i;
        lastRet = i - 1;
        checkForComodification();
      }
    }

    final void checkForComodification() {
      if (modCount != expectedModCount)
        throw new ConcurrentModificationException();
    }
  }

  @SuppressWarnings("unchecked")
  static <E> E elementAt(Object[] es, int index) {
    return (E) es[index];
  }

  @Override
  public int lastIndexOf(Object o) {
    for (int i = size - 1; i > 0; i--)
      if (array[i].equals(o))
        return i;
    return -1;
  }

  @Override
  public ListIterator<T> listIterator() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
  }

  @Override
  public ListIterator<T> listIterator(int index) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
  }

  @Override
  public boolean remove(Object o) {
    int index = indexOf(o);
    if (index < 0)
      return false;
    remove(index);
    return true;
  }

  @Override
  public T remove(int index) {
    T val = get(index);

    for (int i = index; i < size; i++)
      array[i] = array[i + 1];
    size--;
    modCount++;
    return val;
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    boolean changed = false;
    var iter = c.iterator();
    while (iter.hasNext())
      changed |= remove(iter.next());
    return changed;
  }

  @Override
  public boolean retainAll(Collection<?> c) {

    int last_size = size;
    MyList<T> result = new MyList<>();

    for (int i = 0; i < size; i++)
      if (c.contains(array[i]))
        result.add(array[i]);

    array = result.array;
    size = result.size;

    if (last_size != size)
      modCount++;
    return last_size != size;
  }

  @Override
  public T set(int index, T val) {
    var old = get(index);
    if (!old.equals(val))
      modCount++;
    set(index, val);
    return old;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public List<T> subList(int fromIndex, int toIndex) {

    if (fromIndex > toIndex)
      throw new IllegalArgumentException("toIndex must be more that fromIndex");
    if (fromIndex < 0)
      throw new IndexOutOfBoundsException("fromIndex must be more 0");
    if (toIndex > size)
      throw new IndexOutOfBoundsException("toIndex must be less or equal list size");

    var trimmed_array = Arrays.copyOfRange(array, fromIndex, toIndex);
    return new MyList<>(trimmed_array);
  }

  @Override
  public Object[] toArray() {
    return Arrays.copyOf(array, size);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T[] toArray(T[] ar) {
    if (ar.length != size)
      throw new IllegalArgumentException("array must have same size with list");

    for (int i = 0; i < size; i++)
      ar[i] = (T) array[i];

    return ar;
  }

}
