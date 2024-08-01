package org.example;

import java.util.List;

public interface Sorter {
  public <T extends Comparable<? super T>> void sort(List<T> list);
}
