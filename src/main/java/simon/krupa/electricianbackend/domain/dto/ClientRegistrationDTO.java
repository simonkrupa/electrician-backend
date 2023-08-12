package simon.krupa.electricianbackend.domain.dto;

public record ClientRegistrationDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String token
) {
    public ClientRegistrationDTO(Long id, String firstName, String lastName, String email, String phoneNumber, String token){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.token = token;
    }
}
