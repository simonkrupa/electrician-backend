package simon.krupa.electricianbackend.domain.dto;

public record ClientAuthenticationDTO(
        String token
) {
    public ClientAuthenticationDTO(String token){
        this.token = token;
    }
}
