package dev.fizlrock.domain;

/**
 * User
 */
public class User {
  protected String username;
  protected Long id = null;

  public User() {
  }

  public User(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String toString(){
    return String.format("{id: %d, username: %s}", id, username);
  }

}
