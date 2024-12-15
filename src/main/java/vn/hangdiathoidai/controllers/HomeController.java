package vn.hangdiathoidai.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vn.hangdiathoidai.entity.Product;
import vn.hangdiathoidai.entity.Review;
import vn.hangdiathoidai.enums.ProductStatus;
import vn.hangdiathoidai.services.ProductService;
import vn.hangdiathoidai.services.ReviewService;

import java.util.List;

@Controller
public class HomeController {
	@Autowired
	private ProductService productService;
	@Autowired
	private  ReviewService reviewService;

    @GetMapping
	public String homePage(Model model) {
		// Fetch all products
		List<Product> activeProducts = productService.getActiveProducts();
		model.addAttribute("products", activeProducts);
		return "home";
	}
	@GetMapping("/product/{id}")
	public String getProductDetails(@PathVariable("id") Long id, Model model) {
		Product product = productService.getProductById(id);

		if (product == null) {
			model.addAttribute("errorMessage", "Sản phẩm không tồn tại.");
			return "error";
		}

		List<Review> reviews = reviewService.getReviewsByProduct(id);

		model.addAttribute("product", product);
		model.addAttribute("reviews", reviews);

		if (reviews.isEmpty()) {
			model.addAttribute("noReviewsMessage", "Chưa có đánh giá nào cho sản phẩm này.");
		}

		return "product";
	}
	@GetMapping("/shop")
	public String shopPage(Model model) {
		List<Product> cdProducts = productService.findAllBySubCategoryIdAndStatus(1L, ProductStatus.ACTIVE);
		List<Product> cassetteProducts = productService.findAllBySubCategoryIdAndStatus(3L,ProductStatus.ACTIVE);
		List<Product> vinylProducts = productService.findAllBySubCategoryIdAndStatus(2L,ProductStatus.ACTIVE);

		model.addAttribute("cdProducts", cdProducts);
		model.addAttribute("cassetteProducts", cassetteProducts);
		model.addAttribute("vinylProducts", vinylProducts);

		return "shop";
	}
}
