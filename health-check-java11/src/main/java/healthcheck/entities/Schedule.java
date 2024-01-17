package healthcheck.entities;
import healthcheck.enums.Interval;
import jakarta.persistence.*;
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
    @SequenceGenerator(name = "schedule_gen",sequenceName = "schedule_seq",
            initialValue = 25,
            allocationSize = 1)
    private Long id;
    private LocalDate startDateWork;
    private LocalDate endDateWork;
    private LocalTime startDayTime;
    private LocalTime endDayTime;
    private LocalTime StartBreakTime;
    private LocalTime endBreakTime;
    @Enumerated(EnumType.STRING)
    private Interval intervalInMinutes;
    @ElementCollection
    @CollectionTable(name = "schedule_day_of_week", joinColumns = @JoinColumn(name = "schedule_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "day_of_week")
    @Column(name = "is_working_day")
    private Map<DayOfWeek, Boolean> dayOfWeek;
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.REMOVE)
    private List<TimeSheet> timeSheets;
    @OneToOne(cascade = {CascadeType.DETACH})
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
    @ManyToOne
    private Department department;

}



