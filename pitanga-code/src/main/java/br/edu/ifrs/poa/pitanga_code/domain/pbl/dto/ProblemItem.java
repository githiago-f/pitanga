package br.edu.ifrs.poa.pitanga_code.domain.pbl.dto;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifrs.poa.pitanga_code.domain.pbl.entities.Problem;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.entities.Scenario;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.vo.Difficulty;
import lombok.Getter;

@Getter
public class ProblemItem {
    private final Long id;
    private final String slug;
    private final String title;
    private final Difficulty difficulty;
    private final List<Scenario> scenarios;

    public ProblemItem(Problem problem) {
        id = problem.getId();
        slug = problem.getSlug();
        title = problem.getTitle();
        difficulty = problem.getDifficultyLevel();
        scenarios = new ArrayList<>();

        problem.getTestingScenarios().forEach(i -> {
            if (i.getIsExample()) {
                scenarios.add(i);
            }
        });
    }
}
