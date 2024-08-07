package dev.fizlrock.domain;

/**
 * Device
 */
public class Device {

  protected Long id;
  protected String name;
  protected Long owner_id;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getOwnerId() {
    return owner_id;
  }

  public void setOwnerId(Long owner_id) {
    this.owner_id = owner_id;
  }

}
