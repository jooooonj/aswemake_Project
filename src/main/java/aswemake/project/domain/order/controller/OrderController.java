package aswemake.project.domain.order.controller;

import aswemake.project.domain.order.entity.request.CreateOrderRequestDto;
import aswemake.project.domain.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Long> create(@AuthenticationPrincipal User user,
                                       @Valid @RequestBody CreateOrderRequestDto createOrderRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                orderService.createOrder(user.getUsername(), createOrderRequestDto));
    }

    @GetMapping("/{orderId}/price")
    public ResponseEntity<Integer> getOrderPrice (@PathVariable("orderId") Long orderId){
        return ResponseEntity.ok().body(orderService.getOrderPrice(orderId));
    }
}
