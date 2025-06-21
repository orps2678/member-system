package com.example.membersystem.exception;

import com.example.membersystem.common.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全域異常處理器
 * 統一處理系統中的所有異常，返回標準化的錯誤響應
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 處理業務異常
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResult<Void>> handleBusinessException(
            BusinessException e, HttpServletRequest request) {

        log.warn("業務異常 - URL: {}, 錯誤: {}", request.getRequestURL(), e.getMessage());

        ApiResult<Void> result = ApiResult.error(e.getCode(), e.getMessage());
        return ResponseEntity.status(e.getCode()).body(result);
    }

    /**
     * 處理參數驗證異常 (@Valid)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResult<Void>> handleValidationException(
            MethodArgumentNotValidException e, HttpServletRequest request) {

        log.warn("參數驗證異常 - URL: {}", request.getRequestURL());

        String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        ApiResult<Void> result = ApiResult.badRequest("參數驗證失敗: " + errorMessage);
        return ResponseEntity.badRequest().body(result);
    }

    /**
     * 處理綁定異常
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResult<Void>> handleBindException(
            BindException e, HttpServletRequest request) {

        log.warn("參數綁定異常 - URL: {}", request.getRequestURL());

        String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        ApiResult<Void> result = ApiResult.badRequest("參數綁定失敗: " + errorMessage);
        return ResponseEntity.badRequest().body(result);
    }

    /**
     * 處理約束違反異常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResult<Void>> handleConstraintViolationException(
            ConstraintViolationException e, HttpServletRequest request) {

        log.warn("約束違反異常 - URL: {}", request.getRequestURL());

        String errorMessage = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        ApiResult<Void> result = ApiResult.badRequest("參數約束違反: " + errorMessage);
        return ResponseEntity.badRequest().body(result);
    }

    /**
     * 處理缺少請求參數異常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResult<Void>> handleMissingParameterException(
            MissingServletRequestParameterException e, HttpServletRequest request) {

        log.warn("缺少請求參數異常 - URL: {}, 參數: {}", request.getRequestURL(), e.getParameterName());

        ApiResult<Void> result = ApiResult.badRequest("缺少必要參數: " + e.getParameterName());
        return ResponseEntity.badRequest().body(result);
    }

    /**
     * 處理方法參數類型不匹配異常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResult<Void>> handleTypeMismatchException(
            MethodArgumentTypeMismatchException e, HttpServletRequest request) {

        log.warn("參數類型不匹配異常 - URL: {}, 參數: {}", request.getRequestURL(), e.getName());

        ApiResult<Void> result = ApiResult.badRequest(
                String.format("參數 '%s' 類型錯誤，期望類型: %s",
                        e.getName(),
                        e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "未知"));

        return ResponseEntity.badRequest().body(result);
    }

    /**
     * 處理 HTTP 請求方法不支持異常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResult<Void>> handleMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e, HttpServletRequest request) {

        log.warn("HTTP 方法不支持異常 - URL: {}, 方法: {}", request.getRequestURL(), e.getMethod());

        ApiResult<Void> result = ApiResult.error(405,
                String.format("不支持的 HTTP 方法: %s，支持的方法: %s",
                        e.getMethod(),
                        String.join(", ", e.getSupportedMethods())));

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(result);
    }

    /**
     * 處理 HTTP 媒體類型不支持異常
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResult<Void>> handleMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException e, HttpServletRequest request) {

        log.warn("HTTP 媒體類型不支持異常 - URL: {}, 類型: {}", request.getRequestURL(), e.getContentType());

        ApiResult<Void> result = ApiResult.error(415,
                "不支持的媒體類型: " + e.getContentType());

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(result);
    }

    /**
     * 處理資源未找到異常 (404)
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResult<Void>> handleNoResourceFoundException(
            NoResourceFoundException e, HttpServletRequest request) {

        log.warn("資源未找到異常 - URL: {}", request.getRequestURL());

        ApiResult<Void> result = ApiResult.notFound("請求的資源不存在");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    /**
     * 處理資料庫相關異常
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResult<Void>> handleDataAccessException(
            DataAccessException e, HttpServletRequest request) {

        log.error("資料庫異常 - URL: {}, 錯誤: {}", request.getRequestURL(), e.getMessage(), e);

        ApiResult<Void> result = ApiResult.error(500, "資料庫操作失敗");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * 處理空指針異常
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResult<Void>> handleNullPointerException(
            NullPointerException e, HttpServletRequest request) {

        log.error("空指針異常 - URL: {}, 錯誤: {}", request.getRequestURL(), e.getMessage(), e);

        ApiResult<Void> result = ApiResult.error(500, "系統內部錯誤");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * 處理非法參數異常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResult<Void>> handleIllegalArgumentException(
            IllegalArgumentException e, HttpServletRequest request) {

        log.warn("非法參數異常 - URL: {}, 錯誤: {}", request.getRequestURL(), e.getMessage());

        ApiResult<Void> result = ApiResult.badRequest("參數錯誤: " + e.getMessage());
        return ResponseEntity.badRequest().body(result);
    }

    /**
     * 處理所有其他未捕獲的異常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResult<Void>> handleGeneralException(
            Exception e, HttpServletRequest request) {

        log.error("未知異常 - URL: {}, 類型: {}, 錯誤: {}",
                request.getRequestURL(), e.getClass().getSimpleName(), e.getMessage(), e);

        ApiResult<Void> result = ApiResult.error(500, "系統內部錯誤");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
}