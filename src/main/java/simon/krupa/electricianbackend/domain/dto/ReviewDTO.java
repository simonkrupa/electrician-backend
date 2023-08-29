package simon.krupa.electricianbackend.domain.dto;

import simon.krupa.electricianbackend.domain.Job;

import java.util.Date;

public record ReviewDTO (
        Long id,
        Integer stars,
        Date creationDate,
        String description,
        Job job
) {
}
