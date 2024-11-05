package com.ingsis.jcli.permissions.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ingsis.jcli.permissions.models.User;
import com.ingsis.jcli.permissions.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

  @Autowired private UserService userService;

  @MockBean private UserRepository userRepository;
  @MockBean private JwtDecoder jwtDecoder;

  @Test
  public void saveNewUser() {
    String userId = "userId";
    String email = "email";
    User user = new User(userId, email);

    when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());
    when(userRepository.save(user)).thenReturn(user);

    userService.saveUser(userId, email);

    verify(userRepository, times(1)).save(user);
  }

  @Test
  public void saveExistingUser() {
    String userId = "userId";
    String email = "email";
    User user = new User(userId, email);

    when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));

    userService.saveUser(userId, email);

    verify(userRepository, times(0)).save(user);
  }

  @Test
  public void userExistsTrue() {
    String userId = "userId";
    User user = new User(userId, "email");

    when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));

    assertTrue(userService.userExists(userId));
  }

  @Test
  public void userExistsFalse() {
    String userId = "userId";

    when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());

    assertFalse(userService.userExists(userId));
  }
}
