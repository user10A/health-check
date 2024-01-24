package healthcheck.repo.Dao.Impl;
import healthcheck.dto.Doctor.ResponseToGetDoctorsByDepartment;
import healthcheck.repo.Dao.DoctorDao;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class DoctorDaoImpl implements DoctorDao {
    private final JdbcTemplate jdbcTemplate;
    @Override
    public List<ResponseToGetDoctorsByDepartment> getDoctorsByDepartment() {
        var sql = """
                     SELECT
                        dep.facility as department_name,
                        d.id as doctor_id,
                        CONCAT(d.first_name, ' ', d.last_name) as full_name,
                        d.image,
                        d.position

                    FROM
                        doctor d
                            JOIN department dep ON d.department_id = dep.id
                    GROUP BY dep.facility, d.id, full_name, d.image, d.position
                    ORDER BY dep.facility;
                   """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> ResponseToGetDoctorsByDepartment.builder()
                .department(rs.getString("department_name"))
                .doctorId(rs.getLong("doctor_id"))
                .full_name(rs.getString("full_name"))
                .image(rs.getString("image"))
                .position(rs.getString("position")).build()
        );
    }
}
