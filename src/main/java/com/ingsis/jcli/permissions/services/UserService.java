package com.ingsis.jcli.permissions.services;

import com.ingsis.jcli.permissions.models.User;
import com.ingsis.jcli.permissions.repository.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Optional<User> getUserById(String userId) {
    return userRepository.findByUserId(userId);
  }

  public User getUser(String userId) {
    Optional<User> userOptional = getUserById(userId);
    if (userOptional.isPresent()) {
      return userOptional.get();
    } else {
      User user = new User(userId);
      userRepository.save(user);
      return user;
    }
  }
}
