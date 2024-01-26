package healthcheck.service;

import healthcheck.dto.Doctor.DoctorSaveRequest;
import healthcheck.dto.SimpleResponse;
import healthcheck.entities.Doctor;
import healthcheck.enums.Facility;

import java.util.List;

public interface DoctorService {
    SimpleResponse saveDoctor(DoctorSaveRequest request);
    List<Doctor> getDoctorsByDepartment(Facility facility);
}