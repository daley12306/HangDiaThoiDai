package vn.hangdiathoidai.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import vn.hangdiathoidai.entity.Address;
import vn.hangdiathoidai.entity.Carrier;
import vn.hangdiathoidai.entity.Cart;
import vn.hangdiathoidai.entity.CartItem;
import vn.hangdiathoidai.entity.Voucher;
import vn.hangdiathoidai.enums.DiscountType;
import vn.hangdiathoidai.services.AddressService;
import vn.hangdiathoidai.services.CarrierService;
import vn.hangdiathoidai.services.CartItemService;
import vn.hangdiathoidai.services.CartService;
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
			} else if (voucher.getDiscountType() == DiscountType.SHIPPING) {
				Carrier carrier = (Carrier) session.getAttribute("selectedCarrier");
				System.out.println(carrier == null);
				session.setAttribute("discountShipping",
						(int) (voucher.getDiscountValue() * carrier.getShippingFee() / 100));
			}
		}

		return "redirect:/checkout";
	}
}
