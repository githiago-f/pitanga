package br.edu.ifrs.poa.pitanga_code.domain.pbl.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.edu.ifrs.poa.pitanga_code.domain.pbl.vo.ScenarioID;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "test_scenarios")
public class Scenario {
    @EmbeddedId
    private ScenarioID id;

    @Column(columnDefinition = "TEXT")
    private String input;
    private Boolean isExample = false;

    @ManyToOne
    @JsonBackReference
    @MapsId("problemId")
    @JoinColumn(insertable = false, updatable = false)
    private Problem problem;

    public Scenario(String input, Boolean isExample) {
        this.input = input;
        this.isExample = isExample;
    }

    public void setId(ScenarioID scenarioID) {
        this.id = scenarioID;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((input == null) ? 0 : input.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Scenario other = (Scenario) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (input == null) {
            if (other.input != null)
                return false;
        } else if (!input.equals(other.input))
            return false;
        return true;
    }
}
