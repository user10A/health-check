package healthcheck.service;

import healthcheck.dto.Doctor.DoctorResponse;
import healthcheck.dto.Doctor.DoctorResponseByWord;
import healthcheck.dto.Doctor.DoctorSaveRequest;
import healthcheck.dto.Doctor.DoctorUpdateRequest;
import healthcheck.dto.SimpleResponse;
import healthcheck.entities.Doctor;
import healthcheck.enums.Facility;

import java.util.List;

public interface DoctorService {
    SimpleResponse saveDoctor(DoctorSaveRequest request);
    List<Doctor> getDoctorsByDepartment(Facility facility);
    DoctorResponse getDoctorById(Long id);
    SimpleResponse updateDoctor(Facility facility, Long id,DoctorUpdateRequest request);
    List<DoctorResponseByWord> getAllDoctorsBySearch(String word);
    List<DoctorResponseByWord> getAllDoctors();
    SimpleResponse deleteDoctorById(Long doctorId);
    SimpleResponse updateDoctorStatusById(Long id,boolean b);

}