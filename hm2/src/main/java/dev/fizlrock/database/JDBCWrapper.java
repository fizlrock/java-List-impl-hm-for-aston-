package dev.fizlrock.database;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * JdbcWrapper
 */

public class JDBCWrapper {

  public JDBCWrapper(Connection con) {
    this.con = con;
  }

  private Connection con;

  private static void setArgsToStatement(PreparedStatement stmnt, Object... params) {
    try {
      for (int i = 0; i < params.length; i++) {
        var obj = params[i];
        switch (obj) {
          case String str -> stmnt.setString(i + 1, str);
          case Long number -> stmnt.setLong(i + 1, number);
          case null, default -> throw new IllegalArgumentException("matcher not found");
        }
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * @param sql
   * @param params
   * @return rows changed
   */
  public int executeSQL(String sql, Object... params) {
    try {
      var statement = con.prepareStatement(sql);
      setArgsToStatement(statement, params);
      return statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

  }

  /**
   * @param sql
   * @param params
   * @return rows changed
   */
  @SuppressWarnings("unchecked")
  public <T> T executeSQLAndGetKey(String sql, Class<T> keyClass, Object... params) {
    try {
      var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      setArgsToStatement(statement, params);

      statement.executeUpdate();
      var key = statement.getGeneratedKeys();
      key.next();
      return (T) switch (keyClass.getName()) {
        case "java.lang.Long" -> key.getLong(1);
        case "java.lang.String" -> key.getString(1);
        default -> throw new IllegalArgumentException("Unexpected value: " + keyClass);
      };

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

  }

  public <T> List<T> executeSelectRequest(Class<T> entityClass, String sql, Object... args) {

    List<T> objects = new ArrayList<>();
    try (var stmnt = con.prepareStatement(sql)) {
      setArgsToStatement(stmnt, args);
      stmnt.execute();
      var results = stmnt.getResultSet();
      while (results.next())
        objects.add(mapRowSetToClass(entityClass, results));

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return objects;

  }

  public <T> T mapRowSetToClass(Class<T> clazz, ResultSet set) {
    T instance = createInstance(clazz);

    Field[] fields = clazz.getDeclaredFields();
    System.out.println(fields.length);

    try {
      for (var f : fields) {
        f.setAccessible(true);
        String fname = f.getName();
        Class<?> type = f.getType();
        int index = set.findColumn(fname);

        Object value = switch (type.getName()) {
          case "java.lang.Long" -> set.getLong(index);
          case "java.lang.String" -> set.getString(index);
          case null, default -> null;
        };
        f.set(instance, value);

      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return instance;

  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private static <T> T createInstance(Class<T> clazz) {

    Constructor[] ctors = clazz.getDeclaredConstructors();
    Constructor ctor = null;
    for (int i = 0; i < ctors.length; i++) {
      ctor = ctors[i];
      if (ctor.getGenericParameterTypes().length == 0)
        break;
    }

    if (ctor == null) {
      String message = String.format("Constructor without args not finded for class: %s", clazz.getName());
      throw new IllegalStateException(message);
    }
    try {
      ctor.setAccessible(true);
      T c = (T) ctor.newInstance();
      return c;
    } catch (Exception x) {
      throw new RuntimeException(x);
    }
  }

}
