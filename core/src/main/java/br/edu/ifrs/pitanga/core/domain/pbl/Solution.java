package br.edu.ifrs.pitanga.core.domain.pbl;

import java.util.Date;
import java.util.stream.Stream;

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
    private String hash;

    @Builder.Default
    private Boolean passAllValidations = false;

    @Column(insertable = false, updatable = false)
    private Date createdAt;

    @JoinColumn(insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Challenge challenge;

    public Boolean compareHash(Solution toCompare) {
        if(toCompare == null) return false;
        return getHash().equals(toCompare.getHash());
    }

    public void setVersion(Solution oldVersion) {
        Long version = 1l;
        if(oldVersion != null) {
            version = oldVersion.id.getVersion() + 1;
        }
        id.setVersion(version);
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Stream<Validation> validations() {
        return getChallenge().getValidations().stream();
    }
}
