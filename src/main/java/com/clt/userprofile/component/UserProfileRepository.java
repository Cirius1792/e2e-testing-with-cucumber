package com.clt.userprofile.component;

import reactor.core.publisher.Mono;

public interface UserProfileRepository {
    Mono<UserProfileEntity> retrieveUserById(Long id);
}
