package healthcheck.repo.Dao.Impl;

import healthcheck.dto.TimeSheet.TimeSheetResponse;
import healthcheck.repo.Dao.TimeSheetDao;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Arrays;


@Repository
@RequiredArgsConstructor
public class TimeSheetDaoImpl implements TimeSheetDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<TimeSheetResponse> getTimesheetDoctor(String facility) {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(7);
        LocalTime startTime = LocalTime.now();
        String sql =
                """
        SELECT
            doc.id AS doctor_id,
            doc.image AS image,
            CONCAT(doc.first_name, ' ', doc.last_name) AS doctor_full_name,
            t.date_of_consultation AS date_of_consultation,
            STRING_AGG(t.start_time_of_consultation::TEXT, ', ' ORDER BY t.start_time_of_consultation) AS start_times
        FROM
            department d
        JOIN
            doctor doc ON d.id = doc.department_id
        JOIN
            schedule sched ON doc.id = sched.doctor_id
        JOIN
            time_sheet t ON sched.id = t.schedule_id
        WHERE
            d.facility = ? AND t.available = false AND t.date_of_consultation BETWEEN TO_DATE(?, 'YYYY-MM-DD') AND TO_DATE(?, 'YYYY-MM-DD') AND (
            t.date_of_consultation > TO_DATE(?, 'YYYY-MM-DD') OR t.start_time_of_consultation > CAST(? AS TIME))
        GROUP BY
            doc.id, doc.image, doctor_full_name, t.date_of_consultation
        ORDER BY
            doctor_full_name, t.date_of_consultation;
        """;
         return jdbcTemplate.query(sql, new Object[]{facility, start.toString(), end.toString(), start.toString(), startTime.toString()}, (rs, rowNum) ->
              TimeSheetResponse.builder()
                    .doctorId(rs.getLong("doctor_id"))
                    .imageDoctor(rs.getString("image"))
                    .doctorFullName(rs.getString("doctor_full_name"))
                    .dayOfWeek(getDayOfWeek(rs.getDate("date_of_consultation").toLocalDate()).name())
                    .dateOfConsultation(String.valueOf(rs.getDate("date_of_consultation").toLocalDate()))
                    .startTimeOfConsultation(Arrays.asList(rs.getString("start_times").split(", ")))
                    .build());
    }
        public DayOfWeek getDayOfWeek (LocalDate date){
            return date.getDayOfWeek();
        }
    }