package br.edu.ifrs.poa.pitanga_code.domain.coding.entities;

import java.util.Set;

import br.edu.ifrs.poa.pitanga_code.domain.coding.vo.Difficulty;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "challenges", indexes = {
        @Index(unique = true, columnList = "slug")
})
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

    @Column(length = 100, name = "slug")
    private String slug;

    @Nullable
    @Column(nullable = true, columnDefinition = "TEXT")
    private String customBaseCode;

    @Enumerated(EnumType.STRING)
    private Difficulty difficultyLevel;

    @ManyToMany(targetEntity = Language.class, fetch = FetchType.EAGER)
    private Set<Language> allowedLanguages;

    public Challenge(String title, String description, String creator, String customBaseCode,
            Difficulty difficultyLevel, Set<Language> allowedLanguages) {
        this.title = title;
        this.description = description;
        this.creator = creator;
        this.customBaseCode = customBaseCode;
        this.difficultyLevel = difficultyLevel;
        this.allowedLanguages = allowedLanguages;
        this.slug = title.toLowerCase().trim().substring(0, 100).replace(" ", "-");
    }

    public boolean checkAllow(Language language) {
        return allowedLanguages == null ||
                allowedLanguages.size() == 0 ||
                allowedLanguages.contains(language);
    }
}
