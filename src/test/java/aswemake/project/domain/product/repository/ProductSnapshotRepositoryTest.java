package aswemake.project.domain.product.repository;

import aswemake.project.domain.product.entity.ProductSnapshot;
import aswemake.project.global.config.JpaAuditingConfig;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;

@DataJpaTest
@Import(JpaAuditingConfig.class)
@Slf4j
class ProductSnapshotRepositoryTest {
    @Autowired
    protected ProductRepository productRepository;
    @Autowired
    protected ProductSnapshotRepository productSnapshotRepository;

    @Test
    @DisplayName("getProductPriceAtSpecificTime - 특정 시점의 상품 가격을 조회한다.")
    void getProductPriceAtSpecificTime_fail() throws Exception {
        //given
        Long productId = 1L;
        ProductSnapshot snapshot1 = ProductSnapshot.builder()
                .productId(1L)
                .productName("테스트용1")
                .price(5000)
                .fromDate(LocalDateTime.now().minusDays(1))
                .toDate(LocalDateTime.now().plusDays(1))
                .build();

        ProductSnapshot snapshot2 = ProductSnapshot.builder()
                .productId(1L)
                .productName("테스트용2")
                .price(10000)
                .fromDate(LocalDateTime.now().minusDays(5))
                .toDate(LocalDateTime.now().minusDays(1))
                .build();

        ProductSnapshot savedSnapshot1 = productSnapshotRepository.save(snapshot1);
        ProductSnapshot savedSnapshot2 = productSnapshotRepository.save(snapshot2);

        //when,then
        log.info("현재 시간을 기준으로 상품 가격 검색");
        Integer price1 = productSnapshotRepository.getProductPriceAtSpecificTime(productId, LocalDateTime.now());

        log.info("3일전을 기준으로 상품 가격 검색");
        Integer price2 = productSnapshotRepository.getProductPriceAtSpecificTime(productId, LocalDateTime.now().minusDays(3));

        //then
        Assertions.assertThat(price1).isNotNull();
        Assertions.assertThat(price1).isEqualTo(snapshot1.getPrice());

        Assertions.assertThat(price2).isNotNull();
        Assertions.assertThat(price2).isEqualTo(snapshot2.getPrice());
    }
}