package healthcheck.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "doctor_gen")
    @SequenceGenerator(name = "doctor_gen",sequenceName = "doctor_seq", allocationSize = 1, initialValue = 26)
    private Long id;
    private String firstName;
    private String lastName;
    private String image;
    private String position;
    private String description;
    private boolean isActive;
    @ManyToOne(cascade = {CascadeType.DETACH})
    @JsonIgnore
    private Department department;
    @OneToMany(mappedBy = "doctor",cascade = {CascadeType.REMOVE})
    @JsonIgnore
    private List<Appointment> appointments;
    @OneToOne(mappedBy = "doctor",cascade = {CascadeType.REMOVE})
    @JsonIgnore
    private Schedule schedule;
    @OneToMany(mappedBy = "doctor",cascade = {CascadeType.REMOVE})
    @JsonIgnore
    private List<Feedback> feedbacks;

    @Column(name = "creation_date")
    private Timestamp creationDate;

    public String getFullNameDoctor() {
        return this.getFirstName() + " " + this.getLastName();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", image='" + image + '\'' +
                ", position='" + position + '\'' +
                ", description='" + description + '\'' +
                ", isActive=" + isActive +
                '}';
    }

}