package vn.hangdiathoidai.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.hangdiathoidai.entity.Cart;
import vn.hangdiathoidai.entity.CartItem;
import vn.hangdiathoidai.entity.User;
import vn.hangdiathoidai.entity.Voucher;
import vn.hangdiathoidai.services.CartItemService;
import vn.hangdiathoidai.services.CartService;
import vn.hangdiathoidai.services.UserService;
import vn.hangdiathoidai.services.VoucherService;

@Controller
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
	CartService cartService;
	
	@Autowired
	CartItemService cartItemService;
	
	@Autowired
	UserService userService;
	
	@GetMapping("")
	public String cart(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		User user = userService.findByUsername(userDetails.getUsername());
		Cart cart = cartService.findByUserId(user.getId()).get();
		List<CartItem> cartItems = cartItemService.findByCartId(cart.getId());
		model.addAttribute("cart", cart);
		model.addAttribute("cartItems", cartItems);
		return "user/cart";
	}
	
	@Transactional
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
		User user = userService.findByUsername(userDetails.getUsername());
		Cart cart = cartService.findByUserId(user.getId()).get();
		CartItem cartItem = cartItemService.findById(id);
		cart.setTotal(cart.getTotal() - cartItem.getProductsSku().getPrice() * cartItem.getQuantity());
		cartService.save(cart);
		cartItemService.deleteById(id);
		return "redirect:/cart";
	}
}
