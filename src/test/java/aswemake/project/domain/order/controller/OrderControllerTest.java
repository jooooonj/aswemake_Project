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
}