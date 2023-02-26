package com.clt.userprofile.router;

import com.clt.userprofile.router.request.ErrorResponse;
import java.util.Map;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {
  static final String DEFAULT_CODE = "AP999";
  static final String DEFAULT_DESC = "Internal Server Error";

  public GlobalErrorWebExceptionHandler(
      GlobalErrorAttributes globalErrorAttributes,
      ApplicationContext applicationContext,
      ServerCodecConfigurer serverCodecConfigurer) {
    super(globalErrorAttributes, new WebProperties.Resources(), applicationContext);
    super.setMessageWriters(serverCodecConfigurer.getWriters());
    super.setMessageReaders(serverCodecConfigurer.getReaders());
  }

  @Override
  protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
    return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
  }

  private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {

    final Map<String, Object> errorPropertiesMap =
        getErrorAttributes(request, ErrorAttributeOptions.defaults());

    int statusCode =
        Integer.parseInt(
            errorPropertiesMap
                .getOrDefault(ErrorAttributesKey.STATUS_CODE.getKey(), "500")
                .toString());
    return ServerResponse.status(HttpStatus.valueOf(statusCode))
        .contentType(MediaType.APPLICATION_JSON)
        .body(
            BodyInserters.fromValue(
                new ErrorResponse(
                    errorPropertiesMap
                        .getOrDefault(ErrorAttributesKey.CODE.getKey(), DEFAULT_CODE)
                        .toString(),
                    errorPropertiesMap
                        .getOrDefault(ErrorAttributesKey.DESCRIPTION.getKey(), DEFAULT_DESC)
                        .toString())));
  }
}
