package com.zteng.moraleducation.controller;

import com.alibaba.fastjson.JSON;
import com.zteng.moraleducation.common.CommonException;
import com.zteng.moraleducation.common.CommonResult;
import com.zteng.moraleducation.common.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({CommonException.class})
    public CommonResult commonException(CommonException ex) {
        log.error("自定义异常：", ex);
        return CommonResult.failed(ex.getErrCode(), ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public CommonResult validException(ConstraintViolationException ex) {
        log.error("参数校验异常：", ex);
        return CommonResult.failed(ResultCode.VALIDATE_FAILED.getCode(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.error("参数校验异常：", ex);
        return CommonResult.failed(ResultCode.VALIDATE_FAILED.getCode(), JSON.toJSONString(errors));
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public CommonResult duplicateKeyException(DuplicateKeyException ex) {
        log.error("重复插入：", ex);
        return CommonResult.failed(ResultCode.DUPLICATE_KEY.getCode(), ex.getMessage());
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public CommonResult illegalArgumentHandler(IllegalArgumentException ex) {
        log.error("参数错误异常：", ex);
        return CommonResult.failed(ex.getMessage());
    }
}
