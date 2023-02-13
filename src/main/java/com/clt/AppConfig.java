package com.clt;

import java.time.Clock;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.clt.accounts.client.AccountsClient;
import com.clt.accounts.client.AccountsClientImpl;
import com.clt.accounts.component.AccountComponent;
import com.clt.accounts.component.AccountComponentImpl;
import com.clt.accounts.router.AccountRouter;
import com.clt.userprofile.component.UserProfileComponent;
import com.clt.userprofile.component.UserProfileComponentImpl;
import com.clt.userprofile.component.UserProfileRepository;
import com.clt.userprofile.repository.UserProfileJpaRepository;
import com.clt.userprofile.repository.UserProfileRepositoryImpl;
import com.clt.userprofile.router.UserProfileRouter;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class AppConfig {

    @Bean
    @Qualifier("accountId")
    public Long accountId(@Value("${accountId}") Long accountId){
        log.info("Configuring application using account id: {}", accountId);
        return accountId;
    }


    @Bean
    public AccountsClient accountsClient(@Value("${clients.platform.endpoint}") String endpoint, @Value("${clients.apiKey}") String apiKey){
        return new AccountsClientImpl(endpoint, apiKey);
    }

    @Bean
    public AccountComponent accountService(Long accountId, AccountsClient AccountsClient){
        return new AccountComponentImpl(accountId, AccountsClient);
    }

    @Bean
    public RouterFunction<ServerResponse> accountApis(AccountComponent accountService){
        return new AccountRouter(accountService, Clock.systemDefaultZone()).accountApis();
    }

    @Bean
    public UserProfileRepository userProfileRepository(UserProfileJpaRepository jpaRepository){
        return new UserProfileRepositoryImpl(jpaRepository);
    }

    @Bean
    public UserProfileComponent userProfileComponent(UserProfileRepository userProfileRepository){
        return new UserProfileComponentImpl(userProfileRepository);
    }

    @Bean
    public RouterFunction<ServerResponse> userProfileApi(UserProfileComponent userProfileComponent){
        return new UserProfileRouter(userProfileComponent).userProfileApis();
    }
}
