package aswemake.project.domain.order.repository;

import aswemake.project.domain.member.entity.Member;
import aswemake.project.domain.member.repository.MemberRepository;
import aswemake.project.domain.order.entity.Order;
import aswemake.project.domain.order.entity.OrderItem;
import aswemake.project.domain.product.entity.ProductSnapshot;
import aswemake.project.domain.product.repository.ProductSnapshotRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
class OrderRepositoryTest {
    @Autowired
    protected OrderRepository orderRepository;
    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected ProductSnapshotRepository productSnapshotRepository;

    @Test
    @DisplayName("findOrderPriceByOrderId - 특정 주문에 대한 총금액을 조회한다.")
    void findOrderPriceByOrderId() {
        //given
        Member member = memberRepository.save(Member.builder().email("member@naver.com").password("1234").build());

        Order order = Order.builder()
                .member(member)
                .deliveryFee(5000)
                .totalPrice(10000)
                .build();
        Order savedorder = orderRepository.save(order);

        //when
        Integer orderPriceByOrderId = orderRepository.findOrderPriceByOrderId(savedorder.getId());

        //then
        Assertions.assertThat(orderPriceByOrderId).isEqualTo(order.getTotalPrice());
    }

    @Test
    @DisplayName("findOrderItemByOrderItemId - orderItemId 값에 대한 orderItem을 반환한다.")
    void findOrderItemByOrderItemId() {
        //given
        int quantity = 5;
        Long orderItemId = 1L;
        Member member = memberRepository.save(Member.builder().email("member@naver.com").password("1234").build());
        ProductSnapshot product = productSnapshotRepository.save(ProductSnapshot.builder()
                .id(1L)
                .productName("테스트 상품")
                .price(10000)
                .productId(1L)
                .fromDate(LocalDateTime.now())
                .toDate(LocalDateTime.now().plusDays(1))
                .build());
        OrderItem orderItem = OrderItem.builder()
                .product(product)
                .quantity(5)
                .orderItemPrice(product.getPrice() * 5)
                .build();
        Order order = Order.builder()
                .member(member)
                .deliveryFee(5000)
                .totalPrice(10000)
                .build();
        order.connectOrderItems(List.of(orderItem));
        Order savedorder = orderRepository.save(order);

        //when
        OrderItem findOrderItem = orderRepository.findOrderItemByOrderItemId(orderItemId).get();

        //then
        Assertions.assertThat(findOrderItem.getQuantity()).isEqualTo(quantity);
        Assertions.assertThat(findOrderItem.getOrderItemPrice()).isEqualTo(product.getPrice() * quantity);
        Assertions.assertThat(findOrderItem.getOrder().getId()).isEqualTo(savedorder.getId());
    }
}