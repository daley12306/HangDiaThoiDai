package vn.hangdiathoidai.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import vn.hangdiathoidai.entity.Product;
import vn.hangdiathoidai.enums.ProductStatus;
import vn.hangdiathoidai.repository.OrderItemRepository;
import vn.hangdiathoidai.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
    private ProductRepository productRepository;
    @Autowired
    private final OrderItemRepository orderItemRepository;
	
	@PersistenceContext
    private EntityManager entityManager;
	
    @Transactional
    @Override
	public Product save(Product product) {
    	if (product.getId() != null) {
            // Nếu sản phẩm đã tồn tại, nối lại thực thể
            return entityManager.merge(product);
        } else {
            // Nếu là sản phẩm mới, lưu vào cơ sở dữ liệu
            entityManager.persist(product);
            return product;
        }
    }

    // Xóa sản phẩm
    @Override
	public void delete(Long id) {
        productRepository.deleteById(id);
    }

    // Sửa sản phẩm
    @Override
	public Product update(Product product) {
        return productRepository.save(product);
    }

    // Tìm kiếm sản phẩm theo tên với phân trang
    @Override
	public Page<Product> searchProducts(String name, int page, int size) {
        return productRepository.findByNameContaining(name, PageRequest.of(page, size));
    }

    // Lấy tất cả sản phẩm với phân trang
    @Override
	public Page<Product> getAllProducts(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size));
    }

	@Override
	public Product getProductById(Long id) {
		return productRepository.findById(id).orElse(null);
	}

	@Override
	public long getTotalProducts() {
		return productRepository.count();
	}

	@Override
	public Optional<Product> findById(Long id) {
		return productRepository.findById(id);
	}
  
    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
    }
    public List<Product> getActiveProducts() {
        return productRepository.findAllByStatus(ProductStatus.ACTIVE);
    }

    @Override
    public List<Product> findAllBySubCategoryIdAndStatus(Long category_id, ProductStatus status) {
        return productRepository.findAllBySubCategoryIdAndStatus(category_id, status);
    }



    public ProductServiceImpl(OrderItemRepository orderItemRepository, ProductRepository productRepository) {
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getTopSellingProducts(int limit) {
        Pageable pageable = PageRequest.of(0, limit); // Giới hạn số lượng kết quả
        List<Map<String, Object>> results = orderItemRepository.findTopSellingProducts(pageable);

        // Chuyển đổi kết quả từ truy vấn thành danh sách Product
        return results.stream()
                .map(result -> productRepository.findById((Long) result.get("productId"))
                        .orElseThrow(() -> new RuntimeException("Product not found")))
                .toList();
    }


	@Override
	public List<Product> getLatestProducts() {
		return productRepository.findTop10ByOrderByIdDesc();
	}
	
}
