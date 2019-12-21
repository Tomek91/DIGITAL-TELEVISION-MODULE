package pl.com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.app.model.PackageDeal;

import java.util.Optional;

public interface PackageDealRepository extends JpaRepository<PackageDeal, Long> {
    Optional<PackageDeal> findByTvPackage_IdEqualsAndAgreement_IdEquals(Long tvPackageId, Long agreementId);
}
