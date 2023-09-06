package aswemake.project.domain.coupon.entity;

import aswemake.project.domain.coupon.entity.scopeType.CouponApplyScope;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Min;
import lombok.*;
import java.util.UUID;

@Entity
@DiscriminatorValue("FIXED")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FixedDiscountCoupon extends DiscountCoupon{
    @Column(name = "fixed_discount_amount")
    @Min(value = 1000, message = "고정 할인금액은 1000원 이상이어야 합니다.")
    private int fixedDiscountAmount;

    public FixedDiscountCoupon(String couponCode, CouponApplyScope couponApplyScope, int fixedDiscountAmount) {
        super(couponCode, couponApplyScope);
        this.fixedDiscountAmount = fixedDiscountAmount;
    }

    public static FixedDiscountCoupon create(int fixedDiscountAmount, CouponApplyScope couponApplyScope){
        return new FixedDiscountCoupon(UUID.randomUUID().toString(), couponApplyScope, fixedDiscountAmount);
    }

    //테스트 데이터 생성용
    public static FixedDiscountCoupon createStub(int fixedDiscountAmount, CouponApplyScope couponApplyScope, String couponCode){
        return new FixedDiscountCoupon(couponCode, couponApplyScope, fixedDiscountAmount);
    }
}
