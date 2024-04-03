package br.edu.ifrs.pitanga.core.domain.pbl.services;

import org.springframework.stereotype.Service;

import br.edu.ifrs.pitanga.core.domain.pbl.Solution;
import br.edu.ifrs.pitanga.core.domain.pbl.services.commands.SubmitSolutionCommand;
import br.edu.ifrs.pitanga.core.domain.repositories.SolutionsRepository;

@Service
public class SolutionsExecutingService {
    private SolutionsRepository solutionsRepository;

    public SolutionsExecutingService(SolutionsRepository solutionsRepository) {
        this.solutionsRepository = solutionsRepository;
    }

    public void handle(SubmitSolutionCommand submission) {
        Solution solution = solutionsRepository.save(submission.toEntity());
    }
}
