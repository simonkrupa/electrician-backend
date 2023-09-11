package simon.krupa.electricianbackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import simon.krupa.electricianbackend.domain.Review;
import simon.krupa.electricianbackend.domain.dto.ReviewDTO;
import simon.krupa.electricianbackend.domain.dto.mapper.ReviewDTOMapper;
import simon.krupa.electricianbackend.exception.ResourceNotFoundException;
import simon.krupa.electricianbackend.repositories.ReviewRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewDTOMapper reviewDTOMapper;

    public List<ReviewDTO> getAll(){
        return reviewRepository.findAll()
                .stream()
                .map(reviewDTOMapper)
                .collect(Collectors.toList());
    }

    public ReviewDTO getById(Long id) {
        try {
            return reviewDTOMapper.apply(reviewRepository.getById(id));
        } catch (Exception e) {
            throw new ResourceNotFoundException("no review with this id");
        }
    }

    public Void delete(Long id) {
        Review review = reviewRepository.getById(id);
        if (review != null){
            reviewRepository.delete(review);
        } else {
            throw new ResourceNotFoundException("no review with this id");
        }
        return null;
    }
}
