package dev.fizlrock.controllers;

import dev.fizlrock.domain.User;
import dev.fizlrock.myspring.web.ResponseEntity;
import dev.fizlrock.myspring.web.annotations.Endpoint;
import dev.fizlrock.myspring.web.annotations.JsonAPIController;
import dev.fizlrock.services.UserDeviceService;

@JsonAPIController
public class UserController {

  private UserDeviceService userService;

  public UserController(UserDeviceService userService) {
    this.userService = userService;
  }

  @Endpoint(pathTemplate = "/user", method = "GET")
  public ResponseEntity getAllUsers() {
    return new ResponseEntity(200, userService.findAllUsers());
  }

  @Endpoint(pathTemplate = "/user/{userId}", method = "GET")
  public ResponseEntity getUserByID(Long userId) {
    var user = userService.findUserById(userId);
    if (user.isEmpty())
      return new ResponseEntity(404, null);
    return new ResponseEntity(200, user.get());
  }

  @Endpoint(pathTemplate = "/user/{userId}", method = "DELETE")
  public ResponseEntity deleteUserByID(Long userId) {
    var isDeleted = userService.deleteUserByID(userId);
    if (isDeleted)
      return new ResponseEntity(204, null);
    return new ResponseEntity(200, null);
  }

  @Endpoint(pathTemplate = "/user/{userId}", method = "PUT")
  public ResponseEntity updateUser(User user, Long userId) {
    var updatedEntity = userService.updateUserByID(userId, user);
    if (updatedEntity == null) {
      user = userService.createNewUser(user);
      return new ResponseEntity(201, user);
    } else if (updatedEntity.equals(user))
      return new ResponseEntity(204, null);
    else
      return new ResponseEntity(200, user);
  }

  @Endpoint(pathTemplate = "/user", method = "CREATE")
  public ResponseEntity createUser(User user) {
    var createdUser = userService.createNewUser(user);
    return new ResponseEntity(201, createdUser);
  }

}
