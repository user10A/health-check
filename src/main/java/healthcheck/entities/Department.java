package healthcheck.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import healthcheck.enums.Facility;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Cacheable
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "application_gen")
    @SequenceGenerator(name = "application_gen",sequenceName = "application_seq", allocationSize = 1, initialValue = 21)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Facility facility;
    @OneToMany(mappedBy = "department", cascade = CascadeType.DETACH)
    @JsonIgnore
    private List<Doctor> doctors;
    @OneToMany(mappedBy = "department", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Appointment> appointments;
    @OneToMany(mappedBy ="department", cascade = {CascadeType.DETACH})
    @JsonIgnore
    private List<Schedule> schedules;
    @OneToMany(mappedBy = "department")
    @JsonIgnore
    private List<Result> results;

    public void addDoctor(Doctor doctor) {
        if (doctor == null) {
            doctors = new ArrayList<>();
        }
        doctors.add(doctor);
    }
    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", facility=" + facility +
                '}';
    }
}