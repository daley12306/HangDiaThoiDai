package vn.hangdiathoidai.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.hangdiathoidai.entity.Category;
import vn.hangdiathoidai.entity.SubCategory;
import vn.hangdiathoidai.repository.CategoryRepository;
import vn.hangdiathoidai.repository.SubCategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
    private CategoryRepository categoryRepository;
	@Autowired
    private SubCategoryRepository subCategoryRepository;
	
    // Thêm mới Category
    @Override
	public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    // Cập nhật Category
    @Override
	public Category updateCategory(Long id, Category category) {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (existingCategory.isPresent()) {
            category.setId(id);  // Đảm bảo set ID của Category cũ vào Category mới
            return categoryRepository.save(category);
        }
        return null;  // Trả về null nếu không tìm thấy Category
    }

    // Xóa Category
    @Override
	public boolean deleteCategory(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Lấy danh sách tất cả các Category
    @Override
	public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Lấy Category theo id
    @Override
	public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }
    @Override
	public Page<Category> getCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);  // Tạo đối tượng Pageable
        return categoryRepository.findAll(pageable);  // Lấy danh sách category phân trang
    }

    @Override
	public Category saveCategory(Category category) {
        return categoryRepository.save(category);  // Thêm hoặc sửa category
    }
    
    @Override
	public Page<Category> searchCategories(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (search != null && !search.isEmpty()) {
            return categoryRepository.findByNameContaining(search, pageable);
        }
        return categoryRepository.findAll(pageable);
    }

	@Override
	public Page<Category> findByNameContaining(String search, Pageable pageable) {
		return categoryRepository.findByNameContaining(search, pageable);
	}

	@Override
	public Page<Category> getAllCategories(Pageable pageable) {
	    return categoryRepository.findAll(pageable);
	}
	
	// Lấy danh sách SubCategory theo Category
    @Override
	public List<SubCategory> getSubCategoriesByCategory(Optional<Category> category) {
        return subCategoryRepository.findByParentCategory(category);
    }

}
