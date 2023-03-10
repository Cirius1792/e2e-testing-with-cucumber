package com.clt.userprofile;

import com.clt.userprofile.component.UserProfileComponent;
import com.clt.userprofile.component.UserProfileComponentImpl;
import com.clt.userprofile.repository.UserProfileJpaRepository;
import com.clt.userprofile.repository.UserProfileRepository;
import com.clt.userprofile.repository.UserProfileRepositoryImpl;
import com.clt.userprofile.router.UserProfileRouter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Slf4j
@Configuration
@EnableR2dbcRepositories
public class AppConfig {

  @Bean
  public UserProfileRepository userProfileRepository(UserProfileJpaRepository jpaRepository) {
    return new UserProfileRepositoryImpl(jpaRepository);
  }

  @Bean
  public UserProfileComponent userProfileComponent(UserProfileRepository userProfileRepository) {
    return new UserProfileComponentImpl(userProfileRepository);
  }

  @Bean
  public RouterFunction<ServerResponse> userProfileApi(UserProfileComponent userProfileComponent) {
    log.info("Configuring Routes");
    return new UserProfileRouter(userProfileComponent).userProfileApis();
  }
}
