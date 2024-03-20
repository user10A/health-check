package healthcheck.service.Impl;

import healthcheck.dto.Doctor.*;
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
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Locale;


@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepo doctorRepo;
    private final DepartmentRepo departmentRepo;
    private final DoctorDao doctorDao;
private final MessageSource messageSource;
    @Override
    @Transactional
    public SimpleResponse saveDoctor(DoctorSaveRequest request) {
        Department department = departmentRepo.findById(request.getDepartmentId())
                .orElseThrow(() -> new NotFoundException(
                        messageSource.getMessage("error.department_not_found", new Object[]{request.getDepartmentId()}, Locale.getDefault())
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

        log.info("Врач успешно сохранен: " + doctor.getFirstName() + " " + doctor.getLastName());
        String successMessage = messageSource.getMessage("doctor.save.success", null, Locale.getDefault());
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .messageCode(successMessage)
                .build();
    }

    @Override
    public List<Doctor> getDoctorsByDepartment(Facility facility) {
        Department department = departmentRepo.getDepartmentByFacility(facility).orElseThrow(() ->

                new NotFoundException(messageSource.getMessage("department.not.found", null, Locale.getDefault())));

        log.info("Получены врачи для отделения: " + department.getFacility().name());

        return doctorRepo.getDoctorsByDepartment(department);
    }

    @Override
    public DoctorResponse getDoctorById(Long id) {
        Doctor doctor = doctorRepo.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(messageSource.getMessage("doctor.not.found", new Object[]{id}, Locale.getDefault())));
        return DoctorResponse.builder()
                .id(doctor.getId())
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .position(doctor.getPosition())
                .image(doctor.getImage())
                .department(doctor.getDepartment().getFacility().name())
                .description(doctor.getDescription())
                .build();
    }

    @Override
    @Transactional
    public SimpleResponse updateDoctor(Facility facility, Long id, DoctorUpdateRequest request) {
        Department department = departmentRepo.getDepartmentByFacility(facility)
                .orElseThrow(() -> new NotFoundException(messageSource.getMessage("department.not.found", null, Locale.getDefault())));

        Doctor doctor = doctorRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(messageSource.getMessage("doctor.not.found", new Object[]{id}, Locale.getDefault())));
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
        String successMessage = messageSource.getMessage("doctor.update.success", null, Locale.getDefault());
        return new SimpleResponse(successMessage, HttpStatus.OK);    }

    // Специалисты. Методы: 1 - Search по именам. 2 - Вывод всех докторов. 3 - Удаление ->
    @Override
    public List<DoctorResponseByWord> getAllDoctorsBySearch(String word) {
        return doctorDao.getAllDoctorsBySearch(word);
    }

    @Override
    public List<DoctorResponseByWord> getAllDoctors() {
        return doctorDao.getAllDoctors();
    }

    @Override
    @Transactional
    public SimpleResponse deleteDoctorById(Long doctorId) {
        Doctor doctor = doctorRepo.findById(doctorId).orElseThrow(() ->
                new NotFoundException(messageSource.getMessage("doctor.not.found", new Object[]{doctorId}, Locale.getDefault())));

        doctorRepo.delete(doctor);
        String successMessage = messageSource.getMessage("doctor.delete.success", null, Locale.getDefault());
        return SimpleResponse.builder().messageCode(successMessage).httpStatus(HttpStatus.OK).build();
    }

    @Override
    public SimpleResponse updateDoctorStatusById(Long id, boolean b) {
        Doctor doctor = doctorRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(messageSource.getMessage("doctor.not.found", new Object[]{id}, Locale.getDefault())));
        doctor.setActive(b);
        doctorRepo.save(doctor);
        String successMessage = messageSource.getMessage("doctor.status.update.success", null, Locale.getDefault());
        return SimpleResponse.builder().messageCode(successMessage).httpStatus(HttpStatus.OK).build();
    }

    @Override
    public List<DoctorsGetAllByDepartmentsResponse> getAllDoctorsSortByDepartments() {
        return doctorDao.getAllDoctorsSortByDepartments();
    }
}