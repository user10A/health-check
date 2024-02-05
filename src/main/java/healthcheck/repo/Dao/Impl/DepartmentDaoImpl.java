package healthcheck.repo.Dao.Impl;

import healthcheck.dto.Department.DepartmentResponse;
import healthcheck.enums.Facility;
import healthcheck.repo.Dao.DepartmentDao;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DepartmentDaoImpl implements DepartmentDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<DepartmentResponse> getAllFacility() {

        var sql = "SELECT d.id, d.facility FROM Department d ORDER BY d.facility";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            DepartmentResponse departmentResponse = new DepartmentResponse();
            departmentResponse.setId(rs.getLong("id"));
            departmentResponse.setFacility(Facility.valueOf(rs.getString("facility")));
            return departmentResponse;
        });
    }



}
