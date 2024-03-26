package healthcheck.repo.Dao.Impl;

import healthcheck.dto.Schedule.ResponseToGetSchedules;
import healthcheck.dto.Schedule.ScheduleDate;
import healthcheck.repo.Dao.ScheduleDao;
import healthcheck.repo.Dao.TimeSheetDao;
import healthcheck.repo.ScheduleRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ScheduleDaoImpl implements ScheduleDao {
    private final JdbcTemplate jdbcTemplate;
    private final ScheduleRepo scheduleRepo;
    private final TimeSheetDao timeSheetDao;

    @Override
    public List<ResponseToGetSchedules> getAllSchedules() {
        LocalDate start = LocalDate.now();
        LocalDate end = scheduleRepo.getByEndDateWorkSchedule();

        String sql = """
       SELECT
                     doctor.id,
                     CONCAT(doctor.first_name, ' ', doctor.last_name) AS doctor_full_name,
                     doctor.image,
                     doctor.position,
                     s.creation_date
                 FROM
                     doctor
                 JOIN schedule s on doctor.id = s.doctor_id
                 GROUP BY
                     doctor.id,
                     doctor_full_name,
                     doctor.image,
                     doctor.position,
                     s.creation_date
                 ORDER BY
                     doctor.id
       """;

        List<ResponseToGetSchedules> getAll = jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            Long id = resultSet.getLong(1);
            List<ScheduleDate> dates = timeSheetDao.getDoctorWorkingDate(start.toString(), end.toString(), id);
            ResponseToGetSchedules response = new ResponseToGetSchedules();
            response.setId(resultSet.getLong(1));
            response.setImage(resultSet.getString("image"));
            response.setSurname(resultSet.getString("doctor_full_name"));
            response.setPosition(resultSet.getString("position"));

            Timestamp creationTimestamp = resultSet.getTimestamp("creation_date");
            if (creationTimestamp != null) {
                response.setCreationDate(creationTimestamp.toLocalDateTime());
            }

            response.setDates(dates);
            return response;
        });

        getAll.sort(Comparator.comparing(ResponseToGetSchedules::getCreationDate, Comparator.nullsLast(Comparator.reverseOrder())));

        return getAll;
    }

    @Override
    public List<ResponseToGetSchedules> getScheduleByDate(LocalDate startDate, LocalDate endDate) {
        String sql = """
                   SELECT
                    doctor.id,
                    CONCAT(doctor.first_name, ' ', doctor.last_name) AS doctor_full_name,
                    doctor.image,
                    doctor.position
                FROM
                    doctor
                GROUP BY
                    doctor.id,
                    doctor_full_name,
                    doctor.image,
                    doctor.position
                ORDER BY
                doctor.id
                """;
        List<ResponseToGetSchedules> getAll = jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            Long id = resultSet.getLong(1);
            List<ScheduleDate> dates = timeSheetDao.getDoctorWorkingDate(startDate.toString(), endDate.toString(), id);
            ResponseToGetSchedules response = new ResponseToGetSchedules();
            response.setId(resultSet.getLong(1));
            response.setImage(resultSet.getString("image"));
            response.setSurname(resultSet.getString("doctor_full_name"));
            response.setPosition(resultSet.getString("position"));
            response.setDates(dates);
            return response;
        });
        return getAll;
    }

    @Override
    public List<ResponseToGetSchedules> getScheduleBySearch(String word) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = scheduleRepo.getByEndDateWorkSchedule();
        String sql =
                """
                SELECT
                           doctor.id,
                           CONCAT(doctor.first_name, ' ', doctor.last_name) AS doctor_full_name,
                           doctor.image,
                           doctor.position
                       FROM
                           doctor
                       WHERE
                           LOWER(doctor.first_name) LIKE CONCAT('%', LOWER(?), '%') OR
                           LOWER(doctor.last_name) LIKE CONCAT('%', LOWER(?), '%')
                       GROUP BY
                           doctor.id,
                           doctor_full_name,
                           doctor.image,
                           doctor.position
                """;
        List<ResponseToGetSchedules> getAll = jdbcTemplate.query(sql, new Object[]{word,word}, (resultSet, rowNum) -> {
            Long id = resultSet.getLong(1);
            List<ScheduleDate> dates = timeSheetDao.getDoctorWorkingDate(startDate.toString(), endDate.toString(), id);
            ResponseToGetSchedules response = new ResponseToGetSchedules();
            response.setId(resultSet.getLong(1));
            response.setImage(resultSet.getString("image"));
            response.setSurname(resultSet.getString("doctor_full_name"));
            response.setPosition(resultSet.getString("position"));
            response.setDates(dates);
            return response;
        });
        return getAll;
    }
}