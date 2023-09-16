package simon.krupa.electricianbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import simon.krupa.electricianbackend.domain.dto.ClientDTO;
import simon.krupa.electricianbackend.domain.request.ClientAuthenticationRequest;
import simon.krupa.electricianbackend.domain.request.ClientRegistrationRequest;
import simon.krupa.electricianbackend.domain.dto.ClientAuthenticationDTO;
import simon.krupa.electricianbackend.domain.dto.ClientRegistrationDTO;
import simon.krupa.electricianbackend.domain.request.ClientRequest;
import simon.krupa.electricianbackend.services.ClientService;

import java.util.List;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClientDTO>> getAll(){
        return new ResponseEntity<>(clientService.getAllClients(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDTO> getById(@PathVariable("id") Long id){
        return new ResponseEntity<>(clientService.getClientById(id), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        this.clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientRegistrationDTO> create(@RequestBody ClientRegistrationRequest request) {
        return new ResponseEntity<>(clientService.createClient(request), HttpStatus.CREATED);
    }

    @PostMapping(path = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientAuthenticationDTO> authenticate(@RequestBody ClientAuthenticationRequest request) {
        return new ResponseEntity<>(clientService.authenticateClient(request), HttpStatus.OK);
    }

//    @PostMapping(path = "/register/admin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<ClientRegistrationDTO> createAdmin(@RequestBody ClientRegistrationRequest request) {
//        return new ResponseEntity<>(clientService.createAdmin(request), HttpStatus.CREATED);
//    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDTO> update(@RequestBody ClientRequest request, @PathVariable Long id, @AuthenticationPrincipal UserDetails currentUser){
        return new ResponseEntity<>(clientService.update(request, id, currentUser), HttpStatus.OK);
    }
}
