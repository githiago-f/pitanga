package br.edu.ifrs.poa.pitanga_code.domain.coding.entities;

import java.util.Optional;
import java.util.Set;

import br.edu.ifrs.poa.pitanga_code.domain.coding.vo.Difficulty;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "challenges")
public class Challenge {
    @Id
    @Column(name = "challenge_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String creator;

    @Nullable
    @Column(nullable = true, columnDefinition = "TEXT")
    private String customBaseCode;

    @Enumerated(EnumType.STRING)
    private Difficulty difficultyLevel;

    @ManyToMany(targetEntity = Language.class, fetch = FetchType.EAGER)
    private Set<Language> allowedLanguages;

    public boolean checkAllow(Language language) {
        return allowedLanguages == null ||
                allowedLanguages.size() == 0 ||
                allowedLanguages.contains(language);
    }
}
