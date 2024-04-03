package br.edu.ifrs.pitanga.core.domain.pbl;

import br.edu.ifrs.pitanga.core.domain.pbl.vo.ValidationId;
import jakarta.persistence.*;

@Entity(name = "validations")
public class Validation {
    @EmbeddedId
    private ValidationId id;
    private String testInput;
    private String expectedOutput;
}
