package com.ingsis.jcli.permissions.models;

import com.ingsis.jcli.permissions.common.PermissionType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {

  @Id @Getter private String userId;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "user_friends",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "friend_id"))
  private Set<User> friends = new HashSet<>();

  @Setter
  @Getter
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id")
  private Set<SnippetPermission> snippetPermissions = new HashSet<>();

  public User(String userId) {
    this.userId = userId;
  }

  public void addFriend(User friend) {
    if (!friends.contains(friend)) {
      friends.add(friend);
      friend.addFriend(this);
    }
  }

  public Set<User> getFriends() {
    return Collections.unmodifiableSet(friends);
  }

  public void addSnippetPermission(Long snippetId, PermissionType permissionType) {
    Optional<SnippetPermission> existingPermission =
        snippetPermissions.stream().filter(sp -> sp.getSnippetId().equals(snippetId)).findFirst();

    if (existingPermission.isPresent()) {
      List<PermissionType> permissions = existingPermission.get().getPermissions();
      if (!permissions.contains(permissionType)) {
        permissions.add(permissionType);
      }
    } else {
      SnippetPermission newPermission =
          new SnippetPermission(snippetId, new ArrayList<>(List.of(permissionType)));
      snippetPermissions.add(newPermission);
    }
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof User && userId.equals(((User) obj).userId);
  }

  @Override
  public int hashCode() {
    return userId.hashCode();
  }
}
