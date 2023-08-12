package simon.krupa.electricianbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import simon.krupa.electricianbackend.domain.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Long > {

}
