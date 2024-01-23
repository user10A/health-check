package healthcheck.repo.Dao.impl;
import healthcheck.dto.User.UserResponse;
import healthcheck.dto.User.UserResponseGetById;
import healthcheck.enums.Status;
import healthcheck.repo.Dao.UserDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.webjars.NotFoundException;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<UserResponse> getAllAppointmentsOfUser(Long id) {
        var sql = """
                   SELECT
                       a.appointment_date,
                       a.appointment_time,
                       a.status,
                       CONCAT(d.first_name, ' ', d.last_name) AS full_name,
                       dep.facility,
                       u.id as user_id,
                       d.image
                   FROM
                       appointment a
                           JOIN doctor d ON a.doctor_id = d.id
                           JOIN department dep ON d.department_id = dep.id
                           JOIN users u ON a.user_id = u.id
                   WHERE
                           a.user_id = ?
                   GROUP BY
                       a.appointment_date,
                       a.appointment_time,
                       a.status,
                       full_name,
                       dep.facility,
                       u.id,
                       d.image;
                """;
        return jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> UserResponse.builder()
                .appointmentDate(rs.getDate(1).toLocalDate())
                .appointmentTime(rs.getTime(2).toLocalTime())
                .status(Status.valueOf(rs.getString(3).toUpperCase()))  // Convert to upper case for safety
                .surname(rs.getString(4))
                .department(rs.getString(5))
                .id(rs.getLong("user_id"))
                .image(rs.getString("image"))
                .build());
    }
    @Override
    public UserResponseGetById getById(Long id) {
        var sql = """
                    SELECT
                        u.first_name as first_name,
                        u.last_name as last_name,
                        ua.email as email,
                        u.phone_number as phone_number,
                        d.image as image_url,
                        a.appointment_date as appointment_date,
                        a.appointment_time as appointment_time,
                        a.status as status,
                        CONCAT(d.first_name, ' ', d.last_name) AS full_name,
                        dep.facility as department
                    FROM
                        appointment a
                            JOIN users u ON a.user_id = u.id
                            JOIN user_account ua ON u.user_account_id = ua.id
                            JOIN doctor d ON a.doctor_id = d.id
                            JOIN department dep ON d.department_id = dep.id
                    WHERE
                            a.id = ?
                    GROUP BY
                        u.first_name,
                        u.last_name,
                        ua.email,
                        u.phone_number,
                        d.image,  
                        a.appointment_date,
                        a.appointment_time,
                        a.status,
                        full_name,
                        dep.facility;
                """;
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                    UserResponseGetById.builder()
                            .first_name(rs.getString("first_name"))
                            .last_name(rs.getString("last_name"))
                            .email(rs.getString("email"))
                            .phone_number(rs.getString("phone_number"))
                            .image(rs.getString("image_url"))
                            .appointmentDate(rs.getDate("appointment_date").toLocalDate())
                            .appointmentTime(rs.getTime("appointment_time").toLocalTime())
                            .status(Status.valueOf(rs.getString("status")))
                            .surnameOfDoctor(rs.getString("full_name"))
                            .department(rs.getString("department"))
                            .build()
            );
        } catch (NotFoundException e) {
            throw new NotFoundException("Appointment with this id not found");
        }
    }

    @Override
    public int clearMyAppointments(Long id) {
        var sql = """
                    DELETE FROM appointment WHERE user_id = ?;                         
                """;
        return jdbcTemplate.update(sql, id);
    }
}