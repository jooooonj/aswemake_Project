package aswemake.project.domain.order.entity;

import aswemake.project.domain.coupon.entity.DiscountCoupon;
import aswemake.project.domain.order.entity.request.OrderItemRequestDto;
import aswemake.project.domain.product.entity.Product;
import aswemake.project.domain.product.entity.ProductSnapshot;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductSnapshot product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "order_item_price", nullable = false)
    private int orderItemPrice;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private DiscountCoupon coupon; //결제 완료 후 적용

    public static OrderItem create(Product product, OrderItemRequestDto orderItemRequestDto){
        return OrderItem
                .builder()
                .product(product.getProductSnapshot())
                .quantity(orderItemRequestDto.getQuantity())
                .orderItemPrice(product.getPrice() * orderItemRequestDto.getQuantity())
                .build();
    }

    public void connectOrder(Order order){
        this.order = order;
    }
}
