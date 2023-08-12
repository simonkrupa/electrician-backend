package simon.krupa.electricianbackend.services;

import org.springframework.stereotype.Service;
import simon.krupa.electricianbackend.domain.Client;
import simon.krupa.electricianbackend.domain.dto.ClientDTO;
import simon.krupa.electricianbackend.domain.dto.mapper.ClientDTOMapper;
import simon.krupa.electricianbackend.domain.request.ClientRegistrationRequest;
import simon.krupa.electricianbackend.exception.ConflictException;
import simon.krupa.electricianbackend.exception.ResourceNotFoundException;
import simon.krupa.electricianbackend.repositories.ClientRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientDTOMapper clientDTOMapper;

    public ClientService(ClientRepository clientRepository, ClientDTOMapper clientDTOMapper) {
        this.clientRepository = clientRepository;
        this.clientDTOMapper = clientDTOMapper;
    }

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

    public ClientDTO createClient(ClientRegistrationRequest body) {
        if(!this.clientRepository.isEmailUsed(body.email()).isPresent()) {
            Client client = new Client(
                    body.firstName(),
                    body.lastName(),
                    body.email(),
                    body.password(),
                    body.phoneNumber(),
                    body.role());
            return clientDTOMapper.apply(clientRepository.save(client));
        } else {
            throw new ConflictException("email [%s] already registered".formatted(body.email()));
        }
    }

}
