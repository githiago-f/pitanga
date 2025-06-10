package br.edu.ifrs.poa.pitanga_code.domain.pbl.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ScenarioID {
    private Long id;
    private Long problemId;
}
