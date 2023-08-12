package simon.krupa.electricianbackend.domain.dto;

import simon.krupa.electricianbackend.domain.Client;

import java.math.BigDecimal;

public record JobDTO (
        Long id,
        String title,
        String description,
        boolean finished,
        BigDecimal price,
        ClientDTO client
) {
}
