package aswemake.project.domain.product.service;

import aswemake.project.domain.product.entity.Product;
import aswemake.project.domain.product.entity.ProductSnapshot;
import aswemake.project.domain.product.entity.request.CreateProductRequestDto;
import aswemake.project.domain.product.entity.request.ModifyProductPriceRequestDto;
import aswemake.project.domain.product.entity.response.ProductResponseDto;
import aswemake.project.domain.product.repository.ProductRepository;
import aswemake.project.domain.product.repository.ProductSnapshotRepository;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Slf4j
class ProductServiceTest {
    @InjectMocks
    protected ProductService productService;
    @Mock
    protected ProductRepository productRepository;
    @Mock
    protected ProductSnapshotRepository productSnapshotRepository;

    @Test
    @DisplayName("상품 등록시 상품 스냅샷이 생성되고, 상품 정보를 반환한다.")
    void register(){
        //given
        CreateProductRequestDto requestDto = CreateProductRequestDto.builder()
                .productName("테스트 상품")
                .price(10000)
                .build();
        Product product = Product.create(requestDto);
        ProductSnapshot productSnapshot = ProductSnapshot.create(product);

        given(productRepository.save(any(Product.class))).willReturn(product);
        given(productSnapshotRepository.save(any(ProductSnapshot.class))).willReturn(productSnapshot);

        //when
        ProductResponseDto productResponse = productService.register(requestDto);

        //then
        log.info("상품 등록 성공시 상품 정보를 가진 상품 스냅샷이 생성된다.");
        Assertions.assertThat(product.getProductSnapshot().getProductName()).isEqualTo(product.getProductName());

        log.info("상품 등록 성공시 상품 정보를 담은 값을 반환한다.");
        Assertions.assertThat(productResponse.getProductName()).isEqualTo(requestDto.getProductName());
        log.info("return/상품명 : {} " , productResponse.getProductName());
        Assertions.assertThat(productResponse.getPrice()).isEqualTo(requestDto.getPrice());
        log.info("return/상품가격 : {} " , productResponse.getPrice());
    }

    @Test
    @DisplayName("상품 가격 수정 성공시 기존 스냅샷의 기간이 만료되고 새로운 스냅샷이 생성된다.")
    void modify(){
        //given
        Long productId = 1L;
        CreateProductRequestDto createProductRequestDto = CreateProductRequestDto.builder()
                .productName("테스트 상품")
                .price(10000)
                .build();
        Product product = Product.create(createProductRequestDto); //가격 수정 전 상품
        ProductSnapshot oldProductSnapshot = ProductSnapshot.create(product); //기존 스냅샷
        product.exchangeSnapshot(oldProductSnapshot);

        Assertions.assertThat(product.getPrice()).isEqualTo(createProductRequestDto.getPrice());
        log.info("수정 전 상품 가격 : {} ", product.getPrice());
        log.info("수정 전 스냅샷 만료기간 : {} ", product.getProductSnapshot().getToDate());

        ModifyProductPriceRequestDto modifyProductPriceRequestDto = ModifyProductPriceRequestDto.builder()
                .price(20000)
                .build();

        given(productRepository.findById(eq(productId))).willReturn(Optional.of(product));
        given(productSnapshotRepository.save(any(ProductSnapshot.class))).willReturn(mock(ProductSnapshot.class));

        //when
        ProductResponseDto productResponse = productService.modify(productId, modifyProductPriceRequestDto);

        //then
        Assertions.assertThat(productResponse.getPrice()).isEqualTo(modifyProductPriceRequestDto.getPrice());

        log.info("========================================");
        log.info("수정 요청 가격 : {} " , modifyProductPriceRequestDto.getPrice());
        log.info("========================================");
        log.info("수정 후 상품 가격 : {} " , product.getPrice());
        log.info("수정 후 기존 스냅샷 만료기간 : {} ", oldProductSnapshot.getToDate());
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