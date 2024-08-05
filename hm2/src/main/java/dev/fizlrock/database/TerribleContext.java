package dev.fizlrock.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dev.fizlrock.dao.UserRepository;

/**
 * ConnectionHolder
 */
public class TerribleContext {
  private static Map<Class<?>, Object> container = new HashMap<>();

  private TerribleContext() {
  }

  public static void init() {

    try {
      Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/trackerdb", "admin",
          "gracesecret");
      container.put(Connection.class, con);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    createBean(JDBCWrapper.class);
    createBean(UserRepository.class);

    System.out.println(container);

  }

  private static void createBean(Class<?> beanClass) {

    try {
      var ctors = beanClass.getDeclaredConstructors();
      if (ctors.length != 1)
        throw new IllegalStateException("bean must have 1 constructor with deps");
      var ctor = ctors[0];
      if (ctor.getParameterCount() == 0)
        container.put(beanClass, ctor.newInstance());
      else {
        var types = ctor.getParameterTypes();
        var args = new ArrayList<Object>();

        for (Class<?> neededClass : types) {
          var bean = container.get(neededClass);
          if (bean == null)
            throw new IllegalStateException(String.format("Bean %s not found in context", neededClass.getName()));

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
    return (T) container.get(beanClass);
  }

}
