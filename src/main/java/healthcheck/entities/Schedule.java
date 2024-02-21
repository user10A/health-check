package healthcheck.entities;

import healthcheck.enums.DaysOfRepetition;
import healthcheck.enums.Interval;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Column;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.MapKeyEnumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    private LocalTime startBreakTime;
    private LocalTime endBreakTime;
    @Enumerated(EnumType.STRING)
    private Interval intervalInMinutes;
    @ElementCollection
    @CollectionTable(name = "schedule_day_of_week", joinColumns = @JoinColumn(name = "schedule_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "day_of_week")
    @Column(name = "is_working_day")
    private Map<DaysOfRepetition, Boolean> dayOfWeek;
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.REMOVE)
    private List<TimeSheet> timeSheets;
    @OneToOne(cascade = {CascadeType.DETACH})
    private Doctor doctor;
    @ManyToOne
    private Department department;

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}