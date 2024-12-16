package vn.hangdiathoidai.controllers;

import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
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

import vn.hangdiathoidai.entity.*;
import vn.hangdiathoidai.services.*;


@Slf4j
@Controller
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
	CartService cartService;
	
	@Autowired
	CartItemService cartItemService;
	@Autowired
	ProductService productService;
	@Autowired
	ProductSkuService skuService;
	
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

	@PostMapping("/add/{productId}")
	public String addToCart(@PathVariable("productId") Long productId,
							@RequestParam("quantity") Integer quantity,
							RedirectAttributes redirectAttributes, 
							@AuthenticationPrincipal UserDetails userDetails) {
		User user = userService.findByUsername(userDetails.getUsername());
		Long userId = user.getId();

		try {
			// Fetch the product by productId
			Product product = productService.getProductById(productId);
			if (product == null) {
				redirectAttributes.addFlashAttribute("errorMessage", "Sản phẩm không tồn tại.");
				return "redirect:/shop";
			}

			// Fetch the SKU for the product
			ProductsSku sku = skuService.findByProductId(productId);
			if (sku == null) {
				redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy SKU cho sản phẩm.");
				return "redirect:/shop";
			}
			cartService.addToCart(productId, userId, sku, quantity);

			// Add success message
			redirectAttributes.addFlashAttribute("message", "Sản phẩm đã được thêm vào giỏ hàng!");

		} catch (Exception e) {
			// Handle any errors
			redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi thêm vào giỏ hàng.");
		}

		// Redirect to shop
		return "redirect:/product/" + productId;
	}


}
