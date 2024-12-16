package vn.hangdiathoidai.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hangdiathoidai.entity.Product;
import vn.hangdiathoidai.enums.ProductStatus;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	
	Page<Product> findByNameContaining(String name, Pageable pageable);
 
  List<Product> findAllByStatus(ProductStatus status);
  
  List<Product> findAllBySubCategoryIdAndStatus(Long categoryId, ProductStatus status);
  
	List<Product> findTop10ByOrderByIdDesc();


}
