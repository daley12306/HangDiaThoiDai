package vn.hangdiathoidai.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hangdiathoidai.entity.Category;
import vn.hangdiathoidai.entity.SubCategory;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
	Page<SubCategory> findByParentCategory(Optional<Category> parentCategory, Pageable pageable);
	
	// Tìm kiếm SubCategory theo tên
    Page<SubCategory> findByNameContaining(String name, Pageable pageable);
    
    // Phân trang SubCategory
    Page<SubCategory> findAll(Pageable pageable);

	List<SubCategory> findByParentCategory(Optional<Category> category);
	
	List<SubCategory> findByParentCategoryId(Long categoryId);
}
