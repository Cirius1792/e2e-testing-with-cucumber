package com.clt.userprofile.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;

@Repository
public interface UserProfileJpaRepository extends ReactiveCrudRepository<UserProfileModel, Long>{

    Mono<UserProfileModel> findById(Long id);
    Mono<UserProfileModel> findByUsername(String username);    
}
