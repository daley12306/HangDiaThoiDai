package vn.hangdiathoidai.controllers.admin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import vn.hangdiathoidai.entity.Category;
import vn.hangdiathoidai.entity.Product;
import vn.hangdiathoidai.entity.SubCategory;
import vn.hangdiathoidai.services.CategoryService;
import vn.hangdiathoidai.services.FileStorageService;
import vn.hangdiathoidai.services.ProductService;
import vn.hangdiathoidai.services.SubCategoryService;

@Controller
@RequestMapping("/admin/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private SubCategoryService subcategoryService;
    @Autowired
    private FileStorageService fileStorageService;
    @Value("${upload.dir}") // Đường dẫn thư mục upload được cấu hình trong application.properties
    private String uploadDir;
    
    // Hiển thị danh sách sản phẩm
    @GetMapping
    public String listProducts(@RequestParam(defaultValue = "") String search,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               Model model) {
        Page<Product> products = productService.searchProducts(search, page, size);
        model.addAttribute("products", products);
        model.addAttribute("search", search);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        return "admin/product/list";
    }

    // Hiển thị form thêm sản phẩm
    @GetMapping("/create")
	public String createProductForm(Model model) {
		List<SubCategory> subCategories = subcategoryService.getAllSubCategories();
		model.addAttribute("subCategories", subCategories);
        model.addAttribute("product", new Product());
        return "admin/product/add";
    }

    // Xử lý thêm sản phẩm
    @PostMapping("/create")
    public String createProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile file,
					    		@RequestParam(defaultValue = "0") int page,  // Nhận tham số trang
					            @RequestParam(defaultValue = "10") int size) {
    	if (!file.isEmpty()) {
            try {
                // Lưu ảnh như đã làm trước đây
                String uploadDirectory = uploadDir + File.separator;
                File dir = new File(uploadDirectory);
                if (!dir.exists()) {
                    dir.mkdirs(); // Tạo thư mục nếu chưa tồn tại
                }

                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path path = Paths.get(uploadDirectory + File.separator + fileName);
                Files.write(path, file.getBytes());
                product.setCover(fileName);  // Lưu đường dẫn ảnh vào trường avatar của User
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    	
        productService.save(product);
        
        long totalProducts = productService.getTotalProducts();  // Lấy tổng số người dùng
        int totalPages = (int) Math.ceil((double) totalProducts / size);  // Tính số trang
        if (totalProducts % size == 0 && totalProducts > 0) {
            totalPages--;  // Điều chỉnh trang cuối nếu chia hết
        }

        // Redirect về trang cuối
        return "redirect:/admin/products?page=" + (totalPages-1) + "&size=" + size;
    }

    // Hiển thị form sửa sản phẩm
    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("subCategories", subcategoryService.getAllSubCategories());
        return "admin/product/edit";
    }

    // Xử lý sửa sản phẩm
    @PostMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id, @ModelAttribute Product product,
    						  @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        Product oldProduct = productService.getProductById(id);
        String oldFile = oldProduct.getCover();
        if (file != null && !file.isEmpty()) {
            // Lưu ảnh mới nếu có
            String filename = fileStorageService.saveFile(file);  // Hàm này xử lý việc lưu ảnh
            product.setCover(filename);  // Cập nhật trường avatar với ảnh mới
        } else {
            // Nếu không thay đổi ảnh, giữ nguyên avatar cũ
            product.setCover(oldFile);
        }
        product.setId(id);
        productService.update(product);  // Cập nhật sản phẩm vào cơ sở dữ liệu
        return "redirect:/admin/products";
    }


    // Xóa sản phẩm
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes, HttpSession session) {
        productService.delete(id);
        return "redirect:/admin/products";
    }
}

