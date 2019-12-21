package pl.com.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.app.model.enums.OperationOrder;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "package_order")
public class PackageOrder {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private OperationOrder operationOrder;

    @Column(nullable = false)
    private LocalDate orderDate;

    private Boolean isCompleted;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "tvPackage_id")
    private TvPackage tvPackage;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;



}
