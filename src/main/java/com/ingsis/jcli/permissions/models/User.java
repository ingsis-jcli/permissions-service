package com.ingsis.jcli.permissions.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@NoArgsConstructor
@Table(name = "friends")
public class User {

  @Id @Getter private String userId;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "user_friends",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "friend_id"))
  private Set<User> friends = new HashSet<>();

  public User(String userId) {
    this.userId = userId;
  }

  public User(String userId, Collection<User> friends) {
    this.userId = userId;
    this.friends.addAll(friends);
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

  @Override
  public boolean equals(Object obj) {
    return obj instanceof User && userId.equals(((User) obj).userId);
  }

  @Override
  public int hashCode() {
    return userId.hashCode();
  }
}
