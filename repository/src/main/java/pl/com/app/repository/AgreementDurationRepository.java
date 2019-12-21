package pl.com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.app.model.AgreementDuration;

import java.util.Optional;

public interface AgreementDurationRepository extends JpaRepository<AgreementDuration, Long> {
    Optional<AgreementDuration> findByDuration(Integer duration);
}
