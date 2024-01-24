package healthcheck.service;

import healthcheck.dto.Doctor.DoctorSaveRequest;
import healthcheck.dto.Doctor.ResponseToGetDoctorsByDepartment;
import healthcheck.dto.SimpleResponse;
import java.util.List;

public interface DoctorService {
    SimpleResponse saveDoctor(DoctorSaveRequest request);
    List<ResponseToGetDoctorsByDepartment> getDoctorsByDepartment();
}