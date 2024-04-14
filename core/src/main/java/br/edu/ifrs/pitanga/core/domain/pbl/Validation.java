package br.edu.ifrs.pitanga.core.domain.pbl;

import br.edu.ifrs.pitanga.core.domain.pbl.vo.ValidationId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "validations")
public class Validation {
    @EmbeddedId
    private ValidationId id;
    private String testInput;
    private String expectedOutput;
}
