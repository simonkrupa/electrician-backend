package simon.krupa.electricianbackend.domain.request;

public record ReviewRequest (
        Integer stars,
        String description,
        Long jobId
) {
}
