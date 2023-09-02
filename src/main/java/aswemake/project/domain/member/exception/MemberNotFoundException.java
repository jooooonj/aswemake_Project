package aswemake.project.domain.member.exception;

import aswemake.project.global.exception.BaseException;
import aswemake.project.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends BaseException {
    private static final ErrorCode code = ErrorCode.MEMBER_NOT_FOUND;

    public MemberNotFoundException(String message) {
        super(code, HttpStatus.BAD_REQUEST, message);
    }
}
