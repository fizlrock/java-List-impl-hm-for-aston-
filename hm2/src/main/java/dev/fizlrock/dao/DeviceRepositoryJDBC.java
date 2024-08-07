package dev.fizlrock.dao;

import java.util.List;
import java.util.Optional;

import dev.fizlrock.domain.Device;

/**
 * DeviceRepositoryJDBC
 */
public class DeviceRepositoryJDBC implements CrudRepository<Device, Long> {

  private JDBCWrapper wrapper;

  public DeviceRepositoryJDBC(JDBCWrapper wrapper) {
    this.wrapper = wrapper;
  }

  @Override
  public List<Device> findAll() {
    return wrapper.executeSelectRequest(Device.class, "select * from device");
  }

  @Override
  public Optional<Device> findById(Long id) {
    var devices = wrapper.executeSelectRequest(
        Device.class,
        "select * from device where id = ?", id);
    Device device = null;
    if (devices.size() > 0)
      device = devices.get(0);
    return Optional.ofNullable(device);
  }

  @Override
  public boolean deleteById(Long id) {
    return wrapper.executeSQL("delete from device where id = ?", id) > 0;
  }

  @Override
  public boolean updateById(Long id, Device entity) {

    return wrapper.executeSQL("update device where id = ? set name = ?, owner_id = ? ",
        id, entity.getName(), entity.getOwnerId()) > 0;
  }

  @Override
  public Device save(Device entity) {

    var key = wrapper.executeSQLAndGetKey(
        "insert into device(name, owner_id) values (?, ?)",
        Long.class, entity.getName(), entity.getOwnerId());

    entity.setId(key);
    return entity;
  }

}
