package br.edu.ifrs.poa.pitanga_code.app.http;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrs.poa.pitanga_code.domain.coding.dtos.Lang;
import br.edu.ifrs.poa.pitanga_code.domain.coding.repository.LanguagesRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping({ "/languages" })
public class LanguagesController {
    private final LanguagesRepository languagesRepository;

    @GetMapping
    public ResponseEntity<List<Lang>> listLanguages() {
        List<Lang> langs = languagesRepository.findLanguageList();
        return ResponseEntity.ok(langs);
    }
}
