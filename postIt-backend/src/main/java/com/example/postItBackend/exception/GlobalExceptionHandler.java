package com.example.postItBackend.exception;

import com.example.postItBackend.dto.ApiResponseVer3;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

//    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException exception, HttpServletRequest request) {
        HashMap<String, Object> exceptionBody = new HashMap<>();

        exceptionBody.put("timestamp", LocalDateTime.now().toString());
        exceptionBody.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        exceptionBody.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        exceptionBody.put("message", exception.getMessage());
        exceptionBody.put("path", request.getRequestURI());

        logger.error(exception.getMessage(), exception); // CustomException으로 전역 예외처리 후 콘솔에 스택 트레이스가 나오지 않아 따로 설정 필요.

        return new ResponseEntity<>(exceptionBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException exception, HttpServletRequest request) {
        log.info(":::: Global Exception Handler");
//        HashMap<String, Object> exceptionBody = new HashMap<>();
//        exceptionBody.put("timestamp", LocalDateTime.now().toString());
//        exceptionBody.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
//        exceptionBody.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//        exceptionBody.put("message", exception.getMessage());
//        exceptionBody.put("path", request.getRequestURI());

        logger.error(exception.getMessage(), exception);
        exception.printStackTrace();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseVer3.error(request.getRequestURI(), HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
    }

//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException exception, HttpServletRequest request) {
//        log.info(":::: Global Exception Handler.accessDeniedException");
//        logger.error(exception.getMessage(), exception);
//        exception.printStackTrace();
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                .body(ApiResponseVer3.error(request.getRequestURI(), HttpStatus.UNAUTHORIZED.value(), exception.getMessage()));
//    }
}
