package vn.hangdiathoidai.controllers;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import vn.hangdiathoidai.entity.Address;
import vn.hangdiathoidai.entity.Carrier;
import vn.hangdiathoidai.entity.Cart;
import vn.hangdiathoidai.entity.CartItem;
import vn.hangdiathoidai.entity.OrderDetail;
import vn.hangdiathoidai.entity.OrderItem;
import vn.hangdiathoidai.entity.Voucher;
import vn.hangdiathoidai.enums.DiscountType;
import vn.hangdiathoidai.enums.OrderStatus;
import vn.hangdiathoidai.enums.PaymentMethod;
import vn.hangdiathoidai.services.AddressService;
import vn.hangdiathoidai.services.CarrierService;
import vn.hangdiathoidai.services.CartItemService;
import vn.hangdiathoidai.services.CartService;
import vn.hangdiathoidai.services.OrderDetailService;
import vn.hangdiathoidai.services.OrderItemService;
import vn.hangdiathoidai.services.VNPAYService;
import vn.hangdiathoidai.services.VoucherService;

@Controller
@RequestMapping("/checkout")
public class CheckOutController {

	@Autowired
	CartService cartService;

	@Autowired
	CartItemService cartItemService;

	@Autowired
	AddressService addressService;

	@Autowired
	CarrierService carrierService;

	@Autowired
	VoucherService voucherService;
	
	@Autowired
	VNPAYService vnPayService;
	
	@Autowired
	OrderDetailService orderDetailService;
	
	@Autowired
	OrderItemService orderItemService;

	@GetMapping("")
	public String showCheckOut(HttpSession session, ModelMap model) {
		// Default user
		Cart cart = cartService.findByUserId(2L).get();
		List<CartItem> cartItems = cartItemService.findByCartId(cart.getId());
		List<Address> addresses = addressService.findByUserId(2L);
		List<Carrier> carriers = carrierService.findActiveCarriersSortedByFee();

		model.addAttribute("cart", cart);
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("addresses", addresses);
		model.addAttribute("carriers", carriers);

		Address selectedAddress = (Address) session.getAttribute("selectedAddress") != null ? (Address) session.getAttribute("selectedAddress") : addresses.get(0);
		Carrier selectedCarrier = (Carrier) session.getAttribute("selectedCarrier") != null ? (Carrier) session.getAttribute("selectedCarrier") : carriers.get(0);
		Object discountProduct = session.getAttribute("discountProduct") != null ? session.getAttribute("discountProduct") : 0;
		Object discountShipping = session.getAttribute("discountShipping") != null ? session.getAttribute("discountShipping") : 0;

		model.addAttribute("selectedAddress", selectedAddress);
		model.addAttribute("selectedCarrier", selectedCarrier);
		model.addAttribute("discountProduct", discountProduct);
		model.addAttribute("discountShipping", discountShipping);
		
		session.setAttribute("selectedAddress", selectedAddress);
		session.setAttribute("selectedCarrier", selectedCarrier);
		session.setAttribute("discountProduct", discountProduct);
		session.setAttribute("discountShipping", discountShipping);
		
		return "user/checkout";
	}

	@GetMapping("/change-address/{id}")
	public String changeAddress(HttpSession session, ModelMap model, @PathVariable Long id) {
		Address address = addressService.findById(id).get();
		session.setAttribute("selectedAddress", address);
		return "redirect:/checkout";
	}

	@GetMapping("/change-carrier/{id}")
	public String changeCarrier(HttpSession session, ModelMap model, @PathVariable Long id) {
		Carrier carrier = carrierService.getCarrierById(id).get();
		session.setAttribute("selectedCarrier", carrier);
		if (session.getAttribute("discountShipping") != null) {
			session.setAttribute("discountShipping", 0);
		}
		return "redirect:/checkout";
	}

	@PostMapping("/voucher")
	public String applyVoucher(HttpSession session, RedirectAttributes redirectAttributes,
			@RequestParam String voucherCode) {
		Voucher voucher = voucherService.findByCode(voucherCode);
		// Default user
		Cart cart = cartService.findByUserId(2L).get();
		if (voucherCode.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "Vui lòng nhập mã giảm giá");
		} else if (voucher == null) {
			redirectAttributes.addFlashAttribute("message", "Mã giảm giá không tồn tại");
		} else if (voucher.getMinOrderValue() > cart.getTotal()) {
			redirectAttributes.addFlashAttribute("message",
					"Đơn hàng của bạn chưa đạt giá trị tối thiểu để sử dụng mã giảm giá");
		} else if (voucher.getStartDate().isAfter(java.time.LocalDate.now())) {
			redirectAttributes.addFlashAttribute("message", "Mã giảm giá chưa có hiệu lực");
		} else if (voucher.getEndDate().isBefore(java.time.LocalDate.now())) {
			redirectAttributes.addFlashAttribute("message", "Mã giảm giá đã hết hạn");
		} else if (voucher.getQuantity() == 0) {
			redirectAttributes.addFlashAttribute("message", "Mã giảm giá đã hết lượt sử dụng");
		} else {
			if (voucher.getDiscountType() == DiscountType.PRODUCT) {
				session.setAttribute("discountProduct", (int) (voucher.getDiscountValue() * cart.getTotal() / 100));
				session.setAttribute("shippingVoucher", voucher);
			} else if (voucher.getDiscountType() == DiscountType.SHIPPING) {
				Carrier carrier = (Carrier) session.getAttribute("selectedCarrier");
				System.out.println(carrier == null);
				session.setAttribute("discountShipping",
						(int) (voucher.getDiscountValue() * carrier.getShippingFee() / 100));
				session.setAttribute("productVoucher", voucher);
			}
		}

