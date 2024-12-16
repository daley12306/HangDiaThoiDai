package vn.hangdiathoidai.controllers.admin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import vn.hangdiathoidai.entity.Category;
import vn.hangdiathoidai.entity.Product;
import vn.hangdiathoidai.entity.ProductsSku;
import vn.hangdiathoidai.entity.SubCategory;
import vn.hangdiathoidai.services.CategoryService;
import vn.hangdiathoidai.services.FileStorageService;
import vn.hangdiathoidai.services.ProductService;
import vn.hangdiathoidai.services.ProductSkuService;
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
    @Autowired
    private ProductSkuService productsSkuService;
    @Value("${upload.dir.product}") 
    private String uploadDir;
    
    // Hiển thị danh sách sản phẩm
    @GetMapping
    public String listProducts(@RequestParam(defaultValue = "") String search,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               Model model, HttpSession session, HttpServletRequest request) {
        Page<Product> products = productService.searchProducts(search, page, size);
        
        session.setAttribute("currentPage", page);
        model.addAttribute("products", products);
        model.addAttribute("search", search);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        
        model.addAttribute("currentUrl", request.getRequestURI());
        
        return "admin/product/list";
    }

    // Hiển thị form thêm sản phẩm
    @GetMapping("/create")
	public String createProductForm(Model model) {
		
    	List<SubCategory> subCategories = subcategoryService.getAllSubCategories();

        // Nhóm SubCategory theo Category
        Map<String, List<SubCategory>> groupedSubCategories = subCategories.stream()
                .collect(Collectors.groupingBy(subCategory -> subCategory.getParentCategory().getName()));

        model.addAttribute("groupedSubCategories", groupedSubCategories);
        model.addAttribute("product", new Product());
        return "admin/product/add";  // Sử dụng Thymeleaf template
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
        
        long totalProduct = productService.getTotalProducts();
        int totalPages = (int) Math.ceil((double) totalProduct / size);  // Tính tổng số trang

        
        if (totalPages == 0) {
			totalPages = 1; // Nếu không có phần tử nào, tổng số trang là 1
		}
        
        // Quay lại trang cuối cùng
        return "redirect:/admin/products?page=" + (totalPages - 1) + "&size=" + size;  // Quay lại danh sách SubCategory
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
    						  @RequestParam(value = "file", required = false) MultipartFile file, HttpSession session) throws IOException {
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
        product.setCreatedAt(oldProduct.getCreatedAt());
        productService.update(product);  // Cập nhật sản phẩm vào cơ sở dữ liệu
        
        Integer page = (Integer) session.getAttribute("currentPage");
        if (page == null) {
            page = 0; 
        }
        return "redirect:/admin/products?page="+page;
    }


    // Xóa sản phẩm
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes, HttpSession session) {
        productService.delete(id);
        
        Integer currentPage = (Integer) session.getAttribute("currentPage");
        if (currentPage == null) {
            currentPage = 0;  // Nếu không có trang hiện tại, mặc định là trang đầu tiên
        }

        // Kiểm tra số lượng SubCategory còn lại sau khi xóa
        long totalProduct = productService.getTotalProducts();  // Tổng số phần tử
        int size = 10;  // Số lượng phần tử mỗi trang (có thể lấy từ tham số hoặc mặc định)
        int totalPages = (int) Math.ceil((double) totalProduct / size);  // Tính tổng số trang
        
        // Nếu số trang còn lại là 0, chuyển về trang đầu tiên
        if (totalProduct == 0 || currentPage >= totalPages) {
            currentPage = Math.max(totalPages - 1, 0);  // Nếu không còn phần tử hoặc trang hiện tại vượt quá tổng số trang, quay lại trang trước đó
        }

        // Quay lại trang đúng
        return "redirect:/admin/products?page=" + currentPage + "&size=" + size;  // Quay lại danh sách SubCategory
    }
    
 // Lấy danh sách SKU của sản phẩm theo productId
    @GetMapping("/edit/sku/{productId}")
    public String showSkusByProductId(@PathVariable Long productId, Model model) {
        // Lấy danh sách SKU của sản phẩm theo productId
        List<ProductsSku> productSkus = productsSkuService.getSkuByProductId(productId);

        // Kiểm tra nếu không có SKU cho sản phẩm, redirect về trang sản phẩm
        if (productSkus.isEmpty()) {
            return "redirect:/admin/products";
        }
        
        Product product = productService.getProductById(productId);
        
        // Thêm danh sách SKU và productId vào model để sử dụng trong view
        model.addAttribute("productSkus", productSkus);
        model.addAttribute("productId", productId);
        model.addAttribute("productName", product.getName());

        return "admin/product/sku";  // Trả về trang hiển thị danh sách SKU
    }

    // Chỉnh sửa SKU theo SKU ID
    @PostMapping("/edit/sku/{skuId}")
    public String editSku(@PathVariable Long skuId, @ModelAttribute ProductsSku updatedSku, HttpSession session) {
        // Cập nhật thông tin SKU trong cơ sở dữ liệu
        productsSkuService.updateSku(skuId, updatedSku);

        // Sau khi cập nhật, quay lại trang sản phẩm
        Integer page = (Integer) session.getAttribute("currentPage");
        if (page == null) {
            page = 0;  // Nếu không có trang hiện tại, mặc định là trang đầu tiên
        }
        return "redirect:/admin/products?page="+page; // Redirect back to list
    }

}

