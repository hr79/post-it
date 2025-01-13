package com.example.postItBackend.config;

import com.example.postItBackend.common.ApiResponseVer2;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Slf4j
//@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CommonHttpMessageConverter extends AbstractHttpMessageConverter<ApiResponseVer2<Object>> {
    private final ObjectMapper objectMapper;

    public CommonHttpMessageConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    @Override
    protected boolean supports(Class<?> clazz) {
        log.info("#1Execute CommonHttpMessageConverter supports");
        return clazz.equals(String.class); // String 클래스 타입만 처리할 수 있게 동작
    }

    @Override
    protected ApiResponseVer2<Object> readInternal(Class<? extends ApiResponseVer2<Object>> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        throw new UnsupportedOperationException("This converter can only support writing operation");
    }

    @Override
    protected void writeInternal(ApiResponseVer2<Object> resultMessage, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        log.info("5#Execute AbstractHttpMessageConverter - writeInternal");
        String responseMessage = this.objectMapper.writeValueAsString(resultMessage);
        StreamUtils.copy(responseMessage.getBytes(StandardCharsets.UTF_8), outputMessage.getBody());
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return Collections.singletonList(MediaType.APPLICATION_JSON);
    }
}
