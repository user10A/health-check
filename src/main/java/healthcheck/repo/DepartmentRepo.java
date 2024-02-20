package healthcheck.repo;

import healthcheck.entities.Department;
import healthcheck.enums.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepo extends JpaRepository<Department,Long> {
    Department findByFacility(Facility facility);
    Optional<Department> getDepartmentByFacility(Facility facility);
}