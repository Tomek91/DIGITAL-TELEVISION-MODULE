package pl.com.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "deals")
public class PackageDeal {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "tvPackage_id")
    private TvPackage tvPackage;

    @ManyToOne()
    @JoinColumn(name = "agreement_id")
    private Agreement agreement;



}
