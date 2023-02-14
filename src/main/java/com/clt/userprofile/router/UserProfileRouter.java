package com.clt.userprofile.router;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;

import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.clt.userprofile.component.UserProfileComponent;
import com.clt.userprofile.component.UserProfileEntity;
import com.clt.userprofile.router.request.CreateUserRequest;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import reactor.core.publisher.Mono;

public class UserProfileRouter {
        static final String USER_ID_QUERY_PARAM = "userId";
        static final String USER_TAG_SWAGGER = "User Profile";
        final UserProfileComponent userProfileComponent;

        public UserProfileRouter(UserProfileComponent userProfileComponent) {
                this.userProfileComponent = userProfileComponent;
        }

        Mono<ServerResponse> getUser(ServerRequest request) {
                String userId = request.pathVariable(USER_ID_QUERY_PARAM);
                return ServerResponse.ok()
                                .body(this.userProfileComponent.retrieveUserProfile(userId), UserProfileEntity.class);
        }

        Mono<ServerResponse> createUser(ServerRequest request) {
                // TODO
                return ServerResponse.ok().build();
        }

        Mono<ServerResponse> deleteUser(ServerRequest request) {
                // TODO
                return ServerResponse.ok().build();
        }

        Mono<ServerResponse> updateUser(ServerRequest request) {
                // TODO
                return ServerResponse.ok().build();
        }

        public RouterFunction<ServerResponse> userProfileApis() {
                return route()
                                .GET("/profile", this::getUser,
                                                ops -> ops.operationId("getUser")
                                                                .tag(USER_TAG_SWAGGER)
                                                                .response(responseBuilder().responseCode("200")
                                                                                .implementation(UserProfileEntity.class))
                                                                .response(responseBuilder().responseCode("404")
                                                                                .description("The user does not exists"))
                                                                .parameter(parameterBuilder().in(ParameterIn.PATH)
                                                                                .name(USER_ID_QUERY_PARAM)
                                                                                .description("User Id")))
                                .POST("/profile", this::createUser,
                                                ops -> ops.operationId("createUser")
                                                                .tag(USER_TAG_SWAGGER)
                                                                .requestBody(requestBodyBuilder().implementation(
                                                                                CreateUserRequest.class))
                                                                .response(responseBuilder()
                                                                                .responseCode("201")
                                                                                .implementation(UserProfileEntity.class))
                                                                .response(responseBuilder()
                                                                                .responseCode("400")
                                                                                .description("Missing Required parameters")))
                                .DELETE("/profile", this::deleteUser,
                                                ops -> ops.operationId("getUser")
                                                                .tag(USER_TAG_SWAGGER)
                                                                .response(responseBuilder().responseCode("200")
                                                                                .implementation(UserProfileEntity.class))
                                                                .response(responseBuilder().responseCode("404")
                                                                                .description("The user does not exists"))
                                                                .parameter(parameterBuilder().in(ParameterIn.PATH)
                                                                                .name(USER_ID_QUERY_PARAM)
                                                                                .description("User Id")))
                                .PATCH("/profile", this::updateUser,
                                                ops -> ops.operationId("getUser")
                                                                .tag(USER_TAG_SWAGGER)
                                                                .response(responseBuilder().responseCode("200")
                                                                                .implementation(UserProfileEntity.class))
                                                                .response(responseBuilder().responseCode("404")
                                                                                .description("The user does not exists"))
                                                                .parameter(parameterBuilder().in(ParameterIn.PATH)
                                                                                .name(USER_ID_QUERY_PARAM)
                                                                                .description("User Id")))
                                .build();
        }
}
