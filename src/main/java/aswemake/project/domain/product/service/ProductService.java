package aswemake.project.domain.product.service;

import aswemake.project.domain.product.entity.Product;
import aswemake.project.domain.product.entity.ProductSnapshot;
import aswemake.project.domain.product.entity.request.CreateProductRequestDto;
import aswemake.project.domain.product.entity.request.ModifyProductPriceRequestDto;
import aswemake.project.domain.product.entity.response.ProductResponseDto;
import aswemake.project.domain.product.exception.ProductNotFoundException;
import aswemake.project.domain.product.repository.ProductRepository;
import aswemake.project.domain.product.repository.ProductSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductSnapshotRepository productSnapshotRepository;

    public Product findById(Long productId){
        return productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("존재하지 않는 게시글입니다.")
        );
    }

    @Transactional
    public ProductResponseDto register(CreateProductRequestDto createProductRequestDto) {
        Product product = productRepository.save(Product.create(createProductRequestDto));
        saveProductSnapshot(product);
        return ProductResponseDto.of(product);
    }

    @Transactional
    public ProductResponseDto modify(Long productId,
                                     ModifyProductPriceRequestDto modifyProductPriceRequestDto) {
        Product product = findById(productId);
        product.modifyPrice(modifyProductPriceRequestDto.getPrice());
        saveProductSnapshot(product);
        return ProductResponseDto.of(product);
    }

    @Transactional
    public void delete(Long productId) {
        productRepository.deleteById(productId);
    }

    private void saveProductSnapshot(Product product) {
        ProductSnapshot snapshot = productSnapshotRepository.save(ProductSnapshot.create(product));
        product.exchangeSnapshot(snapshot);
    }

    public Integer getProductPriceAtSpecificTime(Long productId, LocalDateTime timestamp) {
        Integer productPriceAtSpecificTime = productSnapshotRepository.getProductPriceAtSpecificTime(productId, timestamp);
        if(productPriceAtSpecificTime == null)
            throw new ProductNotFoundException(timestamp+" 시점에 " + productId+"번 상품은 존재하지 않습니다.");

        return productPriceAtSpecificTime;
    }
}
