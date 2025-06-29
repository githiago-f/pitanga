package br.edu.ifrs.poa.pitanga_code.infra.lib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CmdHelper {
    private List<String> argumentString;

    public CmdHelper() {
        argumentString = new ArrayList<>();
    }

    public static CmdHelper builder() {
        return new CmdHelper();
    }

    public CmdHelper run(String... parts) {
        for (String part : parts) {
            argumentString.add(part);
        }

        return this;
    }

    public CmdHelper and() {
        argumentString.add("&&");
        return this;
    }

    public CmdHelper pipe() {
        argumentString.add("|");
        return this;
    }

    public record Output(String out, String err, int exit) {
    }

    public Output build() {
        try {
            String command = String.join(" ", argumentString);
            var process = new ProcessBuilder("sh", "-c", command).start();

            int exitValue = process.waitFor();

            StringBuilder result = new StringBuilder();
            result.append("Exited with code :: " + exitValue);
            process.errorReader().lines().forEach(result::append);

            String err = result.toString();
            result.delete(0, err.length());

            process.inputReader().lines().forEach(result::append);

            return new Output(result.toString(), err, exitValue);
        } catch (IOException | InterruptedException exception) {
            log.error("Failed -> {}", exception);
            return new Output("", exception.getMessage(), Integer.MAX_VALUE);
        }
    }
}
