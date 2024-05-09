package br.edu.ifrs.pitanga.core.domain.pbl.vo;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class SolutionId {
    private Long version;
    @Column(name = "challenge_id")
    private UUID challengeId;
    @Column(name = "submitter_id")
    private String submitterId;

    public String toString() {
        return submitterId + "/" + challengeId.toString() + "/" + version;
    }
}
