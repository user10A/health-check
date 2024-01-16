package healthcheck.repo;
import healthcheck.entities.Result;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultRepo extends JpaRepository<Result,Long> {

}