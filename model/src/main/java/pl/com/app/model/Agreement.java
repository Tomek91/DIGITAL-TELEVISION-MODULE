package pl.com.app.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "agreements")
public class Agreement {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany( mappedBy = "agreement")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<PackageDeal> packageDeals = new HashSet<>();

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "agreement")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Invoice> invoices = new HashSet<>();


}
