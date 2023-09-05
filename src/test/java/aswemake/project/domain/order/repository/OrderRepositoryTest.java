package aswemake.project.domain.order.repository;

import aswemake.project.domain.member.entity.Member;
import aswemake.project.domain.member.repository.MemberRepository;
import aswemake.project.domain.order.entity.Order;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OrderRepositoryTest {
    @Autowired
    protected OrderRepository orderRepository;
    @Autowired
    protected MemberRepository memberRepository;
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
}