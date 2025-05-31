package br.edu.ifrs.poa.pitanga_code.app.dtos;

import java.util.List;

public record RunTestsCommand(
        String code,
        Long languageId,
        Long challengeId,
        List<String> inputs) {
};
