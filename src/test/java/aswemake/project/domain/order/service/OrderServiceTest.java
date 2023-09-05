package aswemake.project.domain.order.service;

import aswemake.project.domain.member.entity.Member;
import aswemake.project.domain.member.entity.MemberRoleType;
import aswemake.project.domain.member.service.MemberService;
import aswemake.project.domain.order.entity.Order;
import aswemake.project.domain.order.entity.request.CreateOrderRequestDto;
import aswemake.project.domain.order.entity.request.OrderItemRequestDto;
import aswemake.project.domain.order.exception.OrderNotFoundException;
import aswemake.project.domain.order.repository.OrderRepository;
import aswemake.project.domain.product.entity.Product;
import aswemake.project.domain.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Slf4j
class OrderServiceTest {
    @InjectMocks
    protected OrderService orderService;
    @Mock
    protected OrderRepository orderRepository;
    @Mock
    protected ProductService productService;
    @Mock
    protected MemberService memberService;

    @Test
    @DisplayName("주문을 생성한다.")
    void createOrder() {
        //given
        Member member = Member.builder()
                .email("member1@naver.com")
                .roleType(MemberRoleType.MEMBER)
                .build();
        Product product = Product.builder()
                .productName("테스트 상품")
                .price(10000)
                .build();

        List<OrderItemRequestDto> orderItemRequestDtoList =
                List.of(new OrderItemRequestDto(1L, 5), new OrderItemRequestDto(2L, 2));
        CreateOrderRequestDto createOrderRequestDto = new CreateOrderRequestDto(orderItemRequestDtoList, 3000);

        Order order = Order.builder()
                .id(1L)
                .build();

        given(memberService.findMember(anyString())).willReturn(member);
        given(productService.findById(anyLong())).willReturn(product);
        given(orderRepository.save(any(Order.class))).willReturn(order);

        //when
        Long orderId = orderService.createOrder(member.getEmail(), createOrderRequestDto);

        //then
        verify(orderRepository).save(any(Order.class));
        Assertions.assertThat(orderId).isEqualTo(order.getId());
    }

    @Test
    @DisplayName("주문 금액을 가져온다. (성공)")
    void getOrderPrice_success() {
        //given
        Long orderId = 1L;

        //when
        orderService.getOrderPrice(orderId);

        //then
        verify(orderRepository).findOrderPriceByOrderId(orderId);
    }

    @Test
    @DisplayName("주문 금액을 가져온다. (실패)")
    void getOrderPrice_fail() {
        //given
        Long orderId = 1L;
        given(orderRepository.findOrderPriceByOrderId(anyLong())).willReturn(null);

        //when, then
        Throwable exception = assertThrows(OrderNotFoundException.class, () -> {
            orderService.getOrderPrice(orderId);
        });

        Assertions.assertThat(exception.getMessage()).isEqualTo("해당 주문은 존재하지 않습니다.");
    }
}