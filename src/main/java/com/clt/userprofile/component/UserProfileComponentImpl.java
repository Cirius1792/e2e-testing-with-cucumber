package com.clt.userprofile.component;

import reactor.core.publisher.Mono;

public class UserProfileComponentImpl implements UserProfileComponent{

    final UserProfileRepository userProfileRepository;

    
    public UserProfileComponentImpl(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }


    @Override
    public Mono<UserProfileEntity> retrieveUserProfile(String userId) {
        return this.userProfileRepository.retrieveUserById(Long.valueOf(userId));
    }
    
}
