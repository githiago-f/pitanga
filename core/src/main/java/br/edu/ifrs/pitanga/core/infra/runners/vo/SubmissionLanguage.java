package br.edu.ifrs.pitanga.core.infra.runners.vo;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class SubmissionLanguage {
    protected String name;
    protected String sourceFile;
    protected String compileCmd;
    protected String runCommand;
}
