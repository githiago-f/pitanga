package br.edu.ifrs.poa.pitanga_code.app.dtos;

import java.util.List;

public record RunTestsRequest(
        String code,
        Long languageId,
        Long problemId,
        List<String> inputs) {
};
