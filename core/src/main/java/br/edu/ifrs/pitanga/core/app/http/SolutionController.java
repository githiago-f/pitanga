package br.edu.ifrs.pitanga.core.app.http;

import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrs.pitanga.core.app.http.dtos.Solution;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/challenges/{challengeId}/solutions")
public class SolutionController {
    @PutMapping()
    public void createOrUpdate(
        @PathVariable String challengeId, @RequestBody Solution solution) {
    }

    @GetMapping("{solutionId}")
    public String view(
        @PathVariable String challengeId, @PathVariable String solutionId) {
        return new String();
    }
}
