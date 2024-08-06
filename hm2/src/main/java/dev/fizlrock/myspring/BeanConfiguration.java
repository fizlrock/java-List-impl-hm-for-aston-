package dev.fizlrock.myspring;

/**
 * BeanConfiguration
 */
@FunctionalInterface
public interface BeanConfiguration {
  Object makeBean();
}
