package healthcheck.service;

import healthcheck.dto.Department.DepartmentResponse;

import java.util.List;

public interface DepartmentService {
    List<DepartmentResponse> getAllFacility();

}