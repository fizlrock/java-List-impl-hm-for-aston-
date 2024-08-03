package dev.fizlrock;

import java.sql.SQLException;

import dev.fizlrock.dao.UserRepositoryImpl;
import dev.fizlrock.domain.User;

/**
 * App
 */
public class App {

  public static void main(String[] args) throws SQLException {
    var rep = new UserRepositoryImpl();
    // System.out.println("hey hey");
    // for (int i = 0; i < 10; i++) {
    // var id = rep.save(new User(String.format("test it 4 %d", i))).getId();
    // System.out.printf("saved %d entity with id %d\n", i, id);
    // }
    var array = rep.findAll();
    System.out.println(array);
  }

}
