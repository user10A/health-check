package healthcheck.repo.Dao.Impl;

import healthcheck.dto.Appointment.AppointmentScheduleTimeSheetResponse;
import healthcheck.repo.Dao.AppointmentDao;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class AppointmentDaoImpl implements AppointmentDao {
    private final JdbcTemplate jdbcTemplate;
    @Override
    public List<AppointmentScheduleTimeSheetResponse> getTheDoctorFreeTimeInTheCalendar(String startDate, String endDate, Long doctorId) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        LocalTime startTime = LocalTime.now();
        Map<LocalDate, List<AppointmentScheduleTimeSheetResponse>> scheduleMap = new LinkedHashMap<>();
        var sql = """
SELECT
    time_sheet.date_of_consultation,
    STRING_AGG(time_sheet.start_time_of_consultation::TEXT, ', ' ORDER BY time_sheet.start_time_of_consultation) AS start_times
FROM
    doctor doc
JOIN
    schedule schedule ON doc.id = schedule.doctor_id
JOIN
    time_sheet time_sheet ON schedule.id = time_sheet.schedule_id
WHERE
time_sheet.date_of_consultation BETWEEN TO_DATE(?, 'YYYY-MM-DD') AND TO_DATE(?, 'YYYY-MM-DD') AND (
            time_sheet.date_of_consultation > TO_DATE(?, 'YYYY-MM-DD') OR time_sheet.start_time_of_consultation > CAST(? AS TIME))
    AND doc.id = ? AND time_sheet.available = false
GROUP BY
    time_sheet.date_of_consultation
ORDER BY
    time_sheet.date_of_consultation;
""";
        try {
            List<AppointmentScheduleTimeSheetResponse> dbResults = jdbcTemplate.query(sql, new Object[]{startDate, endDate,startDate,startTime.toString(),doctorId}, (rs, rowNum) -> {
                LocalDate dateOfConsultation = rs.getDate(1).toLocalDate();
                String dayOfWeek = getDayOfWeek(dateOfConsultation).name();
                return AppointmentScheduleTimeSheetResponse.builder()
                        .dateOfConsultation(dateOfConsultation)
                        .dayOfWeek(dayOfWeek)
                        .startTimeOfConsultation(Arrays.asList(rs.getString("start_times").split(", ")))
                        .build();
            });

            List<LocalDate> allDates = new ArrayList<>();
            while (!start.isAfter(end)) {
                allDates.add(start);
                start = start.plusDays(1);
            }

            for (LocalDate date : allDates) {
                scheduleMap.put(date, new ArrayList<>());
            }

            for (AppointmentScheduleTimeSheetResponse response : dbResults) {
                scheduleMap.get(response.getDateOfConsultation()).add(response);
            }
            List<AppointmentScheduleTimeSheetResponse> finalScheduleList = new ArrayList<>();
            for (Map.Entry<LocalDate, List<AppointmentScheduleTimeSheetResponse>> entry : scheduleMap.entrySet()) {
                LocalDate date = entry.getKey();
                List<AppointmentScheduleTimeSheetResponse> daySchedule = entry.getValue();
                if (daySchedule.isEmpty()) {
                    finalScheduleList.add(new AppointmentScheduleTimeSheetResponse(date, getDayOfWeek(date).name(), new ArrayList<>()));
                } else {
                    finalScheduleList.addAll(daySchedule);
                }
            }
            return finalScheduleList;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public DayOfWeek getDayOfWeek(LocalDate date) {
        return date.getDayOfWeek();
    }
}
