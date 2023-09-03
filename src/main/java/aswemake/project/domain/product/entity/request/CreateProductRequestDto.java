package aswemake.project.domain.product.entity.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Range;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class CreateProductRequestDto {

    @NotBlank(message = "상품명은 필수값입니다.")
    private String productName;

    @Range(min = 1000, message = "상품 가격은 1,000원 이상이어야 합니다.")
    private int price;
}
