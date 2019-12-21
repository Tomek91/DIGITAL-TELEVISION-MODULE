package pl.com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.com.app.model.DeleteAccountOrder;

import java.util.List;

public interface DeleteAccountOrderRepository extends JpaRepository<DeleteAccountOrder, Long> {
    @Query("SELECT d FROM DeleteAccountOrder d WHERE d.isCompleted is null or d.isCompleted = false")
    List<DeleteAccountOrder> findAllNotConfirm();
    void deleteAgreementOrderByUserId_Equals(Long userId);
}
