package healthcheck.repo.Dao.Impl;

import healthcheck.dto.Application.response.ApplicationResponse;
import healthcheck.repo.Dao.ApplicationDao;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ApplicationDaoImpl implements ApplicationDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<ApplicationResponse> getApplications(String word) {
        String sql = """
        SELECT a.id, a.username, a.date_of_application_creation, a.phone_number, a.processed,
        a.creation_date
        FROM Application a 
        WHERE LOWER(a.username) LIKE CONCAT('%', LOWER(?), '%')
        """;

        return jdbcTemplate.query(sql, new Object[]{word}, (resultSet, rowNum)
                -> mapResultSetToApplicationResponse(resultSet));
    }

    @Override
    public List<ApplicationResponse> getAllApplications() {
        String sql = """
            SELECT a.id, a.username, a.date_of_application_creation, a.phone_number, a.processed, 
            a.creation_date
            FROM Application a ORDER BY a.id
            """;

        List<ApplicationResponse> responsesAll = jdbcTemplate.query(sql, (resultSet, rowNum)
                -> mapResultSetToApplicationResponse(resultSet));

        responsesAll.sort(Comparator.comparing(ApplicationResponse::getCreationDate,
                Comparator.nullsLast(Comparator.reverseOrder())));

        return responsesAll;
    }

    public static ApplicationResponse mapResultSetToApplicationResponse(ResultSet resultSet) throws SQLException {
        ApplicationResponse response = new ApplicationResponse();
        response.setId(resultSet.getLong("id"));
        response.setUsername(resultSet.getString("username"));
        response.setDateOfApplicationCreation(resultSet.getDate("date_of_application_creation").toLocalDate());
        response.setPhoneNumber(resultSet.getString("phone_number"));
        response.setProcessed(resultSet.getBoolean("processed"));

        Timestamp creationTimestamp = resultSet.getTimestamp("creation_date");
        if (creationTimestamp != null) {
            response.setCreationDate(Timestamp.valueOf(creationTimestamp.toLocalDateTime()));
        }

        return response;
    }
}