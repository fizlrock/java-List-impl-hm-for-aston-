package dev.fizlrock.dao.impl;

import java.util.List;
import java.util.Optional;

import dev.fizlrock.dao.interfaces.UserRepository;
import dev.fizlrock.domain.User;

/**
 * UserRepository
 */
public class UserRepositoryJDBC extends UserRepository {

  JDBCWrapper wrapper;

  public UserRepositoryJDBC(JDBCWrapper wrapper) {
    this.wrapper = wrapper;
  }

  @Override
  public List<User> findAll() {
    return wrapper.executeSelectRequest(User.class, "select * from app_user");
  }

  @Override
  public Optional<User> findById(Long id) {
    var list = wrapper.executeSelectRequest(User.class, "select * from app_user where id = ?", id);

    User user = null;
    if (list.size() > 0)
      user = list.get(0);
    return Optional.ofNullable(user);
  }

  @Override
  public boolean deleteById(Long id) {
    return wrapper.executeSQL("delete from app_user where id = ?", id) > 0;
  }

  @Override
  public User updateById(Long id, User entity) {
    var updated = wrapper.executeSQL("update app_user where id = ? set username = ?", id, entity.getUsername()) > 0;
    if (updated)
      return wrapper.executeSelectRequest(User.class, "select * from app_user where id = ?", id).get(0);
    else
      return null;
  }

  @Override
  public User save(User entity) {
    Long key = wrapper.executeSQLAndGetKey(
        "insert into app_user(username) values (?)",
        Long.class,
        entity.getUsername());
    entity.setId(key);
    return entity;
  }

}
