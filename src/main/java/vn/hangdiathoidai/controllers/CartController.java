package vn.hangdiathoidai.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.hangdiathoidai.entity.Cart;
import vn.hangdiathoidai.entity.CartItem;
import vn.hangdiathoidai.entity.Voucher;
import vn.hangdiathoidai.services.CartItemService;
import vn.hangdiathoidai.services.CartService;
import vn.hangdiathoidai.services.VoucherService;

@Controller
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
	CartService cartService;
	
	@Autowired
	CartItemService cartItemService;
	
	@GetMapping("")
	public String cart(Model model) {
		// User by default
		Cart cart = cartService.findByUserId(2L).get();
		List<CartItem> cartItems = cartItemService.findByCartId(cart.getId());
		model.addAttribute("cart", cart);
		model.addAttribute("cartItems", cartItems);
		return "user/cart";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Long id) {
		Cart cart = cartService.findByUserId(2L).get();
		CartItem cartItem = cartItemService.findById(id);
		cart.setTotal(cart.getTotal() - cartItem.getProductsSku().getPrice() * cartItem.getQuantity());
		cartService.save(cart);
		cartItemService.deleteById(id);
		return "redirect:/cart";
	}
}
