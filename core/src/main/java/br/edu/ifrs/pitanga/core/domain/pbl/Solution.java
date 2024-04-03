package br.edu.ifrs.pitanga.core.domain.pbl;

import br.edu.ifrs.pitanga.core.domain.pbl.vo.SolutionId;
import br.edu.ifrs.pitanga.core.domain.school.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "solutions")
public class Solution {
    @EmbeddedId
    private SolutionId id;
    private String code;
    private String language;
    
    @JoinColumn(insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Challenge challenge;
    
    @JoinColumn(insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User submitter;
}
