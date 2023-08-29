package simon.krupa.electricianbackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import simon.krupa.electricianbackend.domain.Client;
import simon.krupa.electricianbackend.domain.Job;
import simon.krupa.electricianbackend.domain.Status;
import simon.krupa.electricianbackend.domain.dto.ClientRegistrationDTO;
import simon.krupa.electricianbackend.domain.dto.JobDTO;
import simon.krupa.electricianbackend.domain.dto.mapper.ClientDTOMapper;
import simon.krupa.electricianbackend.domain.dto.mapper.JobDTOMapper;
import simon.krupa.electricianbackend.domain.request.JobRequest;
import simon.krupa.electricianbackend.exception.ConflictException;
import simon.krupa.electricianbackend.exception.ResourceNotFoundException;
import simon.krupa.electricianbackend.repositories.ClientRepository;
import simon.krupa.electricianbackend.repositories.JobRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final ClientRepository clientRepository;
    private final JobDTOMapper jobDTOMapper;
    private final ClientDTOMapper clientDTOMapper;
    public List<JobDTO> getAll(){
        return jobRepository.findAll()
                .stream()
                .map(jobDTOMapper)
                .collect(Collectors.toList());
    }

    public JobDTO createJobRequest(JobRequest request, String currentClient) {
        Job job = new Job();
        job.setTitle(request.title());
        job.setDescription(request.description());
        job.setClient(clientRepository.findByEmail(currentClient).orElseThrow(() -> new ConflictException("wrong email")));
        jobRepository.save(job);
        return new JobDTO(job.getId(), job.getTitle(), job.getDescription(), job.getStatus(), job.getPrice(), clientDTOMapper.apply(job.getClient()));
    }

    public List<JobDTO> getAllRequestedJobs() {
        return jobRepository.getAllRequestedJobs()
                .stream()
                .map(jobDTOMapper)
                .collect(Collectors.toList());
    }

    public JobDTO getById(Long id) {
        try {
            return jobDTOMapper.apply(jobRepository.getById(id));
        } catch (Exception e){
            throw new ResourceNotFoundException("no job with this id");
        }
    }

    public List<JobDTO> getAllUsersJobs(String currentClient) {
        return jobRepository.getUsersJobs(currentClient)
                .stream()
                .map(jobDTOMapper)
                .collect(Collectors.toList());
    }


    public JobDTO acceptJobRequest(Long id) {
        try {
            Job job = jobRepository.getById(id);
            if (job.getStatus() == Status.REQUESTED) {
                job.setStatus(Status.ACCEPTED);
                return jobDTOMapper.apply(jobRepository.save(job));
            }
            return null;
        } catch (Exception e){
            throw new ResourceNotFoundException("wrong");
        }
    }

    public JobDTO finishJob(Long id) {
        try {
            Job job = jobRepository.getById(id);
            if (job.getStatus() == Status.ACCEPTED) {
                job.setStatus(Status.FINISHED);
                return jobDTOMapper.apply(jobRepository.save(job));
            }
            return null;
        } catch (Exception e){
            throw new ResourceNotFoundException("wrong");
        }
    }
}
