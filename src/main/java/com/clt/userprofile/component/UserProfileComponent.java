package com.clt.userprofile.component;

import reactor.core.publisher.Mono;

public interface UserProfileComponent {
    Mono<UserProfileEntity> retrieveUserProfile(String userId);
}
