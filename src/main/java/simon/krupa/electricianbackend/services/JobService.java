package simon.krupa.electricianbackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import simon.krupa.electricianbackend.domain.Client;
import simon.krupa.electricianbackend.domain.Job;
import simon.krupa.electricianbackend.domain.dto.ClientRegistrationDTO;
import simon.krupa.electricianbackend.domain.dto.JobDTO;
import simon.krupa.electricianbackend.domain.dto.JobDTONoClient;
import simon.krupa.electricianbackend.domain.dto.mapper.ClientDTOMapper;
import simon.krupa.electricianbackend.domain.dto.mapper.JobDTOMapper;
import simon.krupa.electricianbackend.domain.dto.mapper.JobDTOMapperNoClient;
import simon.krupa.electricianbackend.domain.request.JobRequest;
import simon.krupa.electricianbackend.exception.ConflictException;
import simon.krupa.electricianbackend.repositories.ClientRepository;
import simon.krupa.electricianbackend.repositories.JobRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final ClientRepository clientRepository;
    private final JobDTOMapper jobDTOMapper;
    private final JobDTOMapperNoClient jobDTONoClient;
    private final ClientDTOMapper clientDTOMapper;
    public List<JobDTONoClient> getAll(){
        return jobRepository.findAll()
                .stream()
                .map(jobDTONoClient)
                .collect(Collectors.toList());
    }

    public JobDTO createJobRequest(JobRequest request, String currentClient) {
        Job job = new Job();
        job.setTitle(request.title());
        job.setDescription(request.description());
        job.setClient(clientRepository.findByEmail(currentClient).orElseThrow(() -> new ConflictException("wrong email")));
        jobRepository.save(job);
        return new JobDTO(job.getId(), job.getTitle(), job.getDescription(), job.isFinished(), job.getPrice(), clientDTOMapper.apply(job.getClient()));
    }
}
