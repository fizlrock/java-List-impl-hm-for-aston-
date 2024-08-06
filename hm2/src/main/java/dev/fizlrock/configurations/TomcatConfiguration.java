package dev.fizlrock.configurations;

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

/**
 * TomcatConfiguration
 */
public class TomcatConfiguration {

  public static Tomcat getTomcat() {

    Tomcat tomcat = new Tomcat();
    tomcat.setBaseDir("build/tomcat_trashcan");
    tomcat.setPort(8080);
    tomcat.getConnector(); // Без не работает, почему-то...


    return tomcat;
  }

}
