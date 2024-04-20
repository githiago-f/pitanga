package br.edu.ifrs.pitanga.core.app.http.dto;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

public record ChallengePageable(
    @RequestParam Optional<Integer> page,
    @RequestParam Optional<Integer> size
) {
    public Pageable toPageable() {
        Pageable pageable = Pageable.ofSize(size.orElse(25));
        pageable.withPage(page.orElse(0));
        return pageable;
    }
}
