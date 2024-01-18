package healthcheck.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "doctor_gen")
    @SequenceGenerator(name = "doctor_gen",sequenceName = "doctor_seq", allocationSize = 1, initialValue = 21)
    private Long id;
    private String firstName;
    private String lastName;
    private String image;
    private String position;
    private String description;
    private boolean isActive;
    @ManyToOne(cascade = {CascadeType.DETACH})
    private Department department;
    @OneToMany(mappedBy = "doctor",cascade = {CascadeType.REMOVE})
    private List<Appointment> appointments;
    @OneToOne(cascade = {CascadeType.REMOVE})
    private Schedule schedule;


}