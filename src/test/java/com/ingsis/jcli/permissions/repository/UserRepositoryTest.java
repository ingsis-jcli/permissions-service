package com.ingsis.jcli.permissions.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ingsis.jcli.permissions.models.User;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class UserRepositoryTest {

  @Autowired private UserRepository userRepository;

  @Test
  void testSaveUser() {
    User user = new User();
    user.setName("John Doe");

    User savedUser = userRepository.save(user);

    assertThat(savedUser.getId()).isNotNull();
    assertThat(savedUser.getName()).isEqualTo("John Doe");
  }

  @Test
  void testFindById() {
    User user = new User();
    user.setName("Jane Doe");

    User savedUser = userRepository.save(user);

    Optional<User> foundUser = userRepository.findById(savedUser.getId());

    assertThat(foundUser).isPresent();
    assertThat(foundUser.get().getId()).isEqualTo(savedUser.getId());
    assertThat(foundUser.get().getName()).isEqualTo("Jane Doe");
  }

  @Test
  void testUpdateUser() {
    User user = new User();
    user.setName("Initial Name");

    User savedUser = userRepository.save(user);

    savedUser.setName("Updated Name");
    User updatedUser = userRepository.save(savedUser);

    assertThat(updatedUser.getName()).isEqualTo("Updated Name");
  }

  @Test
  void testDeleteUser() {
    User user = new User();
    user.setName("User to be deleted");

    User savedUser = userRepository.save(user);

    userRepository.delete(savedUser);

    Optional<User> deletedUser = userRepository.findById(savedUser.getId());
    assertThat(deletedUser).isNotPresent();
  }
}
