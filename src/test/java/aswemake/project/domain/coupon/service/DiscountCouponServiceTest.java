package aswemake.project.domain.coupon.service;

import aswemake.project.domain.coupon.entity.DiscountCoupon;
import aswemake.project.domain.coupon.entity.PercentageDiscountCoupon;
import aswemake.project.domain.coupon.entity.scopeType.CouponApplyScope;
import aswemake.project.domain.coupon.repository.DiscountCouponRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DiscountCouponServiceTest {
    @InjectMocks
    protected DiscountCouponService couponService;
    @Mock
    protected DiscountCouponRepository couponRepository;
    @Test
    @DisplayName("쿠폰 코드를 통해 쿠폰을 조회한다.")
    void findByCouponCode() {
        //given
        DiscountCoupon coupon = PercentageDiscountCoupon.create(50, CouponApplyScope.SPECIFIC_PRODUCT);
        given(couponRepository.findByCouponCode(coupon.getCouponCode())).willReturn(Optional.of(coupon));

        //when
        couponService.findByCouponCode(coupon.getCouponCode());

        //then
        verify(couponRepository).findByCouponCode(coupon.getCouponCode());
    }
}