package dev.fizlrock.controllers;

import java.io.File;
import java.util.List;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import dev.fizlrock.myspring.TerribleContext;
import jakarta.servlet.http.HttpServlet;

/**
 * ServletController
 */
public class ServletController {

  private Tomcat tomcat;
  private Context context;

  public ServletController(Tomcat tomcat, TerribleContext app_context) {
    this.tomcat = tomcat;

    // System.out.printf("%s recieve %d serlets\n", this.getClass().getName(),
    // servlets.length);

    String contextPath = "/";
    String docBase = new File(".").getAbsolutePath();
    context = this.tomcat.addContext(contextPath, docBase);

    var servlets = app_context.getBeans(HttpServlet.class);
    System.out.println("finded " + servlets.size() + " servlets");

    for (var servlet : servlets) {
      var path = getPath(servlet);
      tomcat.addServlet(contextPath, servlet.getClass().getName(), servlet);
      context.addServletMappingDecoded(path, servlet.getClass().getName());
    }
    try {
      tomcat.start();
    } catch (LifecycleException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  private String getPath(HttpServlet serlet) {
    var routePathAnnotation = serlet.getClass().getAnnotationsByType(RoutePath.class);
    if (routePathAnnotation.length == 0)
      throw new IllegalArgumentException("httpservlet must have annotaition RoutePath");
    return routePathAnnotation[0].path();

  }

}
