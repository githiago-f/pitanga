package br.edu.ifrs.poa.pitanga_code.domain.coding.entities;

import java.util.Set;

import br.edu.ifrs.poa.pitanga_code.domain.coding.vo.Difficulty;
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

    @Enumerated(EnumType.STRING)
    private Difficulty difficultyLevel;

    @ManyToMany(targetEntity = Language.class, fetch = FetchType.EAGER)
    private Set<Language> allowedLanguages;

    public Challenge(String title, String description, String creator,
            Difficulty difficultyLevel, Set<Language> allowedLanguages) {
        this.title = title;
        this.description = description;
        this.creator = creator;
        this.difficultyLevel = difficultyLevel;
        this.allowedLanguages = allowedLanguages;

        int size = title.length();
        this.slug = title.toLowerCase().trim().substring(0, size < 100 ? size : 100).replace(" ", "-");
    }

    public boolean checkAllow(Language language) {
        return allowedLanguages == null ||
                allowedLanguages.size() == 0 ||
                allowedLanguages.contains(language);
    }
}
