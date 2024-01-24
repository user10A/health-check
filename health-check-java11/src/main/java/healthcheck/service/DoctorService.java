package healthcheck.service;

import healthcheck.dto.Doctor.DoctorResponse;
import healthcheck.dto.Doctor.DoctorSaveRequest;
import healthcheck.dto.Doctor.DoctorUpdateRequest;
import healthcheck.dto.Doctor.ResponseToGetDoctorsByDepartment;
import healthcheck.dto.SimpleResponse;

import java.util.List;

public interface DoctorService {

    SimpleResponse saveDoctor(DoctorSaveRequest request);
    DoctorResponse getDoctorById(Long id);
    SimpleResponse updateDoctor(Long id,DoctorUpdateRequest request);
    List<ResponseToGetDoctorsByDepartment> getDoctorsByDepartment();

}