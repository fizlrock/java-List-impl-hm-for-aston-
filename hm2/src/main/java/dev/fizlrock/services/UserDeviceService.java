package dev.fizlrock.services;

import java.util.List;
import java.util.Optional;

import dev.fizlrock.dao.interfaces.DeviceRepository;
import dev.fizlrock.dao.interfaces.UserRepository;
import dev.fizlrock.domain.Device;
import dev.fizlrock.domain.User;

/**
 * UserService
 */
public class UserDeviceService {

  private UserRepository userRepo;
  private DeviceRepository deviceRepo;

  public UserDeviceService(UserRepository userRepo, DeviceRepository deviceRepo) {
    this.userRepo = userRepo;
    this.deviceRepo = deviceRepo;
  }

  public Optional<User> findUserById(Long id) {
    var user = userRepo.findById(id);
    return user;
  }

  public List<User> findAllUsers() {
    return userRepo.findAll();
  }

  public User createNewUser(User user) {
    return userRepo.save(user);

  }

  public boolean updateUserByID(Long id, User user) {
    return userRepo.updateById(id, user);
  }

  public boolean deleteUserByID(Long id) {
    return userRepo.deleteById(id);

  }

  public Optional<Device> findDeviceById(Long id) {
    var user = deviceRepo.findById(id);
    return user;
  }

  public List<Device> findAllDevices() {
    return deviceRepo.findAll();
  }

  public Device createNewDevice(Device user) {
    // TODOТут проверка бизнес правил
    return deviceRepo.save(user);
  }

  public boolean updateDeviceByID(Long id, Device user) {
    return deviceRepo.updateById(id, user);
  }

  public boolean deleteDeviceByID(Long id) {
    return userRepo.deleteById(id);

  }

}
