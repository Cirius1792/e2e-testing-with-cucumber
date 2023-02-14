package com.clt.userprofile.component;

import org.apache.commons.lang3.StringUtils;

import reactor.core.publisher.Mono;

public class UserProfileComponentImpl implements UserProfileComponent {

    final UserProfileRepository userProfileRepository;

    public UserProfileComponentImpl(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public Mono<UserProfileEntity> retrieveUserProfile(String userId) {
        return this.userProfileRepository.findUserById(Long.valueOf(userId));
    }

    @Override
    public Mono<UserProfileEntity> createUser(UserProfileEntity newUser) {
        if (StringUtils.isBlank(newUser.getUserName()))
            return Mono.error(new IllegalArgumentException("Username can't be null or empty"));
        return userProfileRepository.findUserByUsername(newUser.getUserName())
                .defaultIfEmpty(newUser)
                .flatMap(el -> {
                    if (el.getId() != null)
                        return Mono.error(() -> new IllegalArgumentException("UserAlready Exists"));
                    return userProfileRepository.saveUser(el);
                });
    }

    @Override
    public Mono<UserProfileEntity> updateUser(UserProfileEntity user) {
        if (user.getId() == null)
            return Mono.error(new IllegalArgumentException("User id can't be null or empty"));
        return userProfileRepository.findUserById(user.getId())
                .switchIfEmpty(
                        Mono.error(() -> new IllegalArgumentException("Trying to update a user that does not exists")))
                .transform(el -> Mono.just(user))
                .flatMap(userProfileRepository::saveUser)
                .log();
    }

}
