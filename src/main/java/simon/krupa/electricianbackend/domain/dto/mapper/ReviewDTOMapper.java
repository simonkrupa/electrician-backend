package simon.krupa.electricianbackend.domain.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import simon.krupa.electricianbackend.domain.Review;
import simon.krupa.electricianbackend.domain.dto.ReviewDTO;

import java.util.function.Function;
@Component
@RequiredArgsConstructor
public class ReviewDTOMapper implements Function<Review, ReviewDTO> {

    private final JobDTOMapper jobDTOMapper;
    @Override
    public ReviewDTO apply(Review review) {
        return new ReviewDTO(
                review.getId(),
                review.getStars(),
                review.getCreationDate(),
                review.getDescription(),
                jobDTOMapper.apply(review.getJob())
        );
    }
}
