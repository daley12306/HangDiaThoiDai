package vn.hangdiathoidai.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.hangdiathoidai.entity.Category;
import vn.hangdiathoidai.entity.SubCategory;
import vn.hangdiathoidai.repository.CategoryRepository;
import vn.hangdiathoidai.repository.SubCategoryRepository;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {
		@Autowired
	    private SubCategoryRepository subCategoryRepository;
		@Autowired
	    private CategoryRepository categoryRepository;
	    // Thêm mới SubCategory
	    @Override
		public SubCategory addSubCategory(SubCategory subCategory) {
	        return subCategoryRepository.save(subCategory);
	    }

	    // Xóa SubCategory
	    @Override
		public void deleteSubCategory(Long id) {
	    	try {
	            subCategoryRepository.deleteById(id); // Thực hiện xóa
	        } catch (DataIntegrityViolationException e) {
	            // Ném lỗi khi không thể xóa
	            throw new DataIntegrityViolationException("Không thể xóa danh mục phụ vì đang được sử dụng!");
	        }
	    }

	    // Sửa SubCategory
	    @Override
		public SubCategory updateSubCategory(Long id, SubCategory updatedSubCategory) {
	        Optional<SubCategory> existingSubCategory = subCategoryRepository.findById(id);
	        if (existingSubCategory.isPresent()) {
	            SubCategory subCategory = existingSubCategory.get();
	            subCategory.setName(updatedSubCategory.getName());
	            subCategory.setParentCategory(updatedSubCategory.getParentCategory());
	            return subCategoryRepository.save(subCategory);
	        }
	        return null;
	    }


	    // Phân trang SubCategory
	    @Override
		public Page<SubCategory> getSubCategories(Pageable pageable) {
	        return subCategoryRepository.findAll(pageable);
	    }

		@Override
		public Page<SubCategory> findBySearchTerm(String searchTerm, Pageable pageable) {
			return subCategoryRepository.findByNameContaining(searchTerm, pageable);
		}

		@Override
		public SubCategory getSubCategoriesById(Long id) {
			return subCategoryRepository.findById(id).orElse(null);
		}

		@Override
		public Page<SubCategory> findByParentCategory(Optional<Category> parentCategory, Pageable pageable) {
			return subCategoryRepository.findByParentCategory(parentCategory, pageable);
		}


		@Override
		public long getTotalSubCategory() {
			return subCategoryRepository.count();
		}

		@Override
		public List<SubCategory> getAllSubCategories() {
			return subCategoryRepository.findAll();
		}

		@Override
		public List<SubCategory> getSubCategoriesByParentCategory(Optional<Category> parentCategory) {
			return subCategoryRepository.findByParentCategory(parentCategory);
		}
		 @Override
		public Category getCategoryById(Long id) {
		        return categoryRepository.findById(id).orElse(null);
		    }

		@Override
		public List<SubCategory> findByParentCategoryId(Long categoryId) {
			return subCategoryRepository.findByParentCategoryId(categoryId);
		}
		
		 @Override
		public List<SubCategory> getAllSortedSubCategories() {
		        return subCategoryRepository.findAllByOrderByParentCategoryNameAscNameAsc();
		    }
}
