package aswemake.project.domain.order.controller;

import aswemake.project.domain.order.entity.request.CreateOrderRequestDto;
import aswemake.project.domain.order.entity.request.OrderItemRequestDto;
import aswemake.project.domain.order.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@Slf4j
class OrderControllerTest {
    @Autowired
    protected OrderController orderController;
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    protected OrderService orderService;

    @Test
    @DisplayName("주문 생성 API")
    @WithMockUser(username = "member@naver.com")
    void create() throws Exception {
        //given
        List<OrderItemRequestDto> orderItemRequestDtoList =
                List.of(new OrderItemRequestDto(1L, 5), new OrderItemRequestDto(2L, 2));
        CreateOrderRequestDto createOrderRequestDto = new CreateOrderRequestDto(orderItemRequestDtoList, 3000);


        //when
        ResultActions resultAction = mockMvc.perform(
                        post("/api/v1/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(createOrderRequestDto))
                )
                .andExpect(status().isCreated());

        //then
        resultAction.andDo(print());
        verify(orderService).createOrder(anyString(), ArgumentMatchers.any(CreateOrderRequestDto.class));
    }

    @Test
    @DisplayName("주문 금액 요청 API")
    @WithMockUser(username = "member@naver.com")
    void getOrderPrice() throws Exception {
        //given
        Long orderId = 1L;

        //when
        ResultActions resultAction = mockMvc.perform(
                        get("/api/v1/orders/" + orderId +"/price"))
                .andExpect(status().isOk());

        //then
        resultAction.andDo(print());
        verify(orderService).getOrderPrice(orderId);
    }

    @Test
    @DisplayName("결제금액 조회 API (쿠폰 X)")
    @WithMockUser(username = "member@naver.com")
    void calculatePaymentPrice_NOT_COUPON() throws Exception {
        //given
        Long orderId = 1L;

        //when
        ResultActions resultAction = mockMvc.perform(
                        post("/api/v1/orders/" + orderId +"/calculate-paymentPrice"))
                .andExpect(status().isOk());

        //then
        resultAction.andDo(print());
        verify(orderService).calculatePaymentPrice(orderId, null, null);
    }

    @Test
    @DisplayName("결제금액 조회 API (쿠폰 - 2만원_고정할인_전체주문)")
    @WithMockUser(username = "member@naver.com")
    void calculatePaymentPrice_FIXED_COUPON1() throws Exception {
        //given
        Long orderId = 1L;
        String couponCode = "2만원_고정할인_전체주문";

        //when
        ResultActions resultAction = mockMvc.perform(
                        post("/api/v1/orders/" + orderId +"/calculate-paymentPrice")
                                .param("couponCode", couponCode))
                .andExpect(status().isOk());

        //then
        resultAction.andDo(print());
        verify(orderService).calculatePaymentPrice(orderId, null, couponCode);
    }

    @Test
    @DisplayName("결제금액 조회 API (쿠폰 - 2만원_고정할인_특정상품)")
    @WithMockUser(username = "member@naver.com")
    void calculatePaymentPrice_FIXED_COUPON2() throws Exception {
        //given
        Long orderId = 1L;
        String couponCode = "2만원_고정할인_전체주문";
        Long orderItemId = 1L;

        //when
        ResultActions resultAction = mockMvc.perform(
                        post("/api/v1/orders/" + orderId +"/calculate-paymentPrice")
                                .param("couponCode", couponCode)
                                .param("orderItemId", String.valueOf(orderItemId)))
                .andExpect(status().isOk());

        //then
        resultAction.andDo(print());
        verify(orderService).calculatePaymentPrice(orderId, orderItemId, couponCode);
    }

    @Test
    @DisplayName("결제금액 조회 API (쿠폰 - 10퍼센트_비율할인_전체주문)")
    @WithMockUser(username = "member@naver.com")
    void calculatePaymentPrice_PERCENT_COUPON1() throws Exception {
        //given
        Long orderId = 1L;
        String couponCode = "10퍼센트_비율할인_전체주문";

        //when
        ResultActions resultAction = mockMvc.perform(
                        post("/api/v1/orders/" + orderId +"/calculate-paymentPrice")
                                .param("couponCode", couponCode))
                .andExpect(status().isOk());

        //then
        resultAction.andDo(print());
        verify(orderService).calculatePaymentPrice(orderId, null, couponCode);
    }

    @Test
    @DisplayName("결제금액 조회 API (쿠폰 - 10퍼센트_비율할인_특정상품)")
    @WithMockUser(username = "member@naver.com")
    void calculatePaymentPrice_PERCENT_COUPON2() throws Exception {
        //given
        Long orderId = 1L;
        String couponCode = "10퍼센트_비율할인_특정상품";
        Long orderItemId = 1L;

        //when
        ResultActions resultAction = mockMvc.perform(
                        post("/api/v1/orders/" + orderId +"/calculate-paymentPrice")
                                .param("couponCode", couponCode)
                                .param("orderItemId", String.valueOf(orderItemId)))
                .andExpect(status().isOk());

        //then
        resultAction.andDo(print());
        verify(orderService).calculatePaymentPrice(orderId, orderItemId, couponCode);
    }
}