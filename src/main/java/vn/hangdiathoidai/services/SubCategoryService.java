package vn.hangdiathoidai.services;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.hangdiathoidai.entity.Category;
import vn.hangdiathoidai.entity.SubCategory;

public interface SubCategoryService {

	Page<SubCategory> getSubCategories(Pageable pageable);

	SubCategory updateSubCategory(Long id, SubCategory updatedSubCategory);

	void deleteSubCategory(Long id);

	SubCategory addSubCategory(SubCategory subCategory);

	Page<SubCategory> findBySearchTerm(String searchTerm, Pageable pageable);

	SubCategory getSubCategoriesById(Long id);

	Page<SubCategory> findByParentCategory(Optional<Category> parentCategory, Pageable pageable);

	long getTotalSubCategory();

	List<SubCategory> getAllSubCategories();

	List<SubCategory> getSubCategoriesByParentCategory(Optional<Category> category);

	Category getCategoryById(Long id);

	List<SubCategory> findByParentCategoryId(Long categoryId);
	
	
}
