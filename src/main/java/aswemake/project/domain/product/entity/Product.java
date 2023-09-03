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

    public static Product create(CreateProductRequestDto createProductRequestDto){
        return Product.builder()
                .productName(createProductRequestDto.getProductName())
                .price(createProductRequestDto.getPrice())
                .build();
    }

}
