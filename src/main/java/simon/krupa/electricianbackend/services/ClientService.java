package simon.krupa.electricianbackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import simon.krupa.electricianbackend.config.JwtService;
import simon.krupa.electricianbackend.domain.Client;
import simon.krupa.electricianbackend.domain.Role;
import simon.krupa.electricianbackend.domain.dto.ClientDTO;
import simon.krupa.electricianbackend.domain.dto.mapper.ClientDTOMapper;
import simon.krupa.electricianbackend.domain.request.ClientAuthenticationRequest;
import simon.krupa.electricianbackend.domain.request.ClientRegistrationRequest;
import simon.krupa.electricianbackend.domain.dto.ClientAuthenticationDTO;
import simon.krupa.electricianbackend.domain.dto.ClientRegistrationDTO;
import simon.krupa.electricianbackend.exception.ConflictException;
import simon.krupa.electricianbackend.exception.ResourceNotFoundException;
import simon.krupa.electricianbackend.repositories.ClientRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientDTOMapper clientDTOMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public List<ClientDTO> getAllClients(){
        return clientRepository.findAll()
                .stream()
                .map(clientDTOMapper)
                .collect(Collectors.toList());
    }

    public ClientDTO getClientById(Long id){
        return clientRepository.selectClientById(id)
                .map(clientDTOMapper)
                .orElseThrow(() -> new ResourceNotFoundException("client with id [%s] not found".formatted(id)
                ));
    }

    public void deleteClient(Long id){
        Client client = clientRepository.selectClientById(id)
                .orElseThrow(() -> new ResourceNotFoundException("client with id [%s] not found".formatted(id)
                ));
        clientRepository.delete(client);
    }

    public ClientRegistrationDTO createClient(ClientRegistrationRequest body) {
        if(!this.clientRepository.isEmailUsed(body.email()).isPresent()) {
            Client client = new Client(
                    body.firstName(),
                    body.lastName(),
                    body.email(),
                    passwordEncoder.encode(body.password()),
                    body.phoneNumber(),
                    Role.USER);
            clientRepository.save(client);
            var jwtToken = jwtService.generateToken(client);
            return new ClientRegistrationDTO(client.getId(), client.getFirstName(), client.getLastName(),
                    client.getEmail(), client.getPhoneNumber(), jwtToken);
        } else {
            throw new ConflictException("email [%s] already registered".formatted(body.email()));
        }
    }

    public ClientAuthenticationDTO authenticateClient(ClientAuthenticationRequest body){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        body.email(),
                        body.password()
                )
        );
        Client client = clientRepository.findByEmail(body.email())
                .orElseThrow(() -> new ResourceNotFoundException("Wrong credentials"));
        var jwtToken = jwtService.generateToken(client);
        return new ClientAuthenticationDTO(jwtToken);
    }

}
