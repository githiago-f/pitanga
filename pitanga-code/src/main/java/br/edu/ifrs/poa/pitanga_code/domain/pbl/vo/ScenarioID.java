package br.edu.ifrs.poa.pitanga_code.domain.pbl.vo;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ScenarioID implements Serializable {
    private Integer id;
    private Long problemId;

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
        ScenarioID other = (ScenarioID) obj;
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
