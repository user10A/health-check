package healthcheck.repo;
import healthcheck.entities.TimeSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface TimeSheetRepo extends JpaRepository<TimeSheet,Long> {

    @Query("select s.available from TimeSheet s where s.schedule.id = :Id and s.dateOfConsultation = :date and s.startTimeOfConsultation = :time")
    Boolean booked(Long Id, LocalDate date, LocalTime time);
    @Query("select t.endTimeOfConsultation from TimeSheet  t where t.schedule.doctor.id = :id and t.startTimeOfConsultation = :startTimeOfConsultation")
    LocalTime getTimeSheetByEndTimeOfConsultation(Long id ,LocalTime startTimeOfConsultation);

}