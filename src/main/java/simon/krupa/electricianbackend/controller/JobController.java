package simon.krupa.electricianbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import simon.krupa.electricianbackend.domain.Client;
import simon.krupa.electricianbackend.domain.dto.JobDTO;
import simon.krupa.electricianbackend.domain.dto.JobDTONoClient;
import simon.krupa.electricianbackend.domain.request.JobRequest;
import simon.krupa.electricianbackend.exception.ConflictException;
import simon.krupa.electricianbackend.services.JobService;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<List<JobDTONoClient>> getAll(){
        return new ResponseEntity<>(jobService.getAll(), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JobDTO> createJobRequest(@RequestBody JobRequest request) {
        String currentClient = getCurrentClient();
        if (currentClient != null) {
            return new ResponseEntity<>(jobService.createJobRequest(request, currentClient), HttpStatus.CREATED);
        } else {
            throw new ConflictException("conflict");
        }
    }
}
