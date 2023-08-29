package simon.krupa.electricianbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import simon.krupa.electricianbackend.domain.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
