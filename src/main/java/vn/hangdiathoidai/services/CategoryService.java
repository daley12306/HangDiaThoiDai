package vn.hangdiathoidai.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.hangdiathoidai.entity.Category;
import vn.hangdiathoidai.entity.SubCategory;

public interface CategoryService {

	Optional<Category> getCategoryById(Long id);

	List<Category> getAllCategories();

	boolean deleteCategory(Long id);

	Category updateCategory(Long id, Category category);

	Category addCategory(Category category);

	Page<Category> getCategories(int page, int size);

	Category saveCategory(Category category);

	Page<Category> searchCategories(String search, int page, int size);

	Page<Category> findByNameContaining(String search, Pageable pageable);

	Page<Category> getAllCategories(Pageable pageable);

	List<SubCategory> getSubCategoriesByCategory(Optional<Category> category);

	long getTotalCategory();

}
