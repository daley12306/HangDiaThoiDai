package vn.hangdiathoidai.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.hangdiathoidai.entity.Review;
import vn.hangdiathoidai.entity.User;
import vn.hangdiathoidai.services.ProductService;
import vn.hangdiathoidai.services.ReviewService;
import vn.hangdiathoidai.services.UserService;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ProductService productService;
    
    @Autowired
    private UserService userService;

    @GetMapping("/{productId}")
    public String getReviewsByProduct(@PathVariable Long productId, Model model) {
        List<Review> reviews = reviewService.getReviewsByProduct(productId);
        model.addAttribute("reviews", reviews);
        return "reviews"; // Tên template hiển thị đánh giá
    }

    @PostMapping("/product/review")
    public String addReview(@RequestParam("productId") Long productId,
                            @RequestParam("rating") Integer rating,
                            @RequestParam("reviewText") String reviewText,
                            RedirectAttributes redirectAttributes,
                            @AuthenticationPrincipal UserDetails userDetails) {
        // Logic để lưu đánh giá
        Review review = new Review();
        review.setProduct(productService.getProductById(productId));
        review.setRating(rating);
        review.setReviewText(reviewText);
        review.setCreatedAt(new Date());
        User user = userService.findByUsername(userDetails.getUsername());
        review.setUser(user);

        reviewService.saveReview(review);

        redirectAttributes.addFlashAttribute("message", "Đánh giá của bạn đã được lưu!");
        return "redirect:/product/" + productId;
    }

}
