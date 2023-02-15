package com.clt.userprofile.component;

import reactor.core.publisher.Mono;

public interface UserProfileRepository {
    Mono<UserProfileEntity> findUserById(Long id);
    Mono<UserProfileEntity> findUserByUsername(String username);

    Mono<UserProfileEntity> saveUser(UserProfileEntity entity);

    Mono<Void> deleteUser(Long id);
}
