package aswemake.project.domain.coupon.service;

import aswemake.project.domain.coupon.entity.DiscountCoupon;
import aswemake.project.domain.coupon.exception.CouponNotFoundException;
import aswemake.project.domain.coupon.repository.DiscountCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiscountCouponService {
    private final DiscountCouponRepository discountCouponRepository;

    public DiscountCoupon findByCouponCode(String couponCode) {
        return discountCouponRepository.findByCouponCode(couponCode).orElseThrow(
                () -> new CouponNotFoundException("존재하지 않는 쿠폰 코드입니다.")
        );
    }
}
