package dev.fizlrock.controllers;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.fizlrock.domain.User;
import dev.fizlrock.services.UserDeviceService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RoutePath(path = "/device/*")
public class DeviceController extends HttpServlet {

  private UserDeviceService userService;
  private ObjectMapper mapper;

  public DeviceController(UserDeviceService userService, ObjectMapper mapper) {
    this.userService = userService;
    this.mapper = mapper;
  }

  /**
   * Получить всех пользователей
   */
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    resp.setContentType("applicaton/json");
    var out = resp.getWriter();

    String userIdstr = req.getPathInfo();
    if (userIdstr == null) {
      var users = userService.findAllUsers();
      System.out.println(req.getPathInfo());
      mapper.writeValue(out, users);
    } else {
      Long userID = Long.parseLong(userIdstr.substring(1));

      var user = userService.findUserById(userID);
      if (user.isPresent()) {
        mapper.writeValue(out, user.get());
        resp.setStatus(HttpServletResponse.SC_OK);
      } else {
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
      }
    }

    out.close();
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    resp.setContentType("applicaton/json");
    var in = req.getReader();
    var out = resp.getWriter();

    var user = mapper.readValue(in, User.class);
    user = userService.createNewUser(user);
    mapper.writeValue(out, user);

    resp.setStatus(HttpServletResponse.SC_CREATED);

    in.close();
    out.close();
  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    resp.setContentType("applicaton/json");
    var in = req.getReader();
    var out = resp.getWriter();
    var userIdStr = req.getPathInfo();

    if (userIdStr == null)
      throw new IllegalArgumentException("you must specify userId as path variable");
    else
      userIdStr = userIdStr.substring(1);

    Long id = Long.parseLong(userIdStr);
    User user = mapper.readValue(in, User.class);
    userService.updateUserByID(id, user);
    resp.setStatus(HttpServletResponse.SC_ACCEPTED);

    in.close();
    out.close();
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    resp.setContentType("applicaton/json");
    var in = req.getReader();
    var out = resp.getWriter();
    var userIdStr = req.getPathInfo();

    if (userIdStr == null)
      throw new IllegalArgumentException("you must specify userId as path variable");
    else
      userIdStr = userIdStr.substring(1);

    Long id = Long.parseLong(userIdStr);
    var deleted = userService.deleteUserByID(id);

    if (deleted)
      resp.setStatus(HttpServletResponse.SC_OK);
    else
      resp.setStatus(HttpServletResponse.SC_NO_CONTENT);

    in.close();
    out.close();

  }
}
