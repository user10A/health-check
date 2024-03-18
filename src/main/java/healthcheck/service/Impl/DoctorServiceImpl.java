package healthcheck.service.Impl;

import healthcheck.dto.Doctor.DoctorResponse;
import healthcheck.dto.Doctor.DoctorResponseByWord;
import healthcheck.dto.Doctor.DoctorSaveRequest;
import healthcheck.dto.Doctor.DoctorUpdateRequest;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepo doctorRepo;
    private final DepartmentRepo departmentRepo;
    private final DoctorDao doctorDao;

    @Override
    @Transactional
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

        log.info("Врач успешно сохранен: " + doctor.getFirstName() + " " + doctor.getLastName());

        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .messageCode("Успешно сохранен!")
                .build();
    }

    @Override
    public List<Doctor> getDoctorsByDepartment(Facility facility) {
        Department department = departmentRepo.getDepartmentByFacility(facility).orElseThrow(() ->

                new NotFoundException("Отделение не найдено"));

        log.info("Получены врачи для отделения: " + department.getFacility().name());

        return doctorRepo.getDoctorsByDepartment(department);
    }

    @Override
    public DoctorResponse getDoctorById(Long id) {
        Doctor doctor = doctorRepo.findById(id)
                .orElseThrow(()-> new NotFoundException("Доктор c таким id :"+id+" не найден"));
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
    public SimpleResponse updateDoctor(Facility facility, Long id,DoctorUpdateRequest request) {
        Department department = departmentRepo.getDepartmentByFacility(facility).orElseThrow(() ->
                new NotFoundException("Department не найден"));
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
                new NotFoundException("Doctor не найден!"));

        doctorRepo.delete(doctor);
        return SimpleResponse.builder().messageCode("Doctor успешно удален").httpStatus(HttpStatus.OK).build();
    }

    @Override
    public SimpleResponse updateDoctorStatusById(Long id, boolean b) {
        Doctor doctor =
                doctorRepo.findById(id)
                        .orElseThrow(()-> new NotFoundException("Доктор c таким id :"+id+" не найден"));
        doctor.setActive(b);
        doctorRepo.save(doctor);
        return SimpleResponse.builder().messageCode("Статус Доктора успешно обновлен").httpStatus(HttpStatus.OK).build();
    }
    // <-
}