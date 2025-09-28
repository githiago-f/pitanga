package br.edu.ifrs.poa.pitanga_code.app.services;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.Language;
import br.edu.ifrs.poa.pitanga_code.domain.coding.repository.LanguagesRepository;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class LanguageDataInitializer implements CommandLineRunner {
    private final String LANGUAGES_FILE = "classpath:db/languages.json";

    private final ResourceLoader resourceLoader;
    private final LanguagesRepository languagesRepository;

    private record LanguageDTO(
            Long id,
            String name,
            String source_file,
            String[] environment,
            String compile_cmd,
            String run_command) {

        public Language toEntity() {
            return Language.builder()
                    .id(id)
                    .name(name)
                    .environment(environment)
                    .runCommand(run_command.split(" "))
                    .compileCMD(compile_cmd.split(" "))
                    .sourceFile(source_file)
                    .build();
        }
    }

    @Override
    public void run(String... args) throws Exception {
        Resource langsFile = resourceLoader.getResource(LANGUAGES_FILE);
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<LanguageDTO>> typeRef = new TypeReference<List<LanguageDTO>>() {
        };
        try (var input = langsFile.getInputStream()) {
            List<LanguageDTO> langs = mapper.readValue(input, typeRef);
            Long persisted = languagesRepository.count();

            if (langs.size() == persisted)
                return;

            for (LanguageDTO language : langs) {
                languagesRepository.save(language.toEntity());
            }
        }
    }
}
