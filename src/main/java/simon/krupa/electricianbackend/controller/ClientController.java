package simon.krupa.electricianbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import simon.krupa.electricianbackend.domain.dto.ClientDTO;
import simon.krupa.electricianbackend.domain.request.ClientAuthenticationRequest;
import simon.krupa.electricianbackend.domain.request.ClientRegistrationRequest;
import simon.krupa.electricianbackend.domain.dto.ClientAuthenticationDTO;
import simon.krupa.electricianbackend.domain.dto.ClientRegistrationDTO;
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
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        this.clientService.deleteClient(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientRegistrationDTO> create(@RequestBody ClientRegistrationRequest body) {
        return new ResponseEntity<>(clientService.createClient(body), HttpStatus.CREATED);
    }

    @PostMapping(path = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientAuthenticationDTO> create(@RequestBody ClientAuthenticationRequest body) {
        return new ResponseEntity<>(clientService.authenticateClient(body), HttpStatus.OK);
    }
}
