package pl.com.app.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "agreement_durations")
public class AgreementDuration {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private Integer duration;

    @ManyToMany(cascade = CascadeType.PERSIST, mappedBy = "agreementDurations")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<TvPackage> tvPackages = new HashSet<>();
}
