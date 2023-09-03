package aswemake.project.domain.product.controller;

import aswemake.project.domain.member.validate.AdmValidator;
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

@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@RestController
public class ProductController {
    private final ProductService productService;
    private final AdmValidator admValidator;

    @PostMapping
    public ResponseEntity<ProductResponseDto> register(@AuthenticationPrincipal User user,
                       @Valid @RequestBody CreateProductRequestDto createProductRequestDto) {
        admValidator.checkAdmin(user.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.register(createProductRequestDto));
    }
}

