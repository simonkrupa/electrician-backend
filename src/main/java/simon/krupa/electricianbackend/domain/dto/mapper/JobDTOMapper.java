package simon.krupa.electricianbackend.domain.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import simon.krupa.electricianbackend.domain.Job;
import simon.krupa.electricianbackend.domain.dto.ClientDTO;
import simon.krupa.electricianbackend.domain.dto.JobDTO;
import simon.krupa.electricianbackend.domain.dto.JobDTONoClient;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JobDTOMapper implements Function<Job, JobDTO> {

    @Autowired
    private ClientDTOMapper clientDTOMapper;
    @Override
    public JobDTO apply(Job job) {
        return new JobDTO(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.isFinished(),
                job.getPrice(),
                clientDTOMapper.apply(job.getClient())
        );
    }
}
