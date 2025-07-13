package br.edu.ifrs.poa.pitanga_code.domain.coding.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.edu.ifrs.poa.pitanga_code.domain.coding.vo.SubmissionId;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.entities.Problem;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Entity
@Getter
@AllArgsConstructor
public class Submission {
    @EmbeddedId
    private SubmissionId id;
    private String code;
    private String status;
    private Integer passedTestCases;

    @JsonBackReference
    @MapsId("problemId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(insertable = false, updatable = false)
    private Problem problem;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    private Language language;
}
