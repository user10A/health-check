package healthcheck.repo;
import healthcheck.dto.Application.ApplicationResponse;
import healthcheck.entities.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepo extends JpaRepository<Application,Long> {
    @Query("SELECT NEW healthcheck.dto.Application.ApplicationResponse(a.id, a.username, a.dateOfApplicationCreation, a.phoneNumber, a.processed) " +
            "FROM Application a WHERE :word = '' OR a.username ILIKE concat('%', :word, '%')")
    List<ApplicationResponse> getApplications(@Param("word")String word);
}