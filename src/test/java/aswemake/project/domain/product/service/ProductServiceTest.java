package aswemake.project.domain.product.service;

import aswemake.project.domain.product.entity.Product;
import aswemake.project.domain.product.entity.request.CreateProductRequestDto;
import aswemake.project.domain.product.entity.request.ModifyProductPriceRequestDto;
import aswemake.project.domain.product.entity.response.ProductResponseDto;
import aswemake.project.domain.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Slf4j
class ProductServiceTest {
    @InjectMocks
    protected ProductService productService;

    @Mock
    protected ProductRepository productRepository;

    @Test
    @DisplayName("register(상품 등록)")
    void register(){
        //given
        CreateProductRequestDto requestDto = CreateProductRequestDto.builder()
                .productName("테스트 상품")
                .price(10000)
                .build();
        Product product = Product.create(requestDto);
        given(productRepository.save(any(Product.class))).willReturn(product);

        //when
        ProductResponseDto productResponse = productService.register(requestDto);

        //then
        Assertions.assertThat(productResponse.getProductName()).isEqualTo(requestDto.getProductName());
        log.info("생성된 상품명 : {} " , productResponse.getProductName());
        Assertions.assertThat(productResponse.getPrice()).isEqualTo(requestDto.getPrice());
        log.info("생성된 상품가격 : {} " , productResponse.getPrice());
    }


    @Test
    @DisplayName("modify(상품 가격 수정)")
    void modify(){
        //given
        Long productId = 1L;
        CreateProductRequestDto createProductRequestDto = CreateProductRequestDto.builder()
                .productName("테스트 상품")
                .price(10000)
                .build();
        Product product = Product.create(createProductRequestDto);

        Assertions.assertThat(product.getPrice()).isEqualTo(createProductRequestDto.getPrice());
        log.info("기존 상품 가격 : {} ", product.getPrice());

        ModifyProductPriceRequestDto modifyProductPriceRequestDto = ModifyProductPriceRequestDto.builder()
                .price(20000)
                .build();

        given(productRepository.findById(eq(productId))).willReturn(Optional.of(product));

        //when
        ProductResponseDto productResponse = productService.modify(productId, modifyProductPriceRequestDto);

        //then
        Assertions.assertThat(productResponse.getPrice()).isEqualTo(modifyProductPriceRequestDto.getPrice());
        log.info("수정 요청 가격 : {} " , modifyProductPriceRequestDto.getPrice());
        log.info("수정된 상품 가격 : {} " , productResponse.getPrice());
    }

    @Test
    @DisplayName("delete(상품 삭제)")
    void delete(){
        //given
        Long productId = 1L;

        //when
        productService.delete(productId);

        //then
        verify(productRepository).deleteById(eq(productId));
        log.info("{}번 상품 삭제 완료", productId);
    }
}