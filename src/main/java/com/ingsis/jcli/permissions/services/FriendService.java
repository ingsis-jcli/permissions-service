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

  public boolean areFriends(String userId1, String userId2) {
    User user1 = userRepository.findByUserId(userId1).orElse(null);
    User user2 = userRepository.findByUserId(userId2).orElse(null);

    if (user1 == null || user2 == null) {
      return false;
    }

    return user1.getFriends().contains(user2);
  }

  public void addFriend(String userId, String friendId) {
    User user = userRepository.findByUserId(userId).orElse(new User(userId));
    User friend = userRepository.findByUserId(friendId).orElse(new User(friendId));

    user.addFriend(friend);

    userRepository.save(user);
    userRepository.save(friend);
  }
}
