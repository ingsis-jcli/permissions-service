package com.ingsis.jcli.permissions.services;

import com.ingsis.jcli.permissions.models.User;
import com.ingsis.jcli.permissions.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendService {

  private final UserRepository userRepository;

  @Autowired
  public FriendService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<String> getFriends(String userId) {
    Optional<User> user = userRepository.findByUserId(userId);

    Set<User> friends = user.map(User::getFriends).orElseGet(Set::of);

    return friends.stream().map(User::getUserId).toList();
  }
}
