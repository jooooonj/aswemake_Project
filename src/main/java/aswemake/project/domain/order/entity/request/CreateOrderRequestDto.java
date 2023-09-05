package aswemake.project.domain.order.entity.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateOrderRequestDto {
    @NotNull @Valid
    List<OrderItemRequestDto> orderItemRequestDtoList;

    @NotNull(message = "배송비는 null일 수 없습니다.")
    private Integer deliveryFee;
}
