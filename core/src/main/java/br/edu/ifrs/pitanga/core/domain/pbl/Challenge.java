package br.edu.ifrs.pitanga.core.domain.pbl;

import java.util.List;
import java.util.UUID;

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

    @OneToMany(mappedBy = "id.challengeId", fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
    private List<Validation> validations;
    private Integer creatorId;

    public void setValidations(List<Validation> validations) {
        this.validations = validations;
    }
}
