package aswemake.project.domain.coupon.entity;

import aswemake.project.domain.coupon.entity.scopeType.CouponApplyScope;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import java.util.UUID;

@Entity
@DiscriminatorValue("PERCENTAGE")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PercentageDiscountCoupon extends DiscountCoupon{
    @Column(name = "discount_percentage")
    @Min(value = 1, message = "할인율은 1% 이상이어야 합니다.")
    @Max(value = 99, message = "할인율은 99% 이하이어야 합니다.")
    private int discountPercentage;

    public PercentageDiscountCoupon(String couponCode, CouponApplyScope couponApplyScope, int discountPercentage) {
        super(couponCode, couponApplyScope);
        this.discountPercentage = discountPercentage;
    }

    public static PercentageDiscountCoupon create(int discountPercentage, CouponApplyScope couponApplyScope){
        return new PercentageDiscountCoupon(UUID.randomUUID().toString(), couponApplyScope, discountPercentage);
    }

    //테스트 데이터 생성용
    public static PercentageDiscountCoupon createStub(int discountPercentage, CouponApplyScope couponApplyScope, String couponCode){
        return new PercentageDiscountCoupon(couponCode, couponApplyScope, discountPercentage);
    }
}
