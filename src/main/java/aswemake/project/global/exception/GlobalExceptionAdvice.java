package aswemake.project.global.exception;

import aswemake.project.domain.member.exception.MemberNotFoundException;
import aswemake.project.domain.member.exception.MemberPasswordNotCorrectException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    //MEMBER
    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponse> MemberNotfoundExceptionHandler(MemberNotFoundException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("[exceptionHandle] ex", e);
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), e.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(MemberPasswordNotCorrectException.class)
    public ResponseEntity<ErrorResponse> MemberPasswordNotCorrectExceptionHandler(MemberPasswordNotCorrectException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("[exceptionHandle] ex", e);
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), e.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }



}
