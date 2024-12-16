package vn.hangdiathoidai.controllers.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import vn.hangdiathoidai.entity.Product;
import vn.hangdiathoidai.services.OrderDetailService;
import vn.hangdiathoidai.services.ProductService;
import vn.hangdiathoidai.services.UserService;

@Controller
@RequestMapping("/admin")
public class DashBoardController {

    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderDetailService orderDetailService;
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpServletRequest request) {
        // Lấy các thông tin thống kê từ các dịch vụ
        long totalUsers = userService.getTotalUsers();
        long totalProducts = productService.getTotalProducts();
        long totalOrders = orderDetailService.getTotalOrders();
        List<Product> latestProducts = productService.getLatestProducts(); // Giả sử bạn có phương thức này
        model.addAttribute("latestProducts", latestProducts);
        List<Product> topSellingProducts = productService.getTopSellingProducts(5);
        model.addAttribute("topSellingProducts", topSellingProducts); // Truyền vào model
        // Thêm các giá trị vào model
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("pageTitle", "Dashboard");
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("currentUrl", request.getRequestURI());
        
        return "admin/dashboard";  // Trả về trang dashboard.html
    }
}
