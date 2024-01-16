package healthcheck.dto;

import demo.entities.Department;
import demo.entities.Schedule;

public class SpecializationResponse {
    boolean status;
    Department department;
    Schedule schedule;
    Schedule time;
}
