package aswemake.project.domain.order.entity.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderItemRequestDto {
    @NotNull(message = "상품 id는 null일 수 없습니다.")
    private Long productId;
    @Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
    private int quantity;
}
