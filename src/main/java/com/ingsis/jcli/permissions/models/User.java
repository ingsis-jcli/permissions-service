package com.ingsis.jcli.permissions.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "friends")
public class User {

  @Id private String userId;

  @ManyToMany
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
    friends.add(friend);
    friend.getFriends().add(this);
  }

  public void removeFriend(User friend) {
    friends.remove(friend);
    friend.getFriends().remove(this);
  }
}
