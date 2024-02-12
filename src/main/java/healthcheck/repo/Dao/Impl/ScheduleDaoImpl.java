package healthcheck.repo.Dao.Impl;

import healthcheck.dto.Schedule.ResponseToGetSchedules;
import healthcheck.exceptions.NotFoundException;
import healthcheck.repo.Dao.ScheduleDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
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
                        time_sheet.date_of_consultation as date_of_consultation,
                        CASE WHEN time_sheet.available = false THEN time_sheet.start_time_of_consultation ELSE null END as start_time_of_consultation,
                        CASE WHEN time_sheet.available = false THEN time_sheet.end_time_of_consultation ELSE null END as end_time_of_consultation,
                        doctor.image,
                        doctor.position
                    FROM
                        doctor
                        JOIN public.schedule schedule ON doctor.id = schedule.doctor_id
                        JOIN public.time_sheet time_sheet ON schedule.id = time_sheet.schedule_id
                        JOIN public.department department ON doctor.department_id = department.id
                    GROUP BY 
                        time_sheet.date_of_consultation,
                        CASE WHEN time_sheet.available = false THEN time_sheet.start_time_of_consultation ELSE null END,
                        CASE WHEN time_sheet.available = false THEN time_sheet.end_time_of_consultation ELSE null END,
                        concat(doctor.first_name, ' ', doctor.last_name),
                        doctor.image,
                        doctor.position
                    ORDER BY 
                        doctor_full_name;
                """;

        try {
            return jdbcTemplate.query(sql, (resultSet, rowNum) -> {
                String dayOfWeek = getDayOfWeek(resultSet.getDate("date_of_consultation").toLocalDate()).name();
                ResponseToGetSchedules response = ResponseToGetSchedules.builder()
                        .surname(resultSet.getString("doctor_full_name"))
                        .dayOfWeek(dayOfWeek)
                        .dateOfConsultation(resultSet.getDate("date_of_consultation").toLocalDate())
                        .startTimeOfConsultation(
                                Optional.ofNullable(resultSet.getTime("start_time_of_consultation"))
                                        .map(java.sql.Time::toLocalTime)
                                        .orElse(null))
                        .endTimeOfConsultation(
                                Optional.ofNullable(resultSet.getTime("end_time_of_consultation"))
                                        .map(java.sql.Time::toLocalTime)
                                        .orElse(null))
                        .image(resultSet.getString("image"))
                        .position(resultSet.getString("position"))
                        .build();
                return response;
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new NotFoundException("Error while fetching schedules");
        }
    }



    public List<ResponseToGetSchedules> getScheduleByDate(String startDate, String endDate) {
        String sql = """
        SELECT
            concat(doctor.first_name, ' ', doctor.last_name) AS doctor_full_name,
            time_sheet.date_of_consultation as date_of_consultation,
            CASE WHEN time_sheet.available = false THEN time_sheet.start_time_of_consultation ELSE null END as start_time_of_consultation,
            CASE WHEN time_sheet.available = false THEN time_sheet.end_time_of_consultation ELSE null END as end_time_of_consultation,
            doctor.image,
            doctor.position
        FROM
            doctor
                JOIN public.schedule schedule ON doctor.id = schedule.doctor_id
                JOIN public.time_sheet time_sheet ON schedule.id = time_sheet.schedule_id
                JOIN public.department department ON doctor.department_id = department.id
        WHERE
            time_sheet.date_of_consultation BETWEEN ?::date AND ?::date
        ORDER BY 
            time_sheet.date_of_consultation, doctor_full_name;
         """;

        try {
            return jdbcTemplate.query(
                    sql,
                    new Object[]{startDate, endDate},
                    (resultSet, rowNum) -> {
                        String dayOfWeek = getDayOfWeek(resultSet.getDate("date_of_consultation").toLocalDate()).name();
                        ResponseToGetSchedules response = ResponseToGetSchedules.builder()
                                .surname(resultSet.getString("doctor_full_name"))
                                .dayOfWeek(dayOfWeek)
                                .dateOfConsultation(resultSet.getDate("date_of_consultation").toLocalDate())
                                .startTimeOfConsultation(
                                        Optional.ofNullable(resultSet.getTime("start_time_of_consultation"))
                                                .map(java.sql.Time::toLocalTime)
                                                .orElse(null))
                                .endTimeOfConsultation(
                                        Optional.ofNullable(resultSet.getTime("end_time_of_consultation"))
                                                .map(java.sql.Time::toLocalTime)
                                                .orElse(null))
                                .image(resultSet.getString("image"))
                                .position(resultSet.getString("position"))
                                .build();
                        return response;
                    });
        } catch (Exception e) {
            e.printStackTrace();
            throw new NotFoundException("Error while fetching schedules");
        }
    }


    public DayOfWeek getDayOfWeek(LocalDate date) {
        return date.getDayOfWeek();
    }
    public List<ResponseToGetSchedules> getScheduleBySearch(String word) {
        String sql = """
    SELECT
        concat(doctor.first_name, ' ', doctor.last_name) AS doctor_full_name,
        time_sheet.date_of_consultation as date_of_consultation,
        CASE WHEN time_sheet.available = false THEN time_sheet.start_time_of_consultation ELSE null END as start_time_of_consultation,
        CASE WHEN time_sheet.available = false THEN time_sheet.end_time_of_consultation ELSE null END as end_time_of_consultation,
        doctor.image,
        doctor.position
    FROM
        doctor
            JOIN public.schedule schedule ON doctor.id = schedule.doctor_id
            JOIN public.time_sheet time_sheet ON schedule.id = time_sheet.schedule_id
            JOIN public.department department ON doctor.department_id = department.id
    WHERE
        (doctor.first_name || ' ' || doctor.last_name) ILIKE ?
    GROUP BY 
        time_sheet.date_of_consultation,
        CASE WHEN time_sheet.available = false THEN time_sheet.start_time_of_consultation ELSE null END,
        CASE WHEN time_sheet.available = false THEN time_sheet.end_time_of_consultation ELSE null END,
        concat(doctor.first_name, ' ', doctor.last_name),
        doctor.image,
        doctor.position
    ORDER BY doctor_full_name;
     """;

        try {
            return jdbcTemplate.query(
                    sql,
                    new Object[]{"%" + word + "%"},
                    (resultSet, rowNum) -> {
                        String dayOfWeek = getDayOfWeek(resultSet.getDate("date_of_consultation").toLocalDate()).name();
                        ResponseToGetSchedules response = ResponseToGetSchedules.builder()
                                .surname(resultSet.getString("doctor_full_name"))
                                .dayOfWeek(dayOfWeek)
                                .dateOfConsultation(resultSet.getDate("date_of_consultation").toLocalDate())
                                .startTimeOfConsultation(
                                        Optional.ofNullable(resultSet.getTime("start_time_of_consultation"))
                                                .map(java.sql.Time::toLocalTime)
                                                .orElse(null))
                                .endTimeOfConsultation(
                                        Optional.ofNullable(resultSet.getTime("end_time_of_consultation"))
                                                .map(java.sql.Time::toLocalTime)
                                                .orElse(null))
                                .image(resultSet.getString("image"))
                                .position(resultSet.getString("position"))
                                .build();
                        return response;
                    });
        } catch (Exception e) {
            e.printStackTrace();
            throw new NotFoundException("Error while fetching schedules");
        }
    }
}