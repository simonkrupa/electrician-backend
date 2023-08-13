package simon.krupa.electricianbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import simon.krupa.electricianbackend.domain.Job;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long > {
    @Query("select j from Job j where j.status = 'REQUESTED' ")
    List<Job> getAllRequestedJobs();

    @Query("select j from Job j where j.client = (select c.id from Client c where c.email=:email)")
    List<Job> getUsersJobs(@Param("email") String email);
}
