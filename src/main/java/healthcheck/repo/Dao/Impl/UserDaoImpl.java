package healthcheck.repo.Dao.Impl;

import healthcheck.dto.User.ResponseToGetAppointmentByUserId;
import healthcheck.dto.User.ResponseToGetUserAppointments;
import healthcheck.dto.User.ResponseToGetUserById;
import healthcheck.dto.User.ResultUsersResponse;
import healthcheck.enums.Status;
import healthcheck.exceptions.NotFoundException;
import healthcheck.repo.Dao.UserDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ResultUsersResponse> getAllPatients() {
        var sql =
                """     
                        SELECT DISTINCT
                         u.id,
                         CONCAT(u.first_name, ' ', u.last_name) AS full_name,
                         u.phone_number,
                         ua.email,
                         r.result_date,
                         u.creation_date
                        FROM
                         users u
                        LEFT JOIN ( SELECT user_id, MAX(result_date) AS max_result_date FROM result GROUP BY user_id)
                         r_max ON r_max.user_id = u.id
                        LEFT JOIN
                         result r ON r.user_id = u.id AND r.result_date = r_max.max_result_date
                        LEFT JOIN
                         user_account ua ON ua.id = u.user_account_id
                        ORDER BY
                         u.id;
                        """;

        List<ResultUsersResponse> responses = jdbcTemplate.query(sql, (rs, rowNum) -> {
            ResultUsersResponse response = new ResultUsersResponse();
            response.setId(rs.getLong("id"));
            response.setSurname(rs.getString("full_name"));
            response.setPhoneNumber(rs.getString("phone_number"));
            response.setEmail(rs.getString("email"));
            if (rs.getDate("result_date") != null) {
                response.setResultDate(rs.getDate("result_date").toString());
            } else {
                response.setResultDate("-");
            }

            Timestamp timestamp = rs.getTimestamp("creation_date");
            if (timestamp != null) {
                response.setCreationDate(timestamp);
            }
            return response;
        });
        responses.sort(Comparator.comparing(ResultUsersResponse::getCreationDate,
                Comparator.nullsLast(Comparator.reverseOrder())));

        return responses;
    }


    @Override
    public List<ResponseToGetUserAppointments> getAllAppointmentsOfUser(Long id) {
        try {
            var sql = """
                       SELECT
                           a.appointment_date,
                           a.appointment_time,
                           a.status,
                           CONCAT(d.first_name, ' ', d.last_name) AS doctor_full_name,
                           dep.facility,
                           a.id as appointment_id,
                           d.image
                       FROM
                           appointment a
                               JOIN doctor d ON a.doctor_id = d.id
                               JOIN department dep ON d.department_id = dep.id
                               JOIN users u ON a.user_id = u.id
                               JOIN user_account ac ON u.user_account_id = ac.id
                       WHERE
                               ac.id = ?;
                    """;
            return jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> ResponseToGetUserAppointments.builder()
                    .appointmentDate(rs.getDate("appointment_date").toLocalDate())
                    .appointmentTime(rs.getTime("appointment_time").toLocalTime())
                    .status(Status.valueOf(rs.getString("status")))
                    .surname(rs.getString("doctor_full_name"))
                    .department(rs.getString("facility"))
                    .id(rs.getLong("appointment_id"))
                    .image(rs.getString("image"))
                    .build());
        } catch (NotFoundException e) {
            throw new NotFoundException("error.user_not_found",new Object[]{id});
        }
    }
    @Override
    public ResponseToGetAppointmentByUserId getUserAppointmentById(Long id) {
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
                    LEFT JOIN users u ON a.user_id = u.id
                    LEFT JOIN user_account ua ON u.user_account_id = ua.id
                    LEFT JOIN doctor d ON a.doctor_id = d.id
                    LEFT JOIN department dep ON d.department_id = dep.id
                WHERE
                    a.id = ?
                
                """;
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                    ResponseToGetAppointmentByUserId.builder()
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
    }

    @Override
    public int clearMyAppointments(Long id) {
        var sql = """
                    DELETE FROM appointment WHERE user_id = ?;                         
                """;
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public ResponseToGetUserById getUserById(Long id) {
        var sql = """
                SELECT
                    u.id,
                    CONCAT(u.first_name, ' ', u.last_name) AS full_name,
                    u.first_name,
                    u.last_name,
                    u.phone_number,
                    ua.email
                FROM
                    users u
                JOIN
                    public.user_account ua ON ua.id = u.user_account_id
                WHERE
                    u.id = ?;
                                
                """;

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> ResponseToGetUserById.builder()
                    .id(rs.getLong("id"))
                    .fullName(rs.getString("full_name"))
                    .last_name(rs.getString("last_name"))
                    .first_name(rs.getString("first_name"))
                    .phone_number(rs.getString("phone_number"))
                    .email(rs.getString("email"))
                    .build()
            );
        } catch (NotFoundException e) {
            throw new NotFoundException("User with this id not found");
        }
    }

    @Override
    public List<ResultUsersResponse> resultUsersBySearch(String word) {
        var sql = """
                SELECT DISTINCT
                u.id,
                CONCAT(u.first_name, ' ', u.last_name) AS full_name,
                u.phone_number,
                ua.email,
                r.result_date
                FROM
                users u
                LEFT JOIN ( SELECT user_id, MAX(result_date) AS max_result_date FROM result GROUP BY user_id)
                r_max ON r_max.user_id = u.id
                LEFT JOIN
                result r ON r.user_id = u.id AND r.result_date = r_max.max_result_date
                LEFT JOIN
                user_account ua ON ua.id = u.user_account_id
                WHERE LOWER(u.first_name) LIKE concat('%', LOWER(?), '%') OR
                          LOWER(u.last_name) LIKE concat('%', LOWER(?), '%') OR
                          LOWER(ua.email) LIKE concat('%', LOWER(?), '%')
                """;

        return jdbcTemplate.query(sql, new Object[]{word, word, word}, (rs, rowNum) -> {
            ResultUsersResponse response = new ResultUsersResponse();
            response.setId(rs.getLong("id"));
            response.setSurname(rs.getString("full_name"));
            response.setPhoneNumber(rs.getString("phone_number"));
            response.setEmail(rs.getString("email"));
            if (rs.getDate("result_date") != null) {
                response.setResultDate(rs.getDate("result_date").toString());
            } else {
                response.setResultDate("-"); // или любое другое значение, которое вы хотите использовать для обозначения отсутствующего результата
            }
            return response;
        });
    }
}