package aswemake.project.domain.order.service;

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

    @Transactional
    public Long createOrder(String username, CreateOrderRequestDto createOrderRequestDto) {
        Member member = memberService.findMember(username);
        Order order = orderRepository.save(
                Order.create(member, createOrderItems(createOrderRequestDto), createOrderRequestDto));
        return order.getId();
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
}
