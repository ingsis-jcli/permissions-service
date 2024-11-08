package com.ingsis.jcli.permissions.common.responses;

import com.ingsis.jcli.permissions.dtos.UserDto;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

public class PaginatedUsers {
  @Setter @Getter private int page;
  @Setter @Getter private int pageSize;
  @Setter @Getter private int count;
  @Setter @Getter private List<UserDto> users;

  public PaginatedUsers(int page, int pageSize, int count, List<UserDto> users) {
    this.page = page;
    this.pageSize = pageSize;
    this.count = count;
    this.users = users;
  }
}
