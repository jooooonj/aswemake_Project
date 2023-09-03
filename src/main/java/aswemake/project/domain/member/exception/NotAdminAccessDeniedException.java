package aswemake.project.domain.member.exception;

import aswemake.project.global.exception.BaseException;
import aswemake.project.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class NotAdminAccessDeniedException extends BaseException {

    private static final ErrorCode code = ErrorCode.NOT_ADMIN_ACCESS_DENIED;

    public NotAdminAccessDeniedException(String message) {
        super(code, HttpStatus.FORBIDDEN, message);
    }
}
