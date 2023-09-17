package simon.krupa.electricianbackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
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
import simon.krupa.electricianbackend.domain.request.ClientRequest;
import simon.krupa.electricianbackend.exception.BadRequestException;
import simon.krupa.electricianbackend.exception.ConflictException;
import simon.krupa.electricianbackend.exception.ResourceNotFoundException;
import simon.krupa.electricianbackend.repositories.ClientRepository;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientDTOMapper clientDTOMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public boolean isValidPhoneNumber(String phoneNumber) {
        Pattern regex = Pattern.compile("\\d{10}");
        Matcher matcher = regex.matcher(phoneNumber);
        return matcher.matches();
    }

    public List<ClientDTO> getAllClients(){
        return clientRepository.findAll()
                .stream()
                .map(clientDTOMapper)
                .collect(Collectors.toList());
    }

    public ClientDTO getClientById(Long id){
        try {
            return clientDTOMapper.apply(clientRepository.getById(id));
        } catch (Exception e){
            throw new ResourceNotFoundException(String.format("no client with this %d", id));
        }
    }

    public void deleteClient(Long id){
        boolean exists = clientRepository.existsById(id);
        if(!exists){
            throw new ResourceNotFoundException(String.format("no client with this id %d", id));
        }
        clientRepository.deleteById(id);
    }

    public ClientRegistrationDTO createClient(ClientRegistrationRequest body) {
        try {
            if (this.clientRepository.isEmailUsed(body.email()).isEmpty()) {
                if (isValidPhoneNumber(body.phoneNumber())) {
                    Client client = new Client(
                            body.firstName(),
                            body.lastName(),
                            body.email(),
                            passwordEncoder.encode(body.password()),
                            body.phoneNumber(),
                            Role.ROLE_USER);
                    clientRepository.save(client);
                    var jwtToken = jwtService.generateToken(client);
                    return new ClientRegistrationDTO(client.getId(), client.getFirstName(),
                            client.getLastName(), client.getEmail(), client.getPhoneNumber(),
                            jwtToken);
                } else {
                    throw new BadRequestException("Bad request");
                }
            } else {
                throw new ConflictException(("email [%s] already registered or " +
                        "phone number not in correct format").formatted(body.email()));
            }
        } catch (Exception e){
            throw new BadRequestException("Bad request");
        }
    }

    //TODO
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

    public ClientDTO update(ClientRequest request, Long id, UserDetails currentUser) {
        try {
            Client client = clientRepository.getById(id);
            if (currentUser.getUsername().equals(client.getEmail())) {
                try {
                    if (request.email() != null) {
                        client.setEmail(request.email());
                    }
                    if (request.firstName() != null) {
                        client.setFirstName(request.firstName());
                    }
                    if (request.lastName() != null) {
                        client.setLastName(request.lastName());
                    }
                    if (request.phoneNumber() != null) {
                        if (isValidPhoneNumber(request.phoneNumber())) {
                            client.setPhoneNumber(request.phoneNumber());
                        } else {
                            throw new ConflictException("Wrong number format");
                        }
                    }
                    return clientDTOMapper.apply(clientRepository.save(client));
                } catch (Exception e) {
                    throw new BadRequestException("Bad request");
                }
            } else {
                throw new BadRequestException("No correct client");
            }
        } catch (Exception e){
            throw new BadRequestException("Bad request");
        }
    }
}
