package vn.hangdiathoidai.services;

import vn.hangdiathoidai.entity.Review;

import java.util.List;

public interface ReviewService {
    List<Review> getReviewsByProduct(Long productId);
    void saveReview(Review review);
}
