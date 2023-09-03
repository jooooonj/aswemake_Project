package aswemake.project.domain.product.entity.request;

import lombok.*;
import org.hibernate.validator.constraints.Range;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class ModifyProductPriceRequestDto {
    @Range(min = 1000, message = "상품 가격은 1,000원 이상이어야 합니다.")
    private int price;
}
