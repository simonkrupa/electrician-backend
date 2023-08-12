package simon.krupa.electricianbackend.domain.dto;

public record ClientDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber
) {

}
