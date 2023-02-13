package com.clt.userprofile.component;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.micrometer.core.instrument.step.StepValue;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class UserProfileComponentTest {

    UserProfileRepository userProfileRepository;
    UserProfileComponent userProfileComponent;

    @BeforeEach
    public void init() {
        userProfileRepository = mock(UserProfileRepository.class);
        userProfileComponent = new UserProfileComponentImpl(userProfileRepository);
    }

    @Test
    public void should_retrieve_a_user() {
        // Given the existence of a user having the ID 0L
        UserProfileEntity user0 = new UserProfileEntity(0L, "test-user", "Mario", "Rossi", "He used to cheer");
        when(userProfileRepository.retrieveUserById(eq(0L)))
                .thenReturn(Mono.just(user0));

        // When I look for the user having ID 0L
        Mono<UserProfileEntity> actualUser = userProfileComponent.retrieveUserProfile("0");

        // The user 0L is returned
        StepVerifier.create(actualUser)
                .expectNext(user0)
                .verifyComplete();
    }
}
