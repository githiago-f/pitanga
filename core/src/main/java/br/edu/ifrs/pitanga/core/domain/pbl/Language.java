package br.edu.ifrs.pitanga.core.domain.pbl;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "languages")
public class Language {
    @Id
    private Long id;

    private String name;
    private String sourceFile;
    private String compileCmd;
    private String runCommand;
}
