package simon.krupa.electricianbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<JobDTO>> getAll() {
        return new ResponseEntity<>(jobService.getAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<JobDTO> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(jobService.getById(id), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<JobDTO> createJobRequest(@RequestBody JobRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(jobService.createJobRequest(request, userDetails.getUsername()), HttpStatus.CREATED);
    }

    @GetMapping(path = "/requested", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<JobDTO>> getAllRequestedJobs() {
        return new ResponseEntity<>(jobService.getAllRequestedJobs(), HttpStatus.OK);
    }

    @GetMapping(path = "/clients", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<JobDTO>> getAllUsersJobs(@AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(jobService.getAllUsersJobs(userDetails.getUsername()), HttpStatus.OK);
    }

    @PutMapping(path = "/accept/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobDTO> acceptJobRequest(@PathVariable("id") Long id){
        return new ResponseEntity<>(jobService.acceptJobRequest(id), HttpStatus.OK);
    }

    @PutMapping(path = "/finish/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobDTO> finishJob(@PathVariable("id") Long id){
        return new ResponseEntity<>(jobService.finishJob(id), HttpStatus.OK);
    }

    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteJobRequest(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails){
        jobService.deleteJobRequest(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<JobDTO> updateJob(@PathVariable Long id, @RequestBody JobRequest request, @AuthenticationPrincipal UserDetails userDetails){
        return new ResponseEntity<>(jobService.update(id, request, userDetails.getUsername()), HttpStatus.OK);
    }
}
