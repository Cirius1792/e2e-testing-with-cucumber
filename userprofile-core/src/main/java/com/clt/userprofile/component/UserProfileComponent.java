package com.clt.userprofile.component;

import reactor.core.publisher.Mono;

public interface UserProfileComponent {
  Mono<UserProfileEntity> retrieveUserProfile(String userId);

  Mono<UserProfileEntity> createUser(UserProfileEntity newUser);

  Mono<UserProfileEntity> updateUser(UserProfileEntity user);

  Mono<Void> deleteUser(String userId);
}
