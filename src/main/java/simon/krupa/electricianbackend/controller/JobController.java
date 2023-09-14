package simon.krupa.electricianbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import simon.krupa.electricianbackend.domain.dto.JobDTO;
import simon.krupa.electricianbackend.domain.request.JobRequest;
import simon.krupa.electricianbackend.exception.ConflictException;
import simon.krupa.electricianbackend.services.JobService;

import java.util.List;

@RestController
@RequestMapping("/api/job")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    private String getCurrentClient() {
        String login =
                SecurityContextHolder.getContext().getAuthentication().getName();
        if (login != null && !login.equals("anonymousUser")) {
            return login;
        }
        return null;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<JobDTO>> getAll() {
        return new ResponseEntity<>(jobService.getAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<JobDTO> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(jobService.getById(id), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<JobDTO> createJobRequest(@RequestBody JobRequest request) {
        String currentClient = getCurrentClient();
        if (currentClient != null) {
            return new ResponseEntity<>(jobService.createJobRequest(request, currentClient), HttpStatus.CREATED);
        } else {
            throw new ConflictException("conflict");
        }
    }

    @GetMapping(path = "/requested", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<JobDTO>> getAllRequestedJobs() {
        return new ResponseEntity<>(jobService.getAllRequestedJobs(), HttpStatus.OK);
    }

    @GetMapping(path = "/clients", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<JobDTO>> getAllUsersJobs() {
        String currentClient = getCurrentClient();
        if (currentClient != null) {
            return new ResponseEntity<>(jobService.getAllUsersJobs(currentClient), HttpStatus.OK);
        } else {
            throw new ConflictException("conflict");
        }
    }

    @PostMapping(path = "/accept/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobDTO> acceptJobRequest(@PathVariable("id") Long id){
        return new ResponseEntity<>(jobService.acceptJobRequest(id), HttpStatus.OK);
    }

    @PostMapping(path = "/finish/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobDTO> finishJob(@PathVariable("id") Long id){
        return new ResponseEntity<>(jobService.finishJob(id), HttpStatus.OK);
    }

    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteJobRequest(@PathVariable("id") Long id){
        return new ResponseEntity<>(jobService.deleteJobRequest(id, getCurrentClient()), HttpStatus.OK);
    }

    @PutMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<JobDTO> updateJob(@PathVariable Long id, @RequestBody JobRequest request){
        String currentClient = getCurrentClient();
        if (currentClient != null) {
            return new ResponseEntity<>(jobService.update(id, request, currentClient), HttpStatus.OK);
        } else {
            throw new ConflictException("conflict");
        }
    }
}
