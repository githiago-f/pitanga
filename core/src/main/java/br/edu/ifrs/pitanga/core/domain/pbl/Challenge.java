package br.edu.ifrs.pitanga.core.domain.pbl;

import java.util.List;
import java.util.UUID;

import br.edu.ifrs.pitanga.core.domain.pbl.vo.ChallengeLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "challenges")
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String title;
    private String description;
    private String baseCode;

    @Enumerated(EnumType.STRING)
    private ChallengeLevel level;

    @OneToMany(mappedBy = "id.challengeId", fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
    private List<Validation> validations;
    private String creatorId;

    public void setValidations(List<Validation> validations) {
        this.validations = validations;
    }
}
