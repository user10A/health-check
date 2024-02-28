package healthcheck.repo;

import healthcheck.entities.TimeSheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface TimeSheetRepo extends JpaRepository<TimeSheet,Long> {
    @Query("select s.available from TimeSheet s where s.schedule.id = :Id and s.dateOfConsultation = :date and s.startTimeOfConsultation = :time")
    Boolean booked(Long Id, LocalDate date, LocalTime time);
    @Query("select t from TimeSheet  t where t.schedule.doctor.id = :doctorId and t.dateOfConsultation = :date and t.startTimeOfConsultation = :startTimeOfConsultation")
    TimeSheet getTimeSheetByDoctorIdAndStartTime(Long doctorId ,LocalDate date,LocalTime startTimeOfConsultation);
    @Query("SELECT t FROM TimeSheet t WHERE t.schedule.doctor.id = :doctorId AND t.dateOfConsultation = :date")
    Page<TimeSheet> getTimeSheetByDoctorIdAndStartTime1(Long doctorId, LocalDate date, Pageable pageable);
}