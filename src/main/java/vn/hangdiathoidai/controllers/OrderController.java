package vn.hangdiathoidai.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ch.qos.logback.core.model.Model;
import vn.hangdiathoidai.entity.OrderDetail;
import vn.hangdiathoidai.entity.OrderItem;
import vn.hangdiathoidai.services.OrderDetailService;
import vn.hangdiathoidai.services.OrderItemService;

@Controller
@RequestMapping("/order")
public class OrderController {
	@Autowired
	private OrderDetailService orderDetailService;
	@Autowired
	private OrderItemService orderItemService;

	@RequestMapping("")
	public String listOrders(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size, ModelMap model) {


		Page<OrderDetail> order = orderDetailService.findOrdersByUserId(2L, page, size);
		model.addAttribute("orders", order.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", order.getTotalPages());

		return "/user/order";
	}
	
	@GetMapping("/detail/{id}")
	public String orderDetail(@PathVariable Long id, ModelMap model) {
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
		return "/user/order-detail";
	}
	
}
