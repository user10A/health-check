package healthcheck.repo.Dao.Impl;

import healthcheck.dto.Schedule.ResponseToGetSchedules;
import healthcheck.exceptions.NotFoundException;
import healthcheck.repo.Dao.ScheduleDao;
import healthcheck.repo.ScheduleRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ScheduleDaoImpl implements ScheduleDao {
    private final JdbcTemplate jdbcTemplate;
    private final ScheduleRepo scheduleRepo;

    @Override
    public List<ResponseToGetSchedules> getAllSchedules() {
        LocalDate start = LocalDate.now();
        LocalDate end = scheduleRepo.getByEndDateWorkSchedule();
        String sql = """
                   SELECT
                       CONCAT(doctor.first_name, ' ', doctor.last_name) AS doctor_full_name,
                       time_sheet.date_of_consultation AS date_of_consultation,
                       time_sheet.start_time_of_consultation AS start_time_of_consultation,
                       time_sheet.end_time_of_consultation AS end_time_of_consultation,
                       doctor.image,
                       doctor.position,
                       schedule_day_of_week.is_working_day as dayOfWeek,
                       time_sheet.available
                   FROM
                       doctor
                       
                           JOIN public.schedule schedule ON doctor.id = schedule.doctor_id
                           JOIN public.time_sheet time_sheet ON schedule.id = time_sheet.schedule_id
                           JOIN public.department department ON doctor.department_id = department.id
                           JOIN schedule_day_of_week schedule_day_of_week ON schedule.id = schedule_day_of_week.schedule_id
                   WHERE time_sheet.date_of_consultation BETWEEN TO_DATE(?, 'YYYY-MM-DD') AND TO_DATE(?, 'YYYY-MM-DD')
                   GROUP BY
                       time_sheet.date_of_consultation,
                       time_sheet.start_time_of_consultation,
                       time_sheet.end_time_of_consultation,
                       doctor_full_name,
                       doctor.image,
                       doctor.position,
                       dayOfWeek,
                       time_sheet.available
                   ORDER BY
                       doctor_full_name
                """;

        try {
            List<ResponseToGetSchedules> getAll = jdbcTemplate.query(sql, new Object[]{start.toString(), end.toString()}, (resultSet, rowNum) -> {
                String dayOfWeek = String.valueOf(getDayOfWeek(resultSet.getDate(2).toLocalDate()));
                ResponseToGetSchedules response = new ResponseToGetSchedules();
                response.setImage(resultSet.getString("image"));
                response.setSurname(resultSet.getString("doctor_full_name"));
                response.setPosition(resultSet.getString("position"));
                response.setDayOfWeek(dayOfWeek);
                response.setDateOfConsultation(resultSet.getDate("date_of_consultation").toLocalDate());
                response.setStartTimeOfConsultation(resultSet.getTime("start_time_of_consultation").toLocalTime());
                response.setEndTimeOfConsultation(resultSet.getTime("end_time_of_consultation").toLocalTime());
                response.setIsWorkingDay(Boolean.valueOf(resultSet.getBoolean("dayOfWeek")));
                response.setAvailableTime((resultSet.getBoolean("available")));
                return response;
            });
            Map<LocalDate, List<ResponseToGetSchedules>> scheduleMap = new LinkedHashMap<>();
            List<LocalDate> allDates = new ArrayList<>();
            while (!start.isAfter(end)) {
                allDates.add(start);
                start = start.plusDays(1);
            }

            for (LocalDate date : allDates) {
                scheduleMap.put(date, new ArrayList<>());
            }

            for (ResponseToGetSchedules response : getAll) {
                scheduleMap.get(response.getDateOfConsultation()).add(response);
            }
            List<ResponseToGetSchedules> finalScheduleList = new ArrayList<>();
            for (Map.Entry<LocalDate, List<ResponseToGetSchedules>> entry : scheduleMap.entrySet()) {
                LocalDate date = entry.getKey();
                List<ResponseToGetSchedules> daySchedule = entry.getValue();
                if (daySchedule.isEmpty()) {
                    String image = null;
                    String surname = null;
                    String position = null;
                    if (!finalScheduleList.isEmpty()) {
                        ResponseToGetSchedules firstSchedule = finalScheduleList.get(0);
                        image = firstSchedule.getImage();
                        surname = firstSchedule.getSurname();
                        position = firstSchedule.getPosition();
                    }
                    finalScheduleList.add(new ResponseToGetSchedules(
                            image,
                            surname,
                            position,
                            getDayOfWeek(date).name(),
                            date,
                            null,
                            null,
                            false,
                            null));
                } else {
                    finalScheduleList.addAll(daySchedule);
                }
            }
            return finalScheduleList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NotFoundException("Error while fetching schedules");
        }
    }



    @Override
    public List<ResponseToGetSchedules> getScheduleByDate(LocalDate startDate, LocalDate endDate) {
        String sql = """
               SELECT
                   CONCAT(doctor.first_name, ' ', doctor.last_name) AS doctor_full_name,
                   time_sheet.date_of_consultation AS date_of_consultation,
                   time_sheet.start_time_of_consultation AS start_time_of_consultation,
                   time_sheet.end_time_of_consultation AS end_time_of_consultation,
                   doctor.image,
                   doctor.position,
                   schedule_day_of_week.is_working_day as dayOfWeek,
                   time_sheet.available
               FROM
                   doctor
                   JOIN public.schedule schedule ON doctor.id = schedule.doctor_id
                   JOIN public.time_sheet time_sheet ON schedule.id = time_sheet.schedule_id
                   JOIN public.department department ON doctor.department_id = department.id
                   JOIN schedule_day_of_week schedule_day_of_week ON schedule.id = schedule_day_of_week.schedule_id
               WHERE
                   time_sheet.date_of_consultation BETWEEN ? AND ?
               GROUP BY
                   time_sheet.date_of_consultation,
                   time_sheet.start_time_of_consultation,
                   time_sheet.end_time_of_consultation,
                   doctor_full_name,
                   doctor.image,
                   doctor.position,
                   dayOfWeek,
                   time_sheet.available
               ORDER BY
                   doctor_full_name
            """;

        try {
            List<ResponseToGetSchedules> getAll = jdbcTemplate.query(sql, new Object[]{startDate, endDate}, (resultSet, rowNum) -> {
                String dayOfWeek = String.valueOf(getDayOfWeek(resultSet.getDate(2).toLocalDate()));
                ResponseToGetSchedules response = new ResponseToGetSchedules();
                response.setImage(resultSet.getString("image"));
                response.setSurname(resultSet.getString("doctor_full_name"));
                response.setPosition(resultSet.getString("position"));
                response.setDayOfWeek(dayOfWeek);
                response.setDateOfConsultation(resultSet.getDate("date_of_consultation").toLocalDate());
                response.setStartTimeOfConsultation(resultSet.getTime("start_time_of_consultation").toLocalTime());
                response.setEndTimeOfConsultation(resultSet.getTime("end_time_of_consultation").toLocalTime());
                response.setIsWorkingDay(Boolean.valueOf(resultSet.getBoolean("dayOfWeek")));
                // Assuming 'available' is of boolean type in the database
                response.setAvailableTime((resultSet.getBoolean("available")));
                return response;
            });

            Map<LocalDate, List<ResponseToGetSchedules>> scheduleMap = new LinkedHashMap<>();
            List<LocalDate> allDates = new ArrayList<>();
            while (!startDate.isAfter(endDate)) {
                allDates.add(startDate);
                startDate = startDate.plusDays(1);
            }

            for (LocalDate date : allDates) {
                scheduleMap.put(date, new ArrayList<>());
            }

            for (ResponseToGetSchedules response : getAll) {
                scheduleMap.get(response.getDateOfConsultation()).add(response);
            }
            List<ResponseToGetSchedules> finalScheduleList = new ArrayList<>();
            for (Map.Entry<LocalDate, List<ResponseToGetSchedules>> entry : scheduleMap.entrySet()) {
                LocalDate date = entry.getKey();
                List<ResponseToGetSchedules> daySchedule = entry.getValue();
                if (daySchedule.isEmpty()) {
                    String image = null;
                    String surname = null;
                    String position = null;
                    if (!finalScheduleList.isEmpty()) {
                        ResponseToGetSchedules firstSchedule = finalScheduleList.get(0);
                        image = firstSchedule.getImage();
                        surname = firstSchedule.getSurname();
                        position = firstSchedule.getPosition();
                    }
                    finalScheduleList.add(new ResponseToGetSchedules(
                            image,
                            surname,
                            position,
                            getDayOfWeek(date).name(),
                            date,
                            null,
                            null,
                            false,
                            null));
                } else {
                    finalScheduleList.addAll(daySchedule);

                }
            }
            return finalScheduleList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NotFoundException("Error while fetching schedules");
        }
    }





    @Override
    public List<ResponseToGetSchedules> getScheduleBySearch(String word) {
        String sql = """
                   SELECT
                       CONCAT(doctor.first_name, ' ', doctor.last_name) AS doctor_full_name,
                       time_sheet.date_of_consultation AS date_of_consultation,
                       time_sheet.start_time_of_consultation AS start_time_of_consultation,
                       time_sheet.end_time_of_consultation AS end_time_of_consultation,
                       doctor.image,
                       doctor.position,
                       schedule_day_of_week.is_working_day as dayOfWeek,
                       time_sheet.available
                   FROM
                       doctor
                       JOIN public.schedule schedule ON doctor.id = schedule.doctor_id
                       JOIN public.time_sheet time_sheet ON schedule.id = time_sheet.schedule_id
                       JOIN public.department department ON doctor.department_id = department.id
                       JOIN schedule_day_of_week schedule_day_of_week ON schedule.id = schedule_day_of_week.schedule_id
                   WHERE
                       (doctor.first_name LIKE ? OR doctor.last_name LIKE ?)  -- Search condition
                   GROUP BY
                       time_sheet.date_of_consultation,
                       time_sheet.start_time_of_consultation,
                       time_sheet.end_time_of_consultation,
                       doctor_full_name,
                       doctor.image,
                       doctor.position,
                       dayOfWeek,
                       time_sheet.available
                """;

        try {
            List<ResponseToGetSchedules> getAll = jdbcTemplate.query(sql, new Object[]{"%" + word + "%", "%" + word + "%"}, (resultSet, rowNum) -> {
                String dayOfWeek = String.valueOf(getDayOfWeek(resultSet.getDate(2).toLocalDate()));
                ResponseToGetSchedules response = new ResponseToGetSchedules();
                response.setImage(resultSet.getString("image"));
                response.setSurname(resultSet.getString("doctor_full_name"));
                response.setPosition(resultSet.getString("position"));
                response.setDayOfWeek(dayOfWeek);
                response.setDateOfConsultation(resultSet.getDate("date_of_consultation").toLocalDate());
                response.setStartTimeOfConsultation(resultSet.getTime("start_time_of_consultation").toLocalTime());
                response.setEndTimeOfConsultation(resultSet.getTime("end_time_of_consultation").toLocalTime());
                response.setIsWorkingDay(Boolean.valueOf(resultSet.getBoolean("dayOfWeek")));
                response.setAvailableTime((resultSet.getBoolean("available")));
                return response;
            });

            Map<LocalDate, List<ResponseToGetSchedules>> scheduleMap = new LinkedHashMap<>();
            List<LocalDate> allDates = new ArrayList<>();
            LocalDate startDate = getAll.stream().map(ResponseToGetSchedules::getDateOfConsultation).min(LocalDate::compareTo).orElse(null);
            LocalDate endDate = getAll.stream().map(ResponseToGetSchedules::getDateOfConsultation).max(LocalDate::compareTo).orElse(null);

            if (startDate != null && endDate != null) {
                LocalDate dateIterator = startDate;
                while (!dateIterator.isAfter(endDate)) {
                    allDates.add(dateIterator);
                    dateIterator = dateIterator.plusDays(1);
                }
            }

            for (LocalDate date : allDates) {
                scheduleMap.put(date, new ArrayList<>());
            }

            for (ResponseToGetSchedules response : getAll) {
                scheduleMap.get(response.getDateOfConsultation()).add(response);
            }

            List<ResponseToGetSchedules> finalScheduleList = new ArrayList<>();
            for (Map.Entry<LocalDate, List<ResponseToGetSchedules>> entry : scheduleMap.entrySet()) {
                LocalDate date = entry.getKey();
                List<ResponseToGetSchedules> daySchedule = entry.getValue();
                if (daySchedule.isEmpty()) {
                    String image = null;
                    String surname = null;
                    String position = null;
                    if (!finalScheduleList.isEmpty()) {
                        ResponseToGetSchedules firstSchedule = finalScheduleList.get(0);
                        image = firstSchedule.getImage();
                        surname = firstSchedule.getSurname();
                        position = firstSchedule.getPosition();
                    }
                    finalScheduleList.add(new ResponseToGetSchedules(
                            image,
                            surname,
                            position,
                            getDayOfWeek(date).name(),
                            date,
                            null,
                            null,
                            false,
                            null));
                } else {
                    finalScheduleList.addAll(daySchedule);
                }
            }

            return finalScheduleList;

        } catch (Exception e) {
            e.printStackTrace();
            throw new NotFoundException("Error while fetching schedules by search");
        }
    }



    private DayOfWeek getDayOfWeek(LocalDate date) {
        return date.getDayOfWeek();
    }
}
