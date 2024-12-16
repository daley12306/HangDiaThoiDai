package vn.hangdiathoidai.controllers.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import vn.hangdiathoidai.entity.OrderDetail;
import vn.hangdiathoidai.entity.OrderItem;
import vn.hangdiathoidai.enums.OrderStatus;
import vn.hangdiathoidai.services.OrderDetailService;
import vn.hangdiathoidai.services.OrderItemService;

@Controller
@RequestMapping("/admin/order")
public class OrderDetailController {
	@Autowired
    private OrderDetailService orderDetailService;
	@Autowired
    private OrderItemService orderItemService;
	
	@RequestMapping("")
	public String listOrders(
	        @RequestParam(value = "keyword", required = false) String keyword,
	        @RequestParam(value = "page", defaultValue = "0") int page,
	        @RequestParam(value = "size", defaultValue = "10") int size,
	        Model model, HttpServletRequest request, Long id) {
	    // Lấy danh sách đơn hàng theo từ khóa (nếu có)
	    Page<OrderDetail> orderPage = orderDetailService.findOrdersByCustomerName(keyword, page, size);
	    
	    // Thêm các thông tin vào model
	    model.addAttribute("orders", orderPage.getContent()); // Danh sách đơn hàng trong trang hiện tại
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", orderPage.getTotalPages());
	    model.addAttribute("keyword", keyword); // Giữ lại từ khóa tìm kiếm cho giao diện
	    
	    model.addAttribute("currentUrl", request.getRequestURI());
	    
	    return "admin/order/list"; // Trả về trang hiển thị danh sách
	}
	
	@PostMapping("/update-status/{id}")
	public String updateOrderStatus(@PathVariable Long id,@RequestParam OrderStatus status, RedirectAttributes redirectAttributes, HttpSession session) {
	    try {
	        // Thực hiện cập nhật trạng thái đơn hàng
	        orderDetailService.updateOrderStatus(id, status);
	        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái thành công!");
	    } catch (Exception e) {
	        // Xử lý khi xảy ra lỗi
	        redirectAttributes.addFlashAttribute(e);
	    }
	    // Trả về trang hiện tại
	    Integer page = (Integer) session.getAttribute("currentPage");
        if (page == null) {
            page = 0;  // Nếu không có trang hiện tại, mặc định là trang đầu tiên
        }
        return "redirect:/admin/order?page="+page;
	}

	
	@GetMapping("/detail/{id}")
	public String viewOrderDetails(@PathVariable Long id, ModelMap model) {
        // Lấy thông tin chi tiết đơn hàng theo ID
        OrderDetail order = orderDetailService.getOrderById(id);

        // Lấy danh sách OrderItem liên quan đến OrderDetail
        List<OrderItem> orderItems = orderItemService.getItemsByOrderId(id);

        model.addAttribute("order", order);
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("selectedAddress", order.getAddress());
        model.addAttribute("selectedCarrier", order.getCarrier());
        
        
        model.addAttribute("shippingFee", order.getCarrier().getShippingFee());
        model.addAttribute("paymentMethod", order.getPaymentMethod().name());
        
        
        int totalProductValue = orderItems.stream()
        	    .mapToInt(item -> item.getQuantity() * item.getProductsSku().getPrice())
        	    .sum();

        model.addAttribute("totalProductValue", totalProductValue);


        return "admin/order/detail"; // Trả về view hiển thị chi tiết đơn hàng
    }
	
}
