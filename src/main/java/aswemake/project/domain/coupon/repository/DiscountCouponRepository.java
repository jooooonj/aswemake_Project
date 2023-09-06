package aswemake.project.domain.coupon.repository;

import aswemake.project.domain.coupon.entity.DiscountCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DiscountCouponRepository extends JpaRepository<DiscountCoupon, Long> {
    Optional<DiscountCoupon> findByCouponCode(String couponCode);

}
