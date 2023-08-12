package simon.krupa.electricianbackend.domain.request;

public record ClientAuthenticationRequest (
        String email,
        String password
) {
}
