package br.edu.ifrs.poa.pitanga_code.domain.coding.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.edu.ifrs.poa.pitanga_code.domain.coding.vo.SubmissionId;
import br.edu.ifrs.poa.pitanga_code.domain.coding.vo.SubmissionStatus;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.dto.ScenarioOutput;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.entities.Problem;
import br.edu.ifrs.poa.pitanga_code.infra.ScenarioOutputJsonConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeSubmission {
    @EmbeddedId
    private SubmissionId id;
    private String code;

    private String creatorId;

    private Integer lastScenarioIndex;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private SubmissionStatus status = SubmissionStatus.PENDING;

    @JsonBackReference
    @MapsId("problemId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(insertable = false, updatable = false)
    private Problem problem;

    @Convert(converter = ScenarioOutputJsonConverter.class)
    private ScenarioOutput failingScenario;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    private Language language;

    public void setId(SubmissionId submissionId) {
        this.id = submissionId;
    }

    public void setFailingScenario(ScenarioOutput scenario) {
        this.failingScenario = scenario;
    }

    public void setLastScenarioIndex(Integer scenarioIndex) {
        lastScenarioIndex = scenarioIndex;
        var isAccepted = scenarioIndex - 1 == problem.getTestingScenarios().size();
        status = isAccepted ? SubmissionStatus.ACCEPTED : SubmissionStatus.FAILED;
    }
}
