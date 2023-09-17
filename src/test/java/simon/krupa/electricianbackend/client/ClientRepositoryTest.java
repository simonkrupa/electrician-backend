package simon.krupa.electricianbackend.client;

import antlr.BaseAST;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import simon.krupa.electricianbackend.ElectricianBackendApplication;
import simon.krupa.electricianbackend.domain.Client;
import simon.krupa.electricianbackend.domain.Role;
import simon.krupa.electricianbackend.repositories.ClientRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        Client admin = new Client("simon", "krupa",
                "simon.krupa@icloud.com", passwordEncoder.encode("heslo"),
                "090", Role.ROLE_ADMIN);
        clientRepository.save(admin);
    }

    @Test
    void itShouldCheckIfClientExistsByEmail() {
        boolean expected = clientRepository.selectExistsEmail("simon.krupa@icloud.com");
        // then
        assertThat(expected).isTrue();
    }

    @Test
    void itShouldCheckIfClientEmailDoesNotExists() {
        boolean expected = clientRepository.selectExistsEmail("alojz@icloud.com");
        assertThat(expected).isFalse();
    }
}
