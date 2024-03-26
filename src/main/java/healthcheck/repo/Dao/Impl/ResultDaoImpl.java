package healthcheck.repo.Dao.Impl;

import healthcheck.dto.Result.ResultsUserResponse;
import healthcheck.repo.Dao.ResultDao;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.Comparator;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ResultDaoImpl implements ResultDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<ResultsUserResponse> getAllResultsByUserId(Long id) {
        var sql = """
               SELECT 
               d.facility, 
               r.result_date, 
               r.time_of_uploading_result, 
               r.result_number, 
               r.pdf_url 
               FROM result r
               JOIN public.department d ON d.id = r.department_id
               JOIN public.users u ON u.id = r.user_id                                                                                   
               WHERE u.id = ?
               ORDER BY result_date, r.time_of_uploading_result
               """;

        List<ResultsUserResponse> getAll = jdbcTemplate.query(sql, new Object[]{id},(rs, rowNum) -> {
            ResultsUserResponse response = ResultsUserResponse.builder()
                    .Facility(rs.getString("facility"))
                    .date(rs.getDate(2).toLocalDate())
                    .localTime(rs.getTime(3).toLocalTime())
                    .numberResult(rs.getString("result_number"))
                    .pdf(rs.getString("pdf_url"))
                    .build();
            return response;
        });

        getAll.sort(Comparator.comparing(response -> {
            return response.getDate().atTime(response.getLocalTime());
        }, Comparator.nullsLast(Comparator.reverseOrder())));

        return getAll;
    }
}