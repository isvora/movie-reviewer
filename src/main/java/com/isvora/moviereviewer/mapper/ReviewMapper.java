package com.isvora.moviereviewer.mapper;

import com.isvora.moviereviewer.database.ReviewEntity;
import com.netflix.dgs.codegen.generated.types.Review;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ReviewMapper {

    public List<Review> toReviews(Set<ReviewEntity> reviewEntities) {
        List<Review> reviews = new ArrayList<>();
        if (reviewEntities == null || reviewEntities.isEmpty()) {
            return reviews;
        } else {
            reviewEntities.forEach(reviewEntity -> reviews.add(toReview(reviewEntity)));
        }
        return reviews;
    }
    public Review toReview(ReviewEntity reviewEntity) {
        return Review.newBuilder()
                .score(reviewEntity.getScore())
                .platform(reviewEntity.getPlatform())
                .build();
    }
}
