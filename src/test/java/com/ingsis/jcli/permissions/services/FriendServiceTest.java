package com.ingsis.jcli.permissions.services;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
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
}
