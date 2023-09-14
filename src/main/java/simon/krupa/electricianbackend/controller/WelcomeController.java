package simon.krupa.electricianbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import simon.krupa.electricianbackend.domain.dto.ClientDTO;

import java.util.List;

@RestController
@RequestMapping("/api/home")
public class WelcomeController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity welcomePage(){
        return ResponseEntity.ok("Welcome to home page.");
    }

}
