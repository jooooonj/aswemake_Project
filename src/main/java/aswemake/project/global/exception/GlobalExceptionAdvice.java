package aswemake.project.global.exception;

import aswemake.project.domain.coupon.exception.CouponNotFoundException;
import aswemake.project.domain.member.exception.MemberNotFoundException;
import aswemake.project.domain.member.exception.MemberPasswordNotCorrectException;
import aswemake.project.domain.member.exception.CustomAccessDeniedException;
import aswemake.project.domain.order.exception.OrderNotFoundException;
import aswemake.project.domain.product.exception.ProductNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    //======member======
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

    @ExceptionHandler(CustomAccessDeniedException.class)
    public ResponseEntity<ErrorResponse> CustomAccessDeniedExceptionHandler(CustomAccessDeniedException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("[exceptionHandle] ex", e);
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), e.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }

    //======product======
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> ProductNotFoundExceptionHandler(ProductNotFoundException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("[exceptionHandle] ex", e);
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), e.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }

    //======order======
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> OrderNotFoundExceptionHandler(OrderNotFoundException e){
        ErrorCode errorCode = e.getErrorCode();
        log.error("[exceptionHandler] ex", e);
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), e.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }

    //======coupon======
    @ExceptionHandler(CouponNotFoundException.class)
    public ResponseEntity<ErrorResponse> CouponNotFoundExceptionHandler(CouponNotFoundException e){
        ErrorCode errorCode = e.getErrorCode();
        log.error("[exceptionHandler] ex", e);
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), e.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }

    //Request
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodValidExceptionHandler(MethodArgumentNotValidException e, HttpServletRequest request){
        log.error("[exceptionHandler] ex", e);
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.REQUEST_VALID_FAIL.getCode()
                ,e.getBindingResult().getFieldError().getDefaultMessage());
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
