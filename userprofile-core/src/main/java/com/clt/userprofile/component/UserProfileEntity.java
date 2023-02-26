package com.clt.userprofile.component;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class UserProfileEntity {
  final Long id;
  final String userName;
  final String name;
  final String surname;
  final String description;

  public static UserProfileEntity copy(UserProfileEntity entity) {
    return UserProfileEntity.builder()
        .id(entity.getId())
        .userName(entity.getUserName())
        .name(entity.getName())
        .surname(entity.getSurname())
        .description(entity.getDescription())
        .build();
  }
}
