package br.edu.ifrs.poa.pitanga_code.domain.coding.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "languages")
public class Language {
    @Id
    @Column(name = "language_id")
    private Long id;

    private String name;
    private String sourceFile;

    private String[] compileCMD;

    private String[] runCommand;
    private String[] environment;
}
