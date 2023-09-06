package aswemake.project.domain.product.controller;

import aswemake.project.global.authentication.AuthenticationValidator;
import aswemake.project.domain.product.entity.request.CreateProductRequestDto;
import aswemake.project.domain.product.entity.request.ModifyProductPriceRequestDto;
import aswemake.project.domain.product.entity.response.ProductResponseDto;
import aswemake.project.domain.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@RestController
public class ProductController {
    private final ProductService productService;
    private final AuthenticationValidator authenticationValidator;

    @PostMapping
    public ResponseEntity<ProductResponseDto> register(@AuthenticationPrincipal User user,
                       @Valid @RequestBody CreateProductRequestDto createProductRequestDto) {
        authenticationValidator.checkMart(user.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.register(createProductRequestDto));
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> modify(@AuthenticationPrincipal User user,
                                                     @Valid @RequestBody ModifyProductPriceRequestDto modifyProductPriceRequestDto,
                                                     @PathVariable("productId") Long productId) {
        authenticationValidator.checkMart(user.getUsername());
        return ResponseEntity.ok(productService.modify(productId, modifyProductPriceRequestDto));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal User user,
                                                     @PathVariable("productId") Long productId) {
        authenticationValidator.checkMart(user.getUsername());
        productService.delete(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/price")
    public ResponseEntity<Integer> getProductPriceAtSpecificTime(
            @RequestParam("productId") Long productId,
            @RequestParam("timestamp") LocalDateTime timestamp) {

        if(timestamp == null)
            timestamp = LocalDateTime.now();

        return ResponseEntity.ok(productService.getProductPriceAtSpecificTime(productId, timestamp));
    }
}

