package simon.krupa.electricianbackend.domain.dto;

import java.math.BigDecimal;

public record JobDTONoClient (
        Long id,
        String title,
        String description,
        boolean finished,
        BigDecimal price
){
}
