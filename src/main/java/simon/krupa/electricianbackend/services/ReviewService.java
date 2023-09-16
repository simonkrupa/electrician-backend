package simon.krupa.electricianbackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import simon.krupa.electricianbackend.domain.Job;
import simon.krupa.electricianbackend.domain.Review;
import simon.krupa.electricianbackend.domain.dto.ReviewDTO;
import simon.krupa.electricianbackend.domain.dto.mapper.ReviewDTOMapper;
import simon.krupa.electricianbackend.domain.request.ReviewRequest;
import simon.krupa.electricianbackend.exception.BadRequestException;
import simon.krupa.electricianbackend.exception.ConflictException;
import simon.krupa.electricianbackend.exception.ResourceNotFoundException;
import simon.krupa.electricianbackend.repositories.JobRepository;
import simon.krupa.electricianbackend.repositories.ReviewRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewDTOMapper reviewDTOMapper;
    private final JobRepository jobRepository;

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
            throw new ResourceNotFoundException(String.format("no review with this %d", id));
        }
    }

    public void delete(Long id) {
        boolean exists = reviewRepository.existsById(id);
        if (!exists){
            throw new ResourceNotFoundException(String.format("no review with this %d", id));
        }
        reviewRepository.deleteById(id);
    }

    public ReviewDTO createReview(ReviewRequest request, String currentClient) {
        try {
            Job job = jobRepository.getById(request.jobId());
            if (job.getClient().getEmail().equals(currentClient) && (request.stars() >= 0 && request.stars() <= 5)) {
                Review review = new Review();
                review.setStars(request.stars());
                review.setDescription(request.description());
                review.setJob(job);
                return reviewDTOMapper.apply(reviewRepository.save(review));
            } else {
                throw new BadRequestException("Bad request");
            }
        } catch (Exception e){
            throw new BadRequestException("Bad request");
        }
    }

    public ReviewDTO updateReview(Long id, ReviewRequest request, String currentClient) {
        try {
            Job job = jobRepository.getById(request.jobId());
            if (job.getClient().getEmail().equals(currentClient) && reviewRepository.existsById(id)) {
                Review review = reviewRepository.getById(id);
                try {
                    if (request.description() != null) {
                        review.setDescription(request.description());
                    }
                    if (request.stars() != null) {
                        if (request.stars() >= 0 && request.stars() <= 5) {
                            review.setStars(request.stars());
                        } else {
                            throw new BadRequestException("Bad request");
                        }
                    }
                    return reviewDTOMapper.apply(this.reviewRepository.save(review));
                } catch (Exception e) {
                    throw new BadRequestException("Bad request");
                }
            } else {
                throw new BadRequestException("Bad request");
            }
        } catch (Exception e) {
            throw new BadRequestException("Bad request");
        }
    }

}
