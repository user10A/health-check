package healthcheck.dto.GlobalSearch;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import healthcheck.enums.Facility;
import lombok.Builder;

@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SearchResponse {
    private Long doctorId;
    private Long departmentId;
    private String doctorFirstName;
    private String doctorLastName;
    private Facility doctorPosition;
    private String image;

    public SearchResponse(Long doctorId, Long departmentId, String doctorFirstName, String doctorLastName, Facility doctorPosition, String image) {
        this.doctorId = doctorId;
        this.departmentId = departmentId;
        this.doctorFirstName = doctorFirstName;
        this.doctorLastName = doctorLastName;
        this.doctorPosition = doctorPosition;
        this.image = image;
    }
}