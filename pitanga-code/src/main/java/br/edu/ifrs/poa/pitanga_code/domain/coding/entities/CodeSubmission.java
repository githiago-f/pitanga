package br.edu.ifrs.poa.pitanga_code.domain.coding.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.edu.ifrs.poa.pitanga_code.domain.coding.vo.SubmissionId;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.entities.Problem;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.entities.Scenario;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder
public class CodeSubmission {
    @EmbeddedId
    private SubmissionId id;
    private String code;

    private Integer passingScenarios;

    @Builder.ObtainVia(method = "isAccepted")
    private Boolean accepted;

    @JsonBackReference
    @MapsId("problemId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(insertable = false, updatable = false)
    private Problem problem;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = { CascadeType.ALL })
    private Scenario failingScenario;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    private Language language;

    public Boolean isAccepted() {
        return passingScenarios == problem.getTestingScenarios().size();
    }
}
