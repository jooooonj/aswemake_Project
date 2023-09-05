package aswemake.project.domain.order.repository;

import aswemake.project.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select o.totalPrice from Order as o where o.id = :orderId")
    Integer findOrderPriceByOrderId(@Param("orderId") Long orderId);
}
