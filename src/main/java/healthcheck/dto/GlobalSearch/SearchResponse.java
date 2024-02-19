package healthcheck.dto.GlobalSearch;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;

@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SearchResponse {
    private Long doctorId;
    private Long departmentId;
    private String doctorFirstName;
    private String doctorLastName;
    private String doctorPosition;
    private String image;
}