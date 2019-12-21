package pl.com.app.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private LocalDate birthday;

    @Column(unique = true, nullable = false)
    private String userName;

    @Column(unique = true, nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    private Boolean active;

    private Boolean locked;

    @Column(nullable = false)
    private LocalDate registrationDate;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "user")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Agreement> agreements = new HashSet<>();

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "user")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<PackageOrder> packageOrders = new HashSet<>();

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "user")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Preference> preferences = new HashSet<>();
}
