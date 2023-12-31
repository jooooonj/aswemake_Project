package aswemake.project.domain.product.controller;

import aswemake.project.global.authentication.AuthenticationValidator;
import aswemake.project.domain.product.entity.request.CreateProductRequestDto;
import aswemake.project.domain.product.entity.request.ModifyProductPriceRequestDto;
import aswemake.project.domain.product.service.ProductService;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@Slf4j
class ProductControllerTest {
    @Autowired
    protected ProductController productController;
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    protected ProductService productService;
    @MockBean
    protected AuthenticationValidator authenticationValidator;

    @Test
    @DisplayName("상품 생성 API")
    @WithMockUser(username = "admin@naver.com")
    void create() throws Exception {
        //given
        CreateProductRequestDto requestDto = CreateProductRequestDto.builder()
                .productName("테스트 상품")
                .price(10000)
                .build();

        //when
        MvcResult result = mockMvc.perform(
                        post("/api/v1/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(requestDto))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();// 400 Bad Request

        //then
        verify(productService).register(ArgumentMatchers.any(CreateProductRequestDto.class));
    }

    @Test
    @DisplayName("상품 가격 수정 API")
    @WithMockUser(username = "admin@naver.com")
    void modify() throws Exception {
        //given
        Long productId = 1L;
        ModifyProductPriceRequestDto requestDto = ModifyProductPriceRequestDto.builder()
                .price(10000)
                .build();

        //when
        MvcResult result = mockMvc.perform(
                        patch("/api/v1/products/"+productId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(requestDto))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //then
        verify(productService).modify(eq(productId), ArgumentMatchers.any(ModifyProductPriceRequestDto.class));
    }

    @Test
    @DisplayName("상품 삭제 API")
    @WithMockUser(username = "admin@naver.com")
    void delete() throws Exception {
        //given
        Long productId = 1L;

        //when
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/products/"+productId))
                .andDo(print())
                .andExpect(status().isNoContent());

        //then
        verify(productService).delete(productId);
    }

    @Test
    @DisplayName("특정 시점의 상품 가격 조회 API")
    @WithMockUser(username = "admin@naver.com")
    void getProductPriceAtSpecificTime() throws Exception {
        //given
        Long productId = 1L;
        LocalDateTime timestamp = LocalDateTime.of(2022, 12, 25, 0, 0);

        //when
        ResultActions result = mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/products/price")
                                .contentType(MediaType.APPLICATION_JSON)
                .param("productId", String.valueOf(productId))
                .param("timestamp", timestamp.toString()))
                .andDo(print())
                .andExpect(status().isOk());

        //then
        verify(productService).getProductPriceAtSpecificTime(productId, timestamp);
    }
}