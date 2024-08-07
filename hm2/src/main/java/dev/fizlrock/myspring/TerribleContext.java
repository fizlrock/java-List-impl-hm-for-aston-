package dev.fizlrock.myspring;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import dev.fizlrock.configurations.ConnectionConfiguration;
import dev.fizlrock.configurations.ObjectMapperConfiguration;
import dev.fizlrock.configurations.TomcatConfiguration;
import dev.fizlrock.controllers.ServletController;
import dev.fizlrock.controllers.UserController;
import dev.fizlrock.dao.JDBCWrapper;
import dev.fizlrock.dao.UserRepositoryJDBC;
import dev.fizlrock.services.UserDeviceService;

/**
 * ConnectionHolder
 */
public class TerribleContext {
  private Map<Class<?>, Object> container = new HashMap<>();

  public TerribleContext() {
    init();
  }

  private void init() {

    container.put(this.getClass(), this);
    createBean(ObjectMapperConfiguration::getObjectMapper);
    createBean(ConnectionConfiguration::getConnection);
    createBean(JDBCWrapper.class);
    createBean(UserRepositoryJDBC.class);
    createBean(UserDeviceService.class);
    createBean(TomcatConfiguration::getTomcat);
    createBean(UserController.class);
    createBean(ServletController.class);

    var report = container.entrySet().stream()
        .map(Entry::getKey)
        .map(Object::toString)
        .collect(Collectors.joining(", "));

    System.out.println("Container: " + report);

  }

  private void createBean(Supplier<?> supplier) {
    var bean = supplier.get();
    container.put(bean.getClass(), bean);
  }

  private void createBean(Class<?> beanClass) {

    try {
      var ctors = beanClass.getDeclaredConstructors();
      if (ctors.length != 1)
        throw new IllegalStateException("bean must have 1 constructor with deps");
      var ctor = ctors[0];
      if (ctor.getParameterCount() == 0)
        container.put(beanClass, ctor.newInstance());
      else {
        var types = ctor.getParameters();
        var args = new ArrayList<Object>();

        for (Parameter neededArg : types) {

          var bean = getBean(neededArg.getType());
          if (bean == null)
            throw new IllegalStateException(String.format("Bean %s not found in context", neededArg.getType()));
          args.add(bean);
        }

        container.put(beanClass, ctor.newInstance(args.toArray()));

      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  @SuppressWarnings("unchecked")
  public <T> T getBean(Class<T> beanClass) {
    var bean = container.get(beanClass);

    if (bean == null)
      for (var entry : container.entrySet())
        if (beanClass.isAssignableFrom(entry.getKey()))
          return (T) entry.getValue();

    return (T) bean;
  }

  @SuppressWarnings("unchecked")
  public <T> List<T> getBeans(Class<T> beanClass) {
    return container.entrySet().stream().filter(
        x -> beanClass.isAssignableFrom(x.getKey()))
        .map(x -> (T) x.getValue())
        .collect(Collectors.toList());
  }

}
