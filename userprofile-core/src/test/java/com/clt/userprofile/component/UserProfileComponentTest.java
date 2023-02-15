package com.clt.userprofile.component;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.clt.userprofile.repository.UserProfileRepository;

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
                when(userProfileRepository.findUserById(eq(0L)))
                                .thenReturn(Mono.just(user0));

                // When I look for the user having ID 0L
                Mono<UserProfileEntity> actualUser = userProfileComponent.retrieveUserProfile("0");

                // The user 0L is returned
                StepVerifier.create(actualUser)
                                .expectNext(user0)
                                .verifyComplete();
        }

        @Test
        public void should_create_a_new_user() {
                // Given that there is no user with username mario.rossi
                String username = "mario.rossi";
                String name = "mario";
                String surname = "rossi";
                when(userProfileRepository.findUserByUsername(eq(username)))
                                .thenReturn(Mono.empty());

                // When mario.rossi tries to register
                Long mockedId = 0L;
                UserProfileEntity entity = UserProfileEntity.builder()
                                .userName(username)
                                .name(name)
                                .surname(surname)
                                .build();
                when(userProfileRepository.saveUser(eq(entity)))
                                .thenReturn(Mono.just(UserProfileEntity.builder()
                                                .id(mockedId)
                                                .userName(username)
                                                .name(name)
                                                .surname(surname)
                                                .build()));
                // Then the registered entity is returned with a valid ID and all the stored
                // fields
                var result = userProfileComponent.createUser(entity);
                StepVerifier.create(result)
                                .expectNext(UserProfileEntity.builder()
                                                .id(mockedId)
                                                .userName(username)
                                                .name(name)
                                                .surname(surname)
                                                .build())
                                .verifyComplete();
        }

        @Test
        public void should_not_create_new_user_because_already_exists() {
                // Given that there is a user with username mario.rossi already
                String username = "mario.rossi";
                String name = "mario";
                String surname = "rossi";
                Long mockedId = 0L;
                UserProfileEntity entity = UserProfileEntity.builder()
                                .id(mockedId)
                                .userName(username)
                                .name(name)
                                .surname(surname)
                                .build();
                when(userProfileRepository.findUserByUsername(eq(username)))
                                .thenReturn(Mono.just(entity));

                // When mario.rossi tries to register
                var result = userProfileComponent.createUser(entity);

                // Then the registration fails with error
                StepVerifier.create(result)
                                .verifyError();
        }

        @Test
        public void should_not_create_user_because_of_missing_params() {
                // Given that a user tries to register without specifying a username
                String username = "";
                String name = "mario";
                String surname = "rossi";
                Long mockedId = 0L;
                UserProfileEntity entity = UserProfileEntity.builder()
                                .id(mockedId)
                                .userName(username)
                                .name(name)
                                .surname(surname)
                                .build();
                // When the user tries to register
                var result = userProfileComponent.createUser(entity);

                // Then the registration fails with error
                StepVerifier.create(result)
                                .verifyError();
        }

        @Test
        public void should_update_name_and_surname_of_existing_user() {
                // Given an existing user
                UserProfileEntity user0 = new UserProfileEntity(0L, "test-user", "Mario", "Rossi", "He used to cheer");
                when(userProfileRepository.findUserById(eq(0L)))
                                .thenReturn(Mono.just(user0));

                // When an existing users tries to update its data
                UserProfileEntity newData = UserProfileEntity.builder()
                                .id(user0.getId())
                                .name("Fabio")
                                .surname("Fazio")
                                .userName("test-user2")
                                .description("New Description")
                                .build();
                when(userProfileRepository.saveUser(eq(newData)))
                                .thenReturn(Mono.just(newData));

                // Then the data are modified and the new one are returned
                var result = userProfileComponent.updateUser(newData);
                StepVerifier.create(result)
                                .expectNext(UserProfileEntity.copy(newData))
                                .verifyComplete();
        }

    @Test
    public void should__fail_to_update_a_non_existing_user() {
        // Given an unexisting user
        when(userProfileRepository.findUserById(eq(0L)))
        .thenReturn(Mono.empty());
        
        // When trying to update the data of an unexisting user
        UserProfileEntity user0 = new UserProfileEntity(0L, "test-user", "Mario", "Rossi", "He used to cheer");

        // Then the data are modified and the new one are returned
        var result = userProfileComponent.updateUser(user0);
        StepVerifier.create(result)
            .verifyError();
    }
}
