package healthcheck.entities;
import healthcheck.enums.Interval;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.CascadeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "schedule_gen")
    @SequenceGenerator(name = "schedule_gen",sequenceName = "schedule_seq", allocationSize = 1, initialValue = 26)
    private Long id;
    private LocalDate startDateWork;
    private LocalDate endDateWork;
    private LocalTime startDayTime;
    private LocalTime endDayTime;
    private LocalTime StartBreakTime;
    private LocalTime endBreakTime;
    @Enumerated(EnumType.STRING)
    private Interval intervalInMinutes;
    @ElementCollection()
    @CollectionTable(name = "schedule_day_of_week", joinColumns = @JoinColumn(name = "schedule_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "is_working_day")
    private Map<DayOfWeek, Boolean> dayOfWeek;
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.REMOVE)
    private List<TimeSheet> timeSheets;
    @OneToOne(mappedBy = "schedule",cascade = {CascadeType.DETACH})
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
    @ManyToOne
    private Department department;

}



