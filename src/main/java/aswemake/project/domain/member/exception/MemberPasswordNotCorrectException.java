package aswemake.project.domain.member.exception;

import aswemake.project.global.exception.BaseException;
import aswemake.project.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class MemberPasswordNotCorrectException extends BaseException {
    private static final ErrorCode code = ErrorCode.MEMBER_PASSWORD_NOT_CORRECT;

    public MemberPasswordNotCorrectException(String message) {
        super(code, HttpStatus.BAD_REQUEST, message);
    }
}

