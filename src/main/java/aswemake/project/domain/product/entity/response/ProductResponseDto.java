package aswemake.project.domain.product.entity.response;

import aswemake.project.domain.product.entity.Product;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ProductResponseDto {
    private Long id;
    private String productName;
    private int price;

    public static ProductResponseDto of(Product product){
        return new ProductResponseDto(
                product.getId(), product.getProductName(), product.getPrice()
        );
    }
}
