package healthcheck.dto.Department;
import healthcheck.enums.Facility;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
public class DepartmentResponse {
    private Long id;
    private Facility facility;


    public DepartmentResponse(Long id, Facility facility) {
        this.id = id;
    }
}
