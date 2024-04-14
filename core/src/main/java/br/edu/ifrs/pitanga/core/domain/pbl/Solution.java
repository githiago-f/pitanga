package br.edu.ifrs.pitanga.core.domain.pbl;

import java.util.Date;

import br.edu.ifrs.pitanga.core.domain.pbl.vo.SolutionId;
import jakarta.persistence.*;
import lombok.*;

@Builder @Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "solutions")
public class Solution {
    @EmbeddedId
    private SolutionId id;
    private String code;
    private String language;
    @Column(insertable = false, updatable = false)
    private Date createdAt;
    
    @JoinColumn(insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Challenge challenge;

    public Boolean compareHash(Solution toCompare) {
        if(toCompare == null) return false;
        return id.getHash().equals(toCompare.getId().getHash());
    }

    public void setHash(String hash) {
        id.setHash(hash);
    }
}
