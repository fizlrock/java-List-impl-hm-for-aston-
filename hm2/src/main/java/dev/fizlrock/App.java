package dev.fizlrock;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import dev.fizlrock.myspring.TerribleContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * App
 */
public class App {

  public static void main(String[] args) throws Exception {
    var context =new TerribleContext();
    var finded = context.getBeans(HttpServlet.class);
    System.out.println(finded);


    // // Нужно добавить зависимость tomcat-embed-core

    // Tomcat tomcat = new Tomcat();
    // tomcat.setBaseDir("build/tomcat_trashcan");
    // tomcat.setPort(8080);
    // tomcat.getConnector(); // Без не работает, почему-то...

    // String contextPath = "/";
    // String docBase = new File(".").getAbsolutePath();
    // Context context = tomcat.addContext(contextPath, docBase);


    // tomcat.addServlet(contextPath, MyServlet.class.getName(), new MyServlet());
    // context.addServletMappingDecoded("/myservlet", MyServlet.class.getName());

    // tomcat.start();
    // tomcat.getServer().await();
  }


  static class MyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
      PrintWriter writer = resp.getWriter();

      writer.println("<html><title>Welcome</title><body>");
      writer.println("<h1>Have a Great Day!</h1>");
      writer.println("</body></html>");
    }
  }

}
