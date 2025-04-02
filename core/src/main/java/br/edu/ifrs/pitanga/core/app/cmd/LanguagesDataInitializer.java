package br.edu.ifrs.pitanga.core.app.cmd;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.core.io.Resource;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class LanguagesDataInitializer implements CommandLineRunner {
    private final String LANGUAGES_FILE = "classpath:db/languages.json";

    private ResourceLoader resourceLoader;

    private record Language(
        Long id,
        String name,
        String source_file,
        String compile_cmd,
        String run_command
    ) {}

    @Override
    public void run(String... args) throws Exception {
        Resource langsFile = resourceLoader.getResource(LANGUAGES_FILE);
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Language>> typeRef = new TypeReference<List<Language>>() {};
        try(var input = langsFile.getInputStream()) {
            List<Language> langs = mapper.readValue(input, typeRef);
            // Long persisted = languagesRepository.count();

            // if(langs.size() == persisted) return;

            for (Language language : langs) {
                // languagesRepository.save(language.toEntity());
            }
        }
    }
}
