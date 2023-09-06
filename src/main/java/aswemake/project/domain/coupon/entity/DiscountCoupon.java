package aswemake.project.domain.coupon.entity;

import aswemake.project.base.baseEntity.BaseEntity;
import aswemake.project.domain.coupon.entity.scopeType.CouponApplyScope;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "coupon_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@NoArgsConstructor
public abstract class DiscountCoupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    @Column(name = "coupon_code", nullable = false)
    private String couponCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "coupon_apply_scope", nullable = false)
    private CouponApplyScope couponApplyScope;

    public DiscountCoupon(String couponCode, CouponApplyScope couponApplyScope) {
        this.couponCode = couponCode;
        this.couponApplyScope = couponApplyScope;
    }
}
