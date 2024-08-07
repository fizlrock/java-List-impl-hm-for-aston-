package dev.fizlrock;

import dev.fizlrock.dao.interfaces.CrudRepository;
import dev.fizlrock.myspring.TerribleContext;

/**
 * App
 */
public class App {

  public static void main(String[] args) throws Exception {
    var context = new TerribleContext();
    var beans = context.getBeans(CrudRepository.class);
    beans.forEach(x -> System.out.println(x.getClass()));
  }

}
