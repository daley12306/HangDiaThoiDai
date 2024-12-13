package vn.hangdiathoidai.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.hangdiathoidai.entity.Category;
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
            Model model) {

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
    public String addCategory(@ModelAttribute Category category) {
        categoryService.addCategory(category);
        return "redirect:/admin/categories";  // Sau khi thêm xong, chuyển đến trang danh sách
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
    public String updateCategory(@PathVariable Long id, @ModelAttribute Category category) {
        categoryService.updateCategory(id, category);
        return "redirect:/admin/categories";  // Sau khi sửa xong, chuyển đến trang danh sách
    }

    // Xóa Category
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        boolean success = categoryService.deleteCategory(id);
        if (success) {
            return "redirect:/admin/categories";  // Sau khi xóa xong, chuyển đến trang danh sách
        }
        return "error";  // Nếu không xóa được, hiển thị trang lỗi
    }
}
