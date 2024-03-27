package healthcheck.repo.Dao.Impl;

import healthcheck.dto.Doctor.DoctorResponse;
import healthcheck.dto.Doctor.DoctorResponseByWord;
import healthcheck.dto.Doctor.DoctorsGetAllByDepartmentsResponse;
import healthcheck.dto.Doctor.DoctorsGetAllByDepartmentsResponse1;
import healthcheck.dto.GlobalSearch.SearchResponse;
import healthcheck.exceptions.NotFoundException;
import healthcheck.repo.Dao.DoctorDao;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DoctorDaoImpl implements DoctorDao {

    private final JdbcTemplate jdbcTemplate;
    private final MessageSource messageSource;


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
                     s.end_date_work,
                     d.position
                 FROM Doctor d
                 JOIN schedule s ON d.id = s.doctor_id
                 JOIN department d2 on d.department_id = d2.id
                 WHERE
                     LOWER(d.first_name) LIKE CONCAT('%', LOWER(?), '%') OR
                     LOWER(d.last_name) LIKE CONCAT('%', LOWER(?), '%')
                 ORDER BY
                     CASE\s
                         WHEN LOWER(d.first_name) LIKE CONCAT(LOWER(?), '%') THEN 0\s
                         WHEN LOWER(d.last_name) LIKE CONCAT(LOWER(?), '%') THEN 1
                     END,
                     d.id, d.first_name, d.last_name;
                """;
        return jdbcTemplate.query(sql, new Object[]{word, word, word, word}, (rs, rowNum) -> DoctorResponseByWord.builder()
                .id(rs.getLong(1))
                .image(rs.getString(2))
                .isActive(rs.getBoolean(3))
                .firstName(rs.getString(4))
                .lastName(rs.getString(5))
                .department(rs.getString(6))
                .endDateWork(rs.getDate(7).toLocalDate())
                .position(rs.getString(8))
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
                                                         s.end_date_work,
                                                         d.position,
                                                         d.creation_date
                                                     FROM Doctor d
                                                     JOIN schedule s ON d.id = s.doctor_id
                                                     JOIN department d2 on d.department_id = d2.id
                ORDER BY d.id
                """;

        List<DoctorResponseByWord> response = jdbcTemplate.query(sql, (rs, rowNum) -> {
            DoctorResponseByWord responseByWord = new DoctorResponseByWord();
            responseByWord.setId(rs.getLong(1));
            responseByWord.setImage(rs.getString(2));
            responseByWord.setIsActive(rs.getBoolean(3));
            responseByWord.setFirstName(rs.getString(4));
            responseByWord.setLastName(rs.getString(5));
            responseByWord.setDepartment(rs.getString(6));
            responseByWord.setEndDateWork(rs.getDate(7).toLocalDate());
            responseByWord.setPosition(rs.getString(8));

            Timestamp creationTimestamp = rs.getTimestamp(9);
            if (creationTimestamp != null) {
                responseByWord.setCreationDate(Timestamp.valueOf(creationTimestamp.toLocalDateTime()));
            }
            return responseByWord;
        });
        response.sort(Comparator.comparing(DoctorResponseByWord::getCreationDate,
                Comparator.nullsLast(Comparator.reverseOrder())));

        return response;
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

    @Override
    public List<DoctorsGetAllByDepartmentsResponse1> getAllDoctorByDepartments(String facility) {
        var sql = """
                SELECT
                    d.id,
                    d.image,
                    CONCAT(d.first_name, ' ', d.last_name) AS doctor_full_name,
                    d.position
                FROM
                    Doctor d
                JOIN
                    department d2 ON d.department_id = d2.id
                WHERE d2.facility=?
                ORDER BY
                 doctor_full_name;
                """;
        return jdbcTemplate.query(sql, new Object[]{facility},(rs, rowNum) -> DoctorsGetAllByDepartmentsResponse1.builder()
                .id(rs.getLong(1))
                .image(rs.getString(2))
                .fullName(rs.getString(3))
                .position(rs.getString(4))
                .build());
    }

    @Override
    public List<DoctorsGetAllByDepartmentsResponse> getAllDoctorsSortByDepartments() {

        String sql =
                """
                SELECT d.facility FROM Department d ORDER BY d.facility
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            return DoctorsGetAllByDepartmentsResponse.builder()
                    .department(rs.getString(1))
                    .doctors(getAllDoctorByDepartments(rs.getString(1)))
                    .build();
        });
    }

    @Override
    public DoctorResponse getDoctorById(Long id) {
        try {
            var sql = """
            SELECT
                d.id,
                d.image,
                CONCAT(d.first_name, ' ', d.last_name) AS doctor_full_name,
                d.first_name,
                d.last_name,
                d.position,
                d.description,
                d2.facility
            FROM
                Doctor d
            JOIN
                department d2 ON d.department_id = d2.id
            WHERE d.id =?
            ORDER BY
             doctor_full_name;
            """;
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> DoctorResponse.builder()
                    .id(rs.getLong(1))
                    .image(rs.getString(2))
                    .fullName(rs.getString(3))
                    .firstName(rs.getString(4))
                    .lastName(rs.getString(5))
                    .position(rs.getString(6))
                    .description(rs.getString(7))
                    .department(rs.getString(8))
                    .build());
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(messageSource.getMessage("doctor.not.found", new Object[]{id}, LocaleContextHolder.getLocale()));
        }
    }
}