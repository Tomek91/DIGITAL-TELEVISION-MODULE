package pl.com.app.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tv_packages")
public class TvPackage {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal cost;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "packages_durations",
            joinColumns = @JoinColumn(name = "package_id"),
            inverseJoinColumns = @JoinColumn(name = "duration_id")
    )
    private Set<AgreementDuration> agreementDurations = new HashSet<>();

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "tvPackage")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<PackageDeal> packageDeals = new HashSet<>();

    @OneToMany(mappedBy = "tvPackage")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<PackageOrder> packageOrders = new HashSet<>();

}





