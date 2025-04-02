package br.edu.ifrs.pitanga.core.infra.runners.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class Submission {
    protected Long id;
    protected String code;
    protected SubmissionLanguage language;

    public final Integer getBoxId() {
        return (int) (id % 1e7);
    }
}
