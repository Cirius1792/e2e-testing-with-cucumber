package com.clt.userprofile.component;

import org.apache.commons.lang3.StringUtils;

import com.clt.userprofile.error.UserProfileException;
import com.clt.userprofile.error.UserProfileNotFoundException;

import reactor.core.publisher.Mono;

public class UserProfileComponentImpl implements UserProfileComponent {

    final UserProfileRepository userProfileRepository;

    public UserProfileComponentImpl(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public Mono<UserProfileEntity> retrieveUserProfile(String userId) {
        return this.userProfileRepository.findUserById(Long.valueOf(userId))
        .switchIfEmpty(Mono.error(() -> new UserProfileNotFoundException("The user does not exist")));
    }

    @Override
    public Mono<UserProfileEntity> createUser(UserProfileEntity newUser) {
        if (StringUtils.isBlank(newUser.getUserName()))
            return Mono.error(new IllegalArgumentException("Username can't be null or empty"));
        return userProfileRepository.findUserByUsername(newUser.getUserName())
                .defaultIfEmpty(newUser)
                .flatMap(el -> {
                    if (el.getId() != null)
                        return Mono.error(() -> new UserProfileException("User Already Exists"));
                    return userProfileRepository.saveUser(el);
                });
    }

    @Override
    public Mono<UserProfileEntity> updateUser(UserProfileEntity user) {
        if (user.getId() == null)
            return Mono.error(new IllegalArgumentException("User id can't be null or empty"));
        return userProfileRepository.findUserById(user.getId())
                .switchIfEmpty(
                        Mono.error(() -> new UserProfileException("Trying to update a user that does not exists")))
                .transform(el -> Mono.just(user))
                .flatMap(userProfileRepository::saveUser)
                .log();
    }

    @Override
    public Mono<Void> deleteUser(String userId) {
        if(StringUtils.isBlank(userId))
            return Mono.error(() -> new UserProfileException("Invalid user id"));
        return this.userProfileRepository.deleteUser(Long.valueOf(userId));
    }

    

}
