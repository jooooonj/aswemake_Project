package aswemake.project.domain.coupon.repository;

import aswemake.project.domain.coupon.entity.DiscountCoupon;
import aswemake.project.domain.coupon.entity.PercentageDiscountCoupon;
import aswemake.project.domain.coupon.entity.scopeType.CouponApplyScope;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
class DiscountCouponRepositoryTest {
    @Autowired
    protected DiscountCouponRepository couponRepository;

    @Test
    @DisplayName("쿠폰 코드가 일치하는 쿠폰이 있으면 조회가 가능하다.")
    void findByCouponCode() {
        //given
        String couponCode = "테스트쿠폰";
        int discountPercent = 10;
        CouponApplyScope couponApplyScope = CouponApplyScope.SPECIFIC_PRODUCT;

        PercentageDiscountCoupon coupon = couponRepository.save(
                PercentageDiscountCoupon.createStub(discountPercent, couponApplyScope, couponCode));

        //when
        Optional<DiscountCoupon> _findCoupon = couponRepository.findByCouponCode(couponCode);

        //then
        Assertions.assertThat(_findCoupon).isNotNull();
        PercentageDiscountCoupon findCoupon = (PercentageDiscountCoupon) _findCoupon.get();
        Assertions.assertThat(findCoupon.getDiscountPercentage()).isEqualTo(discountPercent);
        Assertions.assertThat(findCoupon.getCouponApplyScope().getValue()).isEqualTo(couponApplyScope.getValue());
        Assertions.assertThat(findCoupon.getCouponCode()).isEqualTo(couponCode);
    }
}