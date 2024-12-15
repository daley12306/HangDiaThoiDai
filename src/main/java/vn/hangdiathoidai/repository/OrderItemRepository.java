package vn.hangdiathoidai.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.hangdiathoidai.entity.OrderItem;

import java.util.List;
import java.util.Map;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("SELECT oi.product.id AS productId, SUM(oi.quantity) AS totalQuantity " +
            "FROM OrderItem oi " +
            "GROUP BY oi.product.id " +
            "ORDER BY totalQuantity DESC")
    List<Map<String, Object>> findTopSellingProducts(Pageable pageable);
}
