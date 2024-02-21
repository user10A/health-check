package healthcheck.repo.Dao.Impl;

import healthcheck.dto.Doctor.DoctorResponseByWord;
import healthcheck.dto.GlobalSearch.SearchResponse;
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
    public List<DoctorResponseByWord> getAllDoctorsBySearch(String word) {
        var sql = """
                 SELECT
                                                         d.id,
                                                         d.image,
                                                         d.is_active,
                                                         d.first_name,
                                                         d.last_name,
                                                         d2.facility,
                                                         s.end_date_work
                                                     FROM Doctor d
                                                     JOIN schedule s ON d.id = s.doctor_id
                                                     JOIN department d2 on d.department_id = d2.id
                WHERE CONCAT(d.first_name, ' ', d.last_name) LIKE '%' || ? || '%'
                ORDER BY d.id
                """;
        return jdbcTemplate.query(sql, new Object[]{word}, (rs, rowNum) -> DoctorResponseByWord.builder()
                .id(rs.getLong(1))
                .image(rs.getString(2))
                .isActive(rs.getBoolean(3))
                .firstName(rs.getString(4))
                .lastName(rs.getString(5))
                .department(rs.getString(6))
                .endDateWork(rs.getDate(7).toLocalDate())
                .build());
    }

    @Override
    public List<DoctorResponseByWord> getAllDoctors() {
        var sql = """
                 SELECT
                                                         d.id,
                                                         d.image,
                                                         d.is_active,
                                                         d.first_name,
                                                         d.last_name,
                                                         d2.facility,
                                                         s.end_date_work
                                                     FROM Doctor d
                                                     JOIN schedule s ON d.id = s.doctor_id
                                                     JOIN department d2 on d.department_id = d2.id
                ORDER BY d.id
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> DoctorResponseByWord.builder()
                .id(rs.getLong(1))
                .image(rs.getString(2))
                .isActive(rs.getBoolean(3))
                .firstName(rs.getString(4))
                .lastName(rs.getString(5))
                .department(rs.getString(6))
                .endDateWork(rs.getDate(7).toLocalDate())
                .build());
    }

    @Override
    public List<SearchResponse> globalSearch(String word) {
        var sql = """
                SELECT
                d.id,
                d2.id,
                d.first_name,
                d.last_name,
                d2.facility,
                d.image
                                
                FROM doctor d
                JOIN department d2 on d.department_id = d2.id
                WHERE LOWER(d.first_name) LIKE LOWER(CONCAT('%', ?, '%')) OR
                LOWER(d.last_name) LIKE LOWER(CONCAT('%', ?, '%')) OR
                LOWER(d2.facility) LIKE LOWER(CONCAT('%', ?, '%'))
                """;

        return jdbcTemplate.query(sql, new Object[]{word, word, word}, (rs, rowNum) -> SearchResponse.builder()
                .doctorId(rs.getLong(1))
                .departmentId(rs.getLong(2))
                .doctorFirstName(rs.getString(3))
                .doctorLastName(rs.getString(4))
                .doctorPosition(rs.getString(5))
                .image(rs.getString(6))
                .build());
    }

}