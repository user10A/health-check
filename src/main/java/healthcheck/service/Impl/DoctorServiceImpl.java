package healthcheck.service.Impl;

import com.google.cloud.Timestamp;
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
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

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
                       "error.department_not_found", new Object[]{request.getDepartmentId()})
                );

        Doctor doctor = Doctor.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .position(request.getPosition())
                .image(request.getImage())
                .department(department)
                .description(request.getDescription())
                .isActive(false)
                .creationDate(Timestamp.now().toSqlTimestamp())
                .build();

        department.addDoctor(doctor);
        doctorRepo.save(doctor);

        log.info("Врач успешно сохранен: " + doctor.getFirstName() + " " + doctor.getLastName());
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .messageCode(messageSource.getMessage("doctor.save.success",null,LocaleContextHolder.getLocale()))
                .build();
    }

    @Override
    public List<Doctor> getDoctorsByDepartment(Facility facility) {
        Department department = departmentRepo.getDepartmentByFacility(facility).orElseThrow(() ->

                new NotFoundException("department.not.found"));
        log.info("Получены врачи для отделения: " + department.getFacility().name());
        return doctorRepo.getDoctorsByDepartment(department);
    }

    @Override
    public DoctorResponse getDoctorById(Long id)throws NotFoundException {
       return doctorDao.getDoctorById(id);
    }


    @Override
    @Transactional
    public SimpleResponse updateDoctor(Facility facility, Long id, DoctorUpdateRequest request) {
        Department department = departmentRepo.getDepartmentByFacility(facility)
                .orElseThrow(() -> new NotFoundException("department.not.found"));

        Doctor doctor = doctorRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("doctor.not.found", new Object[]{id}));
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
        return new SimpleResponse(messageSource.getMessage("doctor.update.success",null,LocaleContextHolder.getLocale()), HttpStatus.OK);    }

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
                new NotFoundException("doctor.not.found", new Object[]{doctorId}));

        doctorRepo.delete(doctor);
        return SimpleResponse.builder().messageCode(messageSource.getMessage("doctor.delete.success",null,LocaleContextHolder.getLocale())).httpStatus(HttpStatus.OK).build();
    }

    @Override
    public SimpleResponse updateDoctorStatusById(Long id, boolean b) {
        Doctor doctor = doctorRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("doctor.not.found", new Object[]{id}));
        doctor.setActive(b);
        doctorRepo.save(doctor);
        return SimpleResponse.builder().messageCode(messageSource.getMessage("doctor.status.update.success",null,LocaleContextHolder.getLocale())).httpStatus(HttpStatus.OK).build();
    }
    @Override
    public List<DoctorsGetAllByDepartmentsResponse> getAllDoctorsSortByDepartments() {
        return doctorDao.getAllDoctorsSortByDepartments();
    }
}