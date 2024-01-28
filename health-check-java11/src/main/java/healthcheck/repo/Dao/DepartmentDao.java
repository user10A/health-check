package healthcheck.repo.Dao;

import healthcheck.dto.Department.DepartmentResponse;

import java.util.List;
public interface DepartmentDao {

    List<DepartmentResponse> getAllFacility();
}
