package br.edu.ifrs.pitanga.core.app.http.dto;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.ifrs.pitanga.core.domain.pbl.Challenge;
import br.edu.ifrs.pitanga.core.domain.pbl.vo.ChallengeLevel;
import br.edu.ifrs.pitanga.core.domain.repositories.specifications.ChallengeSpecification;

public record ChallengePageableFilter(
    @RequestParam(required = false) Optional<Integer> page,
    @RequestParam(required = false) Optional<Integer> size,
    @RequestParam(required = false) ChallengeLevel level
) {
    public Specification<Challenge> getSpec() {
        return new ChallengeSpecification(level);
    }

    public Pageable getPage() {
        Integer pageSize = size.orElse(25);
        if(pageSize > 25) {
            pageSize = 25;
        }
        Pageable pageable = Pageable.ofSize(pageSize);
        return pageable.withPage(page.orElse(0));
    }
}
