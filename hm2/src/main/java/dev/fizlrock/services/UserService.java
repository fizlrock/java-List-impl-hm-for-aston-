package dev.fizlrock.services;

import java.util.List;
import java.util.Optional;

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

  public Optional<User> findUserById(Long id) {
    var user = userRepo.findById(id);
    return user;
  }

  public List<User> findAll() {
    return userRepo.findAll();
  }

  public User createNewUser(User user) {
    return userRepo.save(user);

  }

  public boolean updateByID(Long id, User user) {
    return userRepo.updateById(id, user);
  }

  public boolean deleteByID(Long id) {
    return userRepo.deleteById(id);

  }

}
