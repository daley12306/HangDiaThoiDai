package vn.hangdiathoidai.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import vn.hangdiathoidai.entity.Product;
import vn.hangdiathoidai.enums.ProductStatus;

public interface ProductService {

	Page<Product> getAllProducts(int page, int size);

	Page<Product> searchProducts(String name, int page, int size);

	Product update(Product product);

	void delete(Long id);

	Product save(Product product);

	Product getProductById(Long id);

	long getTotalProducts();

	Optional<Product> findById(Long id);


    List<Product> getProducts();
	List<Product> getActiveProducts();

    List<Product> findAllBySubCategoryIdAndStatus(Long category_id, ProductStatus status);
}
