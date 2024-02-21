package healthcheck.entities;

import healthcheck.enums.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "onlineAppointment_gen")
    @SequenceGenerator(name = "onlineAppointment_gen",sequenceName = "onlineAppointment_seq", allocationSize = 1, initialValue = 21)
    private Long id;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    @Enumerated(EnumType.STRING)
    private Status status;
    private boolean processed;
    private String verificationCode;
    @ManyToOne(cascade = {CascadeType.DETACH})
    private User user;
    @ManyToOne(cascade = {CascadeType.DETACH})
    private Department department;
    @ManyToOne(cascade = {CascadeType.DETACH})
    private Doctor doctor;
    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", appointmentDate=" + appointmentDate +
                ", appointmentTime=" + appointmentTime +
                ", status=" + status +
                '}';
    }
}
