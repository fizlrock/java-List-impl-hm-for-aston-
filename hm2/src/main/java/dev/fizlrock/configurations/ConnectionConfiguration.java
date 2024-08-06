package dev.fizlrock.configurations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ConnectionConfiguration
 */
public class ConnectionConfiguration {

  public static Connection getConnection() {
    try {
      Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/trackerdb", "admin",
          "gracesecret");
      return con;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

  }

}
