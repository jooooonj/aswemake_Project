package aswemake.project.domain.product.repository;

import aswemake.project.domain.product.entity.ProductSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;

public interface ProductSnapshotRepository extends JpaRepository<ProductSnapshot, Long> {
    @Query("select ps.price from ProductSnapshot as ps " +
            "where ps.productId = :productId " +
            "and :date >= ps.fromDate and :date < ps.toDate")
    Integer getProductPriceAtSpecificTime(@Param("productId") Long productId,
                                                                    @Param("date") LocalDateTime date);
}
