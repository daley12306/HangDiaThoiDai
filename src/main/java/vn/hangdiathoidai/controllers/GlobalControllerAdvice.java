package vn.hangdiathoidai.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import vn.hangdiathoidai.entity.Cart;
import vn.hangdiathoidai.entity.CartItem;
import vn.hangdiathoidai.entity.User;
import vn.hangdiathoidai.services.CartItemService;
import vn.hangdiathoidai.services.CartService;
import vn.hangdiathoidai.services.UserService;

@ControllerAdvice
public class GlobalControllerAdvice {
	
	@Autowired
    CartService cartService;
	
	@Autowired
	CartItemService cartItemService;
	
	@Autowired
	UserService userService;

    @ModelAttribute
    public void addCartInfoToHeader(ModelMap model, @AuthenticationPrincipal UserDetails userDetails) {
		if(userDetails != null) {
			User user = userService.findByUsername(userDetails.getUsername());
	        Cart cart = cartService.findByUserId(user.getId()).get();
	        List<CartItem> cartItems = cartItemService.findByCartId(cart.getId());
	        
	        model.addAttribute("user", user);
	        model.addAttribute("cartItemCount", cartItems.size()); 
	        model.addAttribute("cart", cart);
	        model.addAttribute("cartItems", cartItems);
		}
    }
}
