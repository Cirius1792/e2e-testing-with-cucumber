package com.clt.userprofile.repository;

import com.clt.userprofile.component.UserProfileEntity;
import reactor.core.publisher.Mono;

public class UserProfileRepositoryImpl implements UserProfileRepository {

  final UserProfileJpaRepository jpaRepository;

  public UserProfileRepositoryImpl(UserProfileJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public Mono<UserProfileEntity> findUserById(Long id) {
    return this.jpaRepository.findById(id).map(UserProfileModel::toEntity);
  }

  @Override
  public Mono<UserProfileEntity> findUserByUsername(String username) {
    return this.jpaRepository.findByUsername(username).map(UserProfileModel::toEntity);
  }

  @Override
  public Mono<UserProfileEntity> saveUser(UserProfileEntity entity) {
    return Mono.just(entity)
        .map(UserProfileModel::fromEntity)
        .flatMap(this.jpaRepository::save)
        .map(UserProfileModel::toEntity);
  }

  @Override
  public Mono<Void> deleteUser(Long id) {
    return this.jpaRepository.deleteById(id);
  }
}
