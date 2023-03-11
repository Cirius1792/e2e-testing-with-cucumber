package com.clt.userprofile.component;

import com.clt.userprofile.error.UserProfileException;
import com.clt.userprofile.error.UserProfileNotFoundException;
import com.clt.userprofile.repository.UserProfileRepository;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

public class UserProfileComponentImpl implements UserProfileComponent {

  private static final int DESCRIPTION_MAX_LEN = 255;
  final UserProfileRepository userProfileRepository;

  public UserProfileComponentImpl(UserProfileRepository userProfileRepository) {
    this.userProfileRepository = userProfileRepository;
  }

  @Override
  public Mono<UserProfileEntity> retrieveUserProfile(String userId) {
    return this.userProfileRepository
        .findUserById(Long.valueOf(userId))
        .switchIfEmpty(
            Mono.error(() -> new UserProfileNotFoundException("The user does not exist")));
  }

  @Override
  public Mono<UserProfileEntity> createUser(UserProfileEntity newUser) {
    if (StringUtils.isBlank(newUser.getUserName()))
      return Mono.error(new IllegalArgumentException("Username can't be null or empty"));
    if (StringUtils.isNotBlank(newUser.getDescription())
        && newUser.getDescription().length() > DESCRIPTION_MAX_LEN)
      return Mono.error(
          new UserProfileException(
              String.format("Field Description is too long. Max Length: %d", DESCRIPTION_MAX_LEN)));
    return userProfileRepository
        .findUserByUsername(newUser.getUserName())
        .defaultIfEmpty(newUser)
        .flatMap(
            el -> {
              if (el.getId() != null)
                return Mono.error(() -> new UserProfileException("User Already Exists"));
              return userProfileRepository.saveUser(el);
            });
  }

  @Override
  public Mono<UserProfileEntity> updateUser(UserProfileEntity user) {
    if (user.getId() == null)
      return Mono.error(new IllegalArgumentException("User id can't be null or empty"));
    return userProfileRepository
        .findUserById(user.getId())
        .switchIfEmpty(
            Mono.error(
                () -> new UserProfileException("Trying to update a user that does not exists")))
        .transform(el -> Mono.just(user))
        .flatMap(userProfileRepository::saveUser)
        .log();
  }

  @Override
  public Mono<UserProfileEntity> deleteUser(String userId) {
    if (StringUtils.isBlank(userId))
      return Mono.error(() -> new UserProfileException("Invalid user id"));
    return Mono.from(this.userProfileRepository.findUserById(Long.valueOf(userId)))
        .switchIfEmpty(
            Mono.error(() -> new UserProfileNotFoundException("The user does not exist")))
        .map(
            el -> {
              // FIXME: Missing mono subscription, therefore the element is never deleted
              this.userProfileRepository.deleteUser(Long.valueOf(userId));
              return el;
            });
  }
}
