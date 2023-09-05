package aswemake.project.domain.order.entity;

import aswemake.project.domain.order.entity.request.OrderItemRequestDto;
import aswemake.project.domain.product.entity.Product;
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
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    public int getOrderItemPrice(){
        return quantity * product.getPrice();
    }

    public static OrderItem create(Product product, OrderItemRequestDto orderItemRequestDto){
        return OrderItem
                .builder()
                .product(product)
                .quantity(orderItemRequestDto.getQuantity())
                .build();
    }

    public void connectOrder(Order order){
        this.order = order;
    }
}
