package com.clt.userprofile.router.request;

import com.clt.userprofile.component.UserProfileEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateUserRequest {
  String userName;
  String name;
  String surname;
  String description;

  public static UserProfileEntity toEntity(CreateUserRequest request) {
    return UserProfileEntity.builder()
        .userName(request.getUserName())
        .name(request.getName())
        .surname(request.getSurname())
        .description(request.getDescription())
        .build();
  }
}
