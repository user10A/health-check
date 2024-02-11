package healthcheck.repo;
import healthcheck.entities.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepo extends JpaRepository<Result,Long> {
    @Query("SELECT r.pdfUrl FROM Result r where r.resultNumber = :resultNumber")
    String getResultByResultNumberResult(@Param("resultNumber") Long resultNumber);
}