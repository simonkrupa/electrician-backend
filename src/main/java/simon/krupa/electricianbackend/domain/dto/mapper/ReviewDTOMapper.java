package simon.krupa.electricianbackend.domain.dto.mapper;

import org.springframework.stereotype.Component;
import simon.krupa.electricianbackend.domain.Review;
import simon.krupa.electricianbackend.domain.dto.ReviewDTO;

import java.util.function.Function;
@Component
public class ReviewDTOMapper implements Function<Review, ReviewDTO> {

    @Override
    public ReviewDTO apply(Review review) {
        return new ReviewDTO(
                review.getId(),
                review.getStars(),
                review.getCreationDate(),
                review.getDescription(),
                review.getJob()
        );
    }
}
