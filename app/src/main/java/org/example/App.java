/*
 * This source file was generated by the Gradle 'init' task
 */

package org.example;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class App {
  public String getGreeting() {
    return "Hello World!";
  }

  public static void main(String[] args) {

    IntWrapper[] ints = new IntWrapper[5];
    ints[0] = new IntWrapper(4);
    ints[1] = new IntWrapper(9);
    ints[2] = new IntWrapper(4);
    ints[3] = new IntWrapper(3);
    ints[4] = new IntWrapper(2);

    System.out.println(Arrays.toString(ints));
    MyMergeSorter.sort(ints);
    System.out.println(Arrays.toString(ints));

  }

  static class IntWrapper implements Comparable<IntWrapper> {

    public final int value;

    public IntWrapper(int value) {
      this.value = value;
    }

    @Override
    public int compareTo(IntWrapper o) {
      return value - o.value;
    }

    @Override
    public String toString() {
      return Integer.toString(value);
    }

  }
}
