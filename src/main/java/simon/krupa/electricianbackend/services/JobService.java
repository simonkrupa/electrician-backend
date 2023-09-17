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
import simon.krupa.electricianbackend.exception.BadRequestException;
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
//    private final EmailSenderService emailSenderService;

    public List<JobDTO> getAll(){
        return jobRepository.findAll()
                .stream()
                .map(jobDTOMapper)
                .collect(Collectors.toList());
    }

    public JobDTO createJobRequest(JobRequest request, String currentClient) {
        try {
            Job job = new Job();
            job.setTitle(request.title());
            job.setDescription(request.description());
            job.setClient(clientRepository.findByEmail(currentClient).orElseThrow(() -> new ConflictException("wrong email")));
            jobRepository.save(job);
//        emailSenderService.sendEmail(System.getenv("MAIL_USERNAME"), String.format("New job request - %s", job.getTitle()), String.format("New job request from %s.\n\nTitle: %s\n%s", job.getClient().getEmail(), job.getTitle(), job.getDescription()));
            return new JobDTO(job.getId(), job.getTitle(), job.getDescription(), job.getStatus(), job.getPrice(), clientDTOMapper.apply(job.getClient()));
        } catch (Exception e) {
            throw new BadRequestException("Bad request");
        }
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
            throw new ResourceNotFoundException(String.format("no job with this %d", id));
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
//                emailSenderService.sendEmail(System.getenv(job.getClient().getEmail()),
//                        String.format("Accepted job request - %s", job.getTitle()),
//                        String.format("Your job request was accepted.\n\nTitle: %s\n%s",
//                                job.getTitle(), job.getDescription()));
                return jobDTOMapper.apply(jobRepository.save(job));
            } else {
                throw new BadRequestException("Bad request");
            }
        } catch (Exception e){
            throw new ResourceNotFoundException(String.format("Not correct id %d", id));
        }
    }

    public JobDTO finishJob(Long id) {
        try {
            Job job = jobRepository.getById(id);
            if (job.getStatus() == Status.ACCEPTED) {
                job.setStatus(Status.FINISHED);
//                emailSenderService.sendEmail(System.getenv(job.getClient().getEmail()),
//                        String.format("Finished job request - %s", job.getTitle()),
//                        String.format("Your job request was finished.\n\nPrice: %.2f",
//                                job.getPrice()));
                return jobDTOMapper.apply(jobRepository.save(job));
            } else {
                throw new BadRequestException("Bad request");
            }
        } catch (Exception e){
            throw new ResourceNotFoundException(String.format("Not correct id %d", id));
        }
    }

    public void deleteJobRequest(Long id, String currentClient) {
        try {
            Job job = jobRepository.getById(id);
            if (currentClient.equals(job.getClient().getEmail())) {
                if (job.getStatus() == Status.REQUESTED) {
                    jobRepository.delete(job);
                } else {
                    throw new ConflictException("job request cannot be deleted.");
                }
//            else if (job.getStatus()==Status.ACCEPTED) {
//                //TODO send notification to approve
//                return null;
//            }
            } else {
                throw new ConflictException("Not correct client");
            }
        } catch (Exception e) {
            throw new ResourceNotFoundException(String.format("No resource with this id %d", id));
        }
    }

    public JobDTO update(Long id, JobRequest request, String currentClient) {
        try {
            Job job = jobRepository.getById(id);
            if (job.getClient().getEmail().equals(currentClient)) {
                if (job.getStatus() == Status.REQUESTED) {
                    if (request.description() != null) {
                        job.setDescription(request.description());
                    }
                    if (request.title() != null) {
                        job.setTitle(request.title());
                    }
                    return jobDTOMapper.apply(jobRepository.save(job));
                } else if (job.getStatus() == Status.ACCEPTED) {
                    if (request.description() != null) {
                        job.setDescription(request.description());
                    }
                    if (request.title() != null) {
                        job.setTitle(request.title());
                    }
//                emailSenderService.sendEmail(System.getenv(System.getenv("MAIL_USERNAME")),
//                        String.format("Updated job request - %s", job.getTitle()),
//                        String.format("Job request %s was updated.\n\nDescription: %s",
//                                job.getTitle(), job.getDescription()));
                    return jobDTOMapper.apply(jobRepository.save(job));
                } else {
                    throw new BadRequestException("Bad request, job finished");
                }
            } else {
                throw new ConflictException("Not correct client");
            }
        } catch (Exception e) {
            throw new BadRequestException("Bad request");
        }
    }
}
