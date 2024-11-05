package com.ingsis.jcli.permissions.services;

import com.ingsis.jcli.permissions.common.exceptions.UserDoesNotExist;
import com.ingsis.jcli.permissions.models.User;
import com.ingsis.jcli.permissions.repository.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final Auth0Service auth0Service;

  @Autowired
  public UserService(UserRepository userRepository, Auth0Service auth0Service) {
    this.userRepository = userRepository;
    this.auth0Service = auth0Service;
  }

  public void saveUser(String userId, String email) {
    if (!userInDb(userId)) {
      userRepository.save(new User(userId, email));
    }
  }

  public boolean userInDb(String userId) {
    return getUserById(userId).isPresent();
  }

  public User getUser(String userId) {
    Optional<User> userOptional = getUserById(userId);
    if (userOptional.isPresent()) {
      return userOptional.get();
    }
    Optional<String> userEmail = auth0Service.getUserEmail(userId);
    if (userEmail.isEmpty()) {
      throw new UserDoesNotExist(userId);
    }
    User user = new User(userId, userEmail.get());
    userRepository.save(user);
    return user;
  }

  public Optional<User> getUserById(String userId) {
    return userRepository.findByUserId(userId);
  }

  public Optional<User> getUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }
}
