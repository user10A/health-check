package healthcheck.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "user_gen")
    @SequenceGenerator(name = "user_gen",sequenceName = "user_seq", allocationSize = 1, initialValue = 21)
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    @OneToOne(cascade = {CascadeType.ALL})
    private UserAccount userAccount;
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Result> results;
    @OneToMany(mappedBy = "user",cascade = {CascadeType.REMOVE})
    private List<Appointment> appointments;
    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE})
    private List<Feedback> feedbacks;

    @Column(name = "creation_date")
    private Timestamp creationDate;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}