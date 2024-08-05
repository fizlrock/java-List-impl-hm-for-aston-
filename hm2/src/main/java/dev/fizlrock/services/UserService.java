package dev.fizlrock.Services;

import dev.fizlrock.dao.UserRepository;
import dev.fizlrock.domain.User;

/**
 * UserService
 */
public class UserService {

  private UserRepository userRepo;

  public UserService(UserRepository userRepo) {
    this.userRepo = userRepo;
  }

  public User findUserById(Long id) {
    var user = userRepo.findById(id);
    return user.get();
  }

}
