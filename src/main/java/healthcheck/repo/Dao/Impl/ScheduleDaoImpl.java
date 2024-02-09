package healthcheck.repo.Dao.Impl;

import healthcheck.dto.Schedule.ResponseToGetSchedules;
import healthcheck.exceptions.NotFoundException;
import healthcheck.repo.Dao.ScheduleDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ScheduleDaoImpl implements ScheduleDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<ResponseToGetSchedules> getAllSchedules() {
        String sql = """
                    SELECT
                        concat(doctor.first_name, ' ', doctor.last_name) AS doctor_full_name,
                        schedule_day.day_of_week as day_of_week,
                        time_sheet.date_of_consultation as date_of_consultation,
                        CASE WHEN time_sheet.available = false THEN time_sheet.start_time_of_consultation ELSE null END as start_time_of_consultation,
                        CASE WHEN time_sheet.available = false THEN time_sheet.end_time_of_consultation ELSE null END as end_time_of_consultation,
                        doctor.image,
                        department.facility
                    FROM
                        public.doctor doctor
                        JOIN public.schedule schedule ON doctor.id = schedule.doctor_id
                        JOIN public.time_sheet time_sheet ON schedule.id = time_sheet.schedule_id
                        JOIN public.schedule_day_of_week schedule_day ON schedule.id = schedule_day.schedule_id
                        JOIN public.department department ON doctor.department_id = department.id
                    GROUP BY doctor.image, day_of_week, time_sheet.date_of_consultation,
                        CASE WHEN time_sheet.available = false THEN time_sheet.start_time_of_consultation ELSE null END,
                        CASE WHEN time_sheet.available = false THEN time_sheet.end_time_of_consultation ELSE null END,
                        concat(doctor.first_name, ' ', doctor.last_name), department.facility
                    ORDER BY doctor_full_name, day_of_week;
                """;

        try {
            return jdbcTemplate.query(sql, (resultSet, rowNum) -> ResponseToGetSchedules.builder()
                    .surname(resultSet.getString("doctor_full_name"))
                    .dayOfWeek(resultSet.getString("day_of_week"))
                    .dateOfConsultation(resultSet.getDate("date_of_consultation").toLocalDate())
                    .startTimeOfConsultation(getLocalTime(resultSet, "start_time_of_consultation"))
                    .endTimeOfConsultation(getLocalTime(resultSet, "end_time_of_consultation"))
                    .image(resultSet.getString("image"))
                    .position(resultSet.getString("facility"))
                    .build());
        } catch (Exception e) {
            throw new NotFoundException("Error while fetching schedules");
        }
    }

    private LocalTime getLocalTime(ResultSet resultSet, String columnName) throws SQLException {
        Time time = resultSet.getTime(columnName);
        return (time != null) ? time.toLocalTime() : null;
    }

    @Override
    public List<ResponseToGetSchedules> getScheduleByDate(String startDate, String endDate) {
        String sql = "SELECT " +
            "concat(doctor.first_name, ' ', doctor.last_name) AS doctor_full_name, " +            "schedule_day.day_of_week AS day_of_week, " +
            "time_sheet.date_of_consultation AS date_of_consultation, " +            "CASE WHEN time_sheet.available = false THEN time_sheet.start_time_of_consultation ELSE null END AS start_time_of_consultation, " +
            "CASE WHEN time_sheet.available = false THEN time_sheet.end_time_of_consultation ELSE null END AS end_time_of_consultation, " +            "doctor.image, " +
            "department.facility " +            "FROM " +
            "public.doctor doctor " +            "JOIN public.schedule schedule ON doctor.id = schedule.doctor_id " +
            "JOIN public.time_sheet time_sheet ON schedule.id = time_sheet.schedule_id " +            "JOIN public.schedule_day_of_week schedule_day ON schedule.id = schedule_day.schedule_id " +
            "JOIN public.department department ON doctor.department_id = department.id " +            "WHERE time_sheet.date_of_consultation BETWEEN ? AND ? " +
            "GROUP BY doctor.image, day_of_week, time_sheet.date_of_consultation, " +            "CASE WHEN time_sheet.available = false THEN time_sheet.start_time_of_consultation ELSE null END, " +
            "CASE WHEN time_sheet.available = false THEN time_sheet.end_time_of_consultation ELSE null END, " +            "concat(doctor.first_name, ' ', doctor.last_name), department.facility " +
            "ORDER BY doctor_full_name, day_of_week";
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startLocalDate = LocalDate.parse(startDate, formatter);        LocalDate endLocalDate = LocalDate.parse(endDate, formatter);
            return jdbcTemplate.query(
                    sql,                (resultSet, rowNum) -> mapRow(resultSet),
                    startLocalDate, endLocalDate        );
        } catch (Exception e) {        e.printStackTrace();
            throw new NotFoundException("Error while fetching schedules");    }
    }
    private ResponseToGetSchedules mapRow(ResultSet resultSet) throws SQLException {    LocalTime startTime = resultSet.getTime("start_time_of_consultation") != null ? resultSet.getTime("start_time_of_consultation").toLocalTime() : null;
        LocalTime endTime = resultSet.getTime("end_time_of_consultation") != null ? resultSet.getTime("end_time_of_consultation").toLocalTime() : null;
        return ResponseToGetSchedules.builder()            .surname(resultSet.getString("doctor_full_name"))
                .dayOfWeek(resultSet.getString("day_of_week"))            .dateOfConsultation(resultSet.getDate("date_of_consultation").toLocalDate())
                .startTimeOfConsultation(startTime)            .endTimeOfConsultation(endTime)
                .image(resultSet.getString("image"))            .position(resultSet.getString("facility"))
                .build();}



    @Override
    public List<ResponseToGetSchedules> getScheduleBySearch(String word) {
        String sql = """
                SELECT
                    concat(doctor.first_name, ' ', doctor.last_name) AS doctor_full_name,
                    schedule_day.day_of_week as day_of_week,
                    time_sheet.date_of_consultation as date_of_consultation,
                    CASE WHEN time_sheet.available = false THEN time_sheet.start_time_of_consultation ELSE null END as start_time_of_consultation,
                    CASE WHEN time_sheet.available = false THEN time_sheet.end_time_of_consultation ELSE null END as end_time_of_consultation,
                    doctor.image,
                    doctor.position
                FROM
                    public.doctor doctor
                        JOIN public.schedule schedule ON doctor.id = schedule.doctor_id
                        JOIN public.time_sheet time_sheet ON schedule.id = time_sheet.schedule_id
                        JOIN public.schedule_day_of_week schedule_day ON schedule.id = schedule_day.schedule_id
                        JOIN public.department department ON doctor.department_id = department.id
                WHERE
                    (doctor.first_name || ' ' || doctor.last_name) ILIKE ?
                GROUP BY doctor.image, day_of_week, time_sheet.date_of_consultation,
                         CASE WHEN time_sheet.available = false THEN time_sheet.start_time_of_consultation ELSE null END,
                         CASE WHEN time_sheet.available = false THEN time_sheet.end_time_of_consultation ELSE null END,
                         concat(doctor.first_name, ' ', doctor.last_name), doctor.position
                ORDER BY doctor_full_name, day_of_week;
                 """;

        try {
            return jdbcTemplate.query(
                    sql,
                    new Object[]{"%" + word + "%"},
                    (resultSet, rowNum) -> ResponseToGetSchedules.builder()
                            .surname(resultSet.getString(1))
                            .dayOfWeek(resultSet.getString(2))
                            .dateOfConsultation(resultSet.getDate(3).toLocalDate())
                            .startTimeOfConsultation(
                                    Optional.ofNullable(resultSet.getTime(4))
                                            .map(java.sql.Time::toLocalTime)
                                            .orElse(null))
                            .endTimeOfConsultation(
                                    Optional.ofNullable(resultSet.getTime(5))
                                            .map(java.sql.Time::toLocalTime)
                                            .orElse(null))
                            .image(resultSet.getString(6))
                            .position(resultSet.getString(7))
                            .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new NotFoundException("Error while fetching schedules");
        }
    }


}


