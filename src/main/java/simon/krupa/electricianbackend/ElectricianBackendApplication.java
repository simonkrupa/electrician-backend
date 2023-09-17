package simon.krupa.electricianbackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import simon.krupa.electricianbackend.domain.Client;
import simon.krupa.electricianbackend.domain.Role;
import simon.krupa.electricianbackend.repositories.ClientRepository;

@SpringBootApplication
public class ElectricianBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElectricianBackendApplication.class, args);
    }


    @Component
    public class CommandLineAppStartupRunner implements CommandLineRunner {
        @Autowired
        ClientRepository clientRepository;

        @Autowired
        PasswordEncoder passwordEncoder;

        @Override
        public void run(String...args) throws Exception {
            Client admin = new Client("simon", "krupa", "simon.krupa@icloud.com", passwordEncoder.encode("heslo"), "090", Role.ROLE_ADMIN);
            clientRepository.save(admin);
        }
    }
}
