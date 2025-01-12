package com.example.postItBackend.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/*
 * 1. controller 데이터 return
 * 2. ResponseWrapper.supports
 * 3. ResponseWrapper.beforeBodyWrite : 데이터를 ApiResponse로 감싼다. ErrorResponse와 성공 데이터를 구분하여 status 결정.
 * 4. CommonHttpMessageConverter.writeInternal : ApiResponse 객체 -> JSON으로 직렬화. HTTP 응답메시지 바디에 JSON 작성
 * 5. 클라이언트로 응답 전달
 * */

// ResponseBodyAdvice : 컨트롤러에서 반환된 데이터를 가로채서 가공함. ApiResponse로 응답 데이터를 가공
//@RestControllerAdvice
@Slf4j
public class ResponseWrapper implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        log.info("2#Execute ResponseWrapper - supports");
        log.info("2#Execute ResponseWrapper - returnType: {}", returnType);
        log.info("2#Execute ResponseWrapper - converterType: {}", converterType);
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        log.info("3#Execute ResponseWrapper - beforeBodyWrite");
        if (body instanceof ErrorResponse) {
            return new ApiResponseVer2<>("ERROR", body);
        }
        return new ApiResponseVer2<>("SUCCESS", body);
    }
}
