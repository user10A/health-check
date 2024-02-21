package healthcheck.repo.Dao.Impl;

import healthcheck.dto.Application.ApplicationResponse;
import healthcheck.repo.Dao.ApplicationDao;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ApplicationDaoImpl implements ApplicationDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<ApplicationResponse> getApplications(String word) {
        String sql = """
    SELECT a.id, a.username, a.date_of_application_creation, a.phone_number, a.processed
    FROM Application a 
    WHERE LOWER(a.username) LIKE concat('%',LOWER (?), '%')
    """;
        return jdbcTemplate.query(sql, new Object[]{word}, (rs, rowNum) ->
                new ApplicationResponse(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getDate("date_of_application_creation").toLocalDate(),
                        rs.getString("phone_number"),
                        rs.getBoolean("processed")
                )
        );

    }

    @Override
    public List<ApplicationResponse> getAllApplications() {
        String sql = "SELECT a.id, a.username, a.date_of_application_creation, a.phone_number, a.processed " +
                "FROM Application a ORDER BY a.id";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new ApplicationResponse(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getDate("date_of_application_creation").toLocalDate(),
                        rs.getString("phone_number"),
                        rs.getBoolean("processed")
                )
        );
    }
}
