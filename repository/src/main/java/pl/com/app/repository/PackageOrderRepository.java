package pl.com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.com.app.model.PackageOrder;

import java.util.List;

public interface PackageOrderRepository extends JpaRepository<PackageOrder, Long> {
    @Query("SELECT p FROM PackageOrder p WHERE p.isCompleted is NULL or p.isCompleted = false")
    List<PackageOrder> findAllNotConfirm();

    void deleteAgreementOrderByUserId_Equals(Long userId);
}
