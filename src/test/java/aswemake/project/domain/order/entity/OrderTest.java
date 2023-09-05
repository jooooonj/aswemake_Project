package aswemake.project.domain.order.entity;

import aswemake.project.domain.member.entity.Member;
import aswemake.project.domain.member.entity.MemberRoleType;
import aswemake.project.domain.order.entity.request.CreateOrderRequestDto;
import aswemake.project.domain.order.entity.request.OrderItemRequestDto;
import aswemake.project.domain.product.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
@Slf4j
class OrderTest {
    @Test
    @DisplayName("올바른 파라미터를 받아서 주문 객체가 생성된다.")
    void create() {
        //given
        Product product = Product.builder()
                .id(1L)
                .productName("테스트 상품")
                .price(10000)
                .build();

        //param 1 (Member)
        Member member = Member.builder()
                .id(1L)
                .email("member1@naver.com")
                .roleType(MemberRoleType.MEMBER)
                .build();

        //param 2 (CreateOrderRequestDto) -> 1번 상품 5개, 배송료 3000원
        List<OrderItemRequestDto> orderItemRequestDtoList = List.of(new OrderItemRequestDto(1L, 5));
        CreateOrderRequestDto createOrderRequestDto = new CreateOrderRequestDto(orderItemRequestDtoList, 3000);

        //param 3 (List<OrderItem>)
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequestDto orderItemRequestDto : createOrderRequestDto.getOrderItemRequestDtoList()) {
            OrderItem orderItem = OrderItem.create(product, orderItemRequestDto);
            orderItems.add(orderItem);
        }

        //when
        Order order = Order.create(member, orderItems, createOrderRequestDto);

        //then
        Assertions.assertThat(order.getDeliveryFee()).isEqualTo(createOrderRequestDto.getDeliveryFee());
        Assertions.assertThat(order.getMember()).isEqualTo(member);
        Assertions.assertThat(order.getOrderItems()).isEqualTo(orderItems);

        //10000 * 5 + 3000
        Assertions.assertThat(order.getTotalPrice()).isEqualTo(53000);
    }
}