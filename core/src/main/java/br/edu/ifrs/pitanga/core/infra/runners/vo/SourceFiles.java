package br.edu.ifrs.pitanga.core.infra.runners.vo;

import br.edu.ifrs.pitanga.core.infra.runners.config.FilesConfiguration;
import lombok.Getter;

@Getter
public class SourceFiles {
    private String workdir, boxdir;
    private String sourceFile, stdinFile, stdoutFile, stderrFile, compileOut;

    public SourceFiles(String workdir, String sourceFile, FilesConfiguration files) {
        this.workdir = workdir;
        this.boxdir = workdir + "/box";
        this.sourceFile = boxdir + "/" + sourceFile;
        this.stdinFile = boxdir + "/" + files.getStdin();
        this.stdoutFile = boxdir + "/" + files.getStdout();
        this.stderrFile = boxdir + "/" + files.getStderr();
        this.compileOut = boxdir + "/compile_out.txt";
    }
}
