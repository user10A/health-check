package healthcheck.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "application_gen")
    @SequenceGenerator(name = "application_gen",sequenceName = "application_seq", allocationSize = 1,
            initialValue = 21)
    private long id;
    private String username;
    private LocalDate dateOfApplicationCreation;
    private String phoneNumber;
    private boolean processed;

    @Column(name = "creation_date")
    private Timestamp creationDate;
}