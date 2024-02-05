package healthcheck.repo;

import healthcheck.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointment,Long> {
    @Query("select a from Appointment a where " +
            "(:word is null or :word = '' or a.user.firstName like concat('%', :word, '%') or " +
            "a.user.lastName like concat('%', :word, '%') or a.doctor.firstName like concat('%', :word, '%') or " +
            "a.doctor.lastName like concat('%', :word, '%'))")
    List<Appointment> getAllAppointment(@Param("word") String word);

    @Query("select a from Appointment a")
    List<Appointment> getAllAppointmentDefault();
}