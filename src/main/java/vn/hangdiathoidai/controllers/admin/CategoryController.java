package vn.hangdiathoidai.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import vn.hangdiathoidai.entity.Category;
import vn.hangdiathoidai.entity.SubCategory;
import vn.hangdiathoidai.services.CategoryService;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // Hiển thị danh sách các Category
    @GetMapping
    public String getCategories(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model, HttpSession session) {

    	session.setAttribute("page", page);
    	
        Page<Category> categories = categoryService.searchCategories(search, page, size);
        model.addAttribute("categories", categories.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", categories.getTotalPages());
        model.addAttribute("search", search);
        return "admin/categories/list";
    }

    // Hiển thị form thêm mới Category
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/categories/add";  // Sử dụng trang add.html trong thư mục admin/categories
    }

    // Thêm mới Category
    @PostMapping("/add")
    public String addCategory(@ModelAttribute Category category, HttpSession session) {
    	
        categoryService.addCategory(category);
        
     // Tính tổng số SubCategory hiện tại
        long totalCategory = categoryService.getTotalCategory();
        int size = 10;  // Số lượng phần tử mỗi trang (có thể lấy từ tham số hoặc mặc định)
        int totalPages = (int) Math.ceil((double) totalCategory / size);  // Tính tổng số trang

        // Nếu tổng số phần tử chia hết cho size, trang cuối cùng là totalPages - 1
        if (totalCategory % size == 0 && totalCategory > 0) {
            totalPages--;  // Điều chỉnh trang cuối cùng nếu chia hết
        }
        
		if (totalPages == 0) {
			totalPages = 1; // Nếu không có phần tử nào, tổng số trang là 1
		}
        // Quay lại trang cuối cùng
        return "redirect:/admin/categories?page=" + (totalPages-1) + "&size=" + size;  // Quay lại danh sách SubCategory
    }

    // Hiển thị form sửa Category
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Category category = categoryService.getCategoryById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        model.addAttribute("category", category);
        return "admin/categories/edit";  // Sử dụng trang edit.html trong thư mục admin/categories
    }

    // Cập nhật Category
    @PostMapping("/edit/{id}")
    public String updateCategory(@PathVariable Long id, @ModelAttribute Category category, HttpSession session) {
        categoryService.updateCategory(id, category);
        
        Integer page = (Integer) session.getAttribute("page");
        if (page == null) {
            page = 0;  // Nếu không có trang hiện tại, mặc định là trang đầu tiên
        }
        return "redirect:/admin/categories?page="+page;  // Sau khi sửa xong, chuyển đến trang danh sách
    }

    // Xóa Category
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes, HttpSession session) {
    	try {
            if (categoryService.deleteCategory(id)) {
                redirectAttributes.addFlashAttribute("successMessage", "Xóa danh mục thành công!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Danh mục không tồn tại!");
            }
        } catch (IllegalStateException e) {
            // Hiển thị thông báo lỗi khi không thể xóa
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            // Xử lý các lỗi không xác định
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi xóa danh mục!");
        }
    	Integer page = (Integer) session.getAttribute("page");
        if (page == null) {
            page = 0;  // Nếu không có trang hiện tại, mặc định là trang đầu tiên
        }
        
        long totalCategory = categoryService.getTotalCategory();  // Tổng số phần tử
        int size = 10;  // Số lượng phần tử mỗi trang (có thể lấy từ tham số hoặc mặc định)
        int totalPages = (int) Math.ceil((double) totalCategory / size);  // Tính tổng số trang
        
        // Nếu số trang còn lại là 0, chuyển về trang đầu tiên
        if (totalCategory == 0 || page >= totalPages) {
            page = Math.max(totalPages - 1, 0);  // Nếu không còn phần tử hoặc trang hiện tại vượt quá tổng số trang, quay lại trang trước đó
        }

        // Quay lại trang đúng
        return "redirect:/admin/categories?page=" + page + "&size=" + size;
    
		}
}
