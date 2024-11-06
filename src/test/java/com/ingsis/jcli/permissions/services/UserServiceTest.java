package com.ingsis.jcli.permissions.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ingsis.jcli.permissions.models.User;
import com.ingsis.jcli.permissions.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private UserRepository userRepository;

  @InjectMocks private UserService userService;

  private User user;

  @BeforeEach
  void setUp() {
    user = new User("user1");
  }

  @Test
  void testGetUserByIdWhenUserExists() {
    when(userRepository.findByUserId("user1")).thenReturn(Optional.of(user));

    Optional<User> retrievedUser = userService.getUserById("user1");

    assertTrue(retrievedUser.isPresent());
    assertEquals(user, retrievedUser.get());
    verify(userRepository).findByUserId("user1");
  }

  @Test
  void testGetUserByIdWhenUserDoesNotExist() {
    when(userRepository.findByUserId("nonexistent")).thenReturn(Optional.empty());

    Optional<User> retrievedUser = userService.getUserById("nonexistent");

    assertTrue(retrievedUser.isEmpty());
    verify(userRepository).findByUserId("nonexistent");
  }

  @Test
  void testGetUserWhenUserExists() {
    when(userRepository.findByUserId("user1")).thenReturn(Optional.of(user));

    User retrievedUser = userService.getUser("user1");

    assertEquals(user, retrievedUser);
    verify(userRepository).findByUserId("user1");
    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  void testGetUserWhenUserDoesNotExist() {
    when(userRepository.findByUserId("newUser")).thenReturn(Optional.empty());
    when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

    User newUser = userService.getUser("newUser");

    assertEquals("newUser", newUser.getUserId());
    verify(userRepository).findByUserId("newUser");
    verify(userRepository).save(newUser);
  }
}
