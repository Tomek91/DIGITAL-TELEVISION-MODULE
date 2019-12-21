package pl.com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.com.app.model.AgreementOrder;

import java.util.List;

public interface AgreementOrderRepository extends JpaRepository<AgreementOrder, Long> {
    @Query("SELECT a FROM AgreementOrder a WHERE a.isCompleted is null or a.isCompleted = false")
    List<AgreementOrder> findAllNotConfirm();

    void deleteAgreementOrderByUserId_Equals(Long userId);
}
