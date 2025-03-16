package org.ktb.matajo.global.error;

import lombok.extern.slf4j.Slf4j;
import org.ktb.matajo.global.common.CommonResponse;
import org.ktb.matajo.global.error.exception.BusinessException;
import org.ktb.matajo.global.error.exception.GeneralException;
import org.ktb.matajo.global.error.exception.PostException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 비즈니스 예외 처리
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<CommonResponse<Void>> handleBusinessException(BusinessException e) {
        log.error("BusinessException: {}", e.getMessage(), e);
        return ResponseEntity
                .status(determineHttpStatus(e))
                .body(CommonResponse.error(e.getMessage()));
    }

    /**
     * 서버 내부 오류 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<Void>> handleException(Exception e) {
        log.error("Internal Server Error: {}", e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonResponse.error("internal_server_error"));
    }

    /**
     * 예외 유형에 따른 HTTP 상태 코드 결정
     */
    private HttpStatus determineHttpStatus(BusinessException e) {
        if (e instanceof GeneralException) {
            if ("invalid_offset_or_limit".equals(e.getMessage())) {
                return HttpStatus.BAD_REQUEST;
            }
        } else if (e instanceof PostException) {
            if ("not_found_posts_page".equals(e.getMessage())) {
                return HttpStatus.NOT_FOUND;
            }
        }
        
        // 기본 상태 코드
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
} 