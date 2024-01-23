package healthcheck.service;

import healthcheck.dto.Doctor.DoctorSaveRequest;
import healthcheck.dto.SimpleResponse;

public interface DoctorService {
    SimpleResponse saveDoctor(DoctorSaveRequest request);
}