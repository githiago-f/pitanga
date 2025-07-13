package br.edu.ifrs.poa.pitanga_code.domain.pbl.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.Language;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.vo.Difficulty;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.vo.ScenarioID;
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

    @OneToMany(mappedBy = "problem", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Scenario> testingScenarios;

    public Problem(String title, String slug, String description, String creator,
            Difficulty difficultyLevel, Set<Language> allowedLanguages) {
        this.title = title;
        this.description = description;
        this.creator = creator;
        this.difficultyLevel = difficultyLevel;
        this.allowedLanguages = allowedLanguages;
        this.slug = slug;

        this.testingScenarios = new ArrayList<>();
    }

    public void includeScenario(Integer id, Scenario scenario) {
        scenario.setId(new ScenarioID(id, this.id));
        scenario.setProblem(this);
        this.testingScenarios.add(scenario);
    }

    public boolean checkAllow(Language language) {
        return allowedLanguages == null ||
                allowedLanguages.size() == 0 ||
                allowedLanguages.contains(language);
    }
}
