package simon.krupa.electricianbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import simon.krupa.electricianbackend.domain.Client;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    @Query("select c from Client c where c.id=:id")
    Optional<Client> selectClientById(@Param("id") Long id);
    @Query("select c from Client c where c.email=:email")
    Optional<Client> isEmailUsed(@Param("email") String email);

    @Query("select c from Client c where c.email=:email")
    Optional<Client> findByEmail(@Param("email") String email);
}
