package vn.hangdiathoidai.services;

import java.util.Optional;

import org.springframework.data.domain.Page;

import vn.hangdiathoidai.entity.Product;

public interface ProductService {

	Page<Product> getAllProducts(int page, int size);

	Page<Product> searchProducts(String name, int page, int size);

	Product update(Product product);

	void delete(Long id);

	Product save(Product product);

	Product getProductById(Long id);

	long getTotalProducts();

	Optional<Product> findById(Long id);

	
}
