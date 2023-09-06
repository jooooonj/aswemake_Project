package aswemake.project.domain.member.exception;

import aswemake.project.global.exception.BaseException;
import aswemake.project.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;
public class CustomAccessDeniedException extends BaseException {
    private static final ErrorCode code = ErrorCode.ACCESS_DENIED;
    public CustomAccessDeniedException(String message) {
        super(code, HttpStatus.FORBIDDEN, message);
    }
}
