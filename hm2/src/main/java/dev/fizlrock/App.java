package dev.fizlrock;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * App
 */
public class App {

  public static void main(String[] args) throws SQLException {
    Connection con;

    con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/earsdb", "admin", "gracesecret");
    Statement st = con.createStatement();
    var result = st.executeQuery("select id, username from app_user;");

    while (result.next()) {
      System.out.printf("%d %s \n", result.getLong("id"), result.getString("username"));

    }

  }

}
