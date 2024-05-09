package br.edu.ifrs.pitanga.core.domain.repositories.specifications;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import br.edu.ifrs.pitanga.core.domain.pbl.Challenge;
import br.edu.ifrs.pitanga.core.domain.pbl.vo.ChallengeLevel;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChallengeSpecification implements Specification<Challenge> {
    private final ChallengeLevel level;

    @Override
    @Nullable
    public Predicate toPredicate(@NonNull Root<Challenge> root,
        @NonNull CriteriaQuery<?> q, @NonNull CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();

        if(level != null) {
            predicates.add(builder.equal(root.get("level"), level));
        }

        return builder.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
