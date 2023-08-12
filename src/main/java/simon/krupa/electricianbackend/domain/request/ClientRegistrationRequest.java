package simon.krupa.electricianbackend.domain.request;

import simon.krupa.electricianbackend.domain.Role;

public record ClientRegistrationRequest (
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String password,
        Role role
){
}
