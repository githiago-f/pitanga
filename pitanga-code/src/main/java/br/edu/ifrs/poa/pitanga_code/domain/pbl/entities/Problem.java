package br.edu.ifrs.poa.pitanga_code.domain.pbl.entities;

import java.util.Set;

import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.Language;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.vo.Difficulty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "problems", indexes = {
        @Index(unique = true, columnList = "slug")
})
@NoArgsConstructor
public class Problem {
    @Id
    @Column(name = "problem_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String creator;

    @Column(length = 100, name = "slug")
    private String slug;

    @Enumerated(EnumType.STRING)
    private Difficulty difficultyLevel;

    @ManyToMany(targetEntity = Language.class, fetch = FetchType.LAZY)
    private Set<Language> allowedLanguages;

    @OneToMany(mappedBy = "problem", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Scenario> testingScenarios;

    public Problem(String title, String description, String creator,
            Difficulty difficultyLevel, Set<Language> allowedLanguages) {
        this.title = title;
        this.description = description;
        this.creator = creator;
        this.difficultyLevel = difficultyLevel;
        this.allowedLanguages = allowedLanguages;

        int size = title.length();
        this.slug = title.toLowerCase()
                .substring(0, size < 100 ? size : 100)
                .trim()
                .replace(" ", "-");
    }

    public boolean checkAllow(Language language) {
        return allowedLanguages == null ||
                allowedLanguages.size() == 0 ||
                allowedLanguages.contains(language);
    }
}
