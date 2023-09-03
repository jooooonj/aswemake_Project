package aswemake.project.domain.product.service;

import aswemake.project.domain.product.entity.Product;
import aswemake.project.domain.product.entity.request.CreateProductRequestDto;
import aswemake.project.domain.product.entity.request.ModifyProductPriceRequestDto;
import aswemake.project.domain.product.entity.response.ProductResponseDto;
import aswemake.project.domain.product.exception.ProductNotFoundException;
import aswemake.project.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public Product findById(Long productId){
        return productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("존재하지 않는 게시글입니다.")
        );
    }

    @Transactional
    public ProductResponseDto register(CreateProductRequestDto createProductRequestDto) {
        Product product = productRepository.save(Product.create(createProductRequestDto));
        return ProductResponseDto.of(product);
    }
}
