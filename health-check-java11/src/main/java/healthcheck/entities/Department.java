package healthcheck.entities;
import healthcheck.enums.Facility;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "application_gen")
    @SequenceGenerator(name = "application_gen",sequenceName = "application_seq",allocationSize = 1)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Facility facility;
    @OneToMany(mappedBy = "department", cascade = CascadeType.DETACH)
    private List<Doctor> doctors;
    @OneToMany(mappedBy = "department", cascade = CascadeType.REMOVE)
    private List<Appointment> appointments;
    @OneToMany(mappedBy ="department", cascade = {CascadeType.DETACH})
    private List<Schedule>schedules;
    @OneToMany(mappedBy = "department")
    private List<Result>results;
}