		return "redirect:/checkout";
	}
	
	@PostMapping("/submit")
	public String submitOrder(HttpSession session, HttpServletRequest request, RedirectAttributes redirectAttributes, @RequestParam String paymentMethod) {
		Cart cart = cartService.findByUserId(2L).get();
		String orderInfo = "Thanh toan don hang " + cart.getId();
		if (paymentMethod.equals("COD")) {
			redirectAttributes.addFlashAttribute("paymentMethod", PaymentMethod.COD);
			redirectAttributes.addFlashAttribute("paymentStatus", 1);
			return "redirect:/checkout/status";
		}
		else {
			String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
	        String vnpayUrl = vnPayService.createOrder(request, cart.getTotal(), orderInfo, baseUrl);
	        return "redirect:" + vnpayUrl;
		}
	}
	
	// Sau khi hoàn tất thanh toán, VNPAY sẽ chuyển hướng trình duyệt về URL này
    @GetMapping("/vnpay-payment-return")
    public String paymentCompleted(HttpServletRequest request, RedirectAttributes redirectAttributes){
        int paymentStatus =vnPayService.orderReturn(request);
        redirectAttributes.addFlashAttribute("paymentMethod", PaymentMethod.VNPAY);
        redirectAttributes.addFlashAttribute("paymentStatus", paymentStatus);
        return "redirect:/checkout/status";
    }
    
    @Transactional
    @GetMapping("/status")
	public String orderStatus(HttpSession session, ModelMap model) {
    	int paymentStatus =  model.getAttribute("paymentStatus") != null ? (int) model.getAttribute("paymentStatus") : 0;
		if (paymentStatus == 1) {
			// Default user
			Cart cart = cartService.findByUserId(2L).get();
			List<CartItem> cartItems = cartItemService.findByCartId(cart.getId());
			
			int randomId = new Random().nextInt(999999);
			int discountProduct = (int) session.getAttribute("discountProduct");
			int discountShipping = (int) session.getAttribute("discountShipping");
			
			OrderDetail order = new OrderDetail();
			BeanUtils.copyProperties(cart, order);
			order.setId(null);
			order.setStatus(OrderStatus.PENDING);
			order.setPaymentMethod(model.getAttribute("paymentMethod") != null ? (PaymentMethod) model.getAttribute("paymentMethod") : PaymentMethod.COD);
			order.setCarrier((Carrier) session.getAttribute("selectedCarrier"));
			order.setAddress((Address) session.getAttribute("selectedAddress"));
			order.setTotal(cart.getTotal() - discountProduct - discountShipping);			
			order = orderDetailService.save(order);
			
			for (CartItem cartItem : cartItems) {
				OrderItem orderItem = new OrderItem();
				BeanUtils.copyProperties(cartItem, orderItem);
				orderItem.setId(null);
				orderItem.setOrder(order);
				orderItemService.save(orderItem);
			}
			
			// Delete items in cart and update total
			cartService.deleteByCartId(cart.getId());
			cart.setTotal(0);
			cartService.save(cart);
			
			// Update voucher
			Voucher productVoucher = (Voucher) session.getAttribute("productVoucher");
			if (productVoucher != null) {
				productVoucher.setQuantity(productVoucher.getQuantity() - 1);
				voucherService.saveVoucher(productVoucher);
			}
			Voucher shippingVoucher = (Voucher) session.getAttribute("shippingVoucher");
			if (shippingVoucher != null) {
				shippingVoucher.setQuantity(shippingVoucher.getQuantity() - 1);
				voucherService.saveVoucher(shippingVoucher);
			}
			
			// Clear session
			session.removeAttribute("selectedAddress");
			session.removeAttribute("selectedCarrier");
			session.removeAttribute("discountProduct");
			session.removeAttribute("discountShipping");
			session.removeAttribute("productVoucher");
			session.removeAttribute("shippingVoucher");
			
			model.addAttribute("order", order);
		}
		return "user/order-result";
	}
}
