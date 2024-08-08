package dev.fizlrock.myspring.web;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.fizlrock.myspring.TerribleContext;
import dev.fizlrock.myspring.web.annotations.Endpoint;
import dev.fizlrock.myspring.web.annotations.JsonAPIController;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * ServletController
 */
public class FrontController extends HttpServlet {

  private Tomcat tomcat;
  private Context context;

  private static record ControllerHandler(String pattern, Object instance, Method method) {
  }

  private List<ControllerHandler> handlers = new ArrayList<>();
  private List<Object> controllers;
  private ObjectMapper mapper;

  public FrontController(Tomcat tomcat, TerribleContext app_context, ObjectMapper mapper) {
    this.tomcat = tomcat;
    this.mapper = mapper;
    controllers = app_context.getBeansWithAnnotations(JsonAPIController.class);

    // System.out.printf("%s recieve %d serlets\n", this.getClass().getName(),
    // servlets.length);

    String contextPath = "/";
    String docBase = new File(".").getAbsolutePath();
    context = this.tomcat.addContext(contextPath, docBase);

    // var servlets = app_context.getBeans(HttpServlet.class);
    // System.out.println("finded " + servlets.size() + " servlets");

    tomcat.addServlet(contextPath, "frontController", this);
    context.addServletMappingDecoded("/*", "frontController");

    initHandlers();
    System.out.println(handlers);

    try {
      tomcat.start();
    } catch (LifecycleException e) {
      e.printStackTrace();
    }

  }

  private void initHandlers() {

    for (Object controller : controllers) {

      var methods = controller.getClass().getMethods();
      for (Method m : methods) {
        Annotation[] endA = m.getAnnotationsByType(Endpoint.class);

        if (endA.length == 0)
          continue;
        if (endA.length > 1)
          throw new IllegalStateException("Too much Endpont annotations. Allowed only one");

        var template = ((Endpoint) endA[0]).pathTemplate();
        handlers.add(new ControllerHandler(template, controller, m));
      }
    }
  }

  /**
   * Метод сопоставляет путь и шаблон.
   * <p>
   * Если путь соответвует шаблону, то метод вернет мапу и если шаблон
   * параметризирован, добавит в нее параметры
   * TODO эффективность
   * 
   * @param template
   * @param path
   * @return
   */
  public static Map<String, String> matchPathAndExtractVariable(String template, String path) {

    if (template.charAt(0) != '/' || path.charAt(0) != '/')
      throw new IllegalArgumentException("Path and path template must start with /");

    var template_parts = template.substring(1).split("/");
    var path_parts = path.substring(1).split("/");

    if (template_parts.length != path_parts.length)
      return null;

    var params = new HashMap<String, String>();

    for (int i = 0; i < path_parts.length; i++) {
      var tp = template_parts[i];
      var pp = path_parts[i];

      if (tp.startsWith("{") && tp.endsWith("}"))
        params.put(tp.substring(1, tp.length() - 1), pp);
      else if (!tp.equals(pp))
        return null;
    }

    return params;
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    Map<String, String> params = new HashMap<>();

    req.getParameterMap().entrySet().stream()
        .forEach(e -> params.put("query_" + e.getKey(), e.getValue()[0]));
    params.put("pathInfo", req.getPathInfo());
    params.put("remoteIp", req.getRemoteAddr());


    var relevant_handler = handlers.stream()
        .map(h -> new Pair<>(h,
            matchPathAndExtractVariable(h.pattern(), req.getPathInfo())))
        .filter(x -> x.value() != null)
        .collect(Collectors.toList());

    if (relevant_handler.size() > 1) {
    } else if (relevant_handler.size() == 0) {

    }

    switch (relevant_handler.size()) {
      case 0 -> sendNotFoundResponse(req, resp);
      case 1 -> {
        params.putAll(relevant_handler.get(0).value);
        procRequest(relevant_handler.get(0).key, params, req, resp);
      }
      default -> {
        var message = String.format("More one endpoint pattern match path. endpoints: %s, path: %s ",
            relevant_handler.toString(), req.getPathInfo());
        throw new IllegalStateException(message);
      }
    }
    ;

  }

  private void procRequest(ControllerHandler controller, Map<String, String> params, HttpServletRequest req,
      HttpServletResponse resp) {

    var neeededParams = controller.method().getParameters();
    List<Object> args = new ArrayList<>();

    for (Parameter p : neeededParams) {
      var p_value = params.get(p.getName());
      if (p_value == null)
        throw new RuntimeException("Запрос не содержит параметра " + p.getName());
      args.add(castTo(p.getType(), p_value));
    }

    ResponseEntity response = null;

    try {
      response = (ResponseEntity) controller.method().invoke(controller.instance, args.toArray());
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }

    if (response != null) {
      try (var out = resp.getOutputStream()) {
        mapper.writeValue(out, response.response());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      resp.setStatus(response.status_code());
    }

  }

  private static void sendNotFoundResponse(HttpServletRequest req, HttpServletResponse resp) {
    try (var pw = new PrintWriter(resp.getOutputStream())) {
      pw.write(String.format("Path %s not found\n", req.getPathInfo()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
  }

  private static record Pair<K, V>(K key, V value) {
  }

  @SuppressWarnings("unchecked")
  private static <T> T castTo(Class<T> type, String value) {
    return switch (type.getName()) {
      case "java.lang.String" -> (T) value;
      case "java.lang.Integer" -> (T) (Integer) Integer.parseInt(value);
      case "java.lang.Long" -> (T) (Long) Long.parseLong(value);
      default -> throw new IllegalStateException("Can't cast to " + type);
    };
  }

}
