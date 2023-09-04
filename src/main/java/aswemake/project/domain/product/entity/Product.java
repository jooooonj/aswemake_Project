package aswemake.project.domain.product.entity;

import aswemake.project.base.baseEntity.BaseEntity;
import aswemake.project.domain.product.entity.request.CreateProductRequestDto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", nullable = false)
    private String productName;
    @Column(name = "price", nullable = false)
    private int price;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_snapshot")
    private ProductSnapshot productSnapshot;

    public static Product create(CreateProductRequestDto createProductRequestDto){
        return Product.builder()
                .productName(createProductRequestDto.getProductName())
                .price(createProductRequestDto.getPrice())
                .build();
    }

    public void modifyPrice(int price){
        this.price = price;
    }

    public void exchangeSnapshot(ProductSnapshot productSnapshot){
        if(this.productSnapshot != null)
            this.productSnapshot.expire();
        this.productSnapshot = productSnapshot;
    }
}
