package simon.krupa.electricianbackend.domain.dto;

import simon.krupa.electricianbackend.domain.Status;

import java.math.BigDecimal;

public record JobDTONoClient (
        Long id,
        String title,
        String description,
        Status status,
        BigDecimal price
){
}
