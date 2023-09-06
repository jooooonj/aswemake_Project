package aswemake.project.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    REQUEST_VALID_FAIL(405, "REQEUST_VALID_FAIL", "Request 검증 실패, 에러 메시지 확인"),

    //MEMBER
    MEMBER_NOT_FOUND(404, "USER_NOT_FOUND", "회원을 찾을 수 없는 경우"),
    MEMBER_PASSWORD_NOT_CORRECT(405, "MEMBER_PASSWORD_NOT_CORRECT", "비밀번호가 일치하지 않는 경우"),
    ACCESS_DENIED(403, "ACCESS_DENIED", "접근 거부"),

    //PRODUCT
    PRODUCT_NOT_FOUND(404, "PRODUCT_NOT_FOUND", "상품을 찾을 수 없습니다."),

    //ORDER
    ORDER_NOT_FOUND(404, "ORDER_NOT_FOUND", "주문을 찾을 수 없습니다."),

    //COUPON
    COUPON_NOT_FOUND(404, "COUPON_NOT_FOUND", "쿠폰을 찾을 수 없습니다.");

    private final int status;
    private final String code;
    private final String description;

    //상태, 코드, 설명
    ErrorCode(int status, String code, String description) {
        this.status = status;
        this.code = code;
        this.description = description;
    }


}
