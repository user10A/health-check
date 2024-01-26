package healthcheck.repo.Dao.Impl;

import healthcheck.dto.User.ResultUsersResponse;
import healthcheck.repo.Dao.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<ResultUsersResponse> getAllPatients() {
        var sql = """
                SELECT
                u.id
                concat(u.first_name,' ',u.last_name) as full_name,
                u.phone_number,
                ua.email ,
                r.result_date  from users u
                join public.result r on r.user_id = u.id
                join public.user_account ua on ua.id = u.user_account_id
                order by full_name;
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ResultUsersResponse response = new ResultUsersResponse();
            response.setId(rs.getLong("id"));
            response.setSurname(rs.getString("full_name"));
            response.setPhoneNumber(rs.getString("phone_number"));
            response.setEmail(rs.getString("email"));
            response.setResultDate(rs.getDate("result_date").toLocalDate());
            return response;
        });
    }
}
