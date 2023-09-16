package simon.krupa.electricianbackend.domain.request;

public record ClientRequest(
        String firstName,
        String lastName,
        String email,
        String phoneNumber
) {
}
