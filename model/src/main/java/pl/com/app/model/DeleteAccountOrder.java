package pl.com.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "delete_account_order")
public class DeleteAccountOrder {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private LocalDate orderDate;

    private Boolean isCompleted;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;

}
