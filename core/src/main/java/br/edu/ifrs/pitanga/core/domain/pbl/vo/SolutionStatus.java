package br.edu.ifrs.pitanga.core.domain.pbl.vo;

public enum SolutionStatus {
    STARTED, SOLVED, NOT_STARTED;

    public static SolutionStatus getStatus(Integer solutions, Boolean passValidations) {
        if(passValidations) {
            return SOLVED;
        }
        if(solutions > 0) {
            return STARTED;
        }
        return NOT_STARTED;
    }
}
