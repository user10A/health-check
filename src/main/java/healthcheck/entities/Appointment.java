package healthcheck.entities;

import healthcheck.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
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
    @Column(name = "creation_date")
    private Timestamp creationDate;
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
