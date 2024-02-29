package healthcheck.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.CascadeType;
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
public class TimeSheet {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "timeSheet_gen")
    @SequenceGenerator(name = "timeSheet_gen",sequenceName = "timeSheet_seq", allocationSize = 1, initialValue = 21)
    private Long id;
    private LocalDate dateOfConsultation;
    private LocalTime startTimeOfConsultation;
    private LocalTime endTimeOfConsultation;
    private boolean available;
    @ManyToOne(cascade = {CascadeType.DETACH})
    @JsonIgnore
    private Schedule schedule;

    @Override
    public String toString() {
        return "TimeSheet{" +
                "id=" + id +
                ", dateOfConsultation=" + dateOfConsultation +
                ", startTimeOfConsultation=" + startTimeOfConsultation +
                ", endTimeOfConsultation=" + endTimeOfConsultation +
                ", available=" + available +
                '}';
    }
}