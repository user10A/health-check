package healthcheck.service.Impl;
import healthcheck.dto.Doctor.DoctorResponse;
import healthcheck.dto.Doctor.DoctorSaveRequest;
import healthcheck.dto.Doctor.DoctorUpdateRequest;
import healthcheck.dto.Doctor.ResponseToGetDoctorsByDepartment;
import healthcheck.dto.SimpleResponse;
import healthcheck.entities.Department;
import healthcheck.entities.Doctor;
import healthcheck.enums.Facility;
import healthcheck.exceptions.NotFoundException;
import healthcheck.repo.Dao.DoctorDao;
import healthcheck.repo.DepartmentRepo;
import healthcheck.repo.DoctorRepo;
import healthcheck.service.DoctorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepo doctorRepo;
    private final DoctorDao doctorDao;
    private final DepartmentRepo departmentRepo;

    @Override
    public SimpleResponse saveDoctor(DoctorSaveRequest request) {
        Department department = departmentRepo.findById(request.getDepartmentId())
                .orElseThrow(() -> new NotFoundException(
                        String.format("Отделение с ID: %d не найдено", request.getDepartmentId())
                ));

        Doctor doctor = Doctor.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .position(request.getPosition())
                .image(request.getImage())
                .department(department)
                .description(request.getDescription())
                .isActive(false)
                .build();

        department.addDoctor(doctor);
        doctorRepo.save(doctor);
        log.info("Сохранен врач с полным именем: " + doctor.getFirstName() + " " + doctor.getLastName());


        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Успешно сохранен!")
                .build();
    }

    @Override
    public List<Doctor> getDoctorsByDepartment(Facility facility) {
        Department department = departmentRepo.getDepartmentByFacility(facility).orElseThrow(() ->
                new NotFoundException("Department not found"));
        return doctorRepo.getDoctorsByDepartment(department);

    public DoctorResponse getDoctorById(Long id) {
        Doctor doctor = doctorRepo.findById(id)
                .orElseThrow(()-> new NotFoundException("Доктор c таким id :"+id+" не найден"));
        return DoctorResponse.builder()
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .position(doctor.getPosition())
                .image(doctor.getImage())
                .department(doctor.getDepartment().getFacility().name())
                .description(doctor.getDescription())
                .build();
    }

    @Override
    public SimpleResponse updateDoctor(Long id,DoctorUpdateRequest request) {
        Department department = departmentRepo.getByFacilityName(request.getFacility());
        Doctor doctor =
                doctorRepo.findById(id)
                .orElseThrow(()-> new NotFoundException("Доктор c таким id :"+id+" не найден"));
        doctor = Doctor.builder()
                .id(doctor.getId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .position(request.getPosition())
                .image(request.getImage())
                .department(department)
                .description(request.getDescription())
                .build();
        department.addDoctor(doctor);
        doctorRepo.save(doctor);
        departmentRepo.save(department);
        return new SimpleResponse("doctor successfully updated",HttpStatus.OK);
    }
}