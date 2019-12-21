package pl.com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.com.app.model.Agreement;

import java.util.List;

public interface AgreementRepository extends JpaRepository<Agreement, Long> {


    @Query("SELECT DISTINCT a FROM Agreement a JOIN a.user u JOIN FETCH a.packageDeals p JOIN FETCH p.tvPackage t WHERE u.id = :userId ORDER BY a.startDate DESC")
    List<Agreement> findUserNewestAgreement(@Param("userId") Long userId);


    @Query("SELECT DISTINCT a FROM Agreement a JOIN a.user u JOIN FETCH a.packageDeals p JOIN FETCH p.tvPackage t WHERE a.startDate <= current_date and a.endDate >= current_date")
    List<Agreement> findAllNotExpired();

    @Query("SELECT a FROM Agreement a WHERE a.endDate = current_date")
    List<Agreement> findAllEnded();

    void deleteAgreementOrderByUserId_Equals(Long userId);

    @Query("SELECT a FROM Agreement a JOIN a.user u WHERE u.userName = :userName ORDER BY a.startDate DESC")
    List<Agreement> findUserNewestAgreementByUsername(@Param("userName") String userName);
}
