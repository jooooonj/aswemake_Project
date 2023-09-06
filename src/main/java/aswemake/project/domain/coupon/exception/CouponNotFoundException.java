package aswemake.project.domain.coupon.exception;

import aswemake.project.global.exception.BaseException;
import aswemake.project.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class CouponNotFoundException extends BaseException {
    private static final ErrorCode code = ErrorCode.COUPON_NOT_FOUND;

    public CouponNotFoundException(String message) {
        super(code, HttpStatus.BAD_REQUEST, message);
    }
}