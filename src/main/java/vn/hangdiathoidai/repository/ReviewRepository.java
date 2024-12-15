package vn.hangdiathoidai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.hangdiathoidai.entity.Review;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // Tìm tất cả đánh giá theo sản phẩm
    List<Review> findByProduct_Id(Long productId);

}
