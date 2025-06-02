package br.edu.ifrs.poa.pitanga_code.domain.pbl.entities;

import br.edu.ifrs.poa.pitanga_code.domain.pbl.vo.ScenarioID;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "test_scenarios")
public class Scenario {
    @EmbeddedId
    private ScenarioID id;

    @Column(columnDefinition = "TEXT")
    private String input;
    @Column(columnDefinition = "TEXT")
    private String output;
    private Boolean isExample = false;

    @ManyToOne
    @JoinColumn(insertable = false, updatable = false)
    private Problem problem;
}
