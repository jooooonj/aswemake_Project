package aswemake.project.domain.order.service;

import aswemake.project.domain.coupon.entity.FixedDiscountCoupon;
import aswemake.project.domain.coupon.entity.PercentageDiscountCoupon;
import aswemake.project.domain.coupon.entity.scopeType.CouponApplyScope;
import aswemake.project.domain.coupon.service.DiscountCouponService;
import aswemake.project.domain.member.entity.Member;
import aswemake.project.domain.member.entity.MemberRoleType;
import aswemake.project.domain.member.service.MemberService;
import aswemake.project.domain.order.entity.Order;
import aswemake.project.domain.order.entity.OrderItem;
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
import java.util.Optional;
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
    @Mock
    protected DiscountCouponService couponService;

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

    @Test
    @DisplayName("결제금액을 조회한다. 쿠폰 코드가 null값이면 단순 주문의 총금액을 반환한다.")
    void calculatePaymentPrice1() {
        //given
        Long orderId = 1L;
        Order order = Order.builder()
                .totalPrice(50000) // 주문 총금액 5만원
                .build();
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        //when
        Integer calculatePaymentPrice = orderService.calculatePaymentPrice(orderId, null, null);

        //then
        Assertions.assertThat(calculatePaymentPrice).isEqualTo(order.getTotalPrice());
    }

    @Test
    @DisplayName("결제금액을 조회한다. (전체 주문에 대해서 고정할인 쿠폰을 적용한다.)")
    void calculatePaymentPrice2() {
        //given
        Long orderId = 1L;
        String couponCode = "2만원_고정할인_전체주문";
        int orderTotalPrice = 50000;
        int discountAmount = 20000;

        Order order = Order.builder()
                .totalPrice(orderTotalPrice) // 주문 총금액 5만원
                .build();
        FixedDiscountCoupon fixedDiscountCoupon = FixedDiscountCoupon.createStub
                (discountAmount, CouponApplyScope.ORDER_ALL, couponCode);

        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));
        given(couponService.findByCouponCode(anyString())).willReturn(fixedDiscountCoupon);

        //when
        Integer calculatePaymentPrice = orderService.calculatePaymentPrice(orderId, null, couponCode);

        //then
        log.info("실제 작동 로직 - {} ", "order.getTotalPrice() - ((FixedDiscountCoupon) coupon).getFixedDiscountAmount()");
        Assertions.assertThat(calculatePaymentPrice).isEqualTo(order.getTotalPrice() - fixedDiscountCoupon.getFixedDiscountAmount());
        Assertions.assertThat(calculatePaymentPrice).isEqualTo(orderTotalPrice - discountAmount);
        Assertions.assertThat(calculatePaymentPrice).isEqualTo(50000 - 20000);
    }

    @Test
    @DisplayName("결제금액을 조회한다. (특정 상품에 대해서 고정할인 쿠폰을 적용한다.)")
    void calculatePaymentPrice3() {
        //given
        Long orderId = 1L;
        String couponCode = "2만원_고정할인_특정상품";
        Long orderItemId = 1L;
        int orderTotalPrice = 50000;
        int discountAmount = 20000;

        Order order = Order.builder()
                .id(1L)
                .totalPrice(orderTotalPrice) // 주문 총금액 5만원
                .build();
        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .orderItemPrice(30000)
                .build(); //주문품목 금액 3만원

        FixedDiscountCoupon fixedDiscountCoupon = FixedDiscountCoupon.createStub
                (discountAmount, CouponApplyScope.ORDER_ALL, couponCode);

        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));
        given(couponService.findByCouponCode(anyString())).willReturn(fixedDiscountCoupon);
        given(orderRepository.findOrderItemByOrderItemId(anyLong())).willReturn(Optional.of(orderItem));

        //when
        Integer calculatePaymentPrice = orderService.calculatePaymentPrice(orderId, orderItemId, couponCode);

        //then
        log.info("실제 작동 로직 - {} ", "order.getTotalPrice() - ((FixedDiscountCoupon) coupon).getFixedDiscountAmount()");
        Assertions.assertThat(calculatePaymentPrice).isEqualTo(order.getTotalPrice() - fixedDiscountCoupon.getFixedDiscountAmount());
        Assertions.assertThat(calculatePaymentPrice).isEqualTo(orderTotalPrice - discountAmount);
        Assertions.assertThat(calculatePaymentPrice).isEqualTo(50000 - 20000);
    }

    @Test
    @DisplayName("결제금액을 조회한다. (주문품목이 주문이 가지고 있는 품목이 아니면 예외발생)")
    void calculatePaymentPrice_exception() {
        //given
        Long orderId = 1L;
        String couponCode = "2만원_고정할인_특정상품";
        Long orderItemId = 1L;
        int orderTotalPrice = 50000;
        int discountAmount = 20000;

        Order order = Order.builder()
                .id(1L)
                .totalPrice(orderTotalPrice) // 주문 총금액 5만원
                .build();
        Order order_fail = Order.builder()
                .id(2L)
                .totalPrice(orderTotalPrice) // 주문 총금액 5만원
                .build();

        OrderItem orderItem = OrderItem.builder()
                .order(order_fail)
                .orderItemPrice(30000)
                .build(); //주문품목 금액 3만원

        FixedDiscountCoupon fixedDiscountCoupon = FixedDiscountCoupon.createStub
                (discountAmount, CouponApplyScope.SPECIFIC_PRODUCT, couponCode);

        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));
        given(orderRepository.findOrderItemByOrderItemId(anyLong())).willReturn(Optional.of(orderItem));

        //when, then
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.calculatePaymentPrice(orderId, orderItemId, couponCode);
        });

        Assertions.assertThat(exception.getMessage()).isEqualTo("해당 주문 품목은 " + order.getId() + "번 주문에 속한 주문이 아닙니다.");
    }

    @Test
    @DisplayName("결제금액을 조회한다. (전체 주문에 대해서 비율할인 쿠폰을 적용한다.)")
    void calculatePaymentPrice4() {
        //given
        Long orderId = 1L;
        String couponCode = "10퍼센트_비율할인_전체주문";
        int orderTotalPrice = 50000;
        int discountPercent = 10;

        Order order = Order.builder()
                .id(1L)
                .deliveryFee(5000)
                .totalPrice(orderTotalPrice) // 주문 총금액 5만원
                .build();

        PercentageDiscountCoupon percentageDiscountCoupon = PercentageDiscountCoupon.createStub
                (discountPercent, CouponApplyScope.ORDER_ALL, couponCode);

        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));
        given(couponService.findByCouponCode(anyString())).willReturn(percentageDiscountCoupon);

        //when
        Integer calculatePaymentPrice = orderService.calculatePaymentPrice(orderId, null, couponCode);

        //then
        log.info("실제 작동 로직 - {} ", "order.getTotalPrice() - (order.getTotalPriceWithoutDeliveryFee()) * ((PercentageDiscountCoupon) coupon).getDiscountPercentage() / 100");
        Assertions.assertThat(calculatePaymentPrice).isEqualTo(order.getTotalPrice() - (order.getTotalPriceWithoutDeliveryFee()) * percentageDiscountCoupon.getDiscountPercentage() / 100);
        Assertions.assertThat(calculatePaymentPrice).isEqualTo(50000 - (50000 - 5000) * 10 / 100);
    }

    @Test
    @DisplayName("결제금액을 조회한다. (특정 상품에 대해서 비율할인 쿠폰을 적용한다.)")
    void calculatePaymentPrice5() {
        //given
        Long orderId = 1L;
        String couponCode = "10퍼센트_비율할인_전체주문";
        Long orderItemId = 1L;
        int orderTotalPrice = 50000;
        int discountPercent = 10;

        Order order = Order.builder()
                .id(1L)
                .deliveryFee(5000)
                .totalPrice(orderTotalPrice) // 주문 총금액 5만원
                .build();
        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .orderItemPrice(30000)
                .build();

        PercentageDiscountCoupon percentageDiscountCoupon = PercentageDiscountCoupon.createStub
                (discountPercent, CouponApplyScope.SPECIFIC_PRODUCT, couponCode);

        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));
        given(couponService.findByCouponCode(anyString())).willReturn(percentageDiscountCoupon);
        given(orderRepository.findOrderItemByOrderItemId(anyLong())).willReturn(Optional.of(orderItem));

        //when
        Integer calculatePaymentPrice = orderService.calculatePaymentPrice(orderId, orderItemId, couponCode);

        //then
        log.info("실제 작동 로직 - {} ", "order.getTotalPrice() - (orderItem.getOrderItemPrice() * ((PercentageDiscountCoupon) coupon).getDiscountPercentage() / 100)");
        Assertions.assertThat(calculatePaymentPrice).isEqualTo(order.getTotalPrice() - (orderItem.getOrderItemPrice() * percentageDiscountCoupon.getDiscountPercentage() / 100));
        Assertions.assertThat(calculatePaymentPrice).isEqualTo(50000 - (30000 * 10 / 100));
    }
}