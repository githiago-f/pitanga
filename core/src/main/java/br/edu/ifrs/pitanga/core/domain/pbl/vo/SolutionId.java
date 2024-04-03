package br.edu.ifrs.pitanga.core.domain.pbl.vo;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class SolutionId {
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE, 
        generator = "solutions_version_seq"
    )
    @SequenceGenerator(name = "solutions_version_seq", allocationSize = 1)
    private Long version;
    @Column(name = "challenge_id")
    private UUID challengeId;
    @Column(name = "submitter_id")
    private UUID submitterId;
}
