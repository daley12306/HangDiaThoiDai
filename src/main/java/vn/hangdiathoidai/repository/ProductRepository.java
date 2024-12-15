package vn.hangdiathoidai.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hangdiathoidai.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	
	Page<Product> findByNameContaining(String name, Pageable pageable);

	List<Product> findTop10ByOrderByIdDesc();

}
