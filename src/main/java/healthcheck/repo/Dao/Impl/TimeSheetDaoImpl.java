package healthcheck.repo.Dao.Impl;

import healthcheck.dto.TimeSheet.TimeSheetResponse;
import healthcheck.repo.Dao.TimeSheetDao;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TimeSheetDaoImpl implements TimeSheetDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<TimeSheetResponse> getTimesheetDoctor(String facility) {
        String sql =
               """
              SELECT
                  doc.id,
                  doc.image,
                  CONCAT(doc.first_name, ' ', doc.last_name) AS doctor_full_name,
                  t.date_of_consultation,
                  t.start_time_of_consultation
              FROM
                  department d
              JOIN doctor doc ON d.id = doc.department_id
              JOIN schedule sched ON doc.id = sched.doctor_id
              JOIN time_sheet t ON sched.id = t.schedule_id
              WHERE d.facility = ? AND t.available = false
              ORDER BY
              doctor_full_name, 
              t.date_of_consultation, 
              t.start_time_of_consultation;
               """;
        return jdbcTemplate.query( sql, new Object[]{facility}, (rs, rowNum) -> {
            LocalDate dateOfConsultation = rs.getDate(4).toLocalDate();
            String dayOfWeek = getDayOfWeek(dateOfConsultation).name();
                    TimeSheetResponse response = TimeSheetResponse.builder()
                    .doctorId(rs.getLong(1))
                    .imageDoctor(rs.getString("image"))
                    .doctorFullName(rs.getString("doctor_full_name"))
                    .dateOfConsultation(rs.getDate(4).toLocalDate())
                    .dayOfWeek(dayOfWeek)
                    .startTimeOfConsultation(rs.getTime(5).toLocalTime())
                    .build();
            return response;
        });
    }
    public DayOfWeek getDayOfWeek(LocalDate date) {
        return date.getDayOfWeek();
    }
}