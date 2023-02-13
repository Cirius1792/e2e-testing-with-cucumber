package com.clt.userprofile.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Mono;

public interface UserProfileJpaRepository extends ReactiveCrudRepository<UserProfileModel, Long>{

    Mono<UserProfileModel> findById(Long id);
    
}
