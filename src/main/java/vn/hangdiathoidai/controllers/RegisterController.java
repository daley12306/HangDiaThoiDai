package vn.hangdiathoidai.controllers;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.hangdiathoidai.entity.Cart;
import vn.hangdiathoidai.entity.Role;
import vn.hangdiathoidai.entity.User;
import vn.hangdiathoidai.models.UserModel;
import vn.hangdiathoidai.services.CartService;
import vn.hangdiathoidai.services.RoleService;
import vn.hangdiathoidai.services.UserService;

@Controller
@RequestMapping("/register")
public class RegisterController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	CartService cartService;

	@GetMapping
	public String register(ModelMap model) {
		model.addAttribute("userModel", new UserModel());
		model.addAttribute("message", model.get("message"));
		return "register";
	}
	
	@Transactional
	@PostMapping("/submit")
	public String submit(UserModel userModel, RedirectAttributes redirectAttributes, BindingResult result) {
		User user = userService.findByUsername(userModel.getUsername());
		if (user != null) {
			redirectAttributes.addFlashAttribute("message", "Tên đăng nhập đã tồn tại");
			return "redirect:/register";
		}
		user = userService.findByEmail(userModel.getEmail());
		if (user != null) {
			redirectAttributes.addFlashAttribute("message", "Email đã tồn tại");
			return "redirect:/register";
		}
		if (userModel.getPhoneNumber().length() != 10) {
			redirectAttributes.addFlashAttribute("message", "Số điện thoại phải có 10 số");
			return "redirect:/register";
		}
		if (userModel.getPassword().length() < 8 || userModel.getPassword().length() > 20) {
			redirectAttributes.addFlashAttribute("message", "Mật khẩu phải từ 8 đến 20 ký tự");
			return "redirect:/register";
		}
		if (!userModel.getPassword().equals(userModel.getConfirmPassword())) {
			redirectAttributes.addFlashAttribute("message", "Mật khẩu không khớp");
			return "redirect:/register";
		}
		if (userModel.getTerms() == null) {
			redirectAttributes.addFlashAttribute("message", "Bạn cần chấp nhận điều khoản");
			return "redirect:/register";
		}
		
		Role role = roleService.findByRoleName("user").get();
		
		user = new User();
		BeanUtils.copyProperties(userModel, user);
		user.setBirthOfDate(new Date());
		user.setRole(role);
		user.setPassword(passwordEncoder.encode(userModel.getPassword()) );
		userService.saveUser(user);
		redirectAttributes.addFlashAttribute("message", "Đăng ký thành công");
		
		// Create cart for user
		Cart cart = new Cart();
		cart.setUser(user);
		cart.setTotal(0);
		cartService.save(cart);
		
		return "redirect:/register";
	}
}
