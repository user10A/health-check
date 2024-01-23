package healthcheck.repo;
import healthcheck.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepo extends JpaRepository<Department,Long> {
    @Query("select d from Department d where d.facility = :facility")
    Department getByFacilityName(@Param("facility") String facility);

}
