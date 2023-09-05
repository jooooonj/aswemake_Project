package aswemake.project.domain.order.exception;

import aswemake.project.global.exception.BaseException;
import aswemake.project.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends BaseException {
    private static final ErrorCode code = ErrorCode.ORDER_NOT_FOUND;

    public OrderNotFoundException(String message) {
        super(code, HttpStatus.BAD_REQUEST, message);
    }
}
