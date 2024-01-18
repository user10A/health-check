package healthcheck.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "application_gen")
    @SequenceGenerator(name = "application_gen",sequenceName = "application_seq",
            initialValue = 21,
            allocationSize = 1)
    private long id;
    private String username;
    private LocalDate dateOfApplicationCreation;
    private String phoneNumber;
    private boolean processed;
}
