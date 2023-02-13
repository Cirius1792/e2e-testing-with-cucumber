package com.clt.userprofile.repository;

import com.clt.userprofile.component.UserProfileEntity;
import com.clt.userprofile.component.UserProfileRepository;

import reactor.core.publisher.Mono;

public class UserProfileRepositoryImpl implements UserProfileRepository {

    final UserProfileJpaRepository jpaRepository;

    public UserProfileRepositoryImpl(UserProfileJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Mono<UserProfileEntity> retrieveUserById(Long id) {
        return this.jpaRepository.findById(id)
                .map(el -> UserProfileEntity.builder()
                        .id(el.getId())
                        .userName(el.getUserName())
                        .name(el.getName())
                        .surname(el.getSurname())
                        .description(el.getDescription())
                        .build());
    }

}
