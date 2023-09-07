package aswemake.project.domain.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSnapshot {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "product_name", nullable = false)
    private String productName;
    @Column(name = "price", nullable = false)
    private int price;
    @Column(name = "from_date", nullable = false)
    private LocalDateTime fromDate;
    @Column(name = "to_date", nullable = false)
    private LocalDateTime toDate;
    @Column(name = "product_id", nullable = false)
    private Long productId; //상품 식별에 사용

    public static ProductSnapshot create(Product product){
        return ProductSnapshot.builder()
                .productId(product.getId())
                .fromDate(LocalDateTime.now()) //마지막으로 수정된 날짜
                .toDate(LocalDateTime.now().plusYears(100)) //임시 마감기한 (무제한)
                .productName(product.getProductName())
                .price(product.getPrice())
                .build();
    }

    //기간 만료 (상품 가격 수정)
    public void expire(LocalDateTime dateTime){
        this.toDate = dateTime;
    }
}
