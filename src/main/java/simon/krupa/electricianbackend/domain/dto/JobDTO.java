package simon.krupa.electricianbackend.domain.dto;

import simon.krupa.electricianbackend.domain.Client;
import simon.krupa.electricianbackend.domain.Status;

import java.math.BigDecimal;

public record JobDTO (
        Long id,
        String title,
        String description,
        Status status,
        BigDecimal price,
        ClientDTO client
) {
}
