package aswemake.project.domain.product.exception;

import aswemake.project.global.exception.BaseException;
import aswemake.project.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends BaseException {
    private static final ErrorCode code = ErrorCode.PRODUCT_NOT_FOUND;

    public ProductNotFoundException(String message) {
        super(code, HttpStatus.BAD_REQUEST, message);
    }
}
