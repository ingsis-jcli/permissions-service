package com.ingsis.jcli.permissions.services;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ingsis.jcli.permissions.models.User;
import com.ingsis.jcli.permissions.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class FriendServiceTest {

  @Autowired private FriendService friendService;

  @MockBean private UserRepository userRepository;
  @MockBean private JwtDecoder jwtDecoder;

  @Test
  public void getFriends() {
    String userId = "userId";
    List<String> friends = List.of("friend1", "friend2", "friend3", "friend4", "friend5");
    List<User> friendUsers = friends.stream().map(User::new).toList();
    User user = new User(userId, friendUsers);

    when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));

    assertThat(friendService.getFriends(userId)).containsExactlyInAnyOrderElementsOf(friends);
  }

  @Test
  public void getFriendsEmpty() {
    String userId = "userId";
    List<String> friends = List.of();
    User user = new User(userId);

    user.setUserId(userId);

    when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));

    assertThat(friendService.getFriends(userId)).containsExactlyInAnyOrderElementsOf(friends);
  }

  @Test
  public void getFriendsNoUserFound() {
    String userId = "userId";

    when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());

    assertThat(friendService.getFriends(userId)).containsExactlyInAnyOrderElementsOf(List.of());
  }

  @Test
  public void addFriend() {
    String userId = "userId";
    String friendId = "friendId";

    User user = new User(userId);
    User friend = new User(friendId);

    when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
    when(userRepository.findByUserId(friendId)).thenReturn(Optional.of(friend));

    friendService.addFriend(userId, friendId);

    verify(userRepository, times(1)).save(user);
    verify(userRepository, times(1)).save(friend);

    assertTrue(user.getFriends().contains(friend));
    assertTrue(friend.getFriends().contains(user));
  }

  @Test
  public void addFriendAlreadyFriends() {
    String userId = "userId";
    String friendId = "friendId";

    User user = new User(userId);
    User friend = new User(friendId);

    user.addFriend(friend);
    friend.addFriend(user);

    when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
    when(userRepository.findByUserId(friendId)).thenReturn(Optional.of(friend));

    friendService.addFriend(userId, friendId);

    verify(userRepository, times(1)).save(user);
    verify(userRepository, times(1)).save(friend);

    assertTrue(user.getFriends().contains(friend));
    assertTrue(friend.getFriends().contains(user));
  }

  @Test
  public void addFriendUserNotFound() {
    String userId = "userId";
    String friendId = "friendId";

    User user = new User(userId);
    User friend = new User(friendId);

    when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());
    when(userRepository.findByUserId(friendId)).thenReturn(Optional.of(friend));

    friendService.addFriend(userId, friendId);

    verify(userRepository, times(1)).save(user);
    verify(userRepository, times(1)).save(friend);

    // assertTrue(user.getFriends().contains(friend));
    assertTrue(friend.getFriends().contains(user));
  }

  @Test
  public void addFriendFriendNotFound() {
    String userId = "userId";
    String friendId = "friendId";

    User user = new User(userId);
    User friend = new User(friendId);

    when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
    when(userRepository.findByUserId(friendId)).thenReturn(Optional.empty());

    friendService.addFriend(userId, friendId);

    verify(userRepository, times(1)).save(user);
    verify(userRepository, times(1)).save(friend);

    assertTrue(user.getFriends().contains(friend));
    // assertTrue(friend.getFriends().contains(user));
  }

  @Test
  public void addFriendNeitherFound() {
    String userId = "userId";
    String friendId = "friendId";

    User user = new User(userId);
    User friend = new User(friendId);

    when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());
    when(userRepository.findByUserId(friendId)).thenReturn(Optional.empty());

    friendService.addFriend(userId, friendId);

    verify(userRepository, times(1)).save(user);
    verify(userRepository, times(1)).save(friend);

    // TODO: check
    // assertTrue(user.getFriends().contains(friend));
    // assertTrue(friend.getFriends().contains(user));
  }
}
