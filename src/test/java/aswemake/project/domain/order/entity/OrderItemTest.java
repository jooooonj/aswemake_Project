package aswemake.project.domain.order.entity;

import aswemake.project.domain.order.entity.request.OrderItemRequestDto;
import aswemake.project.domain.product.entity.Product;
import aswemake.project.domain.product.entity.ProductSnapshot;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
class OrderItemTest {
    @Test
    @DisplayName("주문 목록의 금액을 계산한다.")
    void getOrderItemPrice() {
        //given
        ProductSnapshot product = ProductSnapshot.builder().price(5000).build();
        OrderItem orderItem = OrderItem
                .builder()
                .product(product)
                .quantity(5)
                .orderItemPrice(product.getPrice() * 5)
                .build();

        //when
        int orderItemPrice = orderItem.getOrderItemPrice();

        //then
        Assertions.assertThat(orderItemPrice).isEqualTo(product.getPrice() * orderItem.getQuantity());
        Assertions.assertThat(orderItemPrice).isEqualTo(25000);
    }

    @Test
    @DisplayName("주문 목록을 생성한다.")
    void create() {
        //given
        ProductSnapshot productSnapshot = ProductSnapshot.builder().price(5000).build();
        Product product = Product.builder().productName("테스트 상품").productSnapshot(productSnapshot).price(5000).build();
        OrderItemRequestDto orderItemRequestDto = new OrderItemRequestDto(1L, 5);

        //when
        OrderItem orderItem = OrderItem.create(product, orderItemRequestDto);

        //then
        Assertions.assertThat(orderItem.getProduct()).isEqualTo(productSnapshot);
        Assertions.assertThat(orderItem.getQuantity()).isEqualTo(orderItemRequestDto.getQuantity());
    }

    @Test
    @DisplayName("주문과 연결한다.")
    void connectOrder() {
        //given
        Order order = Order.builder().id(1L).build();
        OrderItem orderItem = OrderItem.builder().build();

        //when
        orderItem.connectOrder(order);

        //then
        Assertions.assertThat(orderItem.getOrder()).isEqualTo(order);
    }
}