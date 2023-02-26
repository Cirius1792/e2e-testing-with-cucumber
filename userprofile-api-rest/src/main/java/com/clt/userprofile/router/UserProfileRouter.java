package com.clt.userprofile.router;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;

import com.clt.userprofile.component.UserProfileComponent;
import com.clt.userprofile.component.UserProfileEntity;
import com.clt.userprofile.router.request.CreateUserRequest;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class UserProfileRouter {
  static final String USER_ID_PATH_PARAM = "id";
  static final String USER_TAG_SWAGGER = "User Profile";
  final UserProfileComponent userProfileComponent;

  public UserProfileRouter(UserProfileComponent userProfileComponent) {
    this.userProfileComponent = userProfileComponent;
  }

  Mono<ServerResponse> getUser(ServerRequest request) {
    String userId = request.pathVariable(USER_ID_PATH_PARAM);
    return ServerResponse.ok()
        .body(this.userProfileComponent.retrieveUserProfile(userId), UserProfileEntity.class);
  }

  Mono<ServerResponse> createUser(ServerRequest request) {
    return request
        .bodyToMono(CreateUserRequest.class)
        .map(CreateUserRequest::toEntity)
        .flatMap(
            body ->
                ServerResponse.ok()
                    .body(this.userProfileComponent.createUser(body), UserProfileEntity.class));
  }

  Mono<ServerResponse> deleteUser(ServerRequest request) {
    return this.userProfileComponent
        .deleteUser(request.pathVariable(USER_ID_PATH_PARAM))
        .flatMap(el -> ServerResponse.ok().build());
  }

  Mono<ServerResponse> updateUser(ServerRequest request) {
    return request
        .bodyToMono(CreateUserRequest.class)
        .map(
            req ->
                UserProfileEntity.builder()
                    .id(Long.valueOf(request.pathVariable(USER_ID_PATH_PARAM)))
                    .userName(req.getUserName())
                    .name(req.getName())
                    .surname(req.getSurname())
                    .description(req.getDescription())
                    .build())
        .flatMap(
            newData ->
                ServerResponse.ok()
                    .body(userProfileComponent.updateUser(newData), UserProfileEntity.class));
  }

  public RouterFunction<ServerResponse> userProfileApis() {
    return route()
        .GET(
            String.format("/profile/{%s}", USER_ID_PATH_PARAM),
            this::getUser,
            ops ->
                ops.operationId("getUser")
                    .tag(USER_TAG_SWAGGER)
                    .parameter(
                        parameterBuilder()
                            .in(ParameterIn.PATH)
                            .name(USER_ID_PATH_PARAM)
                            .description("User Id"))
                    .response(
                        responseBuilder()
                            .responseCode("200")
                            .implementation(UserProfileEntity.class))
                    .response(
                        responseBuilder()
                            .responseCode("404")
                            .description("The user does not exists")))
        .POST(
            "/profile",
            this::createUser,
            ops ->
                ops.operationId("createUser")
                    .tag(USER_TAG_SWAGGER)
                    .requestBody(requestBodyBuilder().implementation(CreateUserRequest.class))
                    .response(
                        responseBuilder()
                            .responseCode("201")
                            .implementation(UserProfileEntity.class))
                    .response(
                        responseBuilder()
                            .responseCode("400")
                            .description("Missing Required parameters")))
        .DELETE(
            String.format("/profile/{%s}", USER_ID_PATH_PARAM),
            this::deleteUser,
            ops ->
                ops.operationId("getUser")
                    .tag(USER_TAG_SWAGGER)
                    .response(
                        responseBuilder()
                            .responseCode("200")
                            .implementation(UserProfileEntity.class))
                    .response(
                        responseBuilder()
                            .responseCode("404")
                            .description("The user does not exists"))
                    .parameter(
                        parameterBuilder()
                            .in(ParameterIn.PATH)
                            .name(USER_ID_PATH_PARAM)
                            .description("User Id")))
        .PATCH(
            String.format("/profile/{%s}", USER_ID_PATH_PARAM),
            this::updateUser,
            ops ->
                ops.operationId("getUser")
                    .tag(USER_TAG_SWAGGER)
                    .response(
                        responseBuilder()
                            .responseCode("200")
                            .implementation(UserProfileEntity.class))
                    .response(
                        responseBuilder()
                            .responseCode("404")
                            .description("The user does not exists"))
                    .parameter(
                        parameterBuilder()
                            .in(ParameterIn.PATH)
                            .name(USER_ID_PATH_PARAM)
                            .description("User Id")))
        .build();
  }
}
