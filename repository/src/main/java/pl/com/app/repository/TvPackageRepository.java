package pl.com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.app.model.TvPackage;

import java.util.List;
import java.util.Optional;

public interface TvPackageRepository extends JpaRepository<TvPackage, Long> {
    Optional<TvPackage> findByName(String name);
    List<TvPackage> findAllByNameIn(List<String> names);
}
