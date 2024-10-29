package com.ingsis.jcli.permissions.services;

import com.ingsis.jcli.permissions.models.User;
import com.ingsis.jcli.permissions.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
    return userRepository.findByUserId(userId).isPresent();
  }

  public List<String> getEmails(String userId, int page, int size) {
    PageRequest pageRequest = PageRequest.of(page, size);
    List<User> users = userRepository.findByUserIdNot(userId, pageRequest).getContent();
    return users.stream().map(User::getEmail).toList();
  }
}
