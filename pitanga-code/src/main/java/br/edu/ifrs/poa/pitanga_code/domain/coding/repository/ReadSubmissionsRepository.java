package br.edu.ifrs.poa.pitanga_code.domain.coding.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.edu.ifrs.poa.pitanga_code.domain.coding.entities.CodeSubmission;
import br.edu.ifrs.poa.pitanga_code.domain.coding.vo.SubmissionId;
import jakarta.annotation.Nullable;

public interface ReadSubmissionsRepository
        extends JpaRepository<CodeSubmission, SubmissionId>,
        JpaSpecificationExecutor<CodeSubmission> {

    Page<CodeSubmission> findAll(@Nullable Specification<CodeSubmission> spec, Pageable pageable);
}
