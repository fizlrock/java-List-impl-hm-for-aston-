package org.example;

import java.util.Arrays;

public class MyMergeSorter<T extends Comparable<T>> {

  private T[] source;
  private T[] buffer;

  private MyMergeSorter(T[] array) {
    source = array;
    buffer = array.clone();
    sort(0, source.length);
  }

  private void sort() {
    sort(0, source.length);
  }

  private void sort(int left, int right) {
    if (right - left == 2) {
      if (source[left].compareTo(source[right - 1]) > 0) {
        var buf = source[right - 1];
        source[right - 1] = source[left];
        source[left] = buf;
      }
    } else if (right - left > 2) {
      int middle = (left + right) / 2; // тут может быть переполнение TODO
      sort(left, middle);
      sort(middle, right);
      merge(left, right);
    }
  }

  private void merge(int left, int right) {
    int middle = (right + left) / 2;
    int i = left, j = middle, k = left;

    while (i < middle && j < right) {
      boolean llessr = source[i].compareTo(source[j]) < 0;
      buffer[k++] = llessr ? source[i++] : source[j++];
    }

    while (i < middle)
      buffer[k++] = source[i++];

    while (j < right) {

      buffer[k++] = source[j++];
    }
    for (int p = left; p < right; p++)
      source[p] = buffer[p];
  }

  public static <T extends Comparable<T>> void sort(T[] array) {
    new MyMergeSorter<>(array).sort();
  }

}
