package healthcheck.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
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
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "result_gen")
    @SequenceGenerator(name = "result_gen",sequenceName = "result_seq", allocationSize = 1, initialValue = 21)
    private Long id;
    private LocalDate resultDate;
    private LocalTime timeOfUploadingResult;
    private String pdfUrl;
    private Long resultNumber;
    @ManyToOne(cascade = {CascadeType.DETACH})
    private Department department;
    @ManyToOne(cascade = {CascadeType.DETACH})
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
