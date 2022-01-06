package de.tudresden.inf.rn.xapi.datatools.datasim.persistence;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DatasimSimulation {
    @GeneratedValue
    @Id
    @Getter
    private UUID id;

    @Getter
    @Setter
    private String remark;

    // The properties below are required by DATASIM
    @Getter
    @Setter
    @ManyToMany
    @Cascade(CascadeType.ALL)
    private Set<DatasimPersonaGroup> personaGroups;

    @Getter
    @Setter
    @OneToMany
    @Cascade(CascadeType.ALL)
    private Map<DatasimPersona, DatasimAlignment> alignments;

    @Getter
    @Setter
    @OneToOne
    @Cascade(CascadeType.ALL)
    private DatasimSimulationParams params;

    @Getter
    @Setter
    @ManyToOne
    // Profiles are reusable, thus no cascade
    private DatasimProfile profile;

    public DatasimSimulation(String remark, Set<DatasimPersonaGroup> personaGroups, Map<DatasimPersona, DatasimAlignment> alignments,
                             DatasimSimulationParams params, DatasimProfile profile) {
        this.remark = remark;
        this.personaGroups = personaGroups;
        this.alignments = alignments;
        this.params = params;
        this.profile = profile;
    }
}