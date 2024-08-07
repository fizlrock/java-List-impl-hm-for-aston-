package dev.fizlrock.controllers;

import java.io.IOException;
import java.io.PrintWriter;

import dev.fizlrock.dao.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RoutePath(path = "/users")
public class UserController extends HttpServlet {

  private UserRepository userRepo;

  public UserController(UserRepository userRepo) {
    this.userRepo = userRepo;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    resp.setContentType("applicaton/json");
    PrintWriter printWriter = resp.getWriter();
    printWriter.write("Hello!");
    printWriter.close();

  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    resp.setContentType("text/html");
    PrintWriter printWriter = resp.getWriter();
    printWriter.write("Hello!");
    printWriter.close();

  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    resp.setContentType("text/html");
    PrintWriter printWriter = resp.getWriter();
    printWriter.write("Hello!");
    printWriter.close();

  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    resp.setContentType("text/html");
    PrintWriter printWriter = resp.getWriter();
    printWriter.write("Hello!");
    printWriter.close();

  }
}
