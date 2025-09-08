package br.edu.ifrs.poa.pitanga_code.domain.coding.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.edu.ifrs.poa.pitanga_code.domain.coding.vo.SubmissionId;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.entities.Problem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class CodeSubmission {
    @EmbeddedId
    private SubmissionId id;
    private String code;

    private Integer[] passingScenarios;

    @JsonBackReference
    @MapsId("problemId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(insertable = false, updatable = false)
    private Problem problem;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    private Language language;
}
