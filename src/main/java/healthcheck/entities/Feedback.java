package healthcheck.entities;

import jakarta.persistence.*;
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
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "fb_gen")
    @SequenceGenerator(name = "fb_gen",sequenceName = "fb_seq", allocationSize = 1,
            initialValue = 11)
    @ManyToOne
    private Doctor doctor;
    @ManyToOne
    private User user;
    private LocalDate localDate;
    private int rating;
    private String comment;
}
