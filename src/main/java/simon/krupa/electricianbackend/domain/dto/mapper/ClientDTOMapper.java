package simon.krupa.electricianbackend.domain.dto.mapper;

import org.springframework.stereotype.Component;
import simon.krupa.electricianbackend.domain.Client;
import simon.krupa.electricianbackend.domain.dto.ClientDTO;

import java.util.function.Function;
@Component
public class ClientDTOMapper implements Function<Client, ClientDTO> {

    @Override
    public ClientDTO apply(Client client) {
        return new ClientDTO(
                client.getId(),
                client.getFirstName(),
                client.getLastName(),
                client.getEmail(),
                client.getPhoneNumber()
        );
    }
}
