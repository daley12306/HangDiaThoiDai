package vn.hangdiathoidai.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import vn.hangdiathoidai.entity.Category;
import vn.hangdiathoidai.entity.SubCategory;
import vn.hangdiathoidai.services.CategoryService;
import vn.hangdiathoidai.services.SubCategoryService;

@Controller
@RequestMapping("/admin/subcategory")
public class SubCategoryController {
    @Autowired
    private SubCategoryService subCategoryService;
    
    @Autowired
    private CategoryService categoryService;

    // Hiển thị danh sách SubCategory với phân trang và tìm kiếm
    @GetMapping
    public String listSubCategories(Model model,
                                     @RequestParam(value = "search", required = false, defaultValue = "") String searchTerm,
                                     @RequestParam(value = "parentCategoryId", required = false) Long parentCategoryId,
                                     @RequestParam(value = "page", defaultValue = "0") int page, HttpSession session, HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, 10); // Phân trang với 10 mục mỗi trang
        
        Page<SubCategory> subCategoriesPage;
        
        if (parentCategoryId != null) {
            Optional<Category> parentCategory = categoryService.getCategoryById(parentCategoryId);
            subCategoriesPage = subCategoryService.findByParentCategory(parentCategory, pageable);
        } else {
            subCategoriesPage = subCategoryService.findBySearchTerm(searchTerm, pageable);
        }
        
        session.setAttribute("currentPage", page);
        
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("subCategories", subCategoriesPage.getContent());
        model.addAttribute("page", subCategoriesPage);
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("parentCategoryId", parentCategoryId); // Truyền ID của Parent Category vào view
        
        model.addAttribute("currentUrl", request.getRequestURI());
        
        return "admin/subcategory/list"; // Trả về view list.html
    }

    // Thêm SubCategory
    @GetMapping("/add")
    public String showAddForm(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("subCategory", new SubCategory());
        return "admin/subcategory/add"; // Trả về form thêm mới
    }

    // Lưu SubCategory
    @PostMapping("/add")
    public String saveSubCategory(@ModelAttribute("subCategory") SubCategory subCategory, HttpSession session) {
        subCategoryService.addSubCategory(subCategory);  // Lưu SubCategory vào cơ sở dữ liệu
        return "redirect:/admin/subcategory"; 
    }


    // Xóa SubCategory
    @GetMapping("/delete/{id}")
    public String deleteSubCategory(@PathVariable("id") Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            subCategoryService.deleteSubCategory(id); // Thực hiện xóa danh mục phụ
            redirectAttributes.addFlashAttribute("successMessage", "Xóa danh mục phụ thành công!");
        } catch (DataIntegrityViolationException e) {
            // Nếu xóa thất bại do ràng buộc dữ liệu
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa danh mục phụ vì đang được sử dụng!");
        } catch (Exception e) {
            // Xử lý các lỗi khác
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi xóa danh mục phụ!");
        }

        // Lấy trang hiện tại từ session
        Integer currentPage = (Integer) session.getAttribute("currentPage");
        if (currentPage == null) {
            currentPage = 0;  // Nếu không có trang hiện tại, mặc định là trang đầu tiên
        }

        // Kiểm tra số lượng SubCategory còn lại sau khi xóa
        long totalSubCategory = subCategoryService.getTotalSubCategory();  // Tổng số phần tử
        int size = 10;  // Số lượng phần tử mỗi trang (có thể lấy từ tham số hoặc mặc định)
        int totalPages = (int) Math.ceil((double) totalSubCategory / size);  // Tính tổng số trang
        
        // Nếu số trang còn lại là 0, chuyển về trang đầu tiên
        if (totalSubCategory == 0 || currentPage >= totalPages) {
            currentPage = Math.max(totalPages - 1, 0);  // Nếu không còn phần tử hoặc trang hiện tại vượt quá tổng số trang, quay lại trang trước đó
        }

        // Quay lại trang đúng
        return "redirect:/admin/subcategory?page=" + currentPage + "&size=" + size;  // Quay lại danh sách SubCategory
    }

    
 // Sửa SubCategory
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
    	List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        SubCategory subCategory = subCategoryService.getSubCategoriesById(id); // Get existing subCategory
        model.addAttribute("subCategory", subCategory);
        return "admin/subcategory/edit"; 
    }

    @PostMapping("/edit/{id}")
    public String updateSubCategory(@PathVariable Long id, @ModelAttribute SubCategory subCategory, HttpSession session) {
        subCategoryService.updateSubCategory(id, subCategory);
        
        Integer page = (Integer) session.getAttribute("currentPage");
        if (page == null) {
            page = 0;  // Nếu không có trang hiện tại, mặc định là trang đầu tiên
        }
        return "redirect:/admin/subcategory?page="+page; // Redirect back to list
    }
    
    @PostMapping("/save")
    public String saveSubCategory(@ModelAttribute SubCategory subCategory, Model model) {
        subCategoryService.addSubCategory(subCategory);
     // Tính tổng số SubCategory hiện tại
        long totalSubCategory = subCategoryService.getTotalSubCategory();
        int size = 10;  // Số lượng phần tử mỗi trang (có thể lấy từ tham số hoặc mặc định)
        int totalPages = (int) Math.ceil((double) totalSubCategory / size);  // Tính tổng số trang

        
        
        // Quay lại trang cuối cùng
		if (totalPages == 0) {
			totalPages = 1;
		}
        return "redirect:/admin/subcategory?page=" + (totalPages - 1) + "&size=" + size;  // Quay lại danh sách SubCategory
    }

}
