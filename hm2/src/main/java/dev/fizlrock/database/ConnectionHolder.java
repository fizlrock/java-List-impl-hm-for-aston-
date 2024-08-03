package dev.fizlrock.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ConnectionHolder
 */
public class ConnectionHolder {
  static Connection con;

  static {
    try {
      con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/trackerdb", "admin", "gracesecret");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static Connection getConnection() {
    return con;

  }
}
