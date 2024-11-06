package com.ingsis.jcli.permissions.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ingsis.jcli.permissions.models.User;
import com.ingsis.jcli.permissions.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FriendServiceTest {

  @Mock private UserRepository userRepository;

  @InjectMocks private FriendService friendService;

  private User user1;
  private User user2;
  private User user3;

  @BeforeEach
  void setUp() {
    user1 = new User("user1");
    user2 = new User("user2");
    user3 = new User("user3");

    user1.addFriend(user2);
  }

  @Test
  void testGetFriends() {
    when(userRepository.findByUserId("user1")).thenReturn(Optional.of(user1));

    List<String> friends = friendService.getFriends("user1");

    assertEquals(1, friends.size());
    assertEquals("user2", friends.get(0));
    verify(userRepository).findByUserId("user1");
  }

  @Test
  void testAreFriendsWhenFriends() {
    when(userRepository.findByUserId("user1")).thenReturn(Optional.of(user1));
    when(userRepository.findByUserId("user2")).thenReturn(Optional.of(user2));

    boolean result = friendService.areFriends("user1", "user2");

    assertTrue(result);
    verify(userRepository).findByUserId("user1");
    verify(userRepository).findByUserId("user2");
  }

  @Test
  void testAreFriendsWhenNotFriends() {
    when(userRepository.findByUserId("user1")).thenReturn(Optional.of(user1));
    when(userRepository.findByUserId("user3")).thenReturn(Optional.of(user3));

    boolean result = friendService.areFriends("user1", "user3");

    assertFalse(result);
    verify(userRepository).findByUserId("user1");
    verify(userRepository).findByUserId("user3");
  }

  @Test
  void testAddFriend() {
    friendService.addFriend(user1, user3);

    assertTrue(user1.getFriends().contains(user3));
    assertTrue(user3.getFriends().contains(user1));

    verify(userRepository).save(user1);
    verify(userRepository).save(user3);
  }
}
