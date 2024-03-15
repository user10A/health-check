package healthcheck.repo;

import healthcheck.entities.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepo extends JpaRepository<Application,Long> {
    boolean existsByPhoneNumber(String s);
}