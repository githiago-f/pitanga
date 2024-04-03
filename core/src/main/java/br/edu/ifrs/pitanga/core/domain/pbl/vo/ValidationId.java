package br.edu.ifrs.pitanga.core.domain.pbl.vo;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ValidationId {
    @Column(name = "id")
    private Long id;
    @Column(name = "challenge_id")
    private UUID challengeId;
}
