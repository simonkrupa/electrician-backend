package simon.krupa.electricianbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ReviewDTO> create(@RequestBody ReviewRequest request, @AuthenticationPrincipal UserDetails userDetails){
         return new ResponseEntity<>(reviewService.createReview(request, userDetails.getUsername()), HttpStatus.CREATED);
    }

    @PutMapping(path = "{id}",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ReviewDTO> update(@PathVariable Long id, @RequestBody ReviewRequest request, @AuthenticationPrincipal UserDetails userDetails){
        return new ResponseEntity<>(reviewService.updateReview(id, request, userDetails.getUsername()), HttpStatus.OK);
    }

}
