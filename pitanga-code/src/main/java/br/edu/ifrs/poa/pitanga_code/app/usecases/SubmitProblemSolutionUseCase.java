package br.edu.ifrs.poa.pitanga_code.app.usecases;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import br.edu.ifrs.poa.pitanga_code.app.dtos.SubmissionRequest;
import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.CodeSubmission;
import br.edu.ifrs.poa.pitanga_code.domain.coding.errors.LanguageNotFoundException;
import br.edu.ifrs.poa.pitanga_code.domain.coding.repository.CreateSubmissionRepository;
import br.edu.ifrs.poa.pitanga_code.domain.coding.repository.LanguagesRepository;
import br.edu.ifrs.poa.pitanga_code.domain.coding.vo.SubmissionId;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.errors.ProblemNotFoundException;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.repository.ReadProblemsRepository;
import br.edu.ifrs.poa.pitanga_code.infra.lib.interfaces.Mediator;
import static br.edu.ifrs.poa.pitanga_code.infra.lib.interfaces.Mediator.Message;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Setter
@RequestScope
@RequiredArgsConstructor
public class SubmitProblemSolutionUseCase {
    private final CreateSubmissionRepository submissionsRepository;
    private final ReadProblemsRepository problemsRepository;
    private final LanguagesRepository languagesRepository;
    private final Mediator mediator;

    private Authentication user;

    public CodeSubmission execute(SubmissionRequest runCommand) {
        var language = languagesRepository.findById(runCommand.languageId());
        var problem = problemsRepository.findByIdWithExamples(runCommand.problemId());

        if (problem.isEmpty()) {
            log.error("Problem(id={}) not found when submiting a solution", runCommand.problemId());
            throw new ProblemNotFoundException(runCommand.problemId());
        }

        if (language.isEmpty()) {
            log.error("Language(id={}) not found when submiting a solution", runCommand.languageId());
            throw new LanguageNotFoundException(runCommand.languageId());
        }

        var lastSubmission = submissionsRepository.findByProblemIdOderedDesc(
                problem.get().getId(), user.getName());

        long lastId = lastSubmission.stream().map(i -> i.getId().getId()).findFirst().orElse(0l) + 1l;

        CodeSubmission submissionData = CodeSubmission.builder()
                .creatorId(user.getName())
                .id(new SubmissionId(lastId, problem.get().getId()))
                .code(runCommand.code())
                .language(language.get())
                .problem(problem.get())
                .build();

        CodeSubmission submission = submissionsRepository.save(submissionData);

        mediator.dispatch(new Message<>("new-submission", submission.getId()));

        return submissionData;
    }
}
