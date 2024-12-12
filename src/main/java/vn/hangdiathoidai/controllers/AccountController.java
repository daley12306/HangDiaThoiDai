package vn.hangdiathoidai.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import vn.hangdiathoidai.entity.Address;
import vn.hangdiathoidai.entity.User;
import vn.hangdiathoidai.models.UserModel;
import vn.hangdiathoidai.services.AddressService;
import vn.hangdiathoidai.services.UserService;

@Controller
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	AddressService addressService;
	
	@GetMapping("")
	public String all() {
		return "redirect:/account/profile";
	}
	
	@GetMapping("/profile")
	public String profile(ModelMap model) {
		// Default user
		User user = userService.findUserById(2L);
		model.addAttribute("user", user);
		return "profile";
	}
	
	@PostMapping("/profile/save")
	public String saveProfile(@Valid @ModelAttribute("user") UserModel userModel,
			@RequestParam int day, @RequestParam int month, @RequestParam int year) throws ParseException {
		// Default user
		User user = userService.findUserById(2L);
		user.setUsername(userModel.getUsername());
		user.setFullName(userModel.getFullName());
		user.setEmail(userModel.getEmail());
		user.setPhoneNumber(userModel.getPhoneNumber());
		user.setBirthOfDate(new SimpleDateFormat("dd-MM-yyyy").parse(day + "-" + month + "-" + year));
		userService.saveUser(user);
		return "redirect:/account/profile";
	}
	
	@GetMapping("/address")
	public String address(ModelMap model) {
		List<Address> addresses = addressService.findByUserId(2L);
		model.addAttribute("addresses", addresses);
		return "address";
	}
	
	@GetMapping("/address/delete/{id}")
	public String deleteAddress(@PathVariable Long id) {
		addressService.deleteAddress(id);
		return "redirect:/account/address";
	}
}
