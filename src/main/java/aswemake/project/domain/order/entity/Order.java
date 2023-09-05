package aswemake.project.domain.order.entity;

import aswemake.project.base.baseEntity.BaseEntity;
import aswemake.project.domain.member.entity.Member;
import aswemake.project.domain.order.entity.request.CreateOrderRequestDto;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(name = "delivery_fee", nullable = false)
    private int deliveryFee;

    @Column(name = "total_price", nullable = false)
    private int totalPrice;
}