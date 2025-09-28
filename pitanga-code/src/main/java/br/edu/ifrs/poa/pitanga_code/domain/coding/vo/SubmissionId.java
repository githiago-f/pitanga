package br.edu.ifrs.poa.pitanga_code.domain.coding.vo;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionId implements Serializable {
    private Long id;
    private Long problemId;

    public void setProblemId(Long problemId) {
        this.problemId = problemId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((problemId == null) ? 0 : problemId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SubmissionId other = (SubmissionId) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (problemId == null) {
            if (other.problemId != null)
                return false;
        } else if (!problemId.equals(other.problemId))
            return false;
        return true;
    }
}
