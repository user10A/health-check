package healthcheck.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Timestamp;
import java.time.LocalDate;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "feedback_gen")
    @SequenceGenerator(name = "feedback_gen",sequenceName = "feedback_seq", allocationSize = 1,
            initialValue = 21)
    private Long id;
    @ManyToOne
    private Doctor doctor;
    @ManyToOne
    private User user;
    private LocalDate localDate;
    private int rating;
    private String comment;
    @Column(name = "creation_date")
    private Timestamp creationDate;
}
