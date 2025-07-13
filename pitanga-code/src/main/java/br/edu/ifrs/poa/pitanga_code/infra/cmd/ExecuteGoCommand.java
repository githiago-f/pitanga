package br.edu.ifrs.poa.pitanga_code.infra.cmd;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.Language;
import br.edu.ifrs.poa.pitanga_code.domain.coding.repository.LanguagesRepository;
import br.edu.ifrs.poa.pitanga_code.infra.sandbox.SandboxProvider;
import br.edu.ifrs.poa.pitanga_code.infra.sandbox.dto.SandboxResult;
import br.edu.ifrs.poa.pitanga_code.infra.sandbox.dto.SandboxRunRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExecuteGoCommand implements CommandLineRunner {
    private final LanguagesRepository languagesRepository;
    private final SandboxProvider sandboxProvider;

    @Override
    public void run(String... args) throws Exception {
        log.info("Running execute Go in sandbox");
        Optional<Language> lang = languagesRepository.findById(6l);
        if (lang.isEmpty()) {
            log.info("Invalid language, there shuld be a language with id = 6");
            return;
        }
        SandboxRunRequest srr = new SandboxRunRequest(
                "package main\nimport \"fmt\"" +
                        "\nfunc main(){\n\tfmt.Println(\"Hello, World!\")\n}",
                List.of(""), lang.get());

        List<SandboxResult> res = sandboxProvider.execute(srr);

        log.info("{}", res);
        Assert.isTrue(res.getFirst().output().equals("Hello, World!"),
                "Response from sandbox should be \"Hello, World!\"");
    }
}
