package aswemake.project.domain.coupon.entity.scopeType;

import lombok.Getter;

@Getter
public enum CouponApplyScope {
    ORDER_ALL("ORDER_ALL"),  // 주문 전체
    SPECIFIC_PRODUCT("ORDER_ALL");// 특정 상품 한정

    private String value;

    CouponApplyScope(String value) {
        this.value = value;
    }
}
