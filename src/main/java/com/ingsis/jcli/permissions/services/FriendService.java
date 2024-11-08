package com.ingsis.jcli.permissions.services;

import com.ingsis.jcli.permissions.models.User;
import com.ingsis.jcli.permissions.repository.UserRepository;
import java.util.List;
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
    User user = userRepository.findByUserId(userId).orElseThrow();
    Set<User> friends = user.getFriends();

    return friends.stream().map(User::getUserId).toList();
  }

  public boolean areFriends(String userId1, String userId2) {
    User user1 = userRepository.findByUserId(userId1).orElseThrow();
    User user2 = userRepository.findByUserId(userId2).orElseThrow();

    return user1.getFriends().contains(user2);
  }

  public void addFriend(User user, User friend) {
    user.addFriend(friend);
    userRepository.save(user);
    userRepository.save(friend);
  }
}
