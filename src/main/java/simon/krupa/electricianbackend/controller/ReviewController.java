package simon.krupa.electricianbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import simon.krupa.electricianbackend.domain.dto.ReviewDTO;
import simon.krupa.electricianbackend.domain.request.ReviewRequest;
import simon.krupa.electricianbackend.exception.ConflictException;
import simon.krupa.electricianbackend.services.ReviewService;

import javax.print.attribute.standard.Media;
import java.util.List;

@RequestMapping("/api/review")
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private String getCurrentClient() {
        String login =
                SecurityContextHolder.getContext().getAuthentication().getName();
        if (login != null && !login.equals("anonymousUser")) {
            return login;
        }
        return null;
    }
    private final ReviewService reviewService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReviewDTO>> getAll() {
        return new ResponseEntity<>(reviewService.getAll(), HttpStatus.OK);
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewDTO> getById(@PathVariable Long id){
        return new ResponseEntity<>(reviewService.getById(id), HttpStatus.OK);
    }

    @DeleteMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable Long id){
        return new ResponseEntity<>(reviewService.delete(id), HttpStatus.ACCEPTED);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewDTO> create(@RequestBody ReviewRequest request){
        String currentClient = getCurrentClient();
        if (currentClient != null) {
            return new ResponseEntity<>(reviewService.createReview(request, currentClient), HttpStatus.CREATED);
        } else {
            throw new ConflictException("conflict");
        }
    }
}
