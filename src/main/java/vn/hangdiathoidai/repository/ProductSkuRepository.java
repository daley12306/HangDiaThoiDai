package vn.hangdiathoidai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hangdiathoidai.entity.ProductsSku;

@Repository
public interface ProductSkuRepository extends JpaRepository<ProductsSku, Long> {
	List<ProductsSku> findByProductId(Long productId);

}
