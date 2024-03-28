package healthcheck.repo.Dao.Impl;

import healthcheck.dto.Appointment.AppointmentResponse;
import healthcheck.dto.Appointment.AppointmentScheduleTimeSheetResponse;
import healthcheck.repo.Dao.AppointmentDao;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class AppointmentDaoImpl implements AppointmentDao {
    private final JdbcTemplate jdbcTemplate;
    @Override
    public List<AppointmentScheduleTimeSheetResponse> getTheDoctorFreeTimeInTheCalendar(String startDate, String endDate, Long doctorId) {
        ZoneId zoneId = ZoneId.of("Asia/Bishkek");
        ZonedDateTime currentTime = ZonedDateTime.now(zoneId);
        LocalTime startTime = currentTime.toLocalTime();
        LocalDate start = LocalDate.parse(startDate);
        LocalDate start1 = currentTime.toLocalDate();
        LocalDate end = LocalDate.parse(endDate);
        Map<LocalDate, List<AppointmentScheduleTimeSheetResponse>> scheduleMap = new LinkedHashMap<>();
        var sql =
        """
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
            List<AppointmentScheduleTimeSheetResponse> dbResults = jdbcTemplate.query(sql, new Object[]{start1.toString(), endDate,start1.toString(),startTime.toString(),doctorId}, (rs, rowNum) -> {
                LocalDate dateOfConsultation = rs.getDate(1).toLocalDate();
                return AppointmentScheduleTimeSheetResponse.builder()
                        .dateOfConsultation(dateOfConsultation)
                        .dayOfWeek(getDayOfWeek(dateOfConsultation).name())
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

    @Override
    public List<AppointmentResponse> getAllAppointment(String word) {
        String sql = """
                SELECT
                    a.id,
                    concat(u.first_name,' ',u.last_name) as full_name,
                    u.phone_number,
                    ua.email,
                    dep.facility,
                    concat(d.first_name,' ',d.last_name) as doctor_full_name,
                    a.appointment_date,
                    a.appointment_time,
                    a.status,
                    a.processed,
                    a.creation_date
                FROM Appointment a
                    JOIN users u ON a.user_id = u.id
                    JOIN Doctor d ON a.doctor_id = d.id
                    JOIN User_Account ua on ua.id = u.user_account_id
                    JOIN department dep ON d.department_id = dep.id
                WHERE  LOWER(u.first_name) LIKE concat('%', LOWER(?), '%') OR
                          LOWER(u.last_name) LIKE concat('%', LOWER(?), '%') OR
                          LOWER(d.first_name) LIKE concat('%', LOWER(?), '%') OR
                          LOWER(d.last_name) LIKE concat('%', LOWER(?), '%')
                      
                """;

        List<AppointmentResponse> responsesWord = jdbcTemplate.query(sql, new Object[]{word,word,word,word},
                (rs, rowNum) -> {
                AppointmentResponse response = new AppointmentResponse();
                    response.setAppointmentId(rs.getLong(1));
                    response.setFullName(rs.getString(2));
                    response.setPhoneNumber(rs.getString(3));
                    response.setEmail(rs.getString(4));
                    response.setFacility(rs.getString(5));
                    response.setSpecialist(rs.getString(6));
                    response.setLocalDate(rs.getDate(7).toLocalDate());
                    response.setLocalTime(rs.getTime(8).toLocalTime());
                    response.setStatus(rs.getString("status"));
                    response.setProcessed(rs.getBoolean(10));

                    Timestamp creationTimestamp = rs.getTimestamp("creation_date");
                    if (creationTimestamp != null) {
                        response.setCreationDate(Timestamp.valueOf(creationTimestamp.toLocalDateTime()));
                    }

                    return response;
                });

        return responsesWord;
    }

    @Override
    public List<AppointmentResponse> getAllAppointmentDefault() {
        String sql = """
                SELECT
                    a.id,
                    concat(u.first_name,' ',u.last_name) as full_name,
                    u.phone_number,
                    ua.email,
                    dep.facility,
                    concat(d.first_name,' ',d.last_name) as doctor_full_name,
                    a.appointment_date,
                    a.appointment_time,
                    a.status,
                    a.processed,
                    a.creation_date
                FROM Appointment a
                    JOIN users u ON a.user_id = u.id
                    JOIN Doctor d ON a.doctor_id = d.id
                    JOIN User_Account ua on ua.id = u.user_account_id
                    JOIN department dep ON d.department_id = dep.id
                order by a.id
                """;
        List<AppointmentResponse> responsesDefault = jdbcTemplate.query(sql, (rs, rowNum) -> {
            AppointmentResponse response = new AppointmentResponse();
            response.setAppointmentId(rs.getLong(1));
            response.setFullName(rs.getString(2));
            response.setPhoneNumber(rs.getString(3));
            response.setEmail(rs.getString(4));
            response.setFacility(rs.getString(5));
            response.setSpecialist(rs.getString(6));
            response.setLocalDate(rs.getDate(7).toLocalDate());
            response.setLocalTime(rs.getTime(8).toLocalTime());
            response.setStatus(rs.getString("status"));
            response.setProcessed(rs.getBoolean(10));

            Timestamp creationTimestamp = rs.getTimestamp("creation_date");
            if (creationTimestamp != null) {
                response.setCreationDate(Timestamp.valueOf(creationTimestamp.toLocalDateTime()));
            }
            return response;
        });
        responsesDefault.sort(Comparator.comparing(AppointmentResponse::getCreationDate,
                Comparator.nullsLast(Comparator.reverseOrder())));

        return responsesDefault;
    }

    public DayOfWeek getDayOfWeek(LocalDate date) {
        return date.getDayOfWeek();
    }
}