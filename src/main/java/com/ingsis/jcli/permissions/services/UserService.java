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

  public void saveUser(String userId, String email) {
    if (!userExists(userId)) {
      userRepository.save(new User(userId, email));
    }
  }

  public boolean userExists(String userId) {
    return getUserById(userId).isPresent();
  }

  public Optional<User> getUserById(String userId) {
    return userRepository.findByUserId(userId);
  }

  public Optional<User> getUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }
}
