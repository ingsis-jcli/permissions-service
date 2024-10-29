package com.ingsis.jcli.permissions.repository;

import com.ingsis.jcli.permissions.models.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

  Optional<User> findByUserId(String userId);
}
