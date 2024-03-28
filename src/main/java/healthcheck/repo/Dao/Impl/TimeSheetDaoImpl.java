package healthcheck.repo.Dao.Impl;

import healthcheck.dto.Feedback.RatingResponse;
import healthcheck.dto.Schedule.ScheduleDate;
import healthcheck.dto.TimeSheet.TimeSheetResponse;
import healthcheck.repo.Dao.FeedbackDao;
import healthcheck.repo.Dao.TimeSheetDao;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.*;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class TimeSheetDaoImpl implements TimeSheetDao {

    private final JdbcTemplate jdbcTemplate;
    private final FeedbackDao feedbackDao;

    @Override
    public List<TimeSheetResponse> getTimesheetDoctor(String facility) {
        ZoneId zoneId = ZoneId.of("Asia/Bishkek");
        ZonedDateTime currentTime = ZonedDateTime.now(zoneId);
        LocalDate start = currentTime.toLocalDate();
        LocalDate end = start.plusDays(7);
        LocalTime startTime = currentTime.toLocalTime();
        String sql =
                """
                SELECT
                    doctor.id AS doctor_id,
                    doctor.image AS image,
                    CONCAT(doctor.first_name, ' ', doctor.last_name) AS doctor_full_name,
                    d.facility AS facility,
                    t.date_of_consultation AS date_of_consultation,
                    STRING_AGG(t.start_time_of_consultation::TEXT, ', ' ORDER BY t.start_time_of_consultation) AS start_times
                FROM
                    department d
                JOIN
                    doctor doctor ON d.id = doctor.department_id
                JOIN
                    schedule sched ON doctor.id = sched.doctor_id
                JOIN
                    time_sheet t ON sched.id = t.schedule_id
                WHERE
                    d.facility = ? AND t.available = false AND t.date_of_consultation BETWEEN TO_DATE(?, 'YYYY-MM-DD') AND TO_DATE(?, 'YYYY-MM-DD') AND (
                    t.date_of_consultation > TO_DATE(?, 'YYYY-MM-DD') OR t.start_time_of_consultation > CAST(? AS TIME))
                GROUP BY
                    doctor.id,doctor.image, doctor_full_name, d.facility, t.date_of_consultation
                ORDER BY
                    t.date_of_consultation;
                """;
        List<TimeSheetResponse> query = jdbcTemplate.query(sql, new Object[]{facility, start.toString(), end.toString(), start.toString(), startTime.toString()}, (rs, rowNum) -> {
            RatingResponse rating = feedbackDao.getRaringResponseByIdDoctor(rs.getLong("doctor_id"));
            return TimeSheetResponse.builder()
                    .doctorId(rs.getLong("doctor_id"))
                    .averageRating(rating.getAverageRating())
                    .count(rating.getCountRating())
                    .imageDoctor(rs.getString("image"))
                    .doctorFullName(rs.getString("doctor_full_name"))
                    .department(rs.getString("facility"))
                    .dayOfWeek(getDayOfWeek(rs.getDate("date_of_consultation").toLocalDate()).name())
                    .dateOfConsultation(String.valueOf(rs.getDate("date_of_consultation").toLocalDate()))
                    .startTimeOfConsultation(Arrays.asList(rs.getString("start_times").split(", ")))
                    .build();
        });
        return query;
    }

    @Override
    public List<ScheduleDate> getDoctorWorkingDate(String startDate, String endDate, Long doctorId) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        Map<LocalDate, List<ScheduleDate>> scheduleMap = new LinkedHashMap<>();
        var sql = """
                 SELECT
                    time_sheet.date_of_consultation,
                    STRING_AGG(CONCAT(time_sheet.start_time_of_consultation, ' - ', time_sheet.end_time_of_consultation, '-',time_sheet.available), ', ' ORDER BY time_sheet.start_time_of_consultation) AS start_times  
                    FROM
                    doctor doc
                JOIN
                    schedule schedule ON doc.id = schedule.doctor_id
                JOIN
                    time_sheet time_sheet ON schedule.id = time_sheet.schedule_id
                WHERE
                    time_sheet.date_of_consultation BETWEEN ? AND ?
                    AND doc.id = ?
                GROUP BY
                    time_sheet.date_of_consultation
                ORDER BY
                    time_sheet.date_of_consultation;
                """;
        try {
            List<ScheduleDate> dbResults = jdbcTemplate.query(sql, new Object[]{start, end, doctorId}, (rs, rowNum) -> {
                LocalDate dateOfConsultation = rs.getDate(1).toLocalDate();
                String startTimesStr = rs.getString("start_times");
                List<String> startTimes = Arrays.asList(startTimesStr.split(", "));
                return ScheduleDate.builder()
                        .dateOfConsultation(dateOfConsultation)
                        .dayOfWeek(getDayOfWeek(dateOfConsultation).name())
                        .startTimeOfConsultation(startTimes)
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
            for (ScheduleDate response : dbResults) {
                scheduleMap.get(response.getDateOfConsultation()).add(response);
            }
            List<ScheduleDate> finalScheduleList = new LinkedList<>();
            for (Map.Entry<LocalDate, List<ScheduleDate>> entry : scheduleMap.entrySet()) {
                LocalDate date = entry.getKey();
                List<ScheduleDate> daySchedule = entry.getValue();
                if (daySchedule.isEmpty()) {
                    finalScheduleList.add(new ScheduleDate(
                            date,
                            getDayOfWeek(date).name(),
                            new ArrayList<>()));
                } else {
                    finalScheduleList.addAll(daySchedule);
                }
            }
            return finalScheduleList;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public DayOfWeek getDayOfWeek (LocalDate date) {
        return date.getDayOfWeek();
    }
}