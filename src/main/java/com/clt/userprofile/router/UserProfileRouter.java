package com.clt.userprofile.router;

import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.clt.userprofile.component.UserProfileComponent;
import com.clt.userprofile.component.UserProfileEntity;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import reactor.core.publisher.Mono;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;

public class UserProfileRouter {
    static final String USER_ID_QUERY_PARAM = "userId";

    final UserProfileComponent userProfileComponent;

    public UserProfileRouter(UserProfileComponent userProfileComponent) {
        this.userProfileComponent = userProfileComponent;
    }

    Mono<ServerResponse> getUser(ServerRequest request) {
        String userId = request.pathVariable(USER_ID_QUERY_PARAM);
        return ServerResponse.ok()
                .body(this.userProfileComponent.retrieveUserProfile(userId), UserProfileEntity.class);
    }

    public RouterFunction<ServerResponse> userProfileApis() {
        return route()
                .GET("/profile", this::getUser,
                        ops -> ops.operationId("getUser")
                                .tag("User Profile")
                                .response(responseBuilder().responseCode("200")
                                        .implementation(UserProfileEntity.class))
                                .response(responseBuilder().responseCode("404")
                                        .description("The user does not exists"))
                                        .parameter(parameterBuilder().in(ParameterIn.PATH).name(USER_ID_QUERY_PARAM).description("User Id")))
                .build();
    }
}
