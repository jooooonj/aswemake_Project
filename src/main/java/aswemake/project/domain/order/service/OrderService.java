package aswemake.project.domain.order.service;

import aswemake.project.domain.coupon.entity.DiscountCoupon;
import aswemake.project.domain.coupon.entity.FixedDiscountCoupon;
import aswemake.project.domain.coupon.entity.PercentageDiscountCoupon;
import aswemake.project.domain.coupon.exception.CouponNotFoundException;
import aswemake.project.domain.coupon.service.DiscountCouponService;
import aswemake.project.domain.member.entity.Member;
import aswemake.project.domain.member.service.MemberService;
import aswemake.project.domain.order.entity.Order;
import aswemake.project.domain.order.entity.OrderItem;
import aswemake.project.domain.order.entity.request.CreateOrderRequestDto;
import aswemake.project.domain.order.entity.request.OrderItemRequestDto;
import aswemake.project.domain.order.exception.OrderNotFoundException;
import aswemake.project.domain.order.repository.OrderRepository;
import aswemake.project.domain.product.entity.Product;
import aswemake.project.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberService memberService;
    private final ProductService productService;
    private final DiscountCouponService discountCouponService;

    public Order findById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException("존재하지 않는 주문입니다.")
        );
    }

    public OrderItem findOrderItemByOrderItemId(Long orderItemId) {
        return orderRepository.findOrderItemByOrderItemId(orderItemId).orElseThrow(
                () -> new OrderNotFoundException("존재하지 않는 주문품목입니다.")
        );
    }

    @Transactional
    public Long createOrder(String username, CreateOrderRequestDto createOrderRequestDto) {
        Member member = memberService.findMember(username);
        Order order = orderRepository.save(
                Order.create(member, createOrderItems(createOrderRequestDto), createOrderRequestDto));
        return order.getId();
    }

    public Integer getOrderPrice(Long orderId) {
        Integer orderPrice = orderRepository.findOrderPriceByOrderId(orderId);
        if (orderPrice == null)
            throw new OrderNotFoundException("해당 주문은 존재하지 않습니다.");

        return orderPrice;
    }

    public Integer calculatePaymentPrice(Long orderId, Long orderItemId, String couponCode) {
        Order order = findById(orderId);
        if (couponCode == null)
            return order.getTotalPrice();

        if (orderItemId == null) //전체 주문에 쿠폰을 적용하는 경우
            return calculatePaymentWithCouponForAllOrder(couponCode, order);

        //특정 상품에 쿠폰을 적용하는 경우
        return calculatePaymentWithCouponForSpecificProduct(couponCode, order, orderItemId);
    }

    private List<OrderItem> createOrderItems(CreateOrderRequestDto createOrderRequestDto) {
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequestDto orderItemRequestDto : createOrderRequestDto.getOrderItemRequestDtoList()) {
            Product product = productService.findById(orderItemRequestDto.getProductId());
            OrderItem orderItem = OrderItem.create(product, orderItemRequestDto);
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    //쿠폰이 전체 주문에 적용된 가격을 계산한다.
    private Integer calculatePaymentWithCouponForAllOrder(String couponCode, Order order) {
        DiscountCoupon coupon = discountCouponService.findByCouponCode(couponCode);

        if (coupon instanceof FixedDiscountCoupon)
            return order.getTotalPrice() - ((FixedDiscountCoupon) coupon).getFixedDiscountAmount();

        if (coupon instanceof PercentageDiscountCoupon)
            return order.getTotalPrice() - (order.getTotalPriceWithoutDeliveryFee()) * ((PercentageDiscountCoupon) coupon).getDiscountPercentage() / 100;

        throw new CouponNotFoundException("존재하지 않는 쿠폰 유형입니다.");
    }

    //쿠폰이 특정 상품에 적용된 가격을 계산한다.
    private Integer calculatePaymentWithCouponForSpecificProduct(String couponCode, Order order, Long orderItemId) {
        OrderItem orderItem = findOrderItemByOrderItemId(orderItemId);
        if(orderItem.getOrder().getId() != order.getId())
            throw new IllegalArgumentException("해당 주문 품목은 " + order.getId() + "번 주문에 속한 주문이 아닙니다.");

        DiscountCoupon coupon = discountCouponService.findByCouponCode(couponCode);

        if (coupon instanceof FixedDiscountCoupon)
            return order.getTotalPrice() - ((FixedDiscountCoupon) coupon).getFixedDiscountAmount();

        if (coupon instanceof PercentageDiscountCoupon)
            return order.getTotalPrice() - (orderItem.getOrderItemPrice() * ((PercentageDiscountCoupon) coupon).getDiscountPercentage() / 100);

        throw new CouponNotFoundException("존재하지 않는 쿠폰 유형입니다.");
    }
}