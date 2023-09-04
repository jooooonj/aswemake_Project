package aswemake.project.domain.product.entity;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

@Slf4j
class ProductTest {

    @Test
    @DisplayName("exchangeSnapshot() - 스냅샷이 새로운 스냅샷으로 교체된다.")
    void exchangeSnapshot() {
        //given
        Long productId = 1L;
        String productName = "테스트 상품";
        int price = 5000;
        LocalDateTime timestamp = LocalDateTime.of(2222, 12, 25, 0, 0, 0,0);

        Product product = Product.builder()
                .id(productId)
                .productName(productName)
                .price(price)
                .build();

        ProductSnapshot snapshot1 = ProductSnapshot.builder()
                .productId(productId)
                .productName(productName)
                .price(price)
                .fromDate(LocalDateTime.now())
                .toDate(timestamp) //2222.12.25
                .build();
        product.exchangeSnapshot(snapshot1); //최초 스냅샷 등록

        ProductSnapshot snapshot2 = ProductSnapshot.builder()
                .productId(productId)
                .productName(productName)
                .price(20000)
                .fromDate(LocalDateTime.now())
                .toDate(timestamp)
                .build();

        //when
        log.info("snapshot1 기존 toDate : {} ", snapshot1.getToDate());
        product.exchangeSnapshot(snapshot2);

        //then
        log.info("snapshot1 업데이트 toDate : {} ", snapshot1.getToDate());
        Assertions.assertThat(snapshot1.getToDate()).isNotEqualTo(timestamp);
        Assertions.assertThat(product.getProductSnapshot()).isEqualTo(snapshot2);
    }
}