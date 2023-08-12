package simon.krupa.electricianbackend.domain.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import simon.krupa.electricianbackend.domain.Job;
import simon.krupa.electricianbackend.domain.dto.JobDTO;
import simon.krupa.electricianbackend.domain.dto.JobDTONoClient;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JobDTOMapperNoClient implements Function<Job, JobDTONoClient> {

    @Override
    public JobDTONoClient apply(Job job) {
        return new JobDTONoClient(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.isFinished(),
                job.getPrice()
        );
    }
}